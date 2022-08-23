
package com.yc.monitorspeed;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 用于计算用户连接的近似带宽
 */
public class ConnectionManager {

    private static final double DEFAULT_SAMPLES_TO_QUALITY_CHANGE = 5;
    private static final int BYTES_TO_BITS = 8;

    /**
     * 确定数据连接质量的默认值。带宽数的单位是千比特每秒(kbps)。
     */
    private static final int DEFAULT_POOR_BANDWIDTH = 150;
    private static final int DEFAULT_MODERATE_BANDWIDTH = 550;
    private static final int DEFAULT_GOOD_BANDWIDTH = 2000;
    private static final long DEFAULT_HYSTERESIS_PERCENT = 20;
    private static final double HYSTERESIS_TOP_MULTIPLIER = 100.0 / (100.0 - DEFAULT_HYSTERESIS_PERCENT);
    private static final double HYSTERESIS_BOTTOM_MULTIPLIER = (100.0 - DEFAULT_HYSTERESIS_PERCENT) / 100.0;
    /**
     * 根据先前计算的带宽值，用来计算当前带宽的因子。这个值越小，移动平均线对新样本的响应越小。
     */
    private static final double DEFAULT_DECAY_CONSTANT = 0.05;
    /**
     * 用户连接的当前带宽取决于响应。
     */
    private final ExponentialGeometricAverage mDownloadBandwidth
            = new ExponentialGeometricAverage(DEFAULT_DECAY_CONSTANT);
    private volatile boolean mInitiateStateChange = false;
    private final AtomicReference<ConnectionQuality> mCurrentBandwidthConnectionQuality =
            new AtomicReference<>(ConnectionQuality.UNKNOWN);
    private AtomicReference<ConnectionQuality> mNextBandwidthConnectionQuality;
    private final ArrayList<ConnectionStateChangeListener> mListenerList = new ArrayList<>();
    private int mSampleCounter;

    /**
     * 以比特/毫秒计算的测量带宽的下界。低于这个值的读数被视为有效的零(因此被忽略)。
     */
    static final long BANDWIDTH_LOWER_BOUND = 10;

    private static class ConnectionClassManagerHolder {
        public static final ConnectionManager INSTANCE = new ConnectionManager();
    }

    /**
     * 获取单利对象
     * @return                              单利对象
     */
    public static ConnectionManager getInstance() {
        return ConnectionClassManagerHolder.INSTANCE;
    }

    private ConnectionManager() {

    }

    /**
     * 增加带宽到当前过滤的延迟计数器。向所有人发送广播
     * {@link ConnectionStateChangeListener} 如果计数器从一个桶移动到另一个桶(即低带宽->中等带宽)。
     */
    public synchronized void addBandwidth(long bytes, long timeInMs) {
        //Ignore garbage values.
        // 变化量小于10计算终止重新记录值
        if (timeInMs == 0 || (bytes) * 1.0 / (timeInMs) * BYTES_TO_BITS < BANDWIDTH_LOWER_BOUND) {
            return;
        }
        double bandwidth = (bytes) * 1.0 / (timeInMs) * BYTES_TO_BITS;
        mDownloadBandwidth.addMeasurement(bandwidth);
        // 如果它为true连续mSampleCounter+1
        if (mInitiateStateChange) {
            mSampleCounter += 1;
            // 判断当前的值和期望的值是否处在同一个范围内，不处在同一个范围内,连续5次计算的值和波动值在同一个范围内的话,减少网络波动造成的误差
            if (getCurrentBandwidthQuality() != mNextBandwidthConnectionQuality.get()) {
                // 计数重新开始,mInitiateStateChange
                mInitiateStateChange = false;
                mSampleCounter = 1;
            }
            //网络稳定到5次以上才通知计算结果，波动比较大的时候不通知，优化更准确
            if (mSampleCounter >= DEFAULT_SAMPLES_TO_QUALITY_CHANGE && significantlyOutsideCurrentBand()) {
                mInitiateStateChange = false;
                mSampleCounter = 1;
                // 确定改变的时候设置通知
                mCurrentBandwidthConnectionQuality.set(mNextBandwidthConnectionQuality.get());
                notifyListeners();
            }
            return;
        }
        // 如果上一个速率和现在的计算速率不一样，那么mInitiateStateChange=true,mNextBandwidthConnectionQuality储存当前的速录
        if (mCurrentBandwidthConnectionQuality.get() != getCurrentBandwidthQuality()) {
            mInitiateStateChange = true;
            mNextBandwidthConnectionQuality = new AtomicReference<>(getCurrentBandwidthQuality());
        }
    }

    private boolean significantlyOutsideCurrentBand() {
        ConnectionQuality currentQuality = mCurrentBandwidthConnectionQuality.get();
        double bottomOfBand;
        double topOfBand;
        switch (currentQuality) {
            case POOR:
                bottomOfBand = 0;
                topOfBand = DEFAULT_POOR_BANDWIDTH;
                break;
            case MODERATE:
                bottomOfBand = DEFAULT_POOR_BANDWIDTH;
                topOfBand = DEFAULT_MODERATE_BANDWIDTH;
                break;
            case GOOD:
                bottomOfBand = DEFAULT_MODERATE_BANDWIDTH;
                topOfBand = DEFAULT_GOOD_BANDWIDTH;
                break;
            case EXCELLENT:
                bottomOfBand = DEFAULT_GOOD_BANDWIDTH;
                topOfBand = Float.MAX_VALUE;
                break;
            default:
                // If current quality is UNKNOWN, then changing is always valid.
                return true;
        }
        double average = mDownloadBandwidth.getAverage();
        if (average > topOfBand) {
            if (average > topOfBand * HYSTERESIS_TOP_MULTIPLIER) {
                return true;
            }
        } else if (average < bottomOfBand * HYSTERESIS_BOTTOM_MULTIPLIER) {
            return true;
        }
        return false;
    }

    /**
     * 重制
     */
    public void reset() {
        mDownloadBandwidth.reset();
        mCurrentBandwidthConnectionQuality.set(ConnectionQuality.UNKNOWN);
    }

    /**
     * 获得移动带宽平均当前所代表的连接质量
     */
    public synchronized ConnectionQuality getCurrentBandwidthQuality() {
        return mapBandwidthQuality(mDownloadBandwidth.getAverage());
    }

    private ConnectionQuality mapBandwidthQuality(double average) {
        if (average < 0) {
            return ConnectionQuality.UNKNOWN;
        }
        if (average < DEFAULT_POOR_BANDWIDTH) {
            return ConnectionQuality.POOR;
        }
        if (average < DEFAULT_MODERATE_BANDWIDTH) {
            return ConnectionQuality.MODERATE;
        }
        if (average < DEFAULT_GOOD_BANDWIDTH) {
            return ConnectionQuality.GOOD;
        }
        return ConnectionQuality.EXCELLENT;
    }


    /**
     * 获取当前平均带宽的方法
     * @return 当前带宽平均，或-1，如果没有平均记录。
     */
    public synchronized double getDownloadKBitsPerSecond() {
        return mDownloadBandwidth.getAverage();
    }

    /**
     * 注册监听操作
     * @param listener                              listener
     */
    public ConnectionQuality register(ConnectionStateChangeListener listener) {
        if (listener != null) {
            mListenerList.add(listener);
        }
        return mCurrentBandwidthConnectionQuality.get();
    }

    /**
     * 移除监听操作
     * @param listener                              listener
     */
    public void remove(ConnectionStateChangeListener listener) {
        if (listener != null) {
            mListenerList.remove(listener);
        }
    }

    /**
     * 刷新
     */
    private void notifyListeners() {
        int size = mListenerList.size();
        for (int i = 0; i < size; i++) {
            ConnectionQuality connectionQuality = mCurrentBandwidthConnectionQuality.get();
            mListenerList.get(i).onBandwidthStateChange(connectionQuality);
        }
    }
}
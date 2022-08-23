package com.yc.monitorspeed;

import android.net.TrafficStats;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;

import java.util.concurrent.atomic.AtomicInteger;


/**
 * 用于定期从TrafficStats中读取数据，以确定ConnectionClass。
 */
public class DeviceBandwidthSampler {

    /**
     * DownloadBandwidthManager记录移动平均值和ConnectionClass
     */
    private final ConnectionManager mConnectionManager;
    /**
     * 由于是多线程下载图片，这里使用 AtomicInteger 可以避免脏数据出现，
     */
    private final AtomicInteger mSamplingCounter;
    private final SamplingHandler mHandler;
    private long mLastTimeReading;
    private static long sPreviousBytes = -1;

    private static class DeviceBandwidthSamplerHolder {
        public static final DeviceBandwidthSampler INSTANCE =
                new DeviceBandwidthSampler(ConnectionManager.getInstance());
    }

    public static DeviceBandwidthSampler getInstance() {
        return DeviceBandwidthSamplerHolder.INSTANCE;
    }

    private DeviceBandwidthSampler(ConnectionManager connectionManager) {
        mConnectionManager = connectionManager;
        mSamplingCounter = new AtomicInteger();
        HandlerThread thread = new HandlerThread("ParseThread");
        thread.start();
        mHandler = new SamplingHandler(thread.getLooper());
    }

    /**
     * 开始检测网络速度
     */
    public void startSampling() {
        if (mSamplingCounter.getAndIncrement() == 0) {
            //开始发送消息
            mHandler.startSamplingThread();
            mLastTimeReading = SystemClock.elapsedRealtime();
        }
    }

    /**
     * 在另一个计时器启动之前，完成采样并防止对ConnectionClass的进一步更改。
     */
    public void stopSampling() {
        if (mSamplingCounter.decrementAndGet() == 0) {
            mHandler.stopSamplingThread();
            addFinalSample();
        }
    }

    /**
     * 用于轮询自上次更新以来的总字节更改，并将其添加到BandwidthManager。
     */
    protected void addSample() {
        //得到手机自启动以来的总共消耗的流量
        long newBytes = TrafficStats.getTotalRxBytes();
        //计算差值
        long byteDiff = newBytes - sPreviousBytes;
        //有变化的时候才去计算
        if (sPreviousBytes >= 0) {
            synchronized (this) {
                //得到当前的时间
                long curTimeReading = SystemClock.elapsedRealtime();
                //传入流量变化的差值和时间差值
                mConnectionManager.addBandwidth(byteDiff, curTimeReading - mLastTimeReading);
                //更新相对的时间戳
                mLastTimeReading = curTimeReading;
            }
        }
        // 更新上一次的总下载量
        sPreviousBytes = newBytes;
    }

    /**
     * 在记录样本后重置先前读取的字节数，这样我们就不会计算采样过程之间下载的字节数。
     */
    protected void addFinalSample() {
        addSample();
        sPreviousBytes = -1;
    }

    /**
     * @return 如果仍有正在采样的线程，则为真，否则为假。
     */
    public boolean isSampling() {
        return (mSamplingCounter.get() != 0);
    }

    private class SamplingHandler extends Handler {
        /**
         * 间隔时间
         */
        private static final long SAMPLE_TIME = 1000;
        private static final int MSG_START = 1;

        public SamplingHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_START) {
                addSample();
                //循环获取样本计算网速
                sendEmptyMessageDelayed(MSG_START, SAMPLE_TIME);
            } else {
                throw new IllegalArgumentException("Unknown what=" + msg.what);
            }
        }

        public void startSamplingThread() {
            //发送消息
            sendEmptyMessage(SamplingHandler.MSG_START);
        }

        public void stopSamplingThread() {
            //移除消息
            removeMessages(SamplingHandler.MSG_START);
        }
    }
}
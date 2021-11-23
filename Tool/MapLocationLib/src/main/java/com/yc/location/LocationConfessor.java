package com.yc.location;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import com.yc.location.bean.DefaultLocation;
import com.yc.location.bean.ErrorInfo;
import com.yc.location.config.LocationUpdateOption;
import com.yc.location.constant.Constants;
import com.yc.location.listener.LocationListenerWrapper;
import com.yc.location.listener.LocationUpdateInternalListener;
import com.yc.location.net.OkHttpUtils;
import com.yc.location.strategy.ILocationStrategy;
import com.yc.location.strategy.LocationStrategyFactory;
import com.yc.location.utils.PermissionSwitchUtils;
import com.yc.location.utils.LocationUtils;

import java.util.HashMap;
import java.util.Set;


public class LocationConfessor {

    private volatile boolean isRunning = false;
    private boolean isLocLoopRunning = false;

    private Context context;
    private Handler mWorkHandler;

	private volatile long mInterval = LocationUpdateOption.IntervalMode.NORMAL.getValue();

    /**
     * 定位SDK主循环
     * 定位频率 - 外部设定
     * 保存上一次定位成功的位置，本次失败时返回之
     * 定位失败时进行间隔最多1s的再循环
     */
    private Runnable regularLocLoop = new RetriveLocTask();
    

    private LocationUpdateInternalListener mLocationInternalListener;

    public String getListenerInfoString() {
        return String.valueOf(mListenersInfo);
    }

    private class RetriveLocTask implements Runnable{
        private ErrorInfo mCurrentErrInfo;

        @Override
        public void run() {
            if(mWorkHandler == null || !isLocLoopRunning) return ;
            if (mIntervalCount > LocationUpdateOption.IntervalMode.BATTERY_SAVE.getValue()) {
                mIntervalCount = mInterval;
                //触发上传逻辑
//                if (mTraceUpload != null) {
//                    mTraceUpload.uploadTrace();
//                }
            }
            //清空mCurrentErrInfo
            mCurrentErrInfo = new ErrorInfo();
            long startTimestamp = LocationUtils.getTimeBoot();

            DefaultLocation didiLocation = mLocationStrategy.retrieveLocation(mCurrentErrInfo);
            if (null != didiLocation) {
                handleLocNotified(didiLocation);
            } else {
                if (mCurrentErrInfo.getErrNo() != 0) {
                    handleErrNotified(mCurrentErrInfo, (int)(LocationUtils.getTimeBoot() - startTimestamp));
                }
            }

            if(isLocLoopRunning && mWorkHandler != null) {
                mWorkHandler.postDelayed(regularLocLoop, mInterval);
                mIntervalCount += mInterval;
            }
        }

        private void handleErrNotified(ErrorInfo errInfo, int deltaTime) {
            if (null != mLocationInternalListener) {
                mLocationInternalListener.onLocationErr(errInfo, deltaTime);
            }
        }

        private void handleLocNotified(DefaultLocation loc) {
            if (null != mLocationInternalListener) {
                mLocationInternalListener.onLocationUpdate(loc, mIntervalCount);
            }
        }
    }

//    private volatile SortedSet<Long> mUpdateIntervals;
    private long mIntervalCount = 0;

    protected LocationConfessor(Context context) {
        this.context = context;
        OkHttpUtils.init();
    }

    ILocationStrategy mLocationStrategy;
    /** 启动 - 初始化 & 启动wifi检查循环 & 启动位置请求循环 */
    protected synchronized void start(Looper workLooper, LocationUpdateInternalListener locationUpdateInternalListener) {
        if(isRunning) return;
        mWorkHandler = new Handler(workLooper);
        mLocationInternalListener = locationUpdateInternalListener;
        mLocationStrategy = LocationStrategyFactory.getInstance(context).createLocationStrategy(false, LocationUtils.getCoordinateType(context));
        mLocationStrategy.setInternalLocationListener(mLocationInternalListener);
        mLocationStrategy.updateLocListenInterval(mInterval);
        mLocationStrategy.start(mWorkHandler);

        mWorkHandler.post(regularLocLoop);
        isLocLoopRunning = true;
        isRunning = true;
    }

    /** 停止 - 解除监听 & 停止wifi检查循环 & 停止位置请求循环 */
    protected synchronized void stop() {
        if(!isRunning) return;

        if (null != mLocationStrategy) {
            mLocationStrategy.stop();
            mLocationStrategy.setInternalLocationListener(null);
            mLocationStrategy = null;
        }
        if(mWorkHandler != null) {
            mWorkHandler.removeCallbacks(regularLocLoop);
            mWorkHandler = null;
        }
        isLocLoopRunning = false;


        isRunning = false;
        mIntervalCount = 0;
        mInterval = LocationUpdateOption.IntervalMode.NORMAL.getValue();
        mLocationInternalListener = null;
    }

    public long getInterval() {
        return mInterval;
    }

    public void setInterval(final long intervalMillis) {
        if (null != mLocationStrategy) {
            mLocationStrategy.updateLocListenInterval(intervalMillis);
        }
        if (isLocLoopRunning) {
            if (mWorkHandler != null) {
                mWorkHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        if (mWorkHandler == null) return;
                        /*
                            新的间隔更大，则重置mIntervalCount开始计算，避免续用之前的值使某些listener再不能被回调。
                            如：之前是1，计值到4，则4+3 4+6，原来的interval6的listener不能再回调。
                        */
                        mIntervalCount = 0l;
                        mInterval = intervalMillis;

                        mWorkHandler.removeCallbacks(regularLocLoop);
                        mWorkHandler.post(regularLocLoop);
                    }
                });
            }
        } else {
            mIntervalCount = 0l;
            mInterval = intervalMillis;
        }

        //omega埋点：为检验权限状态获取是否正确（司机端定位相关新业务功能），记录权限状态值及基本信息。
        if ((intervalMillis == LocationUpdateOption.IntervalMode.NORMAL.getValue()
                || intervalMillis == LocationUpdateOption.IntervalMode.BATTERY_SAVE.getValue())) {
            PermissionSwitchUtils.PermissionSwitchState state = PermissionSwitchUtils.getPermissionSwitchState(context);
            HashMap<String, String> params = new HashMap<>();
            params.put("ui_version", Build.ID);
            params.put("sdk_version", String.valueOf(BuildConfig.VERSION_CODE));
            params.put("location_switch_level", String.valueOf(LocationUtils.getLocationSwitchLevel(context)));
            params.put("location_permission", String.valueOf(LocationUtils.getLocationPermissionLevel(context)));
            params.put("pemission_switch_state", String.valueOf(state.ordinal()));
            //todo
        }
    }

    //保证写此变量的操作都在一个线程中，从而保证不出现并发问题。
    private volatile StringBuilder mListenersInfo = new StringBuilder("");

    /**
     * 更新监听定位的业务方的信息，定位网络请求与轨迹会带上，以便分析问题。
     * 注意：保证此函数在同一个线程调用，不要引起并发问题。
     * @param listenerList 所有业务方的监听listener
     */
    void updateListenerInfo(Set<LocationListenerWrapper> listenerList) {
        if (null == listenerList) {
            return;
        }
        StringBuilder info = new StringBuilder();
        for (LocationListenerWrapper listenerWrapper : listenerList) {
            info.append(listenerWrapper.getOption().getModuleKey())
                    .append("@")
                    .append(listenerWrapper.getOption().getInterval().getValue())
                    .append("#");
        }
        if (info.length() > 0) {
            info.deleteCharAt(info.length()-1);
        }
        mListenersInfo = info;
        if (null != mLocationStrategy) {
            mLocationStrategy.updateListenersInfo(mListenersInfo);
        }

    }

}

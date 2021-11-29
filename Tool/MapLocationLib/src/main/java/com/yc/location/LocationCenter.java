package com.yc.location;


import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Process;

import com.yc.location.bean.DefaultLocation;
import com.yc.location.bean.ErrorInfo;
import com.yc.location.config.LocationUpdateOption;
import com.yc.location.constant.Constants;
import com.yc.location.listener.LocationListener;
import com.yc.location.listener.LocationListenerWrapper;
import com.yc.location.listener.LocationUpdateInternalListener;
import com.yc.location.log.LogHelper;
import com.yc.location.manager.DefaultLocationManager;
import com.yc.location.utils.LocationUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 定位
 */
public class LocationCenter {

    private HashSet<LocationListenerWrapper> locListeners;
    private ReadWriteLock listenersLock = new ReentrantReadWriteLock();
    private volatile Looper serviceLooper = null;
    private volatile ServiceHandler serviceHandler = null;
    private volatile MainHandler mainHander = null;
    private Context mContext = null;
    public static final String[] coortypestr = {"TYPE_WGS84", "TYPE_GCJ02"}; // name
    private LocationConfessor locConfessor = null;
    private boolean isRunning = false;
    private volatile boolean onTrackingFirstLocate = false;
    private long mWifi2CellJumpInterval = 0;
    private ErrorInfo mLastErrInfo = null;
    private long startTime;
    private static final int serviceCmdNone     = 0xDD000000;
    private static final int serviceCmdStart    = 0xDD000001;
    private static final int serviceCmdStop     = 0xDD000002;

    public ErrorInfo getLastErrInfo() {
        return mLastErrInfo;
    }

    public LocationCenter(Context context) {
        LogHelper.write("-LocCenter- " + Constants.serviceTag + "#onCreate");
        mContext = context;
        locListeners = new HashSet<>();
        /* 增加工作线程优先级，为了不影响UI线程，只是将优先级提高一级。测
           试时注意看是否对UI造成影响。
         */
        HandlerThread thread = new LocHandlerThread(Constants.serviceTag + "[" + System.currentTimeMillis() + "]", Process.THREAD_PRIORITY_MORE_FAVORABLE);
        thread.start();
        serviceLooper = thread.getLooper();
        serviceHandler = new ServiceHandler(serviceLooper);
        mainHander = new MainHandler(context.getMainLooper());
        locConfessor = new LocationConfessor(mContext);
    }

    public void stop() {
        isRunning = false;
        onTrackingFirstLocate = false;
        if (serviceHandler != null) {
            Message msg = serviceHandler.obtainMessage(serviceCmdStop);
            serviceHandler.sendMessage(msg);
        }
        mLastErrInfo = null;
    }


    public void start(LocationListenerWrapper l) {
        startTime = LocationUtils.getTimeBoot();
        isRunning = true;
        mLastErrInfo = null;
        addLocListener(l);
        // 初始定位启动
        onTrackingFirstLocate = true;
        if (serviceHandler != null) {
            Message msg = serviceHandler.obtainMessage(serviceCmdStart);
            serviceHandler.sendMessage(msg);
        }
    }

    private void destroyLooper() {
        serviceLooper.quit();
    }

    private boolean checkZeroCoordsSucc(DefaultLocation loc) {
        return (loc != null &&
                loc.getError() == ErrorInfo.ERROR_OK &&
                Math.abs(loc.getLongitude()) > 0.0000001d && Math.abs(loc.getLatitude()) > 0.0000001d);
    }


    boolean isRunning() {
        return isRunning;
    }


    private void notifyError(ErrorInfo errInfo) {
        Lock lock = listenersLock.readLock();
        try {
            lock.lock();
            if (locListeners != null && locListeners.size() > 0) {
                for (LocationListenerWrapper l : locListeners) {
                    l.getListener().onLocationError(errInfo.getErrNo(), errInfo);
                }
            }
        } finally {
            lock.unlock();
        }
    }

    public long getMinInterval() {
        if (locConfessor != null) {
            return locConfessor.getInterval();
        }
        return 0;
    }

    /**
     * 获得监听方信息，复用LocConfessor中的方法
     * @return
     */
    public String getListenersInfo() {
        if (locConfessor != null) {
            return locConfessor.getListenerInfoString();
        }
        return "";
    }


    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
            LogHelper.logFile("ServiceHandler # NEW: new object, hash " + this.hashCode());
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg==null){
                return;
            }
            if (serviceHandler == null) {
                return;
            }
            switch (msg.what) {
                case serviceCmdNone:
                    break;
                case serviceCmdStart:
                    LogHelper.logFile("-LocCenter- start cmd");
                    try {
                        locConfessor.start(serviceLooper, new LocationUpdateInternalListener(){
                            @Override
                            public void onLocationUpdate(DefaultLocation loc, long intervalCount) {
                                if (checkZeroCoordsSucc(loc)) {
                                    if (!DefaultLocation.GPS_PROVIDER.equals(loc.getProvider())) {
                                        //lcc:增加连续定位中过滤跳点策略
                                        if (!shouldJumpPointForProviderSwitch(loc, DefaultLocationManager.lastKnownLocation)) {
                                            DefaultLocationManager.lastKnownLocation = loc;
                                            DefaultLocationManager.lastKnownLocation.setLocalTime(System.currentTimeMillis());
                                        }
                                    } else {
                                        DefaultLocationManager.lastKnownLocation = loc;
                                        DefaultLocationManager.lastKnownLocation.setLocalTime(System.currentTimeMillis());
                                    }
                                    if (mainHander != null) {
                                        mainHander.removeMessages(Constants.MESSAGE_WHAT_ERRINFO);
                                        //Log.i("lcc", "lcc, in handleLocNotified, isLocOnce:" + mIsLocOnce);
                                        Message msg = obtainLocMessage(DefaultLocationManager.lastKnownLocation, (int)intervalCount);
                                        mainHander.sendMessage(msg);
                                    }
                                } else {
                                    LogHelper.logFile("internal listener # on location update but zero loc, provider:" + (null != loc ? loc.getProvider() : null));
                                }
                            }

                            @Override
                            public void onLocationErr(ErrorInfo errInfo, int deltaTime) {
                                if (mainHander != null) {
                                    //delay 1.5s发出错回调，过滤间隔出现定位结果的情况下间隔出现的定位错误（比如GPS信号或网络不稳定）
                                    mainHander.sendMessageDelayed(obtainErrMessage(errInfo, deltaTime), 1500);
                                }
                            }

                            @Override
                            public void onStatusUpdate(String name, int status) {
                                if (mainHander != null) {
                                    mainHander.sendMessage(obtainStatusMessage(name, status));
                                }
                            }
                        });
                    } catch (Throwable e) {
                        LogHelper.logFile("LocCenter # start request didi location exception, "+ e.getMessage());
                    }
                    break;
                case serviceCmdStop:
                    LogHelper.logFile("-LocCenter- stop cmd");
                    try {
                        if (locConfessor != null) {
                            locConfessor.stop();
                        }
                    } catch (Throwable e) {
                        LogHelper.logFile("LocCenter # stop remove didi location exception, "+ e.getMessage());
                    }
                    serviceHandler.removeCallbacksAndMessages(null);
                    serviceHandler = null;
                    destroyLooper();
                    break;
                default:
                    break;
            }
        }

        @Override
        protected void finalize() throws Throwable {
            LogHelper.logFile("ServiceHandler # WARN: finalize called, hash " + this.hashCode());
            super.finalize();
        }
    }

    private final class LocHandlerThread extends HandlerThread {

        public LocHandlerThread(String name, int priority) {
            super(name, priority);
            LogHelper.logFile("LocHandlerThread # NEW: new object, hash " + this.hashCode());
        }

        @Override
        protected void finalize() throws Throwable {
            LogHelper.logFile("LocHandlerThread # WARN: finalize called, hash " + this.hashCode());
            super.finalize();
        }
    }

    protected final class MainHandler extends Handler {

        long updateNormalGpsTimestamp = 0L;

        public MainHandler(Looper looper) {
            super(looper);
        }

		@Override
        protected void finalize() throws Throwable {

            LogHelper.logFile("MainHandler # WARN: finalize called, hash " + this.hashCode());

            super.finalize();
        }

        @Override
        public void handleMessage(Message msg) {

            //LogHelper.write("-LocCenter- MainHander,threadid=" + Thread.currentThread().getId());
            //为连续定位消息，但连续定位服务已停止时
            if (!isRunning) return;

            if (msg.what == DefaultLocation.ERROR_STATUS) {

                String type = (String)(msg.obj);
                int status = msg.arg1;
                if (locListeners != null) {
                    notifyAllListenerStatus(type, status, "");
                }

            } else if (msg.what == Constants.MESSAGE_WHAT_LACATION) {
                DefaultLocation loc = (DefaultLocation) msg.obj;
                String provider = loc.getProvider();
                notifyListeners(loc, msg.arg1);

                if (onTrackingFirstLocate) {
                    onTrackingFirstLocate = false;
                    HashMap<String, Long> params = new HashMap<>();
                    params.put("first_loc_time", LocationUtils.getTimeBoot() - startTime);

                    LogHelper.write("firstlocate_suc");
                }
                if (DefaultLocation.GPS_PROVIDER.equals(provider)) {
                    updateNormalGpsTimestamp = System.currentTimeMillis();
                }
            } else if (msg.what == Constants.MESSAGE_WHAT_ERRINFO) {
                final ErrorInfo errInfo = (ErrorInfo)msg.obj;
                final int deltaTime = msg.arg1;
                mLastErrInfo = errInfo;
                mLastErrInfo.setLocalTime(System.currentTimeMillis());
                notifyError(errInfo);
                if (null != serviceHandler) {
                    serviceHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            insertDiDiErrPoint(errInfo.getErrNo(), deltaTime);
                        }
                    });
                }
                LogHelper.logFile(String.format("Error, errNo: %d, errMsg: %s", errInfo.getErrNo(), errInfo.getErrMessage()));
            }
        }
    }

    private void notifyListeners(DefaultLocation loc, int interval) {
        if (locListeners != null) notifyListenerLocForInterval(loc, (long)interval);
    }

    private void insertDiDiErrPoint(int err, int deltaTime) {
        // 失败默认写入0坐标，根据error区分网络失败与状态失败
    }

    /**
     * 连续定位跳点优化策略：当定位来源变化时，试图过滤掉跳点。
     * @param loc
     * @param lastLoc
     * @return true: 应该跳过最新的定位点（新来源），使用上次定位点。
     */
    private boolean shouldJumpPointForProviderSwitch(DefaultLocation loc, DefaultLocation lastLoc) {
        if (mWifi2CellJumpInterval == 0) {
            return false;
        }
        if (lastLoc == null || lastLoc.isCacheLocation()) {
            return false;
        }
        if (loc == null) {
            return true;
        }
        String lastProvider = lastLoc.getProvider();
        long lastTime = lastLoc.getTime();
        String provider = loc.getProvider();
        long time = loc.getTime();
        switch (lastProvider) {
            //当从wifi切到cell
            case DefaultLocation.WIFI_PROVIDER:
            case DefaultLocation.NLP_PROVIDER:
                if(DefaultLocation.CELL_PROVIDER.equals(provider)) {
                    return (time - lastTime) > mWifi2CellJumpInterval ? false : true;
                }
                break;
            default:
                break;
        }
        return false;
    }

    private void notifyAllListenerStatus(String type, int status, String info) {
        Lock lock = listenersLock.readLock();
        try {
            lock.lock();
            if (locListeners != null && locListeners.size() > 0) {
                for (LocationListenerWrapper l : locListeners) {
                    l.getListener().onStatusUpdate(type, status, info);
                }
            }
        } finally {
            lock.unlock();
        }
    }

    private void notifyListenerLocForInterval(DefaultLocation loc, long interval) {
        Lock lock = listenersLock.readLock();
        try {
            lock.lock();
            if (loc != null && locListeners != null && locListeners.size() > 0) {
                for (LocationListenerWrapper l : locListeners) {
                    if (interval%l.getOption().getInterval().getValue() == 0) {
                        l.getListener().onLocationChanged(loc);
                    }
                }
            }
        } finally {
            lock.unlock();
        }
    }

    public void addLocListener(LocationListenerWrapper l) {
        if (null == l) {
            return;
        }

        Lock lock = listenersLock.writeLock();
        try {
            lock.lock();
            if (locListeners.contains(l)) {
                return;
            }
            boolean listenerExist = false;
            for (LocationListenerWrapper wrapper : locListeners) {
                if (wrapper.getListener() == l.getListener()) {
                    wrapper.setOption(l.getOption());
                    listenerExist = true;
                    break;
                }
            }
            if (!listenerExist) {
                locListeners.add(l);
            }
            //lcc:fix bug:当只有一个listener时，增大其监听频率，没有改变loop的频率。
            long interval = findMinInterval(locListeners);
            if (locConfessor != null) {
                if (interval != locConfessor.getInterval()) {
                    locConfessor.setInterval(interval);
                }
                locConfessor.updateListenerInfo(locListeners);
            }
        } finally {
            lock.unlock();
        }

        LogHelper.write("-LocCenter- loclisteners added, now size is " + locListeners.size());
    }

    public void removeLocListener(LocationListener l) {
        Lock lock = listenersLock.writeLock();
        try {
            lock.lock();
            for (LocationListenerWrapper wrapper : locListeners) {
                if (wrapper.getListener() == l) {
                    locListeners.remove(wrapper);
                    if (locListeners.size()>0) {
                        long interval = findMinInterval(locListeners);
                        if (locConfessor != null) {
                            if (interval != locConfessor.getInterval()) {
                                locConfessor.setInterval(interval);
                            }
                        }
                    }
                    if (locConfessor != null) {
                        locConfessor.updateListenerInfo(locListeners);
                    }
                    return;
                }
            }
        } finally {
            lock.unlock();
        }
        LogHelper.write("-LocCenter- loclisteners removed, now size is " + locListeners.size());
    }

    private long findMinInterval(HashSet<LocationListenerWrapper> locListeners) {
        long interval = LocationUpdateOption.IntervalMode.BATTERY_SAVE.getValue();
        for (LocationListenerWrapper l : locListeners) {
            if (interval > l.getOption().getInterval().getValue()) {
                interval = l.getOption().getInterval().getValue();
            }
        }
        return interval;
    }


    public int getLocListenersLength() {
        return locListeners.size();
    }

    private Message obtainLocMessage(DefaultLocation didiLocation, int intervalCount) {
        Message msg = mainHander.obtainMessage();
        msg.what = Constants.MESSAGE_WHAT_LACATION;
        msg.obj = didiLocation;
        msg.arg1 = intervalCount;
        return msg;
    }

    /**
     * 获取通知错误的message
     * @param errInfo 具体错误信息
     * @return
     */
    private Message obtainErrMessage(ErrorInfo errInfo, int deltaTime) {
        Message msg = mainHander.obtainMessage();
        msg.what = Constants.MESSAGE_WHAT_ERRINFO;
        msg.obj = errInfo;
        msg.arg1 = deltaTime;

        return msg;
    }

    private Message obtainStatusMessage(String type, int status) {
        Message msg = mainHander.obtainMessage();
        msg.what = DefaultLocation.ERROR_STATUS;
        msg.obj = type;
        msg.arg1 = status;
        return msg;
    }
}

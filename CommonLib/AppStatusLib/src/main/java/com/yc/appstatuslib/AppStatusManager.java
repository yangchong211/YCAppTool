package com.yc.appstatuslib;

import android.app.Application;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.IntentFilter;

import com.yc.appstatuslib.broadcast.BatteryBroadcastReceiver;
import com.yc.appstatuslib.broadcast.BluetoothBroadcastReceiver;
import com.yc.appstatuslib.broadcast.GpsBroadcastReceiver;
import com.yc.appstatuslib.broadcast.NetWorkBroadcastReceiver;
import com.yc.appstatuslib.broadcast.ScreenBroadcastReceiver;
import com.yc.appstatuslib.broadcast.WifiBroadcastReceiver;
import com.yc.appstatuslib.info.BatteryInfo;
import com.yc.appstatuslib.info.ThreadInfo;
import com.yc.appstatuslib.listener.AppStatusListener;
import com.yc.appstatuslib.listener.TraceLogListener;
import com.yc.appstatuslib.utils.ThreadManagerUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class AppStatusManager {

    private final List<AppStatusListener> mAppStatusListener;
    private final ResourceCollect mResourceCollect;
    private final BatteryBroadcastReceiver mBatteryReceiver;
    private final GpsBroadcastReceiver mGpsReceiver;
    private final NetWorkBroadcastReceiver mNetWorkReceiver;
    private final ScreenBroadcastReceiver mScreenReceiver;
    private final WifiBroadcastReceiver mWifiBroadcastReceiver;
    private final BluetoothBroadcastReceiver mBluetoothReceiver;
    private final AppStatus mAppStatus;
    private final Context mContext;
    private final boolean threadSwitchOn;

    private AppStatusManager(AppStatusManager.Builder builder) {
        this.mAppStatusListener = new ArrayList<>();
        this.mBatteryReceiver = new BatteryBroadcastReceiver(this);
        this.mGpsReceiver = new GpsBroadcastReceiver(this);
        this.mNetWorkReceiver = new NetWorkBroadcastReceiver(this);
        this.mScreenReceiver = new ScreenBroadcastReceiver(this);
        this.mWifiBroadcastReceiver = new WifiBroadcastReceiver(this);
        this.mBluetoothReceiver = new BluetoothBroadcastReceiver(this);
        this.mAppStatus = new AppStatus(this);
        this.mContext = builder.context;
        this.threadSwitchOn = builder.threadSwitchOn;
        this.mResourceCollect = (new ResourceCollect.Builder())
                .manager(this)
                .traceLog(builder.traceLog)
                .file(builder.file)
                .interval(builder.interval)
                .builder();
        this.init();
    }

    private void init() {
        if (this.mResourceCollect != null) {
            this.mResourceCollect.destroy();
        }
        this.initBatteryReceiver(this.mContext);
        this.initGpsReceiver(this.mContext);
        this.initNetworkReceiver(this.mContext);
        this.initScreenReceiver(this.mContext);
        this.initWifiReceiver(this.mContext);
        this.initBluetoothReceiver(this.mContext);
        if (this.mResourceCollect != null) {
            this.mResourceCollect.init();
        }
        this.mAppStatus.init((Application)this.mContext.getApplicationContext());
    }

    private void initBatteryReceiver(Context context) {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.BATTERY_CHANGED");
        context.registerReceiver(this.mBatteryReceiver, filter);
    }

    private void initGpsReceiver(Context context) {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.location.PROVIDERS_CHANGED");
        context.registerReceiver(this.mGpsReceiver, filter);
    }

    private void initNetworkReceiver(Context context) {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        context.registerReceiver(this.mNetWorkReceiver, filter);
    }

    private void initScreenReceiver(Context context) {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.SCREEN_ON");
        filter.addAction("android.intent.action.SCREEN_OFF");
        filter.addAction("android.intent.action.USER_PRESENT");
        context.registerReceiver(this.mScreenReceiver, filter);
    }

    private void initWifiReceiver(Context context) {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        context.registerReceiver(this.mWifiBroadcastReceiver, filter);
    }

    private void initBluetoothReceiver(Context context) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        //filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        context.registerReceiver(this.mBluetoothReceiver, filter);
    }


    public void destroy() {
        this.mContext.unregisterReceiver(this.mBatteryReceiver);
        this.mContext.unregisterReceiver(this.mGpsReceiver);
        this.mContext.unregisterReceiver(this.mNetWorkReceiver);
        this.mContext.unregisterReceiver(this.mScreenReceiver);
        this.mContext.unregisterReceiver(this.mBluetoothReceiver);
        this.mAppStatusListener.clear();
        if (this.mResourceCollect != null) {
            this.mResourceCollect.destroy();
        }

    }

    public void collection() {
        if (this.mResourceCollect != null) {
            this.mResourceCollect.sendCollectionMsg();
        }
    }

    public BatteryInfo getBatteryInfo() {
        return this.mBatteryReceiver.getBatteryInfo();
    }

    public void registerAppStatusListener(AppStatusListener listener) {
        if (this.mAppStatusListener != null) {
            this.mAppStatusListener.add(listener);
            this.mAppStatus.registerAppStatusListener(listener);
        }
    }

    public boolean unregisterAppStatusListener(AppStatusListener listener) {
        this.mAppStatus.unregisterAppStatusListener(listener);
        return mAppStatusListener != null && this.mAppStatusListener.remove(listener);
    }

    public int getAppStatus() {
        return this.mAppStatus.getAppStatus();
    }

    public void dispatcherWifiState(boolean state) {
        Object[] listeners = this.mAppStatusListener.toArray();
        for (Object listener : listeners) {
            ((AppStatusListener) listener).wifiStatusChange(state);
        }
    }

    public void dispatcherBluetoothState(boolean state) {
        Object[] listeners = this.mAppStatusListener.toArray();
        for (Object listener : listeners) {
            ((AppStatusListener) listener).bluetoothStatusChange(state);
        }
    }

    public void dispatcherScreenState(boolean state) {
        Object[] listeners = this.mAppStatusListener.toArray();
        for (Object listener : listeners) {
            ((AppStatusListener) listener).screenStatusChange(state);
        }
    }

    public void dispatcherGpsState(boolean state) {
        Object[] listeners = this.mAppStatusListener.toArray();
        for (Object listener : listeners) {
            ((AppStatusListener) listener).gpsStatusChange(state);
        }
    }

    public void dispatcherNetworkState(boolean state) {
        if (this.mAppStatusListener != null && this.mAppStatusListener.size() != 0) {
            Object[] listeners = this.mAppStatusListener.toArray();
            for (Object listener : listeners) {
                ((AppStatusListener) listener).networkStatusChange(state);
            }
        }
    }

    public void dispatcherBatteryState(BatteryInfo batteryInfo) {
        Object[] listeners = this.mAppStatusListener.toArray();
        for (Object listener : listeners) {
            ((AppStatusListener) listener).batteryStatusChange(batteryInfo);
        }
        collection();
    }

    public void dispatcherUserPresent() {
        Object[] listeners = this.mAppStatusListener.toArray();
        for (Object listener : listeners) {
            ((AppStatusListener) listener).screenUserPresent();
        }
    }

    public void dispatcherThreadInfo(){
        Object[] listeners = this.mAppStatusListener.toArray();
        if (threadSwitchOn){
            ThreadManagerUtils threadManager = ThreadManagerUtils.getInstance();
            ThreadInfo threadInfo = new ThreadInfo();
            threadInfo.setThreadCount(threadManager.getThreadCount());
            threadInfo.setBlockThreadCount(threadManager.getBlockThread());
            threadInfo.setRunningThreadCount(threadManager.getRunningThread());
            threadInfo.setTimeWaitingThreadCount(threadManager.getTimeWaitingThread());
            threadInfo.setWaitingThreadCount(threadManager.getWaitingThread());
            for (Object listener : listeners) {
                ((AppStatusListener) listener).appThreadStatusChange(threadInfo);
            }
        }
    }

    public static class Builder {
        /**
         * 时间间隔
         */
        private int interval;
        /**
         * 上下文
         */
        private Context context;
        /**
         * file文件
         */
        private File file;
        private TraceLogListener traceLog;
        /**
         * 是否开启线程监控，默认false
         */
        private boolean threadSwitchOn = false;

        public Builder() {
        }

        public AppStatusManager.Builder interval(int interval) {
            this.interval = interval;
            return this;
        }

        public AppStatusManager.Builder file(File file) {
            this.file = file;
            return this;
        }

        public AppStatusManager.Builder context(Context context) {
            this.context = context;
            return this;
        }

        public AppStatusManager.Builder traceLog(TraceLogListener trace) {
            this.traceLog = trace;
            return this;
        }

        public AppStatusManager.Builder threadSwitchOn(boolean threadSwitchOn) {
            this.threadSwitchOn = threadSwitchOn;
            return this;
        }

        public AppStatusManager builder() {
            if (this.context == null) {
                throw new NullPointerException("context is null");
            } else if (this.file == null) {
                throw new NullPointerException("file is null");
            } else {
                if (this.interval == 0) {
                    this.interval = 6000;
                }
                return new AppStatusManager(this);
            }
        }
    }
}
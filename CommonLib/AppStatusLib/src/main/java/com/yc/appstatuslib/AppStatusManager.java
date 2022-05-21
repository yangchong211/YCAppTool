package com.yc.appstatuslib;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.IntentFilter;

import com.yc.appprocesslib.AppStateMonitor;
import com.yc.appstatuslib.broadcast.BatteryBroadcastReceiver;
import com.yc.appstatuslib.broadcast.BluetoothBroadcastReceiver;
import com.yc.appstatuslib.broadcast.GpsBroadcastReceiver;
import com.yc.appstatuslib.broadcast.NetWorkBroadcastReceiver;
import com.yc.appstatuslib.broadcast.ScreenBroadcastReceiver;
import com.yc.appstatuslib.broadcast.WifiBroadcastReceiver;
import com.yc.appstatuslib.info.AppBatteryInfo;
import com.yc.appstatuslib.info.AppThreadInfo;
import com.yc.appstatuslib.listener.AppLogListener;
import com.yc.appstatuslib.listener.AppStatusListener;
import com.yc.appstatuslib.utils.ThreadManagerUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time   : 2017/5/18
 *     desc   : app广播状态管理
 *     revise :
 * </pre>
 */
public final class AppStatusManager {

    private final List<AppStatusListener> mAppStatusListener;
    private final BatteryBroadcastReceiver mBatteryReceiver;
    private final GpsBroadcastReceiver mGpsReceiver;
    private final NetWorkBroadcastReceiver mNetWorkReceiver;
    private final ScreenBroadcastReceiver mScreenReceiver;
    private final WifiBroadcastReceiver mWifiBroadcastReceiver;
    private final BluetoothBroadcastReceiver mBluetoothReceiver;
    private final Context mContext;
    private final boolean threadSwitchOn;

    private AppStatusManager(Builder builder) {
        mAppStatusListener = new ArrayList<>();
        mBatteryReceiver = new BatteryBroadcastReceiver(this);
        mGpsReceiver = new GpsBroadcastReceiver(this);
        mNetWorkReceiver = new NetWorkBroadcastReceiver(this);
        mScreenReceiver = new ScreenBroadcastReceiver(this);
        mWifiBroadcastReceiver = new WifiBroadcastReceiver(this);
        mBluetoothReceiver = new BluetoothBroadcastReceiver(this);
        mContext = builder.context;
        threadSwitchOn = builder.threadSwitchOn;
        init();
    }

    /**
     * 注册广播
     */
    private void init() {
        initBatteryReceiver(mContext);
        initGpsReceiver(mContext);
        initNetworkReceiver(mContext);
        initScreenReceiver(mContext);
        initWifiReceiver(mContext);
        initBluetoothReceiver(mContext);
    }

    /**
     * 注册电量监听广播
     * 因为系统规定监听电量变化的广播接收器不能静态注册，所以这里只能使用动态注册的方式
     * @param context   上下文
     */
    private void initBatteryReceiver(Context context) {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.BATTERY_CHANGED");
        context.registerReceiver(mBatteryReceiver, filter);
    }

    /**
     * 注册GPS监听广播
     * @param context   上下文
     */
    private void initGpsReceiver(Context context) {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.location.PROVIDERS_CHANGED");
        context.registerReceiver(mGpsReceiver, filter);
    }

    /**
     * 注册网络广播
     * @param context   上下文
     */
    private void initNetworkReceiver(Context context) {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        context.registerReceiver(mNetWorkReceiver, filter);
    }

    /**
     * 注册屏幕监听广播
     * @param context   上下文
     */
    private void initScreenReceiver(Context context) {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.SCREEN_ON");
        filter.addAction("android.intent.action.SCREEN_OFF");
        filter.addAction("android.intent.action.USER_PRESENT");
        context.registerReceiver(mScreenReceiver, filter);
    }

    /**
     * 注册Wi-Fi监听广播
     * @param context   上下文
     */
    private void initWifiReceiver(Context context) {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        context.registerReceiver(mWifiBroadcastReceiver, filter);
    }


    /**
     * 注册蓝牙监听广播
     * @param context   上下文
     */
    private void initBluetoothReceiver(Context context) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        //filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        context.registerReceiver(mBluetoothReceiver, filter);
    }

    /**
     * 解绑操作
     */
    public void destroy() {
        mContext.unregisterReceiver(mBatteryReceiver);
        mContext.unregisterReceiver(mGpsReceiver);
        mContext.unregisterReceiver(mNetWorkReceiver);
        mContext.unregisterReceiver(mScreenReceiver);
        mContext.unregisterReceiver(mBluetoothReceiver);
        mAppStatusListener.clear();
    }

    public AppBatteryInfo getBatteryInfo() {
        return mBatteryReceiver.getBatteryInfo();
    }

    public void registerAppStatusListener(AppStatusListener listener) {
        if (mAppStatusListener != null) {
            mAppStatusListener.add(listener);
        }
    }

    public boolean unregisterAppStatusListener(AppStatusListener listener) {
        return mAppStatusListener != null && mAppStatusListener.remove(listener);
    }

    public int getAppStatus() {
        return AppStateMonitor.getInstance().getState();
    }

    public void dispatcherWifiState(boolean state) {
        Object[] listeners = mAppStatusListener.toArray();
        if (listeners!=null){
            for (Object listener : listeners) {
                ((AppStatusListener) listener).wifiStatusChange(state);
            }
        }
    }

    public void dispatcherBluetoothState(boolean state) {
        Object[] listeners = mAppStatusListener.toArray();
        if (listeners!=null){
            for (Object listener : listeners) {
                ((AppStatusListener) listener).bluetoothStatusChange(state);
            }
        }
    }

    public void dispatcherScreenState(boolean state) {
        Object[] listeners = mAppStatusListener.toArray();
        if (listeners!=null){
            for (Object listener : listeners) {
                ((AppStatusListener) listener).screenStatusChange(state);
            }
        }
    }

    public void dispatcherGpsState(boolean state) {
        Object[] listeners = mAppStatusListener.toArray();
        if (listeners!=null){
            for (Object listener : listeners) {
                ((AppStatusListener) listener).gpsStatusChange(state);
            }
        }
    }

    public void dispatcherNetworkState(boolean state) {
        if (mAppStatusListener != null && mAppStatusListener.size() != 0) {
            Object[] listeners = mAppStatusListener.toArray();
            if (listeners!=null){
                for (Object listener : listeners) {
                    ((AppStatusListener) listener).networkStatusChange(state);
                }
            }
        }
    }

    public void dispatcherBatteryState(AppBatteryInfo batteryInfo) {
        Object[] listeners = mAppStatusListener.toArray();
        if (listeners!=null){
            for (Object listener : listeners) {
                ((AppStatusListener) listener).batteryStatusChange(batteryInfo);
            }
        }
    }

    public void dispatcherUserPresent() {
        Object[] listeners = mAppStatusListener.toArray();
        if (listeners!=null){
            for (Object listener : listeners) {
                ((AppStatusListener) listener).screenUserPresent();
            }
        }
    }

    public void dispatcherThreadInfo(){
        Object[] listeners = mAppStatusListener.toArray();
        if (threadSwitchOn){
            ThreadManagerUtils threadManager = ThreadManagerUtils.getInstance();
            AppThreadInfo threadInfo = new AppThreadInfo();
            threadInfo.setThreadCount(threadManager.getThreadCount());
            threadInfo.setBlockThreadCount(threadManager.getBlockThread());
            threadInfo.setRunningThreadCount(threadManager.getRunningThread());
            threadInfo.setTimeWaitingThreadCount(threadManager.getTimeWaitingThread());
            threadInfo.setWaitingThreadCount(threadManager.getWaitingThread());
            if (listeners!=null){
                for (Object listener : listeners) {
                    ((AppStatusListener) listener).appThreadStatusChange(threadInfo);
                }
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
        /**
         * 日志监听
         */
        private AppLogListener traceLog;
        /**
         * 是否开启线程监控，默认false
         */
        private boolean threadSwitchOn = false;

        public Builder() {
        }

        public Builder interval(int interval) {
            this.interval = interval;
            return this;
        }

        public Builder file(File file) {
            this.file = file;
            return this;
        }

        public Builder context(Context context) {
            this.context = context;
            return this;
        }

        public Builder traceLog(AppLogListener trace) {
            this.traceLog = trace;
            return this;
        }

        public Builder threadSwitchOn(boolean threadSwitchOn) {
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
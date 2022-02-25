package com.yc.appstatuslib.listener;

import com.yc.appstatuslib.info.BatteryInfo;
import com.yc.appstatuslib.info.ThreadInfo;

public interface AppStatusListener {

    /**
     * wifi状态监听
     * @param isWifiOn              true表示Wi-Fi可以打开，false表示关闭
     */
    void wifiStatusChange(boolean isWifiOn);

    /**
     * gps状态监听
     * @param isGpsOn               true表示gps可以打开，false表示关闭
     */
    void gpsStatusChange(boolean isGpsOn);

    /**
     * 网络状态监听
     * @param isConnect             true表示网络可以打开，false表示关闭
     */
    void networkStatusChange(boolean isConnect);

    /**
     * 屏幕状态监听
     * @param isScreenOn            true表示屏幕打开，false表示屏幕锁屏
     */
    void screenStatusChange(boolean isScreenOn);

    /**
     * 屏幕状态监听，userPresent表示屏幕打开并且使用呢
     */
    void screenUserPresent();

    /**
     * 蓝牙状态监听
     * @param isBluetoothOn         true表示蓝牙打开，false表示蓝牙断开连接
     */
    void bluetoothStatusChange(boolean isBluetoothOn);

    /**
     * 电量状态监听
     * @param batteryInfo           电量信息
     */
    void batteryStatusChange(BatteryInfo batteryInfo);


    /**
     * app前后台状态监听
     * @param isBack                true表示在推到后台，false表示回到前台
     */
    void appOnFrontOrBackChange(boolean isBack);

    /**
     * 线程类型和数量监听
     * @param threadInfo            线程属性
     */
    void appThreadStatusChange(ThreadInfo threadInfo);

}

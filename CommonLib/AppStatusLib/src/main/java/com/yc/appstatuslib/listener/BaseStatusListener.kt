package com.yc.appstatuslib.listener;

import com.yc.appstatuslib.info.AppBatteryInfo;
import com.yc.appstatuslib.info.AppThreadInfo;

/**
 * <pre>
 *     @author: yangchong
 *     email  : yangchong211@163.com
 *     time   : 2017/5/18
 *     desc   : base实现
 *     revise :
 * </pre>
 */
public class BaseStatusListener implements AppStatusListener{

    /*
     * 监控app的电量变化
     * 监控app的屏幕变化
     * 监控app的网络变化
     * 监控app的线程状态
     * 监控app的内存变化
     * 监控app的前后台变化
     */

    /**
     * wifi状态监听
     * @param isWifiOn              true表示Wi-Fi可以打开，false表示关闭
     */
    @Override
    public void wifiStatusChange(boolean isWifiOn) {

    }

    /**
     * gps状态监听
     * @param isGpsOn               true表示gps可以打开，false表示关闭
     */
    @Override
    public void gpsStatusChange(boolean isGpsOn) {

    }

    /**
     * 网络状态监听
     * @param isConnect             true表示网络可以打开，false表示关闭
     */
    @Override
    public void networkStatusChange(boolean isConnect) {

    }

    /**
     * 屏幕状态监听
     * @param isScreenOn            true表示屏幕打开，false表示屏幕锁屏
     */
    @Override
    public void screenStatusChange(boolean isScreenOn) {

    }

    /**
     * 屏幕状态监听，userPresent表示屏幕打开并且使用呢
     */
    @Override
    public void screenUserPresent() {

    }

    /**
     * 蓝牙状态监听
     * @param isBluetoothOn         true表示蓝牙打开，false表示蓝牙断开连接
     */
    @Override
    public void bluetoothStatusChange(boolean isBluetoothOn) {

    }

    /**
     * 电量状态监听
     * @param batteryInfo           电量信息
     */
    @Override
    public void batteryStatusChange(AppBatteryInfo batteryInfo) {

    }

    /**
     * 线程类型和数量监听
     * @param threadInfo            线程属性
     */
    @Override
    public void appThreadStatusChange(AppThreadInfo threadInfo) {

    }
}

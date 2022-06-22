package com.yc.appstatuslib.listener

import com.yc.appstatuslib.info.AppBatteryInfo
import com.yc.appstatuslib.info.AppThreadInfo

/**
 * @author: yangchong
 * email  : yangchong211@163.com
 * time   : 2017/5/18
 * desc   : base实现
 * revise :
 */
open class BaseStatusListener : AppStatusListener {
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
    override fun wifiStatusChange(isWifiOn: Boolean) {}

    /**
     * gps状态监听
     * @param isGpsOn               true表示gps可以打开，false表示关闭
     */
    override fun gpsStatusChange(isGpsOn: Boolean) {}

    /**
     * 网络状态监听
     * @param isConnect             true表示网络可以打开，false表示关闭
     */
    override fun networkStatusChange(isConnect: Boolean) {}

    /**
     * 屏幕状态监听
     * @param isScreenOn            true表示屏幕打开，false表示屏幕锁屏
     */
    override fun screenStatusChange(isScreenOn: Boolean) {}

    /**
     * 屏幕状态监听，userPresent表示屏幕打开并且使用呢
     */
    override fun screenUserPresent() {}

    /**
     * 蓝牙状态监听
     * @param isBluetoothOn         true表示蓝牙打开，false表示蓝牙断开连接
     */
    override fun bluetoothStatusChange(isBluetoothOn: Boolean) {}

    /**
     * 电量状态监听
     * @param batteryInfo           电量信息
     */
    override fun batteryStatusChange(batteryInfo: AppBatteryInfo?) {}

    /**
     * 线程类型和数量监听
     * @param threadInfo            线程属性
     */
    override fun appThreadStatusChange(threadInfo: AppThreadInfo?) {}
}
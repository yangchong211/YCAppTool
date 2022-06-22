package com.yc.appstatuslib.listener

import com.yc.appstatuslib.info.AppBatteryInfo
import com.yc.appstatuslib.info.AppThreadInfo

/**
 * @author yangchong
 * email  : yangchong211@163.com
 * time   : 2017/5/18
 * desc   : 广播状态监听
 * revise :
 */
interface AppStatusListener {
    /**
     * wifi状态监听
     * @param isWifiOn              true表示Wi-Fi可以打开，false表示关闭
     */
    fun wifiStatusChange(isWifiOn: Boolean)

    /**
     * gps状态监听
     * @param isGpsOn               true表示gps可以打开，false表示关闭
     */
    fun gpsStatusChange(isGpsOn: Boolean)

    /**
     * 网络状态监听
     * @param isConnect             true表示网络可以打开，false表示关闭
     */
    fun networkStatusChange(isConnect: Boolean)

    /**
     * 屏幕状态监听
     * @param isScreenOn            true表示屏幕打开，false表示屏幕锁屏
     */
    fun screenStatusChange(isScreenOn: Boolean)

    /**
     * 屏幕状态监听，userPresent表示屏幕打开并且使用呢
     */
    fun screenUserPresent()

    /**
     * 蓝牙状态监听
     * @param isBluetoothOn         true表示蓝牙打开，false表示蓝牙断开连接
     */
    fun bluetoothStatusChange(isBluetoothOn: Boolean)

    /**
     * 电量状态监听
     * @param batteryInfo           电量信息
     */
    fun batteryStatusChange(batteryInfo: AppBatteryInfo?)

    /**
     * 线程类型和数量监听
     * @param threadInfo            线程属性
     */
    fun appThreadStatusChange(threadInfo: AppThreadInfo?)
}
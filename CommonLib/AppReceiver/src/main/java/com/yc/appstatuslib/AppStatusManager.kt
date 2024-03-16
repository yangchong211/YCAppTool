package com.yc.appstatuslib

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.IntentFilter
import com.yc.appcommoninter.inter.IEventTrack
import com.yc.appcommoninter.inter.ILogger
import com.yc.appcommoninter.inter.IMonitorToggle
import com.yc.appstatuslib.broadcast.*
import com.yc.appstatuslib.info.AppBatteryInfo
import com.yc.appstatuslib.info.AppThreadInfo
import com.yc.appstatuslib.listener.AppStatusListener
import com.yc.appstatuslib.utils.ThreadManagerUtils.Companion.instance
import java.io.File
import java.util.*

/**
 * <pre>
 * @author yangchong
 * email  : yangchong211@163.com
 * time   : 2017/5/18
 * desc   : app广播状态管理
 * revise :
</pre> *
 */
class AppStatusManager private constructor(builder: Builder) {

    private val mAppStatusListener: MutableList<AppStatusListener>?
    private var mBatteryReceiver: BatteryBroadcastReceiver? = null
    private var mGpsReceiver: GpsBroadcastReceiver? = null
    private var mNetWorkReceiver: NetWorkBroadcastReceiver? = null
    private var mScreenReceiver: ScreenBroadcastReceiver? = null
    private var mWifiBroadcastReceiver: WifiBroadcastReceiver? = null
    private var mBluetoothReceiver: BluetoothBroadcastReceiver? = null
    private val mContext: Context?
    private val threadSwitchOn: Boolean
    private val batterySwitchOn: Boolean
    private val gpsSwitchOn: Boolean
    private val networkSwitchOn: Boolean
    private val screenSwitchOn: Boolean
    private val wifiSwitchOn: Boolean
    private val bluetoothSwitchOn: Boolean

    /**
     * 注册广播
     */
    private fun init() {
        if (batterySwitchOn) {
            initBatteryReceiver(mContext)
        }
        if (gpsSwitchOn) {
            initGpsReceiver(mContext)
        }
        if (networkSwitchOn) {
            initNetworkReceiver(mContext)
        }
        if (screenSwitchOn) {
            initScreenReceiver(mContext)
        }
        if (wifiSwitchOn) {
            initWifiReceiver(mContext)
        }
        if (bluetoothSwitchOn) {
            initBluetoothReceiver(mContext)
        }
    }

    /**
     * 注册电量监听广播
     * 因为系统规定监听电量变化的广播接收器不能静态注册，所以这里只能使用动态注册的方式
     * @param context   上下文
     */
    private fun initBatteryReceiver(context: Context?) {
        val filter = IntentFilter()
        filter.addAction("android.intent.action.BATTERY_CHANGED")
        context?.registerReceiver(mBatteryReceiver, filter)
    }

    /**
     * 注册GPS监听广播
     * @param context   上下文
     */
    private fun initGpsReceiver(context: Context?) {
        val filter = IntentFilter()
        filter.addAction("android.location.PROVIDERS_CHANGED")
        context?.registerReceiver(mGpsReceiver, filter)
    }

    /**
     * 注册网络广播
     * @param context   上下文
     */
    private fun initNetworkReceiver(context: Context?) {
        val filter = IntentFilter()
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE")
        context?.registerReceiver(mNetWorkReceiver, filter)
    }

    /**
     * 注册屏幕监听广播
     * @param context   上下文
     */
    private fun initScreenReceiver(context: Context?) {
        val filter = IntentFilter()
        filter.addAction("android.intent.action.SCREEN_ON")
        filter.addAction("android.intent.action.SCREEN_OFF")
        filter.addAction("android.intent.action.USER_PRESENT")
        context?.registerReceiver(mScreenReceiver, filter)
    }

    /**
     * 注册Wi-Fi监听广播
     * @param context   上下文
     */
    private fun initWifiReceiver(context: Context?) {
        val filter = IntentFilter()
        filter.addAction("android.net.wifi.WIFI_STATE_CHANGED")
        context?.registerReceiver(mWifiBroadcastReceiver, filter)
    }

    /**
     * 注册蓝牙监听广播
     * @param context   上下文
     */
    private fun initBluetoothReceiver(context: Context?) {
        val filter = IntentFilter()
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED)
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED)
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
        context?.registerReceiver(mBluetoothReceiver, filter)
    }

    /**
     * 解绑操作【App推出的时候可以调用】
     */
    fun destroy() {
        if (mBatteryReceiver != null) {
            mContext?.unregisterReceiver(mBatteryReceiver)
        }
        if (mGpsReceiver != null) {
            mContext?.unregisterReceiver(mGpsReceiver)
        }
        if (mNetWorkReceiver != null) {
            mContext?.unregisterReceiver(mNetWorkReceiver)
        }
        if (mScreenReceiver != null) {
            mContext?.unregisterReceiver(mScreenReceiver)
        }
        if (mBluetoothReceiver != null) {
            mContext?.unregisterReceiver(mBluetoothReceiver)
        }
        mAppStatusListener?.clear()
    }

    val batteryInfo: AppBatteryInfo?
        get() = mBatteryReceiver?.batteryInfo

    /**
     * 注册广播监听回调
     */
    fun registerAppStatusListener(listener: AppStatusListener) {
        if (mAppStatusListener?.contains(listener) != true) {
            mAppStatusListener?.add(listener)
        }
    }

    /**
     * 解绑注册广播监听回调
     */
    fun unregisterAppStatusListener(listener: AppStatusListener): Boolean {
        return mAppStatusListener != null && mAppStatusListener.remove(listener)
    }

    /**
     * internal 直译为 内部的； 在kt中 internal 限制了 被修饰(类、方法...) 只能在当前model 中使用。
     */
    internal fun dispatcherWifiState(state: Boolean) {
        val listeners: Array<Any>? = mAppStatusListener?.toTypedArray()
        if (listeners != null) {
            for (listener in listeners) {
                (listener as AppStatusListener).wifiStatusChange(state)
            }
        }
    }

    internal fun dispatcherBluetoothState(state: Boolean) {
        val listeners: Array<Any>? = mAppStatusListener?.toTypedArray()
        if (listeners != null) {
            for (listener in listeners) {
                (listener as AppStatusListener).bluetoothStatusChange(state)
            }
        }
    }

    internal fun dispatcherScreenState(state: Boolean) {
        val listeners: Array<Any>? = mAppStatusListener?.toTypedArray()
        if (listeners != null) {
            for (listener in listeners) {
                (listener as AppStatusListener).screenStatusChange(state)
            }
        }
    }

    internal fun dispatcherGpsState(state: Boolean) {
        val listeners: Array<Any>? = mAppStatusListener?.toTypedArray()
        if (listeners != null) {
            for (listener in listeners) {
                (listener as AppStatusListener).gpsStatusChange(state)
            }
        }
    }

    internal fun dispatcherNetworkState(state: Boolean) {
        val listeners: Array<Any>? = mAppStatusListener?.toTypedArray()
        if (listeners != null) {
            for (listener in listeners) {
                (listener as AppStatusListener).networkStatusChange(state)
            }
        }
    }

    internal fun dispatcherBatteryState(batteryInfo: AppBatteryInfo?) {
        val listeners: Array<Any>? = mAppStatusListener?.toTypedArray()
        if (listeners != null) {
            for (listener in listeners) {
                (listener as AppStatusListener).batteryStatusChange(batteryInfo)
            }
        }
    }

    internal fun dispatcherUserPresent() {
        val listeners: Array<Any>? = mAppStatusListener?.toTypedArray()
        if (listeners != null) {
            for (listener in listeners) {
                (listener as AppStatusListener).screenUserPresent()
            }
        }
    }

    internal fun dispatcherThreadInfo() {
        val listeners: Array<Any>? = mAppStatusListener?.toTypedArray()
        if (threadSwitchOn && listeners != null) {
            val threadManager = instance
            val threadInfo = AppThreadInfo()
            if (threadManager != null) {
                threadInfo.threadCount = threadManager.threadCount
                threadInfo.blockThreadCount = threadManager.blockThread
                threadInfo.runningThreadCount = threadManager.runningThread
                threadInfo.timeWaitingThreadCount = threadManager.timeWaitingThread
                threadInfo.waitingThreadCount = threadManager.waitingThread
                for (listener in listeners) {
                    (listener as AppStatusListener).appThreadStatusChange(threadInfo)
                }
            }
        }
    }

    class Builder {

        /**
         * 上下文
         */
        internal var context: Context? = null

        /**
         * file文件
         */
        private var file: File? = null

        /**
         * 日志监听
         */
        private var traceLog: com.yc.appcommoninter.inter.ILogger? = null

        /**
         * ab测试
         */
        private var monitorToggle: com.yc.appcommoninter.inter.IMonitorToggle? = null

        /**
         * 事件统计
         */
        private var eventTrack: com.yc.appcommoninter.inter.IEventTrack? = null

        /**
         * 是否开启线程监控，默认false
         */
        internal var threadSwitchOn = false
        internal var batterySwitchOn = false
        internal var gpsSwitchOn = false
        internal var networkSwitchOn = false
        internal var screenSwitchOn = false
        internal var wifiSwitchOn = false
        internal var bluetoothSwitchOn = false

        fun file(file: File?): Builder {
            this.file = file
            return this
        }

        fun context(context: Context?): Builder {
            this.context = context
            return this
        }

        fun traceLog(trace: com.yc.appcommoninter.inter.ILogger?): Builder {
            traceLog = trace
            return this
        }

        fun setMonitorToggle(toggle: com.yc.appcommoninter.inter.IMonitorToggle): Builder {
            this.monitorToggle = toggle
            return this
        }

        fun setEventTrack(eventTrack: com.yc.appcommoninter.inter.IEventTrack): Builder {
            this.eventTrack = eventTrack
            return this
        }

        fun threadSwitchOn(threadSwitchOn: Boolean): Builder {
            this.threadSwitchOn = threadSwitchOn
            return this
        }

        fun batterySwitchOn(batterySwitchOn: Boolean): Builder {
            this.batterySwitchOn = batterySwitchOn
            return this
        }

        fun gpsSwitchOn(gpsSwitchOn: Boolean): Builder {
            this.gpsSwitchOn = gpsSwitchOn
            return this
        }

        fun networkSwitchOn(networkSwitchOn: Boolean): Builder {
            this.networkSwitchOn = networkSwitchOn
            return this
        }

        fun screenSwitchOn(screenSwitchOn: Boolean): Builder {
            this.screenSwitchOn = screenSwitchOn
            return this
        }

        fun wifiSwitchOn(wifiSwitchOn: Boolean): Builder {
            this.wifiSwitchOn = wifiSwitchOn
            return this
        }

        fun bluetoothSwitchOn(bluetoothSwitchOn: Boolean): Builder {
            this.bluetoothSwitchOn = bluetoothSwitchOn
            return this
        }

        fun builder(): AppStatusManager {
            return when {
                context == null -> {
                    throw NullPointerException("context is null")
                }

                else -> {
                    AppStatusManager(this)
                }
            }
        }
    }

    init {
        mAppStatusListener = ArrayList()
        mBatteryReceiver = BatteryBroadcastReceiver(this)
        mGpsReceiver = GpsBroadcastReceiver(this)
        mNetWorkReceiver = NetWorkBroadcastReceiver(this)
        mScreenReceiver = ScreenBroadcastReceiver(this)
        mWifiBroadcastReceiver = WifiBroadcastReceiver(this)
        mBluetoothReceiver = BluetoothBroadcastReceiver(this)
        mContext = builder.context
        threadSwitchOn = builder.threadSwitchOn
        batterySwitchOn = builder.batterySwitchOn
        gpsSwitchOn = builder.gpsSwitchOn
        networkSwitchOn = builder.networkSwitchOn
        screenSwitchOn = builder.screenSwitchOn
        wifiSwitchOn = builder.wifiSwitchOn
        bluetoothSwitchOn = builder.bluetoothSwitchOn
        if (mContext == null) {
            throw NullPointerException("app status lib init , context must be not null")
        }
        init()
    }

}
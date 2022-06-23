package com.yc.appstatuslib

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.IntentFilter
import com.yc.appcommoninter.IEventTrack
import com.yc.appcommoninter.ILogger
import com.yc.appcommoninter.IMonitorToggle
import com.yc.appprocesslib.AppStateMonitor
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
    private val mBatteryReceiver: BatteryBroadcastReceiver
    private val mGpsReceiver: GpsBroadcastReceiver
    private val mNetWorkReceiver: NetWorkBroadcastReceiver
    private val mScreenReceiver: ScreenBroadcastReceiver
    private val mWifiBroadcastReceiver: WifiBroadcastReceiver
    private val mBluetoothReceiver: BluetoothBroadcastReceiver
    private val mContext: Context?
    private val threadSwitchOn: Boolean

    /**
     * 注册广播
     */
    private fun init() {
        initBatteryReceiver(mContext)
        initGpsReceiver(mContext)
        initNetworkReceiver(mContext)
        initScreenReceiver(mContext)
        initWifiReceiver(mContext)
        initBluetoothReceiver(mContext)
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
     * 解绑操作
     */
    fun destroy() {
        mContext?.unregisterReceiver(mBatteryReceiver)
        mContext?.unregisterReceiver(mGpsReceiver)
        mContext?.unregisterReceiver(mNetWorkReceiver)
        mContext?.unregisterReceiver(mScreenReceiver)
        mContext?.unregisterReceiver(mBluetoothReceiver)
        mAppStatusListener?.clear()
    }

    val batteryInfo: AppBatteryInfo?
        get() = mBatteryReceiver.batteryInfo

    fun registerAppStatusListener(listener: AppStatusListener) {
        mAppStatusListener?.add(listener)
    }

    fun unregisterAppStatusListener(listener: AppStatusListener): Boolean {
        return mAppStatusListener != null && mAppStatusListener.remove(listener)
    }

    val appStatus: Int
        get() = AppStateMonitor.getInstance().state

    fun dispatcherWifiState(state: Boolean) {
        val listeners: Array<Any>? = mAppStatusListener?.toTypedArray()
        if (listeners != null) {
            for (listener in listeners) {
                (listener as AppStatusListener).wifiStatusChange(state)
            }
        }
    }

    fun dispatcherBluetoothState(state: Boolean) {
        val listeners: Array<Any>? = mAppStatusListener?.toTypedArray()
        if (listeners != null) {
            for (listener in listeners) {
                (listener as AppStatusListener).bluetoothStatusChange(state)
            }
        }
    }

    fun dispatcherScreenState(state: Boolean) {
        val listeners: Array<Any>? = mAppStatusListener?.toTypedArray()
        if (listeners != null) {
            for (listener in listeners) {
                (listener as AppStatusListener).screenStatusChange(state)
            }
        }
    }

    fun dispatcherGpsState(state: Boolean) {
        val listeners: Array<Any>? = mAppStatusListener?.toTypedArray()
        if (listeners != null) {
            for (listener in listeners) {
                (listener as AppStatusListener).gpsStatusChange(state)
            }
        }
    }

    fun dispatcherNetworkState(state: Boolean) {
        val listeners: Array<Any>? = mAppStatusListener?.toTypedArray()
        if (listeners != null) {
            for (listener in listeners) {
                (listener as AppStatusListener).networkStatusChange(state)
            }
        }
    }

    fun dispatcherBatteryState(batteryInfo: AppBatteryInfo?) {
        val listeners: Array<Any>? = mAppStatusListener?.toTypedArray()
        if (listeners != null) {
            for (listener in listeners) {
                (listener as AppStatusListener).batteryStatusChange(batteryInfo)
            }
        }
    }

    fun dispatcherUserPresent() {
        val listeners: Array<Any>? = mAppStatusListener?.toTypedArray()
        if (listeners != null) {
            for (listener in listeners) {
                (listener as AppStatusListener).screenUserPresent()
            }
        }
    }

    fun dispatcherThreadInfo() {
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
        var context: Context? = null

        /**
         * file文件
         */
        private var file: File? = null

        /**
         * 日志监听
         */
        private var traceLog: ILogger? = null

        /**
         * ab测试
         */
        private var monitorToggle: IMonitorToggle? = null

        /**
         * 事件统计
         */
        private var eventTrack: IEventTrack? = null

        /**
         * 是否开启线程监控，默认false
         */
        var threadSwitchOn = false

        fun file(file: File?): Builder {
            this.file = file
            return this
        }

        fun context(context: Context?): Builder {
            this.context = context
            return this
        }

        fun traceLog(trace: ILogger?): Builder {
            traceLog = trace
            return this
        }

        fun setMonitorToggle(toggle: IMonitorToggle): Builder {
            this.monitorToggle = toggle
            return this
        }

        fun setEventTrack(eventTrack: IEventTrack): Builder {
            this.eventTrack = eventTrack
            return this
        }

        fun threadSwitchOn(threadSwitchOn: Boolean): Builder {
            this.threadSwitchOn = threadSwitchOn
            return this
        }

        fun builder(): AppStatusManager {
            return when {
                context == null -> {
                    throw NullPointerException("context is null")
                }
                file == null -> {
                    throw NullPointerException("file is null")
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
        init()
    }
}
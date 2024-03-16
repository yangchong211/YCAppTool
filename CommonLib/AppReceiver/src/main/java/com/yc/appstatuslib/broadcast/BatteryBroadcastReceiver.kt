package com.yc.appstatuslib.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.BatteryManager
import com.yc.appstatuslib.AppStatusManager
import com.yc.appstatuslib.info.AppBatteryInfo

/**
 * @author: yangchong
 * email  : yangchong211@163.com
 * time   : 2017/5/18
 * desc   : 因为系统规定监听电量变化的广播接收器不能静态注册，所以这里只能使用动态注册的方式了。
 * revise :
 */
class BatteryBroadcastReceiver(private val mManager: AppStatusManager?) : BroadcastReceiver() {

    var batteryInfo: AppBatteryInfo? = AppBatteryInfo()
        private set

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        if (Intent.ACTION_BATTERY_CHANGED == action) {
            val status = intent.getIntExtra(
                BatteryManager.EXTRA_STATUS,
                BatteryManager.BATTERY_STATUS_UNKNOWN
            )
            val health = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, 0)
            // 当前电量
            val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)
            // 总电量
            val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 0)
            //
            val plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0)
            val voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0)
            val temperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0)
            val technology = intent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY)
            val batteryInfo = AppBatteryInfo.buildBattery(
                status, health, level,
                scale, plugged, voltage, temperature, technology
            )
            if (notify(batteryInfo) && mManager != null) {
                this.batteryInfo = batteryInfo
                mManager.dispatcherBatteryState(this.batteryInfo)
            }
            this.batteryInfo = batteryInfo
        }
    }

    private fun notify(batteryInfo: AppBatteryInfo): Boolean {
        return this.batteryInfo == null ||
                this.batteryInfo?.level != batteryInfo.level ||
                this.batteryInfo?.status != batteryInfo.status ||
                this.batteryInfo?.health != batteryInfo.health ||
                this.batteryInfo?.plugged != batteryInfo.plugged ||
                this.batteryInfo?.temperature != batteryInfo.temperature
    }
}
package com.yc.appstatuslib.info

import java.text.SimpleDateFormat
import java.util.*

/**
 * <pre>
 * @author: yangchong
 * email  : yangchong211@163.com
 * time   : 2017/5/18
 * desc   : app电量信息
 * revise :
</pre> *
 */
class AppBatteryInfo {

    var technology = ""
    var temperature = 0
    var voltage = 0
    var level = 0
    var scale = 0
    var status = ""
    var health = ""
    var plugged = ""
    var humanTime = ""

    override fun toString(): String {
        return toStringInfo()
    }

    fun toStringInfo(): String {
        val stringBuilder = StringBuilder()
        stringBuilder.append("technology : ").append(technology).append("\n")
        stringBuilder.append("temperature : ").append(temperature).append("\n")
        stringBuilder.append("voltage : ").append(voltage).append("\n")
        stringBuilder.append("level : ").append(level).append("\n")
        stringBuilder.append("scale : ").append(scale).append("\n")
        stringBuilder.append("status : ").append(status).append("\n")
        stringBuilder.append("health : ").append(health).append("\n")
        stringBuilder.append("plugged : ").append(plugged)
        return stringBuilder.toString()
    }

    companion object {
        fun buildBattery(
            status: Int, health: Int, level: Int, scale: Int,
            plugged: Int, voltage: Int, temperature: Int,
            technology: String?
        ): AppBatteryInfo {
            val batteryInfo = AppBatteryInfo()
            batteryInfo.status = getStatus(status)
            batteryInfo.health = getHealth(health)
            batteryInfo.level = level
            batteryInfo.scale = scale
            batteryInfo.temperature = temperature
            batteryInfo.technology = technology?:""
            batteryInfo.voltage = voltage
            batteryInfo.plugged = getPlugged(plugged)
            val dateFormat = SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss:SSS", Locale.US
            )
            batteryInfo.humanTime = dateFormat.format(Date())
            return batteryInfo
        }

        private fun getStatus(status: Int): String {
            return when (status) {
                1 -> "unknown"
                2 -> "charging"
                3 -> "discharging"
                4 -> "not charging"
                5 -> "full"
                else -> "unknown-status - $status"
            }
        }

        private fun getPlugged(plugged: Int): String {
            return when (plugged) {
                1 -> "plugged ac"
                2 -> "plugged usb"
                4 -> "plugged wireless"
                3 -> "unknown-Plugged - $plugged"
                else -> "unknown-Plugged - $plugged"
            }
        }

        private fun getHealth(health: Int): String {
            return when (health) {
                1 -> "unknown"
                2 -> "good"
                3 -> "overheat"
                4 -> "dead"
                5 -> "voltage"
                6 -> "unspecified failure"
                7 -> "BATTERY_HEALTH_COLD"
                else -> "unknown-health - $health"
            }
        }
    }
}
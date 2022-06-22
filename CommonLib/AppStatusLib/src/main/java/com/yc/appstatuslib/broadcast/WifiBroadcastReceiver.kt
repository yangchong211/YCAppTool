package com.yc.appstatuslib.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import com.yc.appstatuslib.AppStatusManager

/**
 * @author: yangchong
 * email  : yangchong211@163.com
 * time   : 2017/5/18
 * desc   : 网络监听广播
 * revise :
 */
class WifiBroadcastReceiver(private val mManager: AppStatusManager?) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == WifiManager.WIFI_STATE_CHANGED_ACTION) {
            when (intent.getIntExtra("wifi_state", 4)) {
                0, 2, 4 -> {

                }
                1 -> notifyGpsSwitchState(false)
                3 -> notifyGpsSwitchState(true)
                else -> {

                }
            }
        }
    }

    private fun notifyGpsSwitchState(state: Boolean) {
        mManager?.dispatcherWifiState(state)
    }
}
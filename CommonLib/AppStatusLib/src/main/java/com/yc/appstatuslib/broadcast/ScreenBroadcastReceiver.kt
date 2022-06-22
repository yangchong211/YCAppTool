package com.yc.appstatuslib.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.yc.appstatuslib.AppStatusManager

/**
 * <pre>
 * @author: yangchong
 * email  : yangchong211@163.com
 * time   : 2017/5/18
 * desc   : 屏幕监听广播
 * revise :
</pre> *
 */
class ScreenBroadcastReceiver(private val mManager: AppStatusManager?) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (mManager != null) {
            val action = intent.action
            when {
                Intent.ACTION_SCREEN_ON == action -> {
                    notifyScreenSwitchState(true)
                }
                Intent.ACTION_SCREEN_OFF == action -> {
                    notifyScreenSwitchState(false)
                }
                Intent.ACTION_USER_PRESENT == action -> {
                    mManager.dispatcherUserPresent()
                }
            }
        }
    }

    private fun notifyScreenSwitchState(state: Boolean) {
        mManager?.dispatcherScreenState(state)
    }
}
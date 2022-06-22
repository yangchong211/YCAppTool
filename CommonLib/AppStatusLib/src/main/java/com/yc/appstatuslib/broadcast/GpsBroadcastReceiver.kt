package com.yc.appstatuslib.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import com.yc.appstatuslib.AppStatusManager

/**
 * <pre>
 * @author: yangchong
 * email  : yangchong211@163.com
 * time   : 2017/5/18
 * desc   : GPS监听广播
 * revise :
</pre> *
 */
class GpsBroadcastReceiver(private val mManager: AppStatusManager?) : BroadcastReceiver() {

    private var isGpsEnable = false

    override fun onReceive(context: Context, intent: Intent) {
        val intentAction = intent.action
        val action1 = LocationManager.PROVIDERS_CHANGED_ACTION
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val action2 = LocationManager.MODE_CHANGED_ACTION
            if (intentAction == action1 || intentAction == action2) {
                //fix 部分手机连续调用多次广播问题
                if (isGpsEnabled(context) != isGpsEnable) {
                    notifyGpsSwitchState(isGpsEnabled(context))
                }
                isGpsEnable = isGpsEnabled(context)
            }
        } else {
            if (intentAction == action1) {
                if (isGpsEnabled(context) != isGpsEnable) {
                    notifyGpsSwitchState(isGpsEnabled(context))
                }
                isGpsEnable = isGpsEnabled(context)
            }
        }
    }

    private fun notifyGpsSwitchState(state: Boolean) {
        mManager?.dispatcherGpsState(state)
    }

    companion object {
        /**
         * 判断gps是否关闭
         *
         * @param context 上下文
         * @return 返回true表示打开
         */
        fun isGpsEnabled(context: Context): Boolean {
            val allowed = Settings.System.LOCATION_PROVIDERS_ALLOWED
            val gps = Settings.System.getString(context.contentResolver, allowed)
            return !TextUtils.isEmpty(gps) && gps.contains(LocationManager.GPS_PROVIDER)
        }
    }
}
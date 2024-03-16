package com.yc.appstatuslib.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import com.yc.appstatuslib.AppStatusManager

/**
 * <pre>
 * @author: yangchong
 * email  : yangchong211@163.com
 * time   : 2017/5/18
 * desc   : 网络监听广播
 * revise :
</pre> *
 */
class NetWorkBroadcastReceiver(private val mManager: AppStatusManager?) : BroadcastReceiver() {

    private fun isNetworkEnable(context: Context?): Boolean {
        return if (context == null) {
            false
        } else {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            run {
                val aActiveInfo = connectivityManager.activeNetworkInfo
                aActiveInfo != null && aActiveInfo.isAvailable
            }
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (mManager != null) {
            if (!isNetworkEnable(context)) {
                mManager.dispatcherNetworkState(false)
            } else {
                mManager.dispatcherNetworkState(true)
            }
        }
    }
}
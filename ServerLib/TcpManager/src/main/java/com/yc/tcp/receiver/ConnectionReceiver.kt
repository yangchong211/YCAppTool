package com.yc.tcp.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.yc.logclient.LogUtils
import com.yc.networklib.AppNetworkUtils


class ConnectionChangeReceiver(var netChangeCallback: NetWorkStateChangeCallBack?) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val hasNet = AppNetworkUtils.isConnected()
        LogUtils.d("ConnectionChangeReceiver Network changed:${hasNet} ")
        netChangeCallback?.invoke(hasNet)
    }

    fun reset() {
        netChangeCallback = null
    }
}

typealias NetWorkStateChangeCallBack = (hasNetWork: Boolean) -> Unit
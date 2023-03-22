package com.zuoyebang.iot.mid.tcp.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.yc.logclient.LogUtils
import com.yc.networklib.AppNetworkUtils
import com.zuoyebang.iot.mod.tcp.TcpContext
import com.zuoyebang.iot.mod.tcp.TcpLog

/**
 * 创建者:baixuefei
 * 创建日期:2021/3/31 11:03 AM
 */

class ConnectionChangeReceiver(var netChangeCallback: NetWorkStateChangeCallBack?) :
    BroadcastReceiver() {
    override fun onReceive(
        context: Context,
        intent: Intent
    ) {
        val hasNet = AppNetworkUtils.isConnected()
        LogUtils.d("ConnectionChangeReceiver Network changed:${hasNet} ")
        netChangeCallback?.invoke(hasNet)

    }

    fun reset() {
        netChangeCallback = null
    }
}

typealias NetWorkStateChangeCallBack = (hasNetWork: Boolean) -> Unit
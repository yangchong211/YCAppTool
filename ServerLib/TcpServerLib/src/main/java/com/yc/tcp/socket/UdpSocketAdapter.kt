package com.yc.tcp.socket

import com.yc.logclient.LogUtils
import com.yc.tcp.TcpConfig
import com.yc.tcp.data.TcpDataBean
import com.yc.tcp.inter.IReceive
import com.yc.tcp.inter.ReadCallBack
import com.yc.tcp.inter.SocketAdapter
import java.io.BufferedReader
import java.io.DataInputStream

class UdpSocketAdapter : SocketAdapter {

    companion object {
        val TAG: String = UdpSocketAdapter::class.java.simpleName
    }

    override fun read(mInput: DataInputStream?, receive: IReceive, readCallback: ReadCallBack) {
        LogUtils.e(TAG, TcpConfig.THREAD_RUN)

    }

}
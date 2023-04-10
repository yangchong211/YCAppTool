package com.yc.tcp.inter

import com.yc.tcp.data.TcpDataBean


interface ConnectCallBack {
    fun connectSuccess()
    fun connectFail(exception: Exception)
}

interface WriteCallBack {
    fun writeSuccess(data: TcpDataBean)
    fun writeError(data: TcpDataBean, e: Exception?)
}


interface ReadCallBack {
    fun readSuccess(tcpData: TcpDataBean)
    fun readError(e: Exception?)
}


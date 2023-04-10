package com.yc.tcp


object TcpConfig {

    const val TCPVersion: Byte = 1
    fun getServerHost(): String {
        return ""
    }

    fun getServerPort(): Int {
        return 8000
    }
    const val THREAD_RUN = " Run "
    const val THREAD_OVER = " Over "
}

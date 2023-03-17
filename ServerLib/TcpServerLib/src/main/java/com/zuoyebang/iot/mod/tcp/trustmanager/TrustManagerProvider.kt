package com.zuoyebang.iot.mod.tcp.trustmanager

import javax.net.ssl.TrustManager


interface TrustManagerProvider {
    fun create(): Array<TrustManager>
}

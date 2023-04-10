package com.yc.tcp.trustmanager

import javax.net.ssl.TrustManager


interface TrustManagerProvider {
    fun create(): Array<TrustManager>
}

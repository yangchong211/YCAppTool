package com.zuoyebang.iot.mod.tcp.trustmanager

import javax.net.ssl.TrustManager


class TrustManagerProviderUnSafe : TrustManagerProvider {
    override fun create(): Array<TrustManager> {
        return arrayOf(UnSafeTrustManager())
    }
}


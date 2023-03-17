package com.zuoyebang.iot.mod.tcp.trustmanager

import javax.net.ssl.TrustManager


class TrustManagerProviderComplex(
    private val bksAssetFileName: String,
    private val password: String
) : TrustManagerProvider {
    override fun create(): Array<TrustManager> {
        return arrayOf(TrustManagerComplex(bksAssetFileName, password))
    }
}
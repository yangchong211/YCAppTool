package com.yc.tcp.trustmanager

import com.yc.tcp.TcpContext
import java.security.KeyStore
import javax.net.ssl.TrustManager
import javax.net.ssl.TrustManagerFactory


class TrustManagerProviderSelfCheck(
    private val bksAssetFileName: String,
    private val password: String
) : TrustManagerProvider {
    override fun create(): Array<TrustManager> {

        val keystore = KeyStore.getInstance("BKS")
        val password = password.toCharArray()
        keystore.load(
            TcpContext.mContext?.resources?.assets?.open(bksAssetFileName),
            password
        )
        //TrustManager
        val tmFactory = TrustManagerFactory.getInstance("X509")
        tmFactory.init(keystore)
        return tmFactory.trustManagers
    }
}
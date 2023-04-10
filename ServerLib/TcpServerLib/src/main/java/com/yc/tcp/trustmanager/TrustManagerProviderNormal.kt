package com.yc.tcp.trustmanager

import java.security.KeyStore
import javax.net.ssl.TrustManager
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager


/**
 * 常规的仅验证 正规ca证书的TrustManagerProvider
 */
class TrustManagerProviderNormal : TrustManagerProvider {
    override fun create(): Array<TrustManager> {
        var tms = arrayOfNulls<TrustManager>(0)
        val tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        tmf.init(null as KeyStore?)
        tms = tmf.trustManagers
        var trustManagerList: MutableList<TrustManager> = mutableListOf()
        for (i in tms.indices) {
            if (tms[i] is X509TrustManager) {
                trustManagerList.add(tms[i]!!)
                break
            }
        }
        return trustManagerList.toTypedArray()
    }
}
package com.yc.tcp.trustmanager

import com.yc.logclient.LogUtils
import com.yc.tcp.TcpContext
import java.security.KeyStore
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.TrustManager
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

/**
 * 创建日期:2021/8/9 4:57 下午
 * 同时支持可信CA认证和自签名认证(任一种认证通过认为可信)
 */
class TrustManagerComplex(private val assetFileName: String, val password: String) :
    X509TrustManager {

    private var defaultX509TrustManager: X509TrustManager? = null
    private var privateX509TrustManager: X509TrustManager? = null

    init {
        generatePrivateX509Manager()
        findDefaultX509TrustManager()
    }


    private fun generatePrivateX509Manager() {

        val keystore = KeyStore.getInstance("BKS")
        val password = password.toCharArray()
        keystore.load(
            TcpContext.mContext?.resources?.assets?.open(assetFileName),
            password
        )
        //TrustManager
        val tmFactory = TrustManagerFactory.getInstance("X509")
        tmFactory.init(keystore)
        tmFactory.trustManagers.forEachIndexed { index, trustManager ->
            if (trustManager is X509TrustManager) {
                privateX509TrustManager = trustManager
            }
        }
        if (privateX509TrustManager == null) {
            LogUtils.e("generatePrivateX509Manager failed")
        }

    }

    private fun findDefaultX509TrustManager(): X509TrustManager? {
        var tms = arrayOfNulls<TrustManager>(0)
        val tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        tmf.init(null as KeyStore?)
        tms = tmf.trustManagers

        /*
         * Iterate over the returned trustmanagers, look
         * for an instance of X509TrustManager.  If found,
         * use that as our "default" trust manager.
         */
        for (i in tms.indices) {
            if (tms[i] is X509TrustManager) {
                defaultX509TrustManager = tms[i] as X509TrustManager
                break
            }
        }

        if (defaultX509TrustManager == null) {
            LogUtils.e("defaultX509TrustManager failed")
        }
        return defaultX509TrustManager
    }

    override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {

    }

    /**
     * 同时支持可信CA认证和自签名认证(任一种认证通过认为可信)
     */
    override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {

        var success = true
        //CA认证
        if (defaultX509TrustManager != null) {
            try {
                defaultX509TrustManager!!.checkServerTrusted(chain, authType)
                success = true
                return
            } catch (e: CertificateException) {
                // do any special handling here, or rethrow exception.
                success = false
            }
        }


        //自签名认证
        success = try {
            privateX509TrustManager?.checkServerTrusted(chain, authType)
            true
        } catch (e: java.lang.Exception) {
            // do any special handling here, or rethrow exception.
            false
        }

        if (!success) {
            LogUtils.i("@@@@@@@@@@@@@@@@@ checkServerTrusted fails")
            throw CertificateException("https cert verify fails")
        }

    }

    override fun getAcceptedIssuers(): Array<X509Certificate> {
        return defaultX509TrustManager?.acceptedIssuers ?: emptyArray()
    }
}
package com.yc.notcapturelib

import android.content.Context
import android.os.Build
import android.text.TextUtils
import android.util.Log

class ProxyWifiUtils {

    fun checkWifiProxy(context: Context): Boolean {
        val IS_ICS_OR_LATER = Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH
        val proxyAddress: String?
        val proxyPort: Int?
        if (IS_ICS_OR_LATER) {
            proxyAddress = System.getProperty("http.proxyHost")
            val portStr = System.getProperty("http.proxyPort")
            proxyPort = Integer.parseInt(portStr ?: "-1")
        } else {
            proxyAddress = android.net.Proxy.getHost(context)
            proxyPort = android.net.Proxy.getPort(context)
        }
        Log.i("ProxyWifi","proxyAddress : ${proxyAddress}, prot : ${proxyPort}")
        return !TextUtils.isEmpty(proxyAddress) && proxyPort != -1
    }


}
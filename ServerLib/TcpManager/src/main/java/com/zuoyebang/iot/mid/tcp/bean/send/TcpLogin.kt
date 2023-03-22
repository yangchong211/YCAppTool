package com.zuoyebang.iot.mid.tcp.bean.send

import com.google.gson.annotations.SerializedName
import com.yc.logclient.LogUtils
import com.zuoyebang.iot.mid.tcp.TcpFacade
import com.zuoyebang.iot.mid.tcp.TcpPacket
import com.zuoyebang.iot.mid.tcp.TcpType
import com.zuoyebang.iot.mid.tcp.bean.base.TcpSBean
import com.zuoyebang.iot.mod.tcp.TcpLog
import java.nio.charset.Charset
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


/**
 * 创建者:baixuefei
 * 创建日期:2021/3/30 1:11 PM
 */
data class TcpLogin(
    @SerializedName("udid")
    val udid: String,
    @SerializedName("token")
    val token: String,
    @SerializedName("device_type")
    val device_type: Int,
    @SerializedName("app_version")
    val app_version: String,
    @SerializedName("stamp")
    val stamp: Long,
    @SerializedName("sig")
    var sig: String = "",
) : TcpSBean {

    private fun calculateSig(): String {
        val hashMap = HashMap<String, String>()
        hashMap["udid"] = udid
        hashMap["token"] = token
        hashMap["device_type"] = device_type.toString()
        hashMap["app_version"] = app_version
        hashMap["stamp"] = stamp.toString()
        return generateSig(hashMap)
    }

    private fun generateSig(params: HashMap<String, String>): String {

        val secret = getSecret()
        val sb = StringBuilder()
        val sign = if (params.isEmpty()) {
            ""
        } else {
            params.toSortedMap().forEach { entry ->
                sb.append("${entry.key}=${entry.value}&")
            }
            sb.append("secret=$secret")
            //LogUtils.d("generateSig json:${sb}")
            sb.toString().md5()
        }
        LogUtils.d("generateSig sign :${sign}")
        return sign
    }

    override fun getPrivateTag(): String {
        return "${getTypeTag()}_${System.currentTimeMillis()}"
    }

    override fun toTcpPacket(): TcpPacket {

        sig = calculateSig()
        LogUtils.d("TcpLogin:${this}")
        return TcpPacket(
            privateTag = getPrivateTag(),
            type = TcpType.LOGIN,
            payload = toBytes()
        )
    }

    private fun getSecret(): String {
        return TcpFacade.getDynamicInfo(TcpFacade.SECRET) as? String ?: ""
    }

}

fun String.md5(charset: Charset = Charsets.UTF_8): String {
    return try {
        MessageDigest.getInstance("MD5").digest(toByteArray(charset))
            .joinToString("") { "%02x".format(it) }
    } catch (e: NoSuchAlgorithmException) {
        ""
    }
}
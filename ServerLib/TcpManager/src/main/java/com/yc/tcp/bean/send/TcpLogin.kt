package com.yc.tcp.bean.send

import com.google.gson.annotations.SerializedName
import com.yc.tcp.TcpFacade
import com.yc.tcp.TcpPacket
import com.yc.tcp.TcpType
import com.yc.tcp.bean.base.TcpSendBean
import com.yc.tcp.TcpLog
import java.nio.charset.Charset
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


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
) : TcpSendBean {

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
            //TcpLog.d("generateSig json:${sb}")
            sb.toString().md5()
        }
        TcpLog.d("generateSig sign :${sign}")
        return sign
    }

    private fun String.md5(charset: Charset = Charsets.UTF_8): String {
        return try {
            MessageDigest.getInstance("MD5").digest(toByteArray(charset))
                .joinToString("") { "%02x".format(it) }
        } catch (e: NoSuchAlgorithmException) {
            ""
        }
    }

    override fun getPrivateTag(): String {
        return "${getTypeTag()}_${System.currentTimeMillis()}"
    }

    override fun toTcpPacket(): TcpPacket {
        sig = calculateSig()
        TcpLog.d("TcpLogin:${this}")
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
package com.yc.notcapturelib.sign

import com.yc.appencryptlib.Base64Utils
import com.yc.appencryptlib.Md5EncryptUtils
import java.util.*

/**
 * @author yangchong
 * GitHub : https://github.com/yangchong211/YCAppTool
 * time   : 2020/11/30
 * desc   : sign 工具类
 */
object SignGenerator {

    /**
     * 根据请求参数获取sign加密字符串
     */
    fun generate(params: HashMap<String, String>?, secret: String?): String {
        val sb = StringBuilder()
        if (params == null || params.size == 0) {
            return ""
        }
        //toSortedMap是按key的插入顺序排序
        params.toSortedMap().forEach { entry ->
            sb.append("${entry.key}=${entry.value}&")
        }
        //将secret加在参数字符串的尾部后进行MD5加密
        if (secret != null && secret.isNotEmpty()) {
            sb.append("secret=").append(secret)
        }
        val paramsString = sb.toString()
        return Md5EncryptUtils.getMd5(paramsString)
    }

    /**
     * 根据请求参数获取sign加密字符串
     */
    fun generateBase64(params: HashMap<String, String>?, secret: String?): String {
        val sb = StringBuilder()
        if (params == null || params.size == 0) {
            return ""
        }
        val entries: Set<Map.Entry<String, String>> = params.entries
        for ((key, value) in entries) {
            sb.append(key).append("=").append(value).append("&")
        }
        if (secret != null && secret.isNotEmpty()) {
            sb.append("secret=").append(secret)
        }
        val paramsString = sb.toString()
        val encodeParams = Base64Utils.encodeToStringWrap(paramsString.toByteArray())
        return if (encodeParams != null && encodeParams.isNotEmpty()) {
            Md5EncryptUtils.getMd5(encodeParams)
        } else Md5EncryptUtils.getMd5(paramsString)
    }


}
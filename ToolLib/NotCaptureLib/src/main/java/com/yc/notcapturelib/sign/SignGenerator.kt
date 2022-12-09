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
    fun generate(params: HashMap<String, String>?, secret: String?): String {
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
            Md5EncryptUtils.encryptMD5ToString(encodeParams)
        } else Md5EncryptUtils.encryptMD5ToString(paramsString)
    }
}
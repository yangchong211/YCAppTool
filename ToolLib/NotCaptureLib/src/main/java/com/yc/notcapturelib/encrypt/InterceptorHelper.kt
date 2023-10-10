package com.yc.notcapturelib.encrypt

import android.annotation.SuppressLint
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.yc.eventuploadlib.LoggerReporter
import com.yc.notcapturelib.helper.NotCaptureHelper
import okhttp3.FormBody
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody


/**
 * @author yangchong
 * GitHub : https://github.com/yangchong211/YCAppTool
 * time   : 2020/11/30
 * desc   : 数据处理帮助类
 */
object InterceptorHelper {

    /**
     * 组装请求参数
     */
    @SuppressLint("LongLogTag")
    fun buildNewParameterList(request: Request, needEncode: Boolean = true):
            MutableList<Triple<Boolean, String, String?>> {
        val parameterList = mutableListOf<Pair<String, String?>>()
        val url = request.url
        val requestBody = request.body
        //组装httpUrl数据
        url.run {
            for (index in 0 until querySize) {
                parameterList.add(Pair(queryParameterName(index), queryParameterValue(index)))
            }
        }
        //组装requestBody数据
        requestBody.run {
            if (this is FormBody) {
                for (index in 0 until size) {
                    parameterList.add(Pair(name(index), value(index)))
                }
            }
        }
        val tempFormBody = FormBody.Builder().apply {
            parameterList.forEach { paramPair ->
                add(paramPair.first, paramPair.second ?: "")
            }
        }.build()

        val dataStrBuilder = StringBuilder()
        tempFormBody.run {
            if (needEncode) {
                for (index in 0 until size) {
                    dataStrBuilder.append(encodedName(index))
                    dataStrBuilder.append("=")
                    dataStrBuilder.append(encodedValue(index))
                    if (index < size - 1) {
                        dataStrBuilder.append("&")
                    }
                }
            } else {
                for (index in 0 until size) {
                    dataStrBuilder.append(name(index))
                    dataStrBuilder.append("=")
                    dataStrBuilder.append(value(index))
                    if (index < size - 1) {
                        dataStrBuilder.append("&")
                    }
                }
            }
        }
        val data = dataStrBuilder.toString()
        if (NotCaptureHelper.getInstance().config.isDebug){
            LoggerReporter.report("NotCaptureHelper", "buildNewParameterList data 公共参数: $data")
        }
        //添加自定义参数
        val reservedQueryParamNamesAndValues: MutableList<Triple<Boolean, String, String?>> = mutableListOf()
        val reservedQueryParam = NotCaptureHelper.getInstance().config.reservedQueryParam
        reservedQueryParam?.let {
            tempFormBody.run {
                for (index in 0 until size) {
                    if (name(index) in reservedQueryParam) {
                        reservedQueryParamNamesAndValues.add(Triple(true, name(index), value(index)))
                    }
                }
            }
        }

        //组装数据
        val encryptVersion = NotCaptureHelper.getInstance().config.encryptVersion
        val encryptData = encrypt(encryptVersion, data)
        if (NotCaptureHelper.getInstance().config.isDebug){
            LoggerReporter.report("NotCaptureHelper", "buildNewParameterList encryptData 加密数据: $data")
        }
        return mutableListOf<Triple<Boolean, String, String?>>().apply {
            //添加ev数据，这个是加解密版本号
            add(Triple(false, EncryptDecryptInterceptor.KEY_ENCRYPT_VERSION, encryptVersion))
            //添加data数据，这个是加密的
            add(Triple(false, EncryptDecryptInterceptor.KEY_DATA, encryptData))
            //添加其他参数
            addAll(reservedQueryParamNamesAndValues)
        }
    }

    /**
     * 组装Response响应数据
     */
    @SuppressLint("LongLogTag")
    fun handleResponse1(response: Response , gson: Gson): Response? {
        val responseBody = response.body
        return if (responseBody?.contentType()?.subtype == EncryptDecryptInterceptor.SUBTYPE_JSON) {
            val responseBodyStr = responseBody.string()
            val encryptResponseBody = try {
                gson.fromJson(responseBodyStr, EncryptDecryptInterceptor.EncryptResponseBody::class.java)
            } catch (e: JsonSyntaxException) {
                null
            }
            val encryptVersion = encryptResponseBody?.encryptVersion
            if (!encryptVersion.isNullOrEmpty()) {
                val decryptResponseBodyStr = decrypt(encryptVersion, encryptResponseBody.result ?: "") ?: ""
                if (NotCaptureHelper.getInstance().config.isDebug){
                    LoggerReporter.report("NotCaptureHelper",
                        "handleResponse toResponseBody 解密后响应数据: $decryptResponseBodyStr"
                    )
                }
                response.newBuilder()
                    .body(decryptResponseBodyStr.toResponseBody(responseBody.contentType()))
                    .build()
            } else {
                response.newBuilder()
                    .body(responseBodyStr.toResponseBody(responseBody.contentType()))
                    .build()
            }
        } else {
            response
        }
    }

    /**
     * 解析异常
     */
    @Deprecated("解析异常")
    fun handleResponse2(response: Response , gson: Gson): Response? {
        val responseBody = response.body
        return if (responseBody?.contentType()?.subtype == EncryptDecryptInterceptor.SUBTYPE_JSON) {
            val responseBodyStr = responseBody.string()
            //gson解析数据为bean
            val encryptResponseBody = try {
                gson.fromJson(responseBodyStr, EncryptDecryptInterceptor.EncryptResponseBody::class.java)
            } catch (e: JsonSyntaxException) {
                null
            }
            val encryptVersion = encryptResponseBody?.encryptVersion
            if (!encryptVersion.isNullOrEmpty()) {
                val result = encryptResponseBody.result ?: ""
                if (NotCaptureHelper.getInstance().config.isDebug){
                    LoggerReporter.report(
                        "NotCaptureHelper",
                        "handleResponse result 加密过的响应数据: $result"
                    )
                }
                val decryptResponseBodyStr = decrypt(encryptVersion, result) ?: ""
                val toResponseBody =
                    decryptResponseBodyStr.toResponseBody(responseBody.contentType())
                if (NotCaptureHelper.getInstance().config.isDebug){
                    LoggerReporter.report(
                        "NotCaptureHelper",
                        "handleResponse toResponseBody 解密后响应数据: ${toResponseBody.string()}"
                    )
                }
                response.newBuilder()
                    .body(toResponseBody)
                    .build()
            } else {
                val toResponseBody = responseBodyStr.toResponseBody(responseBody.contentType())
                response.newBuilder()
                    .body(toResponseBody)
                    .build()
            }
        } else {
            response
        }
    }


    /**
     * 加密数据
     */
    private fun encrypt(encryptVersion: String , data : String): String? {
        val encryptKey = NotCaptureHelper.getInstance().config.encryptKey
        val encryptDecryptListener = NotCaptureHelper.getInstance().encryptDecryptListener
        return when (encryptVersion) {
            EncryptDecryptInterceptor.ENCRYPT_VERSION_2 -> {
                encryptDecryptListener.encryptData(encryptKey,data)
            }
            else -> data
        }
    }

    /**
     * 解密数据
     */
    private fun decrypt(encryptVersion: String , data : String): String? {
        // 获取加密和解密的key
        val encryptKey = NotCaptureHelper.getInstance().config.encryptKey
        val encryptDecryptListener = NotCaptureHelper.getInstance().encryptDecryptListener
        return when (encryptVersion) {
            EncryptDecryptInterceptor.ENCRYPT_VERSION_2 -> {
                encryptDecryptListener.decryptData(encryptKey, data)
            }
            else -> data
        }
    }

}
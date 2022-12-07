package com.yc.notcapturelib.encrypt

import android.annotation.SuppressLint
import com.yc.eventuploadlib.LoggerReporter
import com.yc.notcapturelib.helper.NotCaptureHelper
import okhttp3.FormBody
import okhttp3.Request


/**
 * @author yangchong
 * GitHub : https://github.com/yangchong211/YCAppTool
 * time   : 2020/11/30
 * desc   : 数据处理帮助类
 */
object InterceptorHelper {


    @SuppressLint("LongLogTag")
    fun buildNewParameterList(request: Request, needEncode: Boolean = true): MutableList<Triple<Boolean, String, String?>> {
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
        LoggerReporter.report("NotCaptureHelper", "buildNewParameterList data: $data")
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
        val encryptVersion = NotCaptureHelper.getInstance().config.encryptVersion
        return mutableListOf<Triple<Boolean, String, String?>>().apply {
            add(Triple(false, EncryptDecryptInterceptor.KEY_ENCRYPT_VERSION, encryptVersion))
            add(Triple(false, EncryptDecryptInterceptor.KEY_DATA, encrypt(encryptVersion,data)))
            addAll(reservedQueryParamNamesAndValues)
        }
    }

    /**
     * 加密数据
     */
    fun encrypt(encryptVersion: String , data : String): String? {
        val encryptKey = NotCaptureHelper.getInstance().config.encryptKey
        val encryptDecryptListener = NotCaptureHelper.getInstance().encryptDecryptListener
        return when (encryptVersion) {
            EncryptDecryptInterceptor.ENCRYPT_VERSION_2 -> encryptDecryptListener.encryptData(encryptKey,data)
            else -> data
        }
    }

    /**
     * 解密数据
     */
    fun decrypt(encryptVersion: String , data : String): String? {
        val encryptKey = NotCaptureHelper.getInstance().config.encryptKey
        val encryptDecryptListener = NotCaptureHelper.getInstance().encryptDecryptListener
        return when (encryptVersion) {
            EncryptDecryptInterceptor.ENCRYPT_VERSION_2 -> encryptDecryptListener.decryptData(encryptKey, data)
            else -> null
        }
    }

}
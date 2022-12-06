package com.yc.notcapturelib.encrypt

import android.annotation.SuppressLint
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.annotations.SerializedName
import com.yc.notcapturelib.helper.NotCaptureHelper
import okhttp3.*
import okhttp3.ResponseBody.Companion.toResponseBody
import java.io.IOException

/**
 * @author yangchong
 * GitHub : https://github.com/yangchong211/YCAppTool
 * time   : 2020/11/30
 * desc   : 加解密数据
 */
class EncryptDecryptInterceptor : Interceptor {

    private val gson = Gson()

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val handleRequest = handleRequest(request)
        val response: Response = chain.proceed(handleRequest)
        return handleResponse(response)!!
    }

    private fun handleRequest(request: Request): Request {
        val headerIgnoreEncrypt = request.header(HEADER_IGNORE_ENCRYPT)
        val headerForceEncrypt = request.header(HEADER_FORCE_ENCRYPT)
        val ignoreEncrypt = "true" == headerIgnoreEncrypt
        val forceEncrypt = "true" == headerForceEncrypt
        val config = NotCaptureHelper.getInstance().config
        val configEncrypt = config.isEncrypt
        val headerEncrypt = !ignoreEncrypt || !forceEncrypt
        val newRequest : Request
        if (configEncrypt && headerEncrypt) {
            when (request.method) {
                "GET" -> {
                    newRequest = handleRequestGet(request)
                }
                "POST" -> {
                    val requestBody = request.body
                    newRequest = when {
                        requestBody == null || requestBody.contentLength() == 0L || requestBody is FormBody -> {
                            handleRequestPostFormBody(request)
                        }
                        requestBody is MultipartBody -> {
                            handleRequestPostMultipartBody(request)
                        }
                        else -> {
                            request
                        }
                    }
                }
                else -> {
                    newRequest = request
                }
            }
        } else {
            newRequest = request
        }
        return newRequest.newBuilder()
            .removeHeader(HEADER_IGNORE_ENCRYPT)
            .removeHeader(HEADER_FORCE_ENCRYPT)
            .build()
    }

    private fun handleRequestPostMultipartBody(request: Request) : Request {
        val url = request.url
        val requestBody = request.body as MultipartBody
        val newParameterList = buildNewParameterList(request)
        val newUrl = url.newBuilder()
            .encodedQuery(null)
            .encodedFragment(null)
            .apply {
                newParameterList.forEach { paramTriple ->
                    if (paramTriple.first) {
                        addQueryParameter(paramTriple.second, paramTriple.third)
                    }
                }
            }
            .build()
        val newRequestBody = MultipartBody.Builder()
            .setType(requestBody.type)
            .apply {
                newParameterList.forEach { paramTriple ->
                    if (!paramTriple.first) {
                        addFormDataPart(
                            paramTriple.second,
                            paramTriple.third ?: ""
                        )
                    }
                }
                requestBody.parts.forEach { part ->
                    addPart(part)
                }
            }
            .build()
        return request.newBuilder()
            .url(newUrl)
            .post(newRequestBody)
            .build()
    }

    private fun handleRequestPostFormBody(request: Request) : Request {
        val url = request.url
        val requestBody = request.body
        val newParameterList = buildNewParameterList(request)
        val newUrl = url.newBuilder()
            .encodedQuery(null)
            .encodedFragment(null)
            .apply {
                newParameterList.forEach { paramTriple ->
                    if (paramTriple.first) {
                        addQueryParameter(paramTriple.second, paramTriple.third)
                    }
                }
            }
            .build()
        val newRequestBody = FormBody.Builder()
            .apply {
                newParameterList.forEach { paramTriple ->
                    if (!paramTriple.first) {
                        add(paramTriple.second, paramTriple.third ?: "")
                    }
                }
            }
            .build()
        return request.newBuilder()
            .url(newUrl)
            .post(newRequestBody)
            .build()
    }

    private fun handleRequestGet(request: Request): Request {
        val url = request.url
        val newParameterList = buildNewParameterList(request)
        val newUrl = url.newBuilder()
            .encodedQuery(null)
            .encodedFragment(null)
            .apply {
                newParameterList.forEach { paramTriple ->
                    addQueryParameter(paramTriple.second, paramTriple.third)
                }
            }
            .build()
        return request.newBuilder()
            .url(newUrl)
            .get()
            .build()
    }

    private fun handleResponse(response: Response): Response? {
        val responseBody = response.body

        return if (responseBody?.contentType()?.subtype == SUBTYPE_JSON) {
            val responseBodyStr = responseBody.string()
            val encryptResponseBody = try {
                gson.fromJson(responseBodyStr, EncryptResponseBody::class.java)
            } catch (e: JsonSyntaxException) {
                null
            }
            val encryptVersion = encryptResponseBody?.encryptVersion
            if (!encryptVersion.isNullOrEmpty()) {
                val result = encryptResponseBody.result ?: ""
                val decryptResponseBodyStr = decrypt(encryptVersion,result) ?: ""
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

    @SuppressLint("LongLogTag")
    private fun buildNewParameterList(request: Request, needEncode: Boolean = true): MutableList<Triple<Boolean, String, String?>> {
        val parameterList = mutableListOf<Pair<String, String?>>()
        val url = request.url
        val requestBody = request.body
        url.run {
            for (index in 0 until querySize) {
                parameterList.add(Pair(queryParameterName(index), queryParameterValue(index)))
            }
        }
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
            }
            .build()

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
        val reservedQueryParamNamesAndValues: MutableList<Triple<Boolean, String, String?>> = mutableListOf()

        val reservedQueryParam = NotCaptureHelper.getInstance().config.reservedQueryParam
        reservedQueryParam?.let {
            tempFormBody.run {
                for (index in 0 until size) {
                    if (name(index) in reservedQueryParam) {
                        reservedQueryParamNamesAndValues.add(
                            Triple(true, name(index), value(index))
                        )
                    }
                }
            }
        }

        val encryptVersion = NotCaptureHelper.getInstance().config.encryptVersion
        return mutableListOf<Triple<Boolean, String, String?>>().apply {
            add(Triple(false, KEY_ENCRYPT_VERSION, encryptVersion))
            add(Triple(false, KEY_DATA, encrypt(encryptVersion,data)))
            addAll(reservedQueryParamNamesAndValues)
        }
    }

    private fun encrypt(encryptVersion: String , data : String): String? {
        val encryptKey = NotCaptureHelper.getInstance().config.encryptKey
        val encryptDecryptListener = NotCaptureHelper.getInstance().encryptDecryptListener
        return when (encryptVersion) {
            ENCRYPT_VERSION_2 -> encryptDecryptListener.encryptData(encryptKey,data)
            else -> data
        }
    }

    private fun decrypt(encryptVersion: String , data : String): String? {
        val encryptKey = NotCaptureHelper.getInstance().config.encryptKey
        val encryptDecryptListener = NotCaptureHelper.getInstance().encryptDecryptListener
        return when (encryptVersion) {
            ENCRYPT_VERSION_2 -> encryptDecryptListener.decryptData(encryptKey, data)
            else -> data
        }
    }

    private class EncryptResponseBody(
        @SerializedName("ev")
        val encryptVersion: String? = null,
        @SerializedName("result")
        val result: String? = null
    )

    companion object {
        private const val KEY_ENCRYPT_VERSION = "ev"
        private const val KEY_DATA = "data"
        private const val ENCRYPT_VERSION_2 = "2"
        private const val SUBTYPE_JSON = "json"
        private const val HEADER_IGNORE_ENCRYPT = "Ignore-Encrypt"
        private const val HEADER_FORCE_ENCRYPT = "Force-Encrypt"
    }
}
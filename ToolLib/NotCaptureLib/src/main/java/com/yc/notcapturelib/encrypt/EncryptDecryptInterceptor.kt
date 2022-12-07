package com.yc.notcapturelib.encrypt

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.annotations.SerializedName
import com.yc.eventuploadlib.LoggerReporter
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
        val handleResponse = handleResponse(response)
        return handleResponse!!
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
        LoggerReporter.report("NotCaptureHelper", "handleRequest $configEncrypt , $headerEncrypt")
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
        LoggerReporter.report("NotCaptureHelper", "handleRequestPostMultipartBody url: $url")
        LoggerReporter.report("NotCaptureHelper", "handleRequestPostMultipartBody requestBody: $requestBody")
        val newParameterList = InterceptorHelper.buildNewParameterList(request)
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
        LoggerReporter.report("NotCaptureHelper", "handleRequestPostMultipartBody newUrl: $newUrl")
        LoggerReporter.report("NotCaptureHelper", "handleRequestPostMultipartBody newRequestBody: $newRequestBody")
        return request.newBuilder()
            .url(newUrl)
            .post(newRequestBody)
            .build()
    }

    private fun handleRequestPostFormBody(request: Request) : Request {
        val url = request.url
        val requestBody = request.body
        val newParameterList = InterceptorHelper.buildNewParameterList(request)
        LoggerReporter.report("NotCaptureHelper", "handleRequestPostFormBody url: $url")
        LoggerReporter.report("NotCaptureHelper", "handleRequestPostFormBody requestBody: $requestBody")
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
        LoggerReporter.report("NotCaptureHelper", "handleRequestPostFormBody newUrl: $newUrl")
        LoggerReporter.report("NotCaptureHelper", "handleRequestPostFormBody newRequestBody: $newRequestBody")
        return request.newBuilder()
            .url(newUrl)
            .post(newRequestBody)
            .build()
    }

    private fun handleRequestGet(request: Request): Request {
        val url = request.url
        LoggerReporter.report("NotCaptureHelper", "handleRequestGet url: $url")
        val newParameterList = InterceptorHelper.buildNewParameterList(request)
        val newUrl = url.newBuilder()
            .encodedQuery(null)
            .encodedFragment(null)
            .apply {
                newParameterList.forEach { paramTriple ->
                    //使用UTF-8编码查询参数并将其添加到此URL的查询字符串中
                    addQueryParameter(paramTriple.second, paramTriple.third)
                }
            }
            .build()
        LoggerReporter.report("NotCaptureHelper", "handleRequestGet newUrl: $newUrl")
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
                LoggerReporter.report("NotCaptureHelper", "handleResponse result: $result")
                val decryptResponseBodyStr = InterceptorHelper.decrypt(encryptVersion,result) ?: ""
                val toResponseBody =
                    decryptResponseBodyStr.toResponseBody(responseBody.contentType())
                LoggerReporter.report("NotCaptureHelper",
                    "handleResponse toResponseBody: ${toResponseBody.string()}")
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

    private class EncryptResponseBody(
        @SerializedName("ev")
        val encryptVersion: String? = null,
        @SerializedName("result")
        val result: String? = null
    )

    companion object {
        const val KEY_ENCRYPT_VERSION = "ev"
        const val KEY_DATA = "data"
        const val ENCRYPT_VERSION_2 = "2"
        private const val SUBTYPE_JSON = "json"
        private const val HEADER_IGNORE_ENCRYPT = "Ignore-Encrypt"
        private const val HEADER_FORCE_ENCRYPT = "Force-Encrypt"
    }
}
package com.yc.notcapturelib.encrypt

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.yc.eventuploadlib.LoggerReporter
import com.yc.notcapturelib.helper.NotCaptureHelper
import okhttp3.*
import okio.Buffer
import java.io.IOException

/**
 * @author yangchong
 * GitHub : https://github.com/yangchong211/YCAppTool
 * time   : 2020/11/30
 * desc   : 加解密数据
 */
class  EncryptDecryptInterceptor : Interceptor {

    private val gson = Gson()

    /**
     * Request抽象成请求数据
     * Request包括Headers和RequestBody，而RequestBody是abstract的
     * 他的子类是有FormBody(表单提交的)和MultipartBody(文件上传)
     *
     * Response抽象成响应数据
     * Response包括Headers和RequestBody，而ResponseBody是abstract的
     * 所以他的子类也是有两个:RealResponseBody和CacheResponseBody，分别代表真实响应和缓存响应
     */
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val handleRequest = handleRequest(request)
        val response: Response = chain.proceed(handleRequest)
        val handleResponse = InterceptorHelper.handleResponse1(response, gson)
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
        val newRequest: Request
        if (NotCaptureHelper.getInstance().config.isDebug){
            LoggerReporter.report(
                "NotCaptureHelper",
                "handleRequest 加密 $configEncrypt , $headerEncrypt"
            )
        }
        if (configEncrypt && headerEncrypt) {
            when (request.method) {
                "GET" -> {
                    newRequest = handleRequestGet(request)
                }
                "POST" -> {
                    val requestBody = request.body
                    newRequest = when {
                        //如果request请求体是null，或者如果request请求体长度是0
                        requestBody == null || requestBody.contentLength() == 0L || requestBody is FormBody -> {
                            handleRequestPostFormBody(request)
                        }
                        //MultipartBody可用于文件请求(图像，文档..)
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

    private fun handleRequestPostMultipartBody(request: Request): Request {
        val url = request.url
        val requestBody = request.body as MultipartBody
        if (NotCaptureHelper.getInstance().config.isDebug){
            LoggerReporter.report("NotCaptureHelper", "handleRequestPostMultipartBody url: $url")
            LoggerReporter.report(
                "NotCaptureHelper",
                "handleRequestPostMultipartBody requestBody: $requestBody"
            )
        }
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
        if (NotCaptureHelper.getInstance().config.isDebug){
            LoggerReporter.report("NotCaptureHelper", "handleRequestPostMultipartBody newUrl: $newUrl")
            LoggerReporter.report(
                "NotCaptureHelper",
                "handleRequestPostMultipartBody newRequestBody: $newRequestBody"
            )
        }
        return request.newBuilder()
            .url(newUrl)
            .post(newRequestBody)
            .build()
    }

    /**
     * 从okhttp3.RequestBody的对象实例获取主体字符串。writeTo(okio.BufferedSink sink)
     * Okio还具有Buffer类型，它既是BufferedSink(意味着您可以写入)，又是BufferedSource(意味着您可以从中读取)。
     * 因此，我们可以将主体写入Buffer，然后将其作为字符串读回。
     */
    private fun handleRequestPostFormBody(request: Request): Request {
        val url = request.url
        val requestBody = request.body
        val newParameterList = InterceptorHelper.buildNewParameterList(request)
        if (NotCaptureHelper.getInstance().config.isDebug){
            LoggerReporter.report("NotCaptureHelper", "handleRequestPostFormBody url 请求链接: $url")
        }
        try {
            //请求参数body，转换为字符串
            val buffer = Buffer()
            requestBody?.writeTo(buffer)
            val readUtf8 = buffer.readUtf8()
            if (NotCaptureHelper.getInstance().config.isDebug){
                LoggerReporter.report(
                    "NotCaptureHelper",
                    "handleRequestPostFormBody requestBody 请求body: $readUtf8"
                )
            }
        } catch (e: Exception) {
        }
        //创建新的请求链接
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
        //创建新的请求body
        val newRequestBody = FormBody.Builder()
            .apply {
                newParameterList.forEach { paramTriple ->
                    if (!paramTriple.first) {
                        add(paramTriple.second, paramTriple.third ?: "")
                    }
                }
            }
            .build()
        if (NotCaptureHelper.getInstance().config.isDebug){
            LoggerReporter.report("NotCaptureHelper", "handleRequestPostFormBody newUrl 新请求链接: $newUrl")
        }
        try {
            //请求参数body，转换为字符串
            val buffer = Buffer()
            newRequestBody.writeTo(buffer)
            val readUtf8 = buffer.readUtf8()
            if (NotCaptureHelper.getInstance().config.isDebug){
                LoggerReporter.report(
                    "NotCaptureHelper",
                    "handleRequestPostFormBody newRequestBody 新请求body: $readUtf8"
                )
            }
        } catch (e: Exception) {
        }
        return request.newBuilder()
            .url(newUrl)
            .post(newRequestBody)
            .build()
    }

    private fun handleRequestGet(request: Request): Request {
        val url = request.url
        if (NotCaptureHelper.getInstance().config.isDebug){
            LoggerReporter.report("NotCaptureHelper", "handleRequestGet url: $url")
        }
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
        if (NotCaptureHelper.getInstance().config.isDebug){
            LoggerReporter.report("NotCaptureHelper", "handleRequestGet newUrl: $newUrl")
        }
        return request.newBuilder()
            .url(newUrl)
            .get()
            .build()
    }

    class EncryptResponseBody(
        @SerializedName("ev")
        val encryptVersion: String? = null,
        @SerializedName("result")
        val result: String? = null
    )

    companion object {
        const val KEY_ENCRYPT_VERSION = "ev"
        const val KEY_DATA = "data"
        const val ENCRYPT_VERSION_2 = "2"
        const val SUBTYPE_JSON = "json"
        private const val HEADER_IGNORE_ENCRYPT = "Ignore-Encrypt"
        private const val HEADER_FORCE_ENCRYPT = "Force-Encrypt"
    }
}
package com.yc.netinterceptor.log

import android.os.Build
import okhttp3.*
import okhttp3.internal.http.promisesBody
import okio.Buffer
import okio.GzipSource
import java.io.IOException
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     GitHub : https://github.com/yangchong211
 *     time  : 2018/11/9
 *     desc  : 日志打印拦截器
 *     revise:
 * </pre>
 */
class HttpLoggingInterceptor @JvmOverloads constructor(
    private val logger: IHttpLogger = IHttpLogger.DEFAULT
) : Interceptor {

    @Volatile
    private var headersToRedact = emptySet<String>()

    @set:JvmName("level")
    @Volatile
    private var level = HttpLoggerLevel.NONE


    fun redactHeader(name: String) {
        val newHeadersToRedact = TreeSet(String.CASE_INSENSITIVE_ORDER)
        newHeadersToRedact += headersToRedact
        newHeadersToRedact += name
        headersToRedact = newHeadersToRedact
    }

    fun setLevel(level: HttpLoggerLevel) = apply {
        this.level = level
    }

    @JvmName("-deprecated_level")
    @Deprecated(
        message = "moved to var",
        replaceWith = ReplaceWith(expression = "level"),
        level = DeprecationLevel.ERROR
    )
    fun getLevel(): HttpLoggerLevel = level

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val level = this.level
        val request = chain.request()
        if (level == HttpLoggerLevel.NONE) {
            return chain.proceed(request)
        }
        logForRequest(chain,request)

        val startNs = System.nanoTime()
        val response: Response
        try {
            response = chain.proceed(request)
        } catch (e: Exception) {
            logger.log("<-- HTTP FAILED: $e")
            throw e
        }
        return logForResponse(startNs,response)
    }

    /**
     * 打印request日志信息
     */
    private fun logForRequest(chain: Interceptor.Chain,request: Request) {
        val logBody = level == HttpLoggerLevel.BODY
        val logHeaders = logBody || level == HttpLoggerLevel.HEADERS
        //请求体
        val requestBody = request.body
        val connection = chain.connection()
        val sb = StringBuffer()
        sb.append("--> START ${request.method} ${request.url}${if (connection != null) " " + connection.protocol() else ""}")
        if (!logHeaders && requestBody != null) {
            sb.append(" (${requestBody.contentLength()}-byte body)")
        }
        //--> GET https://www.wanandroid.com/wxarticle/chapters/json?token=6666666&id=190000
        logger.log(sb.toString())

        if (logHeaders) {
            val headers = request.headers

            if (requestBody != null) {
                // Request body headers are only present when installed as a network interceptor. When not
                // already present, force them to be included (if available) so their values are known.
                requestBody.contentType()?.let {
                    if (headers["Content-Type"] == null) {
                        logger.log("Content-Type: $it")
                    }
                }
                if (requestBody.contentLength() != -1L) {
                    if (headers["Content-Length"] == null) {
                        val contentLength = requestBody.contentLength()
                        logger.log("Content-Length: $contentLength")
                    }
                }
            }

            for (i in 0 until headers.size) {
                logHeader(headers, i)
            }

            if (!logBody || requestBody == null) {
                logger.log("--> END ${request.method}")
            } else if (bodyHasUnknownEncoding(request.headers)) {
                logger.log("--> END ${request.method} (encoded body omitted)")
            } else if (requestBody.isDuplex()) {
                logger.log("--> END ${request.method} (duplex request body omitted)")
            } else if (requestBody.isOneShot()) {
                logger.log("--> END ${request.method} (one-shot body omitted)")
            } else {
                val buffer = Buffer()
                requestBody.writeTo(buffer)
                logger.log("")
                if (buffer.isProbablyUtf8()) {
                    val contentType = requestBody.contentType()
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        val charset =
                            contentType?.charset(StandardCharsets.UTF_8) ?: StandardCharsets.UTF_8
                        logger.log(buffer.readString(charset))
                    }
                    logger.log("--> END ${request.method} (${requestBody.contentLength()}-byte body)")
                } else {
                    logger.log("--> END ${request.method} (binary ${requestBody.contentLength()}-byte body omitted)")
                }
            }
        }
    }

    /**
     * 打印response日志信息
     */
    private fun logForResponse(startNs: Long,response: Response) : Response{
        val logBody = level == HttpLoggerLevel.BODY
        val logHeaders = logBody || level == HttpLoggerLevel.HEADERS
        val tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs)
        val responseBody = response.body!!
        val contentLength = responseBody.contentLength()
        val bodySize = if (contentLength != -1L) "$contentLength-byte" else "unknown-length"
        //<-- 200 OK https://www.wanandroid.com/article/list/0/json (254ms)
        val stringBuffer = StringBuffer()
        stringBuffer.append("<-- END ${response.code} ")
        if (response.message.isEmpty()){
            stringBuffer.append("")
        } else {
            stringBuffer.append(response.message+" ")
        }

        stringBuffer.append(response.request.url)
        stringBuffer.append(" (${tookMs}ms)")
        if (!logHeaders){
            stringBuffer.append(" ($bodySize body)")
        }
        stringBuffer.append(" ")
        logger.log(stringBuffer.toString())
        val mediaType = responseBody.contentType()
        if (mediaType != null) {
            if (isText(mediaType)) {
                val responseString = StringBuffer()
                val resp = responseBody.string()
                responseString.append("<-- END ResponseBody : ")
                responseString.append(resp)
                logger.log(responseString.toString())
                return response.newBuilder().body(ResponseBody.create(mediaType, resp)).build()
            }
        }
        logger.log(stringBuffer.toString())

        if (logHeaders) {
            val headers = response.headers
            for (i in 0 until headers.size) {
                logHeader(headers, i)
            }

            if (!logBody || !response.promisesBody()) {
                logger.log("<-- END HTTP")
            } else if (bodyHasUnknownEncoding(response.headers)) {
                logger.log("<-- END HTTP (encoded body omitted)")
            } else {
                val source = responseBody.source()
                source.request(Long.MAX_VALUE) // Buffer the entire body.
                var buffer = source.buffer()

                var gzippedLength: Long? = null
                if ("gzip".equals(headers["Content-Encoding"], ignoreCase = true)) {
                    gzippedLength = buffer.size
                    GzipSource(buffer.clone()).use { gzippedResponseBody ->
                        buffer = Buffer()
                        buffer.writeAll(gzippedResponseBody)
                    }
                }



                if (!buffer.isProbablyUtf8()) {
                    logger.log("")
                    logger.log("<-- END HTTP (binary ${buffer.size}-byte body omitted)")
                    return response
                }

                if (contentLength != 0L) {
                    logger.log("")
                    val contentType = responseBody.contentType()
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        val charset: Charset =
                            contentType?.charset(StandardCharsets.UTF_8) ?: StandardCharsets.UTF_8
                        logger.log(buffer.clone().readString(charset))
                    }
                }

                if (gzippedLength != null) {
                    logger.log("<-- END HTTP (${buffer.size}-byte, $gzippedLength-gzipped-byte body)")
                } else {
                    logger.log("<-- END HTTP (${buffer.size}-byte body)")
                }
            }
        }
        return response
    }

    private fun logHeader(headers: Headers, i: Int) {
        val value = if (headers.name(i) in headersToRedact) " " else headers.value(i)
        logger.log("Header-> " + headers.name(i) + ": " + value)
    }

    private fun isText(mediaType: MediaType): Boolean {
        return mediaType.type == "text" || (mediaType.subtype == "json" ||
                mediaType.subtype == "xml" || mediaType.subtype == "html" ||
                mediaType.subtype == "x-www-form-urlencoded")
    }


    private fun bodyHasUnknownEncoding(headers: Headers): Boolean {
        val contentEncoding = headers["Content-Encoding"] ?: return false
        return !contentEncoding.equals("identity", ignoreCase = true) &&
                !contentEncoding.equals("gzip", ignoreCase = true)
    }
}

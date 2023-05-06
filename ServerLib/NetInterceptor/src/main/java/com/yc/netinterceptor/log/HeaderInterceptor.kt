package com.yc.netinterceptor.log

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.util.*

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     GitHub : https://github.com/yangchong211
 *     time  : 2018/11/9
 *     desc  : 添加公共header参数拦截器
 *     revise:
 * </pre>
 */
abstract class HeaderInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest: Request = chain.request()
        val builder: Request.Builder = originalRequest.newBuilder()
        val commonHeader = commonHeader
        if (commonHeader != null && commonHeader.size > 0) {
            val keySet: Set<String> = commonHeader.keys
            for (key in keySet) {
                val value = commonHeader[key]
                builder.addHeader(key + "", value + "")
            }
        }
        val method = originalRequest.method
        val body = originalRequest.body
        val requestBuilder: Request.Builder = builder.method(method, body)
        val request: Request = requestBuilder.build()
        return chain.proceed(request)
    }

    /**
     * 获取公共header参数
     * @return      map集合数据
     */
    abstract val commonHeader: HashMap<String, String>?
}
package com.yc.netinterceptor

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
 *     desc  : 添加公共参数拦截器
 *     revise:
 * </pre>
 */
@Deprecated("")
abstract class ParamsInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest: Request = chain.request()
        val builder: Request.Builder = originalRequest.newBuilder()
        //todo 添加get和post公共参数
        val method = originalRequest.method
        val body = originalRequest.body
        val requestBuilder: Request.Builder = builder.method(method, body)
        val request: Request = requestBuilder.build()
        return chain.proceed(request)
    }

    /**
     * 获取公共参数
     * @return      map集合数据
     */
    abstract val commonParams: HashMap<String, String>?
}
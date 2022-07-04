package com.yc.netinterceptor

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

/**
 * <pre>
 * @author yangchong
 * blog  : https://github.com/yangchong211
 * GitHub : https://github.com/yangchong211
 * time  : 2018/11/9
 * desc  : 加密和解密拦截器
 * revise:
</pre> *
 */
class EncryptDecryptInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        return chain.proceed(request)
    }


}
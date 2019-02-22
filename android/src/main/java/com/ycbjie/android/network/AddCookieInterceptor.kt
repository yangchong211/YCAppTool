package com.ycbjie.android.network


import com.blankj.utilcode.util.SPUtils
import com.ycbjie.android.base.KotlinConstant
import okhttp3.Interceptor
import okhttp3.Response

/**
 * 添加cookie做持久化
 */
class AddCookieInterceptor : Interceptor {

    private val COOKIE_NAME = "Cookie"

    override fun intercept(chain: Interceptor.Chain?): Response {
        val request = chain?.request()
        val domain = request?.url()?.host()
        val builder = request?.newBuilder()
        val userId = SPUtils.getInstance().getInt(KotlinConstant.USER_ID)
        if (domain!!.isNotEmpty() && userId != 0) {
            val cookies = SPUtils.getInstance().getString(domain)
            if (cookies.isNotEmpty()) {
                builder?.addHeader(COOKIE_NAME, cookies)
            }
        }
        val build = builder?.build()
        return chain.proceed(build)
    }

}
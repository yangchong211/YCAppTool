package com.yc.netinterceptor

import android.util.Log
import okhttp3.internal.platform.Platform

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     GitHub : https://github.com/yangchong211
 *     time  : 2018/11/9
 *     desc  : 日志打印接口
 *     revise:
 * </pre>
 */
fun interface IHttpLogger {

    fun log(message: String)

    companion object {
        @JvmField
        val DEFAULT: IHttpLogger = DefaultLogger()

        private class DefaultLogger : IHttpLogger {
            val TAG = "okhttp: "
            override fun log(message: String) {
                //Log.d(TAG, message)
                Platform.get().log(TAG + message)
            }
        }
    }
}
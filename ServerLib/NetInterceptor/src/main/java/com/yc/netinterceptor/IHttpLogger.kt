package com.yc.netinterceptor

import android.util.Log

fun interface IHttpLogger {

    fun log(message: String)

    companion object {
        /** A [IHttpLogger] defaults output appropriate for the current platform. */

        @JvmField
        val DEFAULT: IHttpLogger = DefaultLogger()

        private class DefaultLogger : IHttpLogger {
            val TAG = "okhttp: "
            override fun log(message: String) {
                Log.d(TAG,message)
                //Platform.get().log(TAG + message)
            }
        }
    }
}
package com.ycbjie.android.network

import com.google.gson.JsonParseException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * 自定义异常，比如网络异常，解析异常等
 */
class CustomException {

    companion object {
        /**
         * 未知错误
         */
        private var UNKNOWN: Int = 1000

        /**
         * 解析错误
         */
        private var PARSE_ERROR: Int = 1001

        /**
         * 网络错误
         */
        private var NETWORK_ERROR: Int = 1002

        /**
         * HTTP错误
         */
        var HTTP_ERROR: Int = 1003

        /**
         * 将本地异常解析成ApiException
         */
        fun handleException(cause: Throwable?): ApiException? {
            var exception: ApiException? = if (cause is JsonParseException) {
                ApiException(cause.message, cause, PARSE_ERROR)
            } else if (cause is UnknownHostException ||
                    cause is SocketTimeoutException ||
                    cause is ConnectException) {
                ApiException(cause.message, cause, NETWORK_ERROR)
            } else {
                ApiException(cause?.message, cause, UNKNOWN)
            }
            return exception
        }
    }

}
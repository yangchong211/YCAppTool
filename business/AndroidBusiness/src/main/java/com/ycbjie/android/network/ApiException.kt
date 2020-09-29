package com.ycbjie.android.network


/**
 * 异常处理
 */
class ApiException : Exception {

    private var code: Int = 0

    constructor(message: String?, cause: Throwable?, code: Int) : super(message, cause) {
        this.code = code
    }

}
package com.yc.http.synthesis.model;

import androidx.annotation.NonNull;

/**
 *    @author yangchong
 *    GitHub : https://github.com/yangchong211/YCAppTool
 *    time   : 2019/05/19
 *    desc   : 请求方式
 */
public enum HttpMethod {

    /** GET 请求 */
    GET("GET"),

    /** Post 请求 */
    POST("POST"),

    /** Head 请求 */
    HEAD("HEAD"),

    /** Delete 请求 */
    DELETE("DELETE"),

    /** Put 请求 */
    PUT("PUT"),

    /** Patch 请求 */
    PATCH("PATCH"),

    /** Options 请求 */
    OPTIONS("OPTIONS"),

    /** Trace 请求 */
    TRACE("TRACE");

    /** 请求方式 */
    private final String mMethod;

    HttpMethod(String method) {
        this.mMethod = method;
    }

    @NonNull
    @Override
    public String toString() {
        return mMethod;
    }
}
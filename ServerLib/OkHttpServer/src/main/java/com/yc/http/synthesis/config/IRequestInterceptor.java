package com.yc.http.synthesis.config;

import androidx.annotation.NonNull;

import com.yc.http.synthesis.model.HttpHeaders;
import com.yc.http.synthesis.model.HttpParams;
import com.yc.http.synthesis.request.HttpRequest;

import okhttp3.Request;
import okhttp3.Response;

/**
 *    @author yangchong
 *    GitHub : https://github.com/yangchong211/YCAppTool
 *    time   : 2020/08/05
 *    desc   : 请求参数拦截器
 */
public interface IRequestInterceptor {

    /**
     * 拦截参数
     *
     * @param httpRequest   接口对象
     * @param params        请求参数
     * @param headers       请求头参数
     */
    default void interceptArguments(@NonNull HttpRequest<?> httpRequest, @NonNull HttpParams params, @NonNull HttpHeaders headers) {}

    /**
     * 拦截请求头
     *
     * @param httpRequest   接口对象
     * @param request       请求头对象
     * @return              返回新的请求头
     */
    @NonNull
    default Request interceptRequest(@NonNull HttpRequest<?> httpRequest, @NonNull Request request) {
        return request;
    }

    /**
     * 拦截器响应头
     *
     * @param httpRequest   接口对象
     * @param response      响应头对象
     * @return              返回新的响应头
     */
    @NonNull
    default Response interceptResponse(HttpRequest<?> httpRequest, Response response) {
        return response;
    }
}
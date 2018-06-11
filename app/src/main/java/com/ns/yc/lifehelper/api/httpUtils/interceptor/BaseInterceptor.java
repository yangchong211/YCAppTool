package com.ns.yc.lifehelper.api.httpUtils.interceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2016/6/20
 * 描    述：自定义拦截器，比如请求中有共同的请求头或者key值
 * 修订历史：
 * ================================================
 */
public class BaseInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request req = chain.request();
        Request request = req.newBuilder()
                .addHeader("Cookie", "Cookie值，假设值")
                .addHeader("appType", "android")
                .addHeader("uuid", "uuid")
                .addHeader("key","key值")
                .build();
        return chain.proceed(request);
    }
}
package com.yc.retrofitnetlib;


import com.yc.networklib.AppNetworkUtils;
import com.yc.toolutils.AppLogUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.CacheControl;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by PC on 2017/9/22.
 * 作者：PC
 */

public class InterceptorUtils {


    /**
     * 统一请求拦截器
     * 统一的请求参数
     * 公共参数：可能接口有某些参数是公共的，不可能一个个接口都去加吧
     */
    public static Interceptor commonParamsInterceptor() {
        Interceptor commonParams = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originRequest = chain.request();
                Request request;
                HttpUrl httpUrl = originRequest.url().newBuilder()
                        .addQueryParameter("paltform", "android")
                        .addQueryParameter("version", "1.0.0")
                        .build();
                request = originRequest.newBuilder()
                        .url(httpUrl)
                        .build();
                return chain.proceed(request);
            }
        };
        return commonParams;
    }

    /**
     * 自定义CookieJar
     * @param builder
     */
    public static void addCookie(OkHttpClient.Builder builder){
        builder.cookieJar(new CookieJar() {
            private final HashMap<HttpUrl, List<Cookie>> cookieStore = new HashMap<>();
            @Override
            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                cookieStore.put(url, cookies);
                //保存cookie //也可以使用SP保存
            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl url) {
                List<Cookie> cookies = cookieStore.get(url);
                //取出cookie
                return cookies != null ? cookies : new ArrayList<Cookie>();
            }
        });
    }


    public static Interceptor addNetWorkInterceptor() {
        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                if (!AppNetworkUtils.isConnected()) {
                    request = request.newBuilder()
                            .cacheControl(CacheControl.FORCE_CACHE)
                            .build();
                    AppLogUtils.d("addNetWorkInterceptor"+ "没有网络链接");
                }
                Response response = chain.proceed(request);
                if (AppNetworkUtils.isConnected()) {
                    int maxAge = 0; // 有网络时 设置缓存超时时间0个小时
                    AppLogUtils.d("addNetWorkInterceptor"+ "网络已连接，缓存时间为：" + maxAge);
                    response = response.newBuilder()
                            .addHeader("Cache-Control", "public, max-age=" + maxAge)
                            .removeHeader("Pragma")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                            .build();
                } else {
                    int maxStale = HttpConstant.TIME_CACHE;
                    AppLogUtils.d("addNetWorkInterceptor"+ "网络未连接，缓存时间为：" + maxStale);
                    response = response.newBuilder()
                            .addHeader("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                            .removeHeader("Pragma")
                            .build();
                }
                String cookeHeader = response.header("Set-Cookie", "");
                AppLogUtils.e("cookeHeader1-----------"+cookeHeader);
                return response;
            }
        };
        return interceptor;
    }



    public static Interceptor addCallBack() {
        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                request = request.newBuilder().build();
                Response response = chain.proceed(request);
                return response;
            }
        };
        return interceptor;
    }
    
}

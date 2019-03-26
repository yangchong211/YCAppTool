package com.ycbjie.library.http;


import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.ycbjie.library.constant.Constant;

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
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by PC on 2017/9/22.
 * 作者：PC
 */

public class InterceptorUtils {

    /**
     * 创建日志拦截器
     * @return              HttpLoggingInterceptor
     */
    public static HttpLoggingInterceptor getHttpLoggingInterceptor(boolean debug) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLogger());
        if (debug) {
            // 测试
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            // 打包
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        }
        return loggingInterceptor;
    }


    /**
     * 请求头拦截器
     * 使用addHeader()不会覆盖之前设置的header,若使用header()则会覆盖之前的header
     * @return
     */
    public static Interceptor getRequestHeader() {
        Interceptor headerInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                Request.Builder builder = originalRequest.newBuilder();
                builder.addHeader("version", "1");
                builder.addHeader("time", System.currentTimeMillis() + "");
                String method = originalRequest.method();
                RequestBody body = originalRequest.body();
                Request.Builder requestBuilder = builder.method(method,body);
                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        };
        return headerInterceptor;
    }

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
     * 在无网络的情况下读取缓存，有网络的情况下根据缓存的过期时间重新请求
     * @return
     */
    public static Interceptor getCacheInterceptor() {
        Interceptor commonParams = new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                if (!NetworkUtils.isConnected()) {
                    //无网络下强制使用缓存，无论缓存是否过期,此时该请求实际上不会被发送出去。
                    request = request.newBuilder()
                            .cacheControl(CacheControl.FORCE_CACHE)
                            .build();
                }
                Response response = chain.proceed(request);
                if (NetworkUtils.isConnected()) {
                    //有网络情况下，根据请求接口的设置，配置缓存。
                    // 这样在下次请求时，根据缓存决定是否真正发出请求。
                    String cacheControl = request.cacheControl().toString();
                    //当然如果你想在有网络的情况下都直接走网络，那么只需要
                    //将其超时时间这是为0即可:String cacheControl="Cache-Control:public,max-age=0"
                    int maxAge = 60 * 60;
                    // read from cache for 1 minute
                    return response.newBuilder()
                            .header("Cache-Control", cacheControl)
                            .header("Cache-Control", "public, max-age=" + maxAge)
                            .removeHeader("Pragma") .build();
                } else {
                    //无网络
                    // tolerate 4-weeks stale
                    int maxStale = 60 * 60 * 24 * 28;
                    return response.newBuilder()
                            .header("Cache-Control", "public,only-if-cached,max-stale=360000")
                            .header("Cache-Control", "public,only-if-cached,max-stale=" + maxStale)
                            .removeHeader("Pragma") .build();
                }
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
                if (!NetworkUtils.isConnected()) {
                    request = request.newBuilder()
                            .cacheControl(CacheControl.FORCE_CACHE)
                            .build();
                    LogUtils.d("addNetWorkInterceptor"+ "没有网络链接");
                }
                Response response = chain.proceed(request);
                if (NetworkUtils.isConnected()) {
                    int maxAge = 0; // 有网络时 设置缓存超时时间0个小时
                    LogUtils.d("addNetWorkInterceptor"+ "网络已连接，缓存时间为：" + maxAge);
                    response = response.newBuilder()
                            .addHeader("Cache-Control", "public, max-age=" + maxAge)
                            .removeHeader("Pragma")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                            .build();
                } else {
                    int maxStale = Constant.TIME_CACHE;
                    LogUtils.d("addNetWorkInterceptor"+ "网络未连接，缓存时间为：" + maxStale);
                    response = response.newBuilder()
                            .addHeader("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                            .removeHeader("Pragma")
                            .build();
                }
                String cookeHeader = response.header("Set-Cookie", "");
                LogUtils.e("cookeHeader1-----------"+cookeHeader);
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

package com.ns.yc.lifehelper.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ns.yc.lifehelper.utils.InterceptorUtils;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/1/18
 * 描    述：RetrofitWrapper
 * 修订历史：
 * ================================================
 */
public class RetrofitWrapper {

    private static RetrofitWrapper instance;
    private Retrofit mRetrofit;

    public RetrofitWrapper(String url) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        //拦截日志，依赖
        builder.addInterceptor(InterceptorUtils.getHttpLoggingInterceptor());
        //拦截日志，自定义拦截日志
        //builder.addInterceptor(new LogInterceptor("YC"));
        //添加请求头拦截器
        //builder.addInterceptor(InterceptorUtils.getRequestHeader());
        //添加统一请求拦截器
        //builder.addInterceptor(InterceptorUtils.commonParamsInterceptor());
        //添加缓存拦截器
        //创建Cache
        //File httpCacheDirectory = new File("OkHttpCache");
        //Cache cache = new Cache(httpCacheDirectory, 10 * 1024 * 1024);
        //builder.cache(cache);
        //设置缓存
        //builder.addNetworkInterceptor(InterceptorUtils.getCacheInterceptor());
        //builder.addInterceptor(InterceptorUtils.getCacheInterceptor());
        OkHttpClient build = builder.build();

        //添加自定义CookieJar
        //InterceptorUtils.addCookie(builder);

        //解析json
        Gson gson = new GsonBuilder().setLenient().create();

        //获取实例
        mRetrofit = new Retrofit
                .Builder()                                                  //设置OKHttpClient,如果不设置会提供一个默认的
                .baseUrl(url)                                               //设置baseUrl
                .addConverterFactory(GsonConverterFactory.create(gson))     //添加Gson转换器
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(build)
                .build();
    }

    public  static RetrofitWrapper getInstance(String url){
        //synchronized 避免同时调用多个接口，导致线程并发
        synchronized (RetrofitWrapper.class){
            instance = new RetrofitWrapper(url);
        }
        return instance;
    }

    public <T> T create(final Class<T> service) {
        return mRetrofit.create(service);
    }
}

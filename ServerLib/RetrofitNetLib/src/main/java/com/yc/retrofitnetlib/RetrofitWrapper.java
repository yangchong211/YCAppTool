package com.yc.retrofitnetlib;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.yc.httpserver.CustomCookieJar;
import com.yc.netinterceptor.cache.CacheInterceptor;
import com.yc.netinterceptor.log.HttpLoggerLevel;
import com.yc.netinterceptor.log.HttpLoggingInterceptor;
import com.yc.networklib.BuildConfig;
import com.yc.notcapturelib.ssl.HttpSslConfig;
import com.yc.notcapturelib.ssl.HttpSslFactory;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2016/5/18
 * 描    述：统一接口实例的管理类RetrofitWrapper，清科网络库
 * 修订历史：
 * 特性：
 * 单例形式创建Retrofit实例；
 * 使用okhttp3作为请求客户端；
 * 使用gson作为数据转换器；
 * 使用RxJava优化异步请求流程；
 * 开启数据缓存，无网络时可从缓存读取数据；
 * 辅助类静态方法获取Retrofit Service实例。
 * ================================================
 */
public class RetrofitWrapper {

    private Retrofit mRetrofit;
    private OkHttpClient.Builder okHttpBuilder;


    /**
     * 获取实例，使用单利模式
     * 这里传递url参数，是因为项目中需要访问不同基类的地址
     * @param url               baseUrl
     * @return                  实例对象
     */
    public static RetrofitWrapper getInstance(String url){
        return getInstance(url,null);
    }

    /**
     * 获取实例，使用单利模式
     * 这里传递url参数，是因为项目中需要访问不同基类的地址
     * @param url               baseUrl
     * @return                  实例对象
     */
    public static RetrofitWrapper getInstance(String url,ArrayList<Interceptor> interceptors){
        //synchronized 避免同时调用多个接口，导致线程并发
        RetrofitWrapper instance;
        synchronized (RetrofitWrapper.class){
            instance = new RetrofitWrapper(url,interceptors);
        }
        return instance;
    }

    /**
     * 创建Retrofit
     * @param url               baseUrl
     */
    public RetrofitWrapper(String url , ArrayList<Interceptor> interceptors) {
        okHttpBuilder = new OkHttpClient.Builder();
        //builder.eventListenerFactory(NetworkListener.get());
        //builder.addNetworkInterceptor(new NetworkInterceptor());
        //拦截日志，依赖
        okHttpBuilder.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggerLevel.BODY));
        OkHttpClient build = okHttpBuilder.build();
        okHttpBuilder.addInterceptor(new CacheInterceptor());
        if (interceptors!=null && interceptors.size()>0){
            for (Interceptor interceptor : interceptors){
                okHttpBuilder.addInterceptor(interceptor);
            }
        }
        //添加自定义CookieJar
        okHttpBuilder.cookieJar(new CustomCookieJar());
        initBuilder(url,build);
    }


    private void initBuilder(String url, OkHttpClient build) {
        initSSL();
        initTimeOut();
        if(BuildConfig.DEBUG){
            //不需要错误重连
            okHttpBuilder.retryOnConnectionFailure(false);
        }else {
            //错误重连
            okHttpBuilder.retryOnConnectionFailure(true);
        }
        //获取实例
        mRetrofit = new Retrofit
                //设置OKHttpClient,如果不设置会提供一个默认的
                .Builder()
                //设置baseUrl
                .baseUrl(url)
                //添加转换器Converter(将json 转为JavaBean)，用来进行响应数据转化(反序列化)的ConvertFactory
                .addConverterFactory(GsonConverterFactory.create(JsonUtils.getJson()))
                //添加自定义转换器
                //.addConverterFactory(buildGsonConverterFactory())
                //添加rx转换器，用来生成对应"Call"的CallAdapter的CallAdapterFactory
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(build)
                .build();
    }


    public <T> T create(final Class<T> service) {
        return mRetrofit.create(service);
    }



    /**
     * 初始化完全信任的信任管理器
     */
    @SuppressWarnings("deprecation")
    private void initSSL() {
        try {
            HttpSslConfig httpSslConfig = HttpSslFactory.generateSslConfig();
            //不建议使用这个，方法过时，使用反射机制寻找X509信任管理类，消耗了不必要的性能。
            //okHttpBuilder.sslSocketFactory(sslSocketFactory);
            //会传入信任管理类数组中的第一条
            okHttpBuilder.sslSocketFactory(httpSslConfig.getSslSocketFactory(),httpSslConfig.getTrustManager());
            okHttpBuilder.hostnameVerifier(HttpSslFactory.generateUnSafeHostnameVerifier());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 设置读取超时时间，连接超时时间，写入超时时间值
     */
    private void initTimeOut() {
        okHttpBuilder.readTimeout(20000, TimeUnit.SECONDS);
        okHttpBuilder.connectTimeout(10000, TimeUnit.SECONDS);
        okHttpBuilder.writeTimeout(20000, TimeUnit.SECONDS);
        //错误重连
        okHttpBuilder.retryOnConnectionFailure(true);
    }


    /**
     * 构建GSON转换器
     * 这里，你可以自己去实现
     * @return GsonConverterFactory
     */
    private static GsonConverterFactory buildGsonConverterFactory(){
        GsonBuilder builder = new GsonBuilder();
        builder.setLenient();
        // 注册类型转换适配器
        builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
            @Override
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)  {
                return null == json ? null : new Date(json.getAsLong());
            }
        });

        Gson gson = builder.create();
        return GsonConverterFactory.create(gson);
    }




}

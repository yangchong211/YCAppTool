package com.ycbjie.music.api;

import android.annotation.SuppressLint;

import com.ycbjie.library.BuildConfig;
import com.ycbjie.library.constant.Constant;
import com.ycbjie.library.http.InterceptorUtils;
import com.ycbjie.library.http.JsonUtils;

import java.io.File;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/03/22
 *     desc  : MusicRetrofitWrapper
 *     revise:
 * </pre>
 */
public class MusicRetrofitWrapper {

    private static MusicRetrofitWrapper instance;
    private Retrofit mRetrofit;

    public  static MusicRetrofitWrapper getInstance(String url){
        if(instance==null){
            //synchronized 避免同时调用多个接口，导致线程并发
            synchronized (MusicRetrofitWrapper.class){
                instance = new MusicRetrofitWrapper(url);
            }
        }
        return instance;
    }


    public <T> T create(final Class<T> service) {
        return mRetrofit.create(service);
    }



    private MusicRetrofitWrapper(String url) {
        OkHttpClient okHttpClient = getOkHttpClient();
        //获取实例
        mRetrofit = new Retrofit
                //设置OKHttpClient,如果不设置会提供一个默认的
                .Builder()
                //设置baseUrl
                .baseUrl(url)
                //添加Gson转换器
                .addConverterFactory(GsonConverterFactory.create(JsonUtils.getJson()))
                //.addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }


    private OkHttpClient getOkHttpClient(){
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        //拦截日志，依赖
        builder.addInterceptor(new MusicHttpInterceptor());
        builder.addInterceptor(InterceptorUtils.getHttpLoggingInterceptor(true));


        //拦截日志，自定义拦截日志
        //builder.addInterceptor(new LogInterceptor("YC"));

        //添加请求头拦截器
        //builder.addInterceptor(InterceptorUtils.getRequestHeader());

        //添加网络缓存缓存
        //创建Cache
        if(BuildConfig.DEBUG){
            File httpCacheDirectory = new File("OkHttpCache");
            Cache cache = new Cache(httpCacheDirectory, Constant.CACHE_MAXSIZE);
            builder.cache(cache);
            //添加网络拦截器
            builder.addNetworkInterceptor(InterceptorUtils.getCacheInterceptor());
            builder.addInterceptor(InterceptorUtils.getCacheInterceptor());
        }


        //添加统一请求拦截器
        //builder.addInterceptor(InterceptorUtils.commonParamsInterceptor());
        //添加网络缓存拦截器，网络连接时请求服务器，否则从本地缓存中获取
        builder.addInterceptor(InterceptorUtils.addNetWorkInterceptor());

        //添加自定义CookieJar
        //InterceptorUtils.addCookie(builder);

        // Install the all-trusting trust manager
        initSSL(builder);
        //设置读取超时时间，连接超时时间，写入超时时间值
        initTimeOut(builder);
        if(BuildConfig.DEBUG){
            //不需要错误重连
            builder.retryOnConnectionFailure(false);
        }else {
            //错误重连
            builder.retryOnConnectionFailure(true);
        }
        return builder.build();
    }

    private void initSSL(OkHttpClient.Builder builder) {
        try {
            final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                @SuppressLint("TrustAllX509TrustManager")
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                }

                @SuppressLint("TrustAllX509TrustManager")
                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[]{};
                }
            }};

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new SecureRandom());
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            builder.sslSocketFactory(sslSocketFactory);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void initTimeOut(OkHttpClient.Builder builder) {
        builder.readTimeout(20000, TimeUnit.SECONDS);
        builder.connectTimeout(10000, TimeUnit.SECONDS);
        builder.writeTimeout(20000, TimeUnit.SECONDS);
    }

}
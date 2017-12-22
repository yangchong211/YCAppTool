package com.ns.yc.lifehelper.api;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ns.yc.lifehelper.utils.InterceptorUtils;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
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
    private Gson gson;
    private final OkHttpClient.Builder builder;


    public  static RetrofitWrapper getInstance(String url){
        //synchronized 避免同时调用多个接口，导致线程并发
        synchronized (RetrofitWrapper.class){
            instance = new RetrofitWrapper(url);
        }
        return instance;
    }

    public RetrofitWrapper(String url) {
        builder = new OkHttpClient.Builder();

        //拦截日志，依赖
        builder.addInterceptor(InterceptorUtils.getHttpLoggingInterceptor(true));
        OkHttpClient build = builder.build();

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

        //添加自定义CookieJar
        //InterceptorUtils.addCookie(builder);


        // Install the all-trusting trust manager
        initSSL();
        //设置读取超时时间，连接超时时间，写入超时时间值
        initTimeOut();
        //错误重连
        builder.retryOnConnectionFailure(true);

        //解析json
        gson = getJson();
        //gson = new GsonBuilder().setLenient().create();
        //获取实例
        mRetrofit = new Retrofit
                .Builder()                                                  //设置OKHttpClient,如果不设置会提供一个默认的
                .baseUrl(url)                                               //设置baseUrl
                .addConverterFactory(GsonConverterFactory.create(gson))     //添加Gson转换器
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(build)
                .build();
    }


    public <T> T create(final Class<T> service) {
        return mRetrofit.create(service);
    }

    private Gson getJson() {
        if (gson == null) {
            GsonBuilder builder = new GsonBuilder();
            builder.setLenient();
            builder.setFieldNamingStrategy(new AnnotateNaming());
            builder.serializeNulls();
            gson = builder.create();
        }
        return gson;
    }

    private void initSSL() {
        try {
            final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                }

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


    private void initTimeOut() {
        builder.readTimeout(20000, TimeUnit.SECONDS);
        builder.connectTimeout(10000, TimeUnit.SECONDS);
        builder.writeTimeout(20000, TimeUnit.SECONDS);
    }


    private static class AnnotateNaming implements FieldNamingStrategy {
        @Override
        public String translateName(Field field) {
            ParamNames a = field.getAnnotation(ParamNames.class);
            return a != null ? a.value() : FieldNamingPolicy.IDENTITY.translateName(field);
        }
    }

}


@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface ParamNames {
    String value();
}
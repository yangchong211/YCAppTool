package com.yc.webviewlib.cache;


import com.yc.networklib.net.AppNetworkUtils;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2020/5/17
 *     desc  : http缓存拦截起，主要是设置Cache-Control的这个属性
 *     revise:
 * </pre>
 */
public class HttpCacheInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        String cache = request.header(WebViewCacheWrapper.KEY_CACHE);
        Response originResponse = chain.proceed(request);
        if (cache != null && cache.equals(WebCacheType.NORMAL.ordinal() + "")) {
            return originResponse;
        }
        //Cache-Control 是最重要的规则。常见的取值有private、public、no-cache、max-age、no-store、默认是private
        //Cache-Control仅指定了max-age所以默认是private。
        //缓存时间是31536000，也就是说这个时间段的再次请求这条数据，都会直接获取缓存数据库中的数据，直接使用。
        //关于缓存原理，可以重点看一下这个类的源码：CacheInterceptor
        //缓存，待测试。在响应头里添加
        //Response response = chain.proceed(request);
        //Response.Builder responseBuilder = response.newBuilder();
        //setCacheBuilder(request,responseBuilder);

        return originResponse.newBuilder()
                .removeHeader("pragma")
                .removeHeader("Cache-Control")
                .header("Cache-Control","max-age=3153600000")
                .build();
    }



    private void setCacheBuilder(Request originalRequest, Response.Builder builder) {
        //清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
        builder.removeHeader("Pragma");
        if (!AppNetworkUtils.isConnected()) {
            //无网络下强制使用缓存，无论缓存是否过期,此时该请求实际上不会被发送出去。
            originalRequest = originalRequest.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        }
        if (AppNetworkUtils.isConnected()) {
            //有网络情况下，根据请求接口的设置，配置缓存。
            // 这样在下次请求时，根据缓存决定是否真正发出请求。
            String cacheControl = originalRequest.cacheControl().toString();
            //当然如果你想在有网络的情况下都直接走网络，那么只需要
            //将其超时时间这是为0即可:String cacheControl="Cache-Control:public,max-age=0"
            int maxAge = 60 * 60;
            // read from cache for 1 minute
            builder.addHeader("Cache-Control", cacheControl);
            builder.addHeader("Cache-Control", "public, max-age=" + maxAge);
        } else {
            //无网络
            // tolerate 4-weeks stale
            int maxStale = 60 * 60 * 24 * 28;
            builder.header("Cache-Control", "public,only-if-cached,max-stale=360000");
            builder.header("Cache-Control", "public,only-if-cached,max-stale=" + maxStale);
        }
    }


}

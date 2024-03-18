package com.yc.monitorinterceptor.cache;

import com.yc.networklib.net.AppNetworkUtils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     GitHub : https://github.com/yangchong211
 *     time  : 2018/11/9
 *     desc  : 缓存拦截器
 *     revise:
 * </pre>
 */
public class CacheInterceptor implements Interceptor {

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request request = chain.request();
        if (!AppNetworkUtils.isConnected()) {
            //无网络下强制使用缓存，无论缓存是否过期,此时该请求实际上不会被发送出去。
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        }
        Response response = chain.proceed(request);
        if (AppNetworkUtils.isConnected()) {
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
            // 无网络
            // tolerate 4-weeks stale
            int maxStale = 60 * 60 * 24 * 28;
            return response.newBuilder()
                    .header("Cache-Control", "public,only-if-cached,max-stale=360000")
                    .header("Cache-Control", "public,only-if-cached,max-stale=" + maxStale)
                    .removeHeader("Pragma") .build();
        }
    }
}

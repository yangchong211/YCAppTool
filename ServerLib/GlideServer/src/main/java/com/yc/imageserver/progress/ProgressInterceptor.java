package com.yc.imageserver.progress;


import com.yc.toolutils.system.AppLogUtils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/10/24
 *     desc  : glide加载进度工具
 *     revise: 自定义拦截器
 *             向OkHttp中添加一个自定义的拦截器，就可以在拦截器中捕获到整个HTTP的通讯过程，
 *             然后加入一些自己的逻辑来计算下载进度，这样就可以实现下载进度监听的功能了。
 *             可以参考：https://github.com/JessYanCoding/ProgressManager
 * </pre>
 */
public class ProgressInterceptor implements Interceptor {

    /**
     * 使用了一个Map来保存注册的监听器，Map的键是一个URL地址。
     * 之所以要这么做，是因为你可能会使用Glide同时加载很多张图片，
     * 而这种情况下，必须要能区分出来每个下载进度的回调到底是对应哪个图片URL地址的。
     */
    public static final Map<String, ProgressListener> LISTENER_MAP = new HashMap<>();

    /**
     * 入注册下载监听
     * @param url
     * @param listener
     */
    public static void addListener(String url, ProgressListener listener) {
        LISTENER_MAP.put(url, listener);
    }

    /**
     * 取消注册下载监听
     * @param url
     */
    public static void removeListener(String url) {
        LISTENER_MAP.remove(url);
    }

    @NotNull
    @Override
    public Response intercept(@NotNull Interceptor.Chain chain) throws IOException {
        //拦截到了OkHttp的请求，然后调用proceed()方法去处理这个请求，最终将服务器响应的Response返回。
        Request request = chain.request();
        Response response = chain.proceed(request);
        //拿到请求url
        String url = request.url().toString();
        //拿到相应body对象
        ResponseBody body = response.body();
        AppLogUtils.d("加载图片进度值------自定义拦截器--");
        ProgressResponseBody progressResponseBody = new ProgressResponseBody(url, body);
        Response.Builder builder = response.newBuilder();
        Response newResponse = builder.body(progressResponseBody).build();
        ResponseBody responseBody = newResponse.body();
        return newResponse;
    }

}


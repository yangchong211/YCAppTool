package com.yc.netlib.weaknet;


import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 用于模拟弱网的拦截器
 */
public class WeakNetworkInterceptor implements Interceptor {

    private static final String TAG = "WeakNetworkInterceptor";

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        if (!WeakNetworkManager.get().isActive()) {
            Request request = chain.request();
            return chain.proceed(request);
        }
        final int type = WeakNetworkManager.get().getType();
        switch (type) {
            case WeakNetworkManager.TYPE_TIMEOUT:
                //超时
                return WeakNetworkManager.get().simulateTimeOut(chain);
            case WeakNetworkManager.TYPE_SPEED_LIMIT:
                //限速
                return WeakNetworkManager.get().simulateSpeedLimit(chain);
            default:
                //断网
                return WeakNetworkManager.get().simulateOffNetwork(chain);
        }
    }
}

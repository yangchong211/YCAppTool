package com.yc.monitorinterceptor;


import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/01/30
 *     desc  : 用于模拟弱网的拦截器
 *     revise:
 * </pre>
 */
public class WeakNetworkInterceptor implements Interceptor {

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        if (!WeakNetworkManager.get().isActive()) {
            Request request = chain.request();
            return chain.proceed(request);
        }
        final int type = WeakNetworkManager.get().getType();
        switch (type) {
            //超时
            case WeakNetworkManager.TYPE_TIMEOUT:
                return WeakNetworkManager.get().simulateTimeOut(chain);
            //限速
            case WeakNetworkManager.TYPE_SPEED_LIMIT:
                return WeakNetworkManager.get().simulateSpeedLimit(chain);
            //重定向
            case WeakNetworkManager.TYPE_REDIRECTED:
                return WeakNetworkManager.get().simulateRedirectNetwork(chain);
            //服务端异常
            case WeakNetworkManager.TYPE_SERVER_ERROR:
                return WeakNetworkManager.get().simulateServerErrorNetwork(chain);
            //超时，响应超时
            case WeakNetworkManager.TYPE_TIMEOUT_RESPOND:
                return WeakNetworkManager.get().simulateRespondTimeOut(chain);
            //断网
            default:
                return WeakNetworkManager.get().simulateOffNetwork(chain);
        }
    }
}

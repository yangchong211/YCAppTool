package com.ycbjie.music.api;

import android.os.Build;
import android.support.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/03/22
 *     desc  : 拦截器
 *     revise:
 * </pre>
 */
public class MusicHttpInterceptor implements Interceptor {

    private static final String UA = "User-Agent";

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request()
                .newBuilder()
                //添加具有{@代码名}和{@代码值}的Header。更喜欢这种多值头的方法，比如“Cookie”。
                .addHeader(UA, makeUA())
                .build();
        return chain.proceed(request);
    }

    private String makeUA() {
        return Build.BRAND + "/" + Build.MODEL + "/" + Build.VERSION.RELEASE;
    }

}

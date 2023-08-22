package com.yc.nettestlib;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NetTestHelper {

    private static final String PING_ADDRESS = "http://www.qq.com/";

    /**
     * 单例对象
     */
    private volatile static NetTestHelper sInstance;

    /**
     * 单例模式
     *
     * @return ActivityManager对象
     */
    public static NetTestHelper getInstance() {
        if (sInstance == null) {
            synchronized (NetTestHelper.class) {
                if (sInstance == null) {
                    sInstance = new NetTestHelper();
                }
            }
        }
        return sInstance;
    }

    private NetTestHelper() {

    }

    public void testNetwork(OnNetTestListener listener) {
        pingOutside(listener);
    }


    /**
     * ping功能，有响应代表成功
     *
     * @param onTestListener 回调
     */
    public void pingOutside(OnNetTestListener onTestListener) {
        get(PING_ADDRESS, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                if (onTestListener != null) {
                    if (e.toString().contains("UnknownHostException")
                            || e.toString().contains("unreachable")) {
                        onTestListener.onTest(false, "网络不通，请检查网络设置");
                    } else if (e.toString().contains("SocketTimeoutException")) {
                        onTestListener.onTest(false, "网络连接超时，请检查网络设置");
                    } else {
                        onTestListener.onTest(false, "网络检测失败，" + e.getMessage());
                    }
                }
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                try {
                    if (onTestListener != null) {
                        if (response.body() != null && response.body().string().contains("腾讯")) {
                            onTestListener.onTest(true, "互联网连接正常");
                        } else {
                            onTestListener.onTest(false, "需要认证的网络");
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    onTestListener.onTest(false, "IOException");
                }
            }
        });
    }


    /**
     * get请求
     *
     * @param url 地址
     * @param callback 回调
     */
    private void get(String url, Callback callback) {
        //String url = "http://www.qq.com/";
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        OkHttpClient okHttpClient = builder.build();
        //默认就是GET请求，可以不写
        final Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(callback);
    }


}

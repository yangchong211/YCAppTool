package com.yc.appmonitor.net;




import com.yc.netlib.stetho.NetworkInterceptor;
import com.yc.netlib.stetho.NetworkListener;

import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class OkHttpManager {

    private static final String TAG = "OkHttpManager";
    private static OkHttpManager sInstance;

    public static OkHttpManager getInstance() {
        if (sInstance == null) {
            sInstance = new OkHttpManager();
        }
        return sInstance;
    }

    public void get(String url, Callback callback) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .eventListenerFactory(NetworkListener.get())
                .addNetworkInterceptor(new NetworkInterceptor())
                .build();

        Request request = new Request.Builder()
                .url(url)
                .build();

        okHttpClient.newCall(request).enqueue(callback);
    }

    /**
     * 普通的post请求
     * @param url               请求地址，比如：http://api.tianapi.com/social/
     * @param mapParams         请求参数，比如key=APIKEY；num=10；……
     * @param callback          callback
     *
     *                          使用场景：比如投资界，新芽list页面用post请求
     */
    public void post(String url, Map<String, Object> mapParams, Callback callback){
        StringBuilder sb = new StringBuilder();
        for (String key :mapParams.keySet()) {
            sb.append(key)
                    .append("=")
                    .append("")
                    .append(mapParams.get(key))
                    .append("&");
        }
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .eventListenerFactory(NetworkListener.get())
                .addNetworkInterceptor(new NetworkInterceptor())
                .build();
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/x-www-form-urlencoded"),sb.toString());
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(callback);
    }


}

package com.yc.appmonitor.net;

import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/8/22
 *     desc  : MVC中的model层
 *     revise:
 * </pre>
 */
public class NetworkModel {

    private static final String TAG = "NetworkModel";

    public interface DataListener<T>{
        void onSuccess(T t);
        void onError(String error);
    }

    public void getData(String url,DataListener<String> listener){
        new Thread(){
            @Override
            public void run() {
                super.run();
                OkHttpManager.getInstance().get(url, new Callback() {
                    @Override
                    public void onFailure(Call call, final IOException e) {
                        Log.e(TAG, "e = "+e.getMessage());
                        listener.onError(e.getMessage());
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        final String body;
                        try {
                            body = response.body().string();
                            Log.d(TAG, body);
                            listener.onSuccess(body);
                        } catch (IOException e) {
                            e.printStackTrace();
                            listener.onError(e.getMessage());
                        }
                    }
                });
            }
        }.start();
    }

}

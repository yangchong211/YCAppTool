package com.yc.http.synthesis.model;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;
import okio.Timeout;

/**
 *    @author yangchong
 *    GitHub : https://github.com/yangchong211/YCAppTool
 *    time   : 2019/12/14
 *    desc   : 请求对象代理
 */
public final class CallProxy implements Call {

    private Call mCall;

    public CallProxy(@NonNull Call call) {
        mCall = call;
    }

    public void setCall(@NonNull Call call) {
        mCall = call;
    }

    @NonNull
    @Override
    public Request request() {
        return mCall.request();
    }

    @NonNull
    @Override
    public Response execute() throws IOException {
        return mCall.execute();
    }

    @Override
    public void enqueue(@NonNull Callback responseCallback) {
        mCall.enqueue(responseCallback);
    }

    @Override
    public void cancel() {
        mCall.cancel();
    }

    @Override
    public boolean isExecuted() {
        return mCall.isExecuted();
    }

    @Override
    public boolean isCanceled() {
        return mCall.isCanceled();
    }

    @NonNull
    @Override
    public Timeout timeout() {
        return mCall.timeout();
    }

    @NonNull
    @Override
    public Call clone() {
        return mCall.clone();
    }
}
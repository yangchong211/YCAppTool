package com.yc.http.synthesis.callback;

import com.yc.http.synthesis.EasyConfig;
import com.yc.http.synthesis.EasyLog;
import com.yc.http.synthesis.EasyUtils;
import com.yc.http.synthesis.lifecycle.HttpLifecycleManager;
import com.yc.http.synthesis.model.CallProxy;
import com.yc.http.synthesis.request.HttpRequest;

import java.io.IOException;
import java.net.SocketTimeoutException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 *    @author yangchong
 *    GitHub : https://github.com/yangchong211/YCAppTool
 *    time   : 2019/11/25
 *    desc   : 接口回调基类
 */
public abstract class BaseCallback implements Callback {

    /** 请求配置 */
    private final HttpRequest<?> mHttpRequest;

    /** 请求任务对象 */
    private CallProxy mCall;

    /** 当前重试次数 */
    private int mRetryCount;

    public BaseCallback(HttpRequest<?> request) {
        mHttpRequest = request;
        HttpLifecycleManager.bind(mHttpRequest.getLifecycleOwner());
    }

    public BaseCallback setCall(CallProxy call) {
        mCall = call;
        return this;
    }

    public void start() {
        mCall.enqueue(this);
        onStart(mCall);
    }

    protected CallProxy getCall() {
        return mCall;
    }

    @Override
    public void onResponse(Call call, Response response) {
        try {
            // 收到响应
            onResponse(response);
        } catch (Exception e) {
            // 回调失败
            onFailure(e);
        } finally {
            // 关闭响应
            EasyUtils.closeStream(response);
        }
    }

    @Override
    public void onFailure(Call call, IOException e) {
        // 服务器请求超时重试
        if (e instanceof SocketTimeoutException && mRetryCount < EasyConfig.getInstance().getRetryCount()) {
            // 设置延迟 N 秒后重试该请求
            EasyUtils.postDelayed(() -> {

                // 前提是宿主还没有被销毁
                if (!HttpLifecycleManager.isLifecycleActive(mHttpRequest.getLifecycleOwner())) {
                    // 宿主已被销毁，请求无法进行
                    EasyLog.printLog(mHttpRequest, "LifecycleOwner has been destroyed and the request cannot be made");
                    return;
                }

                mRetryCount++;
                Call newCall = call.clone();
                mCall.setCall(newCall);
                newCall.enqueue(BaseCallback.this);
                // 请求超时，正在执行延迟重试
                EasyLog.printLog(mHttpRequest, "The request timed out, a delayed retry is being performed, the number of retries: " +
                        mRetryCount + " / " + EasyConfig.getInstance().getRetryCount());

            }, EasyConfig.getInstance().getRetryTime());

            return;
        }
        onFailure(e);
    }

    /**
     * 请求开始
     */
    protected abstract void onStart(Call call);

    /**
     * 请求成功
     */
    protected abstract void onResponse(Response response) throws Exception;

    /**
     * 请求失败
     */
    protected abstract void onFailure(Exception e);
}
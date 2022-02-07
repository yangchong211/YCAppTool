package com.yc.netlib.weaknet;

import android.annotation.SuppressLint;
import android.os.SystemClock;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/01/30
 *     desc  : 弱网管理Manager
 *     revise:
 * </pre>
 */
public class WeakNetworkManager {

    public static final int TYPE_OFF_NETWORK = 0;
    public static final int TYPE_TIMEOUT = 1;
    public static final int TYPE_SPEED_LIMIT = 2;

    public static final int DEFAULT_TIMEOUT_MILLIS = 2000;
    public static final int DEFAULT_REQUEST_SPEED = 1;
    public static final int DEFAULT_RESPONSE_SPEED = 1;

    private int mType = TYPE_OFF_NETWORK;
    private long mTimeOutMillis = DEFAULT_TIMEOUT_MILLIS;
    private long mRequestSpeed = DEFAULT_REQUEST_SPEED;
    private long mResponseSpeed = DEFAULT_RESPONSE_SPEED;

    private AtomicBoolean mIsActive = new AtomicBoolean(false);

    private static class Holder {
        private static WeakNetworkManager INSTANCE = new WeakNetworkManager();
    }

    public static WeakNetworkManager get() {
        //单利模式
        return WeakNetworkManager.Holder.INSTANCE;
    }

    /**
     * 判断是否可用
     * @return
     */
    public boolean isActive() {
        return mIsActive.get();
    }

    /**
     * 设置是否可用
     * @param isActive                      是否可用
     */
    public void setActive(boolean isActive) {
        mIsActive.set(isActive);
    }

    /**
     * 设置相关参数
     * @param timeOutMillis                 超时时间
     * @param requestSpeed                  请求限速值
     * @param responseSpeed                 响应限速值
     */
    public void setParameter(long timeOutMillis, long requestSpeed, long responseSpeed) {
        if (timeOutMillis > 0) {
            mTimeOutMillis = timeOutMillis;
        }
        mRequestSpeed = requestSpeed;
        mResponseSpeed = responseSpeed;
    }

    /**
     * 设置类型
     * @param type
     */
    public void setType(int type) {
        mType = type;
    }

    /**
     * 获取类型
     * @return
     */
    public int getType() {
        return mType;
    }

    /**
     * 获取网络超时时间
     * @return
     */
    public long getTimeOutMillis() {
        return mTimeOutMillis;
    }

    /**
     *
     * @return
     */
    public long getRequestSpeed() {
        return mRequestSpeed;
    }

    public long getResponseSpeed() {
        return mResponseSpeed;
    }

    /**
     * 模拟断网
     */
    public Response simulateOffNetwork(Interceptor.Chain chain) throws IOException {
        final Response response = chain.proceed(chain.request());
        ResponseBody responseBody = ResponseBody.create(response.body().contentType(), "");
        Response newResponse = response.newBuilder()
                .code(400)
                .message(String.format("Unable to resolve host %s: No address associated with hostname",
                        chain.request().url().host()))
                .body(responseBody)
                .build();
        return newResponse;
    }

    /**
     * 模拟超时
     *
     * @param chain url
     */
    public Response simulateTimeOut(Interceptor.Chain chain) throws IOException {
        SystemClock.sleep(mTimeOutMillis);
        final Response response = chain.proceed(chain.request());
        ResponseBody body = response.body();
        if (body!=null && body.contentType()!=null){
            ResponseBody responseBody = ResponseBody.create(body.contentType(), "");
            String host = chain.request().url().host();
            @SuppressLint("DefaultLocale")
            String format = String.format("failed to connect to %s  after %dms", host, mTimeOutMillis);
            Response newResponse = response.newBuilder()
                    .code(400)
                    .message(format)
                    .body(responseBody)
                    .build();
            return newResponse;
        }
        return response;
    }

    /**
     * 限速
     */
    public Response simulateSpeedLimit(Interceptor.Chain chain) throws IOException {
        Request request = chain.request();
        final RequestBody body = request.body();
        if (body != null) {
            //大于0使用限速的body 否则使用原始body
            final RequestBody requestBody = mRequestSpeed > 0 ?
                    new SpeedLimitRequestBody(mRequestSpeed, body) : body;
            request = request.newBuilder().method(request.method(), requestBody).build();
        }
        final Response response = chain.proceed(request);
        //大于0使用限速的body 否则使用原始body
        final ResponseBody responseBody = response.body();
        final ResponseBody newResponseBody = mResponseSpeed > 0 ? new
                SpeedLimitResponseBody(mResponseSpeed, responseBody) : responseBody;
        return response.newBuilder().body(newResponseBody).build();
    }
}

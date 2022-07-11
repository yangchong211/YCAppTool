package com.yc.monitorinterceptor;

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

    /**
     * 没有网络
     */
    public static final int TYPE_OFF_NETWORK = 0;
    /**
     * 网络超时
     */
    public static final int TYPE_TIMEOUT = 1;
    /**
     * 限速
     */
    public static final int TYPE_SPEED_LIMIT = 2;
    /**
     * 重定向
     */
    public static final int TYPE_REDIRECTED = 3;
    public static final int DEFAULT_TIMEOUT_MILLIS = 2000;
    public static final int DEFAULT_REQUEST_SPEED = 1;
    public static final int DEFAULT_RESPONSE_SPEED = 1;

    private int mType = TYPE_OFF_NETWORK;
    private long mTimeOutMillis = DEFAULT_TIMEOUT_MILLIS;
    private long mRequestSpeed = DEFAULT_REQUEST_SPEED;
    private long mResponseSpeed = DEFAULT_RESPONSE_SPEED;
    private final AtomicBoolean mIsActive = new AtomicBoolean(false);

    private static class Holder {
        private static final WeakNetworkManager INSTANCE = new WeakNetworkManager();
    }

    public static WeakNetworkManager get() {
        //单利模式
        return WeakNetworkManager.Holder.INSTANCE;
    }

    /**
     * 判断是否可用
     *
     * @return true表示可用
     */
    public boolean isActive() {
        return mIsActive.get();
    }

    /**
     * 设置是否可用
     *
     * @param isActive 是否可用
     */
    public void setActive(boolean isActive) {
        mIsActive.set(isActive);
    }

    /**
     * 设置相关参数
     *
     * @param timeOutMillis 超时时间
     * @param requestSpeed  请求限速值
     * @param responseSpeed 响应限速值
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
     *
     * @param type
     */
    public void setType(int type) {
        mType = type;
    }

    /**
     * 获取类型
     *
     * @return
     */
    public int getType() {
        return mType;
    }

    /**
     * 模拟断网
     * 400（错误请求），401（未授权），403（已禁止），404（未找到）……
     * 此类状态代码表示，相应请求可能出错，已阻止了服务器对请求的处理。
     */
    public Response simulateOffNetwork(Interceptor.Chain chain) throws IOException {
        final Response response = chain.proceed(chain.request());
        ResponseBody body = response.body();
        ResponseBody responseBody = null;
        if (body != null) {
            responseBody = ResponseBody.create(body.contentType(), "");
        }
        return response.newBuilder()
                .code(400)
                .message(String.format("Unable to resolve host %s: No address associated with hostname",
                        chain.request().url().host()))
                .body(responseBody)
                .build();
    }

    /**
     * 模拟服务端网络异常
     * 500（服务器内部错误），501（尚未实施），502（错误网关），503（服务不可用），504（网关超时）
     * 此类状态代码表示，服务器在尝试处理相应请求时发生内部错误。此类错误往往与服务器本身有关（与请求无关）。
     */
    public Response simulateServerErrorNetwork(Interceptor.Chain chain) throws IOException {
        final Response response = chain.proceed(chain.request());
        ResponseBody body = response.body();
        ResponseBody responseBody = null;
        if (body != null) {
            responseBody = ResponseBody.create(body.contentType(), "");
        }
        return response.newBuilder()
                .code(500)
                .message(String.format("Unable to resolve host %s: internal server error",
                        chain.request().url().host()))
                .body(responseBody)
                .build();
    }


    /**
     * 模拟重定向
     */
    public Response simulateRedirectNetwork(Interceptor.Chain chain) throws IOException {
        final Response response = chain.proceed(chain.request());
        ResponseBody body = response.body();
        ResponseBody responseBody = null;
        if (body != null) {
            responseBody = ResponseBody.create(body.contentType(), "");
        }
        return response.newBuilder()
                .code(300)
                .message(String.format("Unable to resolve host %s: The target address has been redirected",
                        chain.request().url().host()))
                .body(responseBody)
                .build();
    }

    /**
     * 模拟超时
     */
    public Response simulateTimeOut(Interceptor.Chain chain) throws IOException {
        SystemClock.sleep(mTimeOutMillis);
        final Response response = chain.proceed(chain.request());
        ResponseBody body = response.body();
        if (body != null && body.contentType() != null) {
            ResponseBody responseBody = ResponseBody.create(body.contentType(), "");
            String host = chain.request().url().host();
            @SuppressLint("DefaultLocale")
            String format = String.format("failed to connect to %s  after %dms", host, mTimeOutMillis);
            return response.newBuilder()
                    .code(400)
                    .message(format)
                    .body(responseBody)
                    .build();
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

package com.yc.http.synthesis.callback;

import com.yc.http.synthesis.EasyLog;
import com.yc.http.synthesis.EasyUtils;
import com.yc.http.synthesis.config.IRequestInterceptor;
import com.yc.http.synthesis.lifecycle.HttpLifecycleManager;
import com.yc.http.synthesis.listener.OnHttpListener;
import com.yc.http.synthesis.model.CacheMode;
import com.yc.http.synthesis.request.HttpRequest;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.Response;

/**
 *    @author yangchong
 *    GitHub : https://github.com/yangchong211/YCAppTool
 *    time   : 2019/11/25
 *    desc   : 正常接口回调
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public final class NormalCallback extends BaseCallback {

    /** 请求配置 */
    private final HttpRequest mHttpRequest;
    /** 接口回调 */
    private OnHttpListener mListener;
    /** 解析类型 */
    private Type mReflectType;

    public NormalCallback(HttpRequest request) {
        super(request);
        mHttpRequest = request;
    }

    public NormalCallback setListener(OnHttpListener listener) {
        mListener = listener;
        mReflectType = mHttpRequest.getRequestHandler().getType(mListener);
        return this;
    }

    @Override
    public void start() {
        CacheMode cacheMode = mHttpRequest.getRequestCache().getCacheMode();
        if (cacheMode != CacheMode.USE_CACHE_ONLY &&
                cacheMode != CacheMode.USE_CACHE_FIRST) {
            super.start();
            return;
        }

        try {
            Object result = mHttpRequest.getRequestHandler().readCache(mHttpRequest,
                    mReflectType, mHttpRequest.getRequestCache().getCacheTime());
            EasyLog.printLog(mHttpRequest, "ReadCache result：" + result);

            // 如果没有缓存，就请求网络
            if (result == null) {
                super.start();
                return;
            }

            // 读取缓存成功
            EasyUtils.post(() -> {
                if (mListener == null || !HttpLifecycleManager.isLifecycleActive(mHttpRequest.getLifecycleOwner())) {
                    return;
                }
                mListener.onStart(getCall());
                mListener.onSucceed(result, true);
                mListener.onEnd(getCall());
            });

            // 如果当前模式是先读缓存再写请求
            if (cacheMode == CacheMode.USE_CACHE_FIRST) {
                EasyUtils.postDelayed(() -> {
                    if (!HttpLifecycleManager.isLifecycleActive(mHttpRequest.getLifecycleOwner())) {
                        return;
                    }
                    // 将回调置为空，避免出现两次回调
                    mListener = null;
                    super.start();
                }, 1);
            }

        } catch (Exception cacheException) {
            EasyLog.printLog(mHttpRequest, "ReadCache error");
            EasyLog.printThrowable(mHttpRequest, cacheException);
            super.start();
        }
    }

    @Override
    protected void onStart(Call call) {
        EasyUtils.post(() -> {
            if (mListener == null || !HttpLifecycleManager.isLifecycleActive(mHttpRequest.getLifecycleOwner())) {
                return;
            }
            mListener.onStart(call);
        });
    }

    @Override
    protected void onResponse(Response response) throws Exception {
        // 打印请求耗时时间
        EasyLog.printLog(mHttpRequest, "RequestConsuming：" +
                (response.receivedResponseAtMillis() - response.sentRequestAtMillis()) + " ms");

        IRequestInterceptor interceptor = mHttpRequest.getRequestInterceptor();
        if (interceptor != null) {
            response = interceptor.interceptResponse(mHttpRequest, response);
        }

        // 解析 Bean 类对象
        final Object result = mHttpRequest.getRequestHandler().requestSucceed(
                mHttpRequest, response, mReflectType);

        CacheMode cacheMode = mHttpRequest.getRequestCache().getCacheMode();
        if (cacheMode == CacheMode.USE_CACHE_ONLY ||
                cacheMode == CacheMode.USE_CACHE_FIRST ||
                cacheMode == CacheMode.USE_CACHE_AFTER_FAILURE) {
            try {
                boolean writeSucceed = mHttpRequest.getRequestHandler().writeCache(mHttpRequest, response, result);
                EasyLog.printLog(mHttpRequest, "WriteCache result：" + writeSucceed);
            } catch (Exception cacheException) {
                EasyLog.printLog(mHttpRequest, "WriteCache error");
                EasyLog.printThrowable(mHttpRequest, cacheException);
            }
        }

        EasyUtils.post(() -> {
            if (mListener == null || !HttpLifecycleManager.isLifecycleActive(mHttpRequest.getLifecycleOwner())) {
                return;
            }
            mListener.onSucceed(result, false);
            mListener.onEnd(getCall());
        });
    }

    @Override
    protected void onFailure(Exception exception) {
        // 打印错误堆栈
        EasyLog.printThrowable(mHttpRequest, exception);
        // 如果设置了只在网络请求失败才去读缓存
        if (exception instanceof IOException && mHttpRequest.getRequestCache().getCacheMode() == CacheMode.USE_CACHE_AFTER_FAILURE) {
            try {
                Object result = mHttpRequest.getRequestHandler().readCache(mHttpRequest,
                        mReflectType, mHttpRequest.getRequestCache().getCacheTime());
                EasyLog.printLog(mHttpRequest, "ReadCache result：" + result);
                if (result != null) {
                    EasyUtils.post(() -> {
                        if (mListener == null || !HttpLifecycleManager.isLifecycleActive(mHttpRequest.getLifecycleOwner())) {
                            return;
                        }
                        mListener.onSucceed(result, true);
                        mListener.onEnd(getCall());
                    });
                    return;
                }
            } catch (Exception cacheException) {
                EasyLog.printLog(mHttpRequest, "ReadCache error");
                EasyLog.printThrowable(mHttpRequest, cacheException);
            }
        }

        final Exception finalException = mHttpRequest.getRequestHandler().requestFail(mHttpRequest, exception);
        if (finalException != exception) {
            EasyLog.printThrowable(mHttpRequest, finalException);
        }

        EasyUtils.post(() -> {
            if (mListener == null || !HttpLifecycleManager.isLifecycleActive(mHttpRequest.getLifecycleOwner())) {
                return;
            }
            mListener.onFail(finalException);
            mListener.onEnd(getCall());
        });
    }
}
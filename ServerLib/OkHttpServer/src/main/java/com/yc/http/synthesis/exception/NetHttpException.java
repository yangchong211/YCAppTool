package com.yc.http.synthesis.exception;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 *    @author yangchong
 *    GitHub : https://github.com/yangchong211/YCAppTool
 *    time   : 2019/05/19
 *    desc   : 网络请求异常
 */
public class NetHttpException extends Exception {

    private final String mMessage;
    private Throwable mThrowable;

    public NetHttpException(String message) {
        super(message);
        mMessage = message;
    }

    public NetHttpException(String message, Throwable cause) {
        super(message, cause);
        mMessage = message;
        mThrowable = cause;
    }

    /**
     * 获取错误信息
     */
    @Override
    public String getMessage() {
        return mMessage;
    }

    @NonNull
    @Override
    public StackTraceElement[] getStackTrace() {
        if (mThrowable != null) {
            return mThrowable.getStackTrace();
        }
        return super.getStackTrace();
    }

    @Nullable
    @Override
    public synchronized Throwable getCause() {
        if (mThrowable != null) {
            return mThrowable.getCause();
        }
        return super.getCause();
    }
}
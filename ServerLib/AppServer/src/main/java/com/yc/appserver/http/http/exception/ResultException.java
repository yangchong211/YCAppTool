package com.yc.appserver.http.http.exception;

import androidx.annotation.NonNull;

import com.yc.appserver.http.http.model.HttpData;
import com.yc.http.synthesis.exception.NetHttpException;

public final class ResultException extends NetHttpException {

    private final HttpData<?> mData;

    public ResultException(String message, HttpData<?> data) {
        super(message);
        mData = data;
    }

    public ResultException(String message, Throwable cause, HttpData<?> data) {
        super(message, cause);
        mData = data;
    }

    @NonNull
    public HttpData<?> getHttpData() {
        return mData;
    }
}
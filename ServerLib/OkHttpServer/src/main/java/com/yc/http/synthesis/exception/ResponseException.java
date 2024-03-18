package com.yc.http.synthesis.exception;

import okhttp3.Response;

/**
 *    @author yangchong
 *    GitHub : https://github.com/yangchong211/YCAppTool
 *    time   : 2019/06/25
 *    desc   : 服务器响应异常
 */
public final class ResponseException extends NetHttpException {

    private final Response mResponse;

    public ResponseException(String message, Response response) {
        super(message);
        mResponse = response;
    }

    public ResponseException(String message, Throwable cause, Response response) {
        super(message, cause);
        mResponse = response;
    }

    public Response getResponse() {
        return mResponse;
    }
}
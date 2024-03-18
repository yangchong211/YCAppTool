package com.yc.http.synthesis.exception;

/**
 *    @author yangchong
 *    GitHub : https://github.com/yangchong211/YCAppTool
 *    time   : 2019/06/25
 *    desc   : 网络连接异常
 */
public final class NetworkException extends NetHttpException {

    public NetworkException(String message) {
        super(message);
    }

    public NetworkException(String message, Throwable cause) {
        super(message, cause);
    }
}
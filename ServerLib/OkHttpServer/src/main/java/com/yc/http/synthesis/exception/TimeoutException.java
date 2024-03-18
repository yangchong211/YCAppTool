package com.yc.http.synthesis.exception;

/**
 *    @author yangchong
 *    GitHub : https://github.com/yangchong211/YCAppTool
 *    time   : 2019/06/25
 *    desc   : 服务器超时异常
 */
public final class TimeoutException extends NetHttpException {

    public TimeoutException(String message) {
        super(message);
    }

    public TimeoutException(String message, Throwable cause) {
        super(message, cause);
    }
}
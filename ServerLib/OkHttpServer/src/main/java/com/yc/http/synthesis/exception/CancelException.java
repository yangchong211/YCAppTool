package com.yc.http.synthesis.exception;

/**
 *    @author yangchong
 *    GitHub : https://github.com/yangchong211/YCAppTool
 *    time   : 2019/06/25
 *    desc   : 请求取消异常
 */
public final class CancelException extends NetHttpException {

    public CancelException(String message) {
        super(message);
    }

    public CancelException(String message, Throwable cause) {
        super(message, cause);
    }
}
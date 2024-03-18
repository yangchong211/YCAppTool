package com.yc.http.synthesis.exception;

/**
 *    @author yangchong
 *    GitHub : https://github.com/yangchong211/YCAppTool
 *    time   : 2019/12/24
 *    desc   : 空实体异常
 */
public final class NullBodyException extends NetHttpException {

    public NullBodyException(String message) {
        super(message);
    }

    public NullBodyException(String message, Throwable cause) {
        super(message, cause);
    }
}
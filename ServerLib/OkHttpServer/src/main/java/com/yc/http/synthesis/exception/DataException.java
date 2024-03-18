package com.yc.http.synthesis.exception;

/**
 *    @author yangchong
 *    GitHub : https://github.com/yangchong211/YCAppTool
 *    time   : 2019/06/25
 *    desc   : 数据解析异常
 */
public final class DataException extends NetHttpException {

    public DataException(String message) {
        super(message);
    }

    public DataException(String message, Throwable cause) {
        super(message, cause);
    }
}
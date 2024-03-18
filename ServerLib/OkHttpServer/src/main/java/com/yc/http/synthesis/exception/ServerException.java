package com.yc.http.synthesis.exception;

/**
 *    @author yangchong
 *    GitHub : https://github.com/yangchong211/YCAppTool
 *    time   : 2019/12/01
 *    desc   : 服务器连接异常
 */
public final class ServerException extends NetHttpException {

    public ServerException(String message) {
        super(message);
    }

    public ServerException(String message, Throwable cause) {
        super(message, cause);
    }
}
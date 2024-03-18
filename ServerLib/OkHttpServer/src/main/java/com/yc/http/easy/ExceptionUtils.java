package com.yc.http.easy;

import android.net.ParseException;

import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

public class ExceptionUtils {

    /**
     * 对应HTTP的状态码
     */
    private static final int BAD_REQUEST = 400;
    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int METHOD_NOT_ALLOWED = 405;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int CONFLICT = 409;
    private static final int PRECONDITION_FAILED = 412;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int BAD_GATEWAY = 502;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int GATEWAY_TIMEOUT = 504;


    /**
     * 服务端自定义异常
     *
     * @param code    code
     * @param content 信息
     */
    public static void serviceException(int code, String content) {
        ServerException serverException = new ServerException();
        serverException.setCode(code);
        serverException.setMessage(content);
        handleException(serverException);
    }

    /**
     * 这个是处理网络异常，也可以处理业务中的异常
     *
     * @param e e异常
     */
    public static void handleException(Throwable e) {
        HttpException ex;
        if (e instanceof ServerException) {
            //服务器返回的错误
            ServerException resultException = (ServerException) e;
            String message = resultException.getMessage();
            ex = new HttpException(resultException);
            ex.setDisplayMessage(message);
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            ex = new HttpException(e);
            //均视为解析错误
            ex.setDisplayMessage("解析错误");
        } else if (e instanceof ConnectException) {
            ex = new HttpException(e);
            //均视为网络错误
            ex.setDisplayMessage("连接失败");
        } else if (e instanceof java.net.UnknownHostException) {
            ex = new HttpException(e);
            //网络未连接
            ex.setDisplayMessage("未知网络未连接");
        } else if (e instanceof SocketTimeoutException) {
            ex = new HttpException(e);
            //网络未连接
            ex.setDisplayMessage("服务器响应超时");
        } else {
            ex = new HttpException(e);
            //未知错误
            ex.setDisplayMessage("未知错误");
        }
        String displayMessage = ex.getDisplayMessage();
        //这里直接吐司日志异常内容，注意正式项目中一定要注意吐司合适的内容
    }


}

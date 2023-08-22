package com.yc.grpcserver;


import com.yc.dataclonelib.BaseParentBean;

/**
 * 响应体的基类
 */
public class BaseResponse extends BaseParentBean {

    protected int code;
    protected String data;
    protected String message;
    protected String methodName;

    public BaseResponse() {

    }

    public BaseResponse(int code, String data) {
        this.code = code;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

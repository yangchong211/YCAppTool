package com.yc.logging.upload;

class RequestResult<T> {
    private boolean success;
    private int code;
    private String msg;
    private T result;

    RequestResult() {

    }

    RequestResult(boolean success, int code, String msg, T result) {
        this.success = success;
        this.code = code;
        this.msg = msg;
        this.result = result;
    }

    public boolean isSuccess() {
        return success;
    }

    public RequestResult<T> setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public int getCode() {
        return code;
    }

    public RequestResult<T> setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public RequestResult<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public T getResult() {
        return result;
    }

    public RequestResult<T> setResult(T result) {
        this.result = result;
        return this;
    }
}

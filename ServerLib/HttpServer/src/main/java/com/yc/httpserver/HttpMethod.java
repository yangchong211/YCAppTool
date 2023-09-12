package com.yc.httpserver;


public enum HttpMethod {

    /**
     * get请求
     */
    GET(1),

    /**
     * post请求参数为json
     */
    POST_JSON(2),

    /**
     * post请求参数为表单
     */
    POST_FORM(3);

    /**
     * 请求方式
     */
    private final int mMethod;

    HttpMethod(int method) {
        this.mMethod = method;
    }

    public int getMethod() {
        return mMethod;
    }
}
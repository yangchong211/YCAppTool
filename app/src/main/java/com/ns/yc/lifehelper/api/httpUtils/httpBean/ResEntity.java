package com.ns.yc.lifehelper.api.httpUtils.httpBean;


public class ResEntity<T> {

    public static final int CODE_SUCCESS = 0;    //完全成功
    public static final int CODE_TOKEN_INVALID = 401;//Token 失效
    public static final int CODE_NO_MISSING_PARAMETER = 400;//缺少参数
    public static final int CODE_NO_OTHER= 403; //其他情况
    public static final int CODE_SHOW_TOAST=400000;//统一提示

    public int code;

    public String message;

    public T data;

}

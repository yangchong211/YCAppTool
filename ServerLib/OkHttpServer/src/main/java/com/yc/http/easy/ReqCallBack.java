package com.yc.http.easy;

public interface ReqCallBack<T> {

    /**
     * 响应成功
     *
     * @param result 返回数据
     */
    void onReqSuccess(T result);

    /**
     * 响应失败
     *
     * @param code     异常code
     * @param errorMsg 异常消息
     */
    void onReqFailed(int code, String errorMsg);

}

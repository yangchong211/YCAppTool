package com.yc.grpcserver;

/**
 * 统一请求回调
 */
public interface GrpcCallback<T>{

    /**
     * 成功的回调
     * @param result 成功字段
     */
    void onSuccess(T result);

    /**
     * 失败的回调
     *
     * @param code 状态码
     * @param msg  信息
     * @param ex   异常信息
     */
    void onFailed(int code, String msg, Throwable ex);

    /**
     * 完成的回调
     */
    void onFinish();

}

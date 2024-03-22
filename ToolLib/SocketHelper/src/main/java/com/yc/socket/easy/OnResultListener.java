package com.yc.socket.easy;

/**
 * 结果回调
 */
public interface OnResultListener {

    /**
     * 请求结果
     * @param success 是否成功
     * @param msgId 消息ID
     */
    void onResult(boolean success, int msgId);
}

package com.yc.http.synthesis.listener;

/**
 *    @author yangchong
 *    GitHub : https://github.com/yangchong211/YCAppTool
 *    time   : 2020/08/15
 *    desc   : 带上传进度回调的监听器
 */
public interface OnUpdateListener<T> extends OnHttpListener<T> {

    /**
     * 上传字节改变
     *
     * @param totalByte             总字节数
     * @param updateByte            已上传字节数
     */
    default void onByte(long totalByte, long updateByte) {}

    /**
     * 上传进度改变
     *
     * @param progress          上传进度值（0-100）
     */
    void onProgress(int progress);
}
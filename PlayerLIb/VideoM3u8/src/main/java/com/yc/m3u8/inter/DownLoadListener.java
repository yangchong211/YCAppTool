package com.yc.m3u8.inter;



/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/11/9
 *     desc  : 加载监听器
 *     revise:
 * </pre>
 */
public interface DownLoadListener {
    /**
     * 开始的时候回调
     */
    void onStart();

    /**
     * 错误的时候回调
     *
     * @param errorMsg
     */
    void onError(Throwable errorMsg);

    /**
     * 下载完成的时候回调
     */
    void onCompleted();
}

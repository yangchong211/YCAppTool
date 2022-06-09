package com.yc.audioplayer.inter;


public interface InterPlayListener {

    /**
     * 播放完成
     */
    void onCompleted();

    /**
     * 播放失败
     * @param error                         失败信息
     */
    void onError(String error);

}

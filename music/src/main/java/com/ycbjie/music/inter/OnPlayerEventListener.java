package com.ycbjie.music.inter;


import com.ycbjie.music.model.bean.AudioBean;

/**
 * 播放进度监听器
 */
public interface OnPlayerEventListener {

    /**
     * 切换歌曲
     * 主要是切换歌曲的时候需要及时刷新界面信息
     */
    void onChange(AudioBean music);


    /**
     * 继续播放
     * 主要是切换歌曲的时候需要及时刷新界面信息，比如播放暂停按钮
     */
    void onPlayerStart();

    /**
     * 暂停播放
     * 主要是切换歌曲的时候需要及时刷新界面信息，比如播放暂停按钮
     */
    void onPlayerPause();

    /**
     * 更新进度
     * 主要是播放音乐或者拖动进度条时，需要更新进度
     */
    void onUpdateProgress(int progress);

    /**
     * 缓冲百分比
     */
    void onBufferingUpdate(int percent);

    /**
     * 更新定时停止播放时间
     */
    void onTimer(long remain);

}

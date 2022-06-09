package com.yc.music.inter;


import com.yc.music.model.AudioBean;
import com.yc.music.service.PlayAudioService;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time  : 2017/5/6
 *     desc  : 音频播放的接口
 *     revise:
 * </pre>
 */
public interface InterPlayAudio {

    void init(PlayAudioService service);

    /**
     * 播放数据
     */
    void play(int position);

    /**
     * 播放数据
     */
    void play();

    void play(AudioBean music);

    /**
     * 停止播放
     */
    void stop();

    /**
     * 播放或者暂停
     */
    void playPause();

    /**
     * 释放音频内容
     */
    void release();

    /**
     * 暂停播放
     */
    void pause();

    void seekTo(int progress);

    void next();

    void prev();

    void updatePlayProgress();

    boolean isPlaying();

    boolean isPreparing();

    long getCurrentPosition();

    boolean isDefault();

    boolean isPausing();

    AudioBean getPlayingMusic();

    int getPlayingPosition();
}

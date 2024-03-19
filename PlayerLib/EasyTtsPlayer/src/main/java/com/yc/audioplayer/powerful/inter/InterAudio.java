package com.yc.audioplayer.powerful.inter;

import android.content.Context;

import com.yc.audioplayer.powerful.bean.AudioPlayData;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     GitHub : <a href="https://github.com/yangchong211/YCVideoPlayer">...</a>
 *     time  : 2018/8/6
 *     desc  : 音频播放接口定义
 *     revise:
 * </pre>
 */
public interface InterAudio {

    void init(InterPlayListener next, Context context);

    /**
     * 播放数据
     *
     * @param data {@link AudioPlayData}
     */
    void play(AudioPlayData data);

    /**
     * 停止播放
     */
    void stop();

    /**
     * 释放音频内容
     */
    void release();

    /**
     * 暂停播放
     */
    void pause();

    /**
     * 回复播放
     */
    void resumeSpeaking();

    /**
     * 是否正在播放
     *
     * @return true 是 false 否
     */
    boolean isPlaying();

}

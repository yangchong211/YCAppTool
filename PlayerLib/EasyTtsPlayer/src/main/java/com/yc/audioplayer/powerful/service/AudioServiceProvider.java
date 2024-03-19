package com.yc.audioplayer.powerful.service;

import android.content.Context;

import com.yc.audioplayer.powerful.bean.AudioPlayData;
import com.yc.audioplayer.powerful.bean.TtsPlayerConfig;
import com.yc.audioplayer.powerful.inter.PlayStateListener;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     GitHub : https://github.com/yangchong211/YCVideoPlayer
 *     time  : 2018/8/6
 *     desc  : provider接口
 *     revise:
 * </pre>
 */
public interface AudioServiceProvider {

    /**
     * 初始化语音服务
     *
     * @param context {@link Context}
     * @param config {@link TtsPlayerConfig}
     */
    void init(Context context , TtsPlayerConfig config);

    /**
     * 是否已经初始化
     *
     * @return true 是
     */
    boolean isInit();

    /**
     * 获取配置config
     * @return      config
     */
    TtsPlayerConfig getConfig();

    /**
     * 停止播放
     */
    void stop();

    /**
     * 暂停播放
     */
    void pause();

    /**
     * 恢复播放
     */
    void resume();

    /**
     * 销毁播放
     */
    void release();

    /**
     * 是否正在播放
     *
     * @return true 正在播放
     */
    boolean isPlaying();

    /**
     * 播放数据
     *
     * @param data {@link AudioPlayData}
     */
    void play(AudioPlayData data);

    /**
     * 播放tts
     *
     * @param tts tts文本
     */
    void playTts(String tts);


    /**
     * 播放音频资源
     *
     * @param url 网络资源
     */
    void playUrl(String url);

    /**
     * 播放音频资源
     *
     * @param rawId 资源文件
     */
    void playAudioResource(int rawId);

    /**
     * 监听单条PlayData播放状态
     *
     * @param playStateListener 监听器
     */
    void setPlayStateListener(PlayStateListener playStateListener);

}

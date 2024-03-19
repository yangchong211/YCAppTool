package com.yc.audioplayer.powerful.bean;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     GitHub : <a href="https://github.com/yangchong211/YCVideoPlayer">...</a>
 *     time  : 2018/8/6
 *     desc  : tts资源类型
 *     revise:
 * </pre>
 */
public enum AudioTtsMode {
    /**
     * tts文本内容，使用TtsPlayer播放
     */
    TTS,
    /**
     * 资源id，使用MediaPlayer播放
     */
    RAW_ID,
    /**
     * 网络资源，使用ExoAudioPlayer播放
     */
    URL,
}

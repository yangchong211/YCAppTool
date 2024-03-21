package com.yc.audiofocus.focus;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/11/9
 *     desc  : 音视频专门处理焦点抢占工具接口
 *     revise:
 * </pre>
 */
public interface IAudioFocus {

    /**
     * 请求获取音频焦点
     */
    void requestAudioFocus();

    /**
     * 放弃音频焦点
     */
    void abandonAudioFocus();

}

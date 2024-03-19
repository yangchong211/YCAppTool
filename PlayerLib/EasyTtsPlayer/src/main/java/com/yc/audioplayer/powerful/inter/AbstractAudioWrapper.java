package com.yc.audioplayer.powerful.inter;


/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     GitHub : <a href="https://github.com/yangchong211/YCVideoPlayer">...</a>
 *     time  : 2018/8/6
 *     desc  : tts抽象类，实现播放监听接口，和音频播放接口
 *     revise:
 * </pre>
 */
public abstract class AbstractAudioWrapper implements InterAudio, InterPlayListener {

    public final Object mMutex = new Object();


}

package com.yc.audioplayer.wrapper;


import com.yc.audioplayer.inter.InterAudio;
import com.yc.audioplayer.inter.InterPlayListener;


/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     GitHub : https://github.com/yangchong211/YCVideoPlayer
 *     time  : 2018/8/6
 *     desc  : tts抽象类，实现播放监听接口，和音频播放接口
 *     revise:
 * </pre>
 */
public abstract class AbstractAudioWrapper implements InterAudio, InterPlayListener {

    public final Object mMutex = new Object();


}

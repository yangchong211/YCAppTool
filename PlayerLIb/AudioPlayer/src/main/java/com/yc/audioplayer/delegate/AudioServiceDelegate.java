
package com.yc.audioplayer.delegate;

import android.content.Context;

import com.yc.audioplayer.bean.AudioPlayData;
import com.yc.audioplayer.inter.InterAudio;
import com.yc.audioplayer.inter.InterPlayListener;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     GitHub : https://github.com/yangchong211/YCVideoPlayer
 *     time  : 2018/8/6
 *     desc  : 代理类
 *     revise:
 * </pre>
 */
public final class AudioServiceDelegate implements InterAudio {

    private final InterAudio mDelegate ;

    public AudioServiceDelegate(InterAudio iAudio) {
        this.mDelegate = iAudio;
    }

    @Override
    public final void init(final InterPlayListener arg0, final Context arg1) {
        if (null != this.mDelegate) {
            this.mDelegate.init(arg0, arg1);
        }
    }

    @Override
    public final void play(final AudioPlayData arg0) {
        if (null != this.mDelegate) {
            this.mDelegate.play(arg0);
        }
    }

    @Override
    public final void stop() {
        if (null != this.mDelegate) {
            this.mDelegate.stop();
        }
    }

    @Override
    public final void release() {
        if (null != this.mDelegate) {
            this.mDelegate.release();
        }
    }

    @Override
    public final void pause() {
        if (null != this.mDelegate) {
            this.mDelegate.pause();
        }
    }

    @Override
    public final void resumeSpeaking() {
        if (null != this.mDelegate) {
            this.mDelegate.resumeSpeaking();
        }
    }

    @Override
    public final boolean isPlaying() {
        return null != this.mDelegate && this.mDelegate.isPlaying();
    }

}


package com.yc.audioplayer.delegate;

import android.content.Context;

import com.yc.audioplayer.bean.AudioPlayData;
import com.yc.audioplayer.inter.InterAudio;
import com.yc.audioplayer.inter.InterPlayListener;
import com.yc.easyexecutor.DelegateTaskExecutor;

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
    public final void init(final InterPlayListener listener, final Context context) {
        if (null != this.mDelegate) {
            this.mDelegate.init(listener, context);
        }
    }

    @Override
    public final void play(final AudioPlayData data) {
        if (null != this.mDelegate) {
            if (DelegateTaskExecutor.getInstance().isMainThread()){
                this.mDelegate.play(data);
            } else {
                DelegateTaskExecutor.getInstance().postToMainThread(new Runnable() {
                    @Override
                    public void run() {
                        mDelegate.play(data);
                    }
                });
            }
        }
    }

    @Override
    public final void stop() {
        if (null != this.mDelegate) {
            if (DelegateTaskExecutor.getInstance().isMainThread()){
                mDelegate.stop();
            } else {
                DelegateTaskExecutor.getInstance().postToMainThread(new Runnable() {
                    @Override
                    public void run() {
                        mDelegate.stop();
                    }
                });
            }
        }
    }

    @Override
    public final void release() {
        if (null != this.mDelegate) {
            if (DelegateTaskExecutor.getInstance().isMainThread()){
                this.mDelegate.release();
            } else {
                DelegateTaskExecutor.getInstance().postToMainThread(new Runnable() {
                    @Override
                    public void run() {
                        mDelegate.release();
                    }
                });
            }
        }
    }

    @Override
    public final void pause() {
        if (null != this.mDelegate) {
            if (DelegateTaskExecutor.getInstance().isMainThread()){
                this.mDelegate.pause();
            } else {
                DelegateTaskExecutor.getInstance().postToMainThread(new Runnable() {
                    @Override
                    public void run() {
                        mDelegate.pause();
                    }
                });
            }
        }
    }

    @Override
    public final void resumeSpeaking() {
        if (null != this.mDelegate) {
            if (DelegateTaskExecutor.getInstance().isMainThread()){
                this.mDelegate.resumeSpeaking();
            } else {
                DelegateTaskExecutor.getInstance().postToMainThread(new Runnable() {
                    @Override
                    public void run() {
                        mDelegate.resumeSpeaking();
                    }
                });
            }
        }
    }

    @Override
    public final boolean isPlaying() {
        return null != this.mDelegate && this.mDelegate.isPlaying();
    }

}

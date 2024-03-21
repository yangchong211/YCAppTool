package com.yc.audioplayer.powerful.service;

import android.content.Context;
import android.net.Uri;

import com.yc.audioplayer.powerful.bean.AudioPlayData;
import com.yc.audioplayer.powerful.bean.TtsPlayerConfig;
import com.yc.audioplayer.powerful.inter.PlayStateListener;
import com.yc.easyexecutor.DelegateTaskExecutor;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     GitHub : <a href="https://github.com/yangchong211/YCVideoPlayer">...</a>
 *     time  : 2018/8/6
 *     desc  : provider接口具体代理类
 *     revise:
 * </pre>
 */
public final class AudioService implements AudioServiceProvider {

    private AudioServiceProvider mDelegate;
    private TtsPlayerConfig mConfig;
    public static final String TAG = "AudioService: ";

    private AudioService() {
    }

    public static AudioService getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public void init(final Context context, TtsPlayerConfig config) {
        mConfig = config;
        if (mConfig.getIsTtsDeque()) {
            mDelegate = new AudioServiceImpl1();
        } else {
            mDelegate = new AudioServiceImpl2();
        }
        this.mDelegate.init(context, config);
    }

    @Override
    public boolean isInit() {
        return null != this.mDelegate && this.mDelegate.isInit();
    }

    @Override
    public TtsPlayerConfig getConfig() {
        if (mConfig == null) {
            mConfig = new TtsPlayerConfig.Builder().build();
        }
        return mConfig;
    }

    @Override
    public void stop() {
        if (null != this.mDelegate) {
            if (DelegateTaskExecutor.getInstance().isMainThread()) {
                mDelegate.stop();
            } else {
                DelegateTaskExecutor.getInstance().postToMainThread(() -> mDelegate.stop());
            }
        }
    }

    @Override
    public void pause() {
        if (null != this.mDelegate) {
            if (DelegateTaskExecutor.getInstance().isMainThread()) {
                mDelegate.pause();
            } else {
                DelegateTaskExecutor.getInstance().postToMainThread(() -> mDelegate.pause());
            }
        }
    }

    @Override
    public void resume() {
        if (null != this.mDelegate) {
            if (DelegateTaskExecutor.getInstance().isMainThread()) {
                mDelegate.resume();
            } else {
                DelegateTaskExecutor.getInstance().postToMainThread(() -> mDelegate.resume());
            }
        }
    }

    @Override
    public void release() {
        if (null != this.mDelegate) {
            if (DelegateTaskExecutor.getInstance().isMainThread()) {
                mDelegate.release();
            } else {
                DelegateTaskExecutor.getInstance().postToMainThread(() -> mDelegate.release());
            }
        }
    }

    @Override
    public boolean isPlaying() {
        return null != this.mDelegate && this.mDelegate.isPlaying();
    }

    @Override
    public void play(final AudioPlayData audioPlayData) {
        if (null != this.mDelegate) {
            if (DelegateTaskExecutor.getInstance().isMainThread()) {
                this.mDelegate.play(audioPlayData);
            } else {
                DelegateTaskExecutor.getInstance().postToMainThread(() -> mDelegate.play(audioPlayData));
            }
        }
    }

    @Override
    public void playTts(final String tts) {
        if (null != this.mDelegate) {
            if (DelegateTaskExecutor.getInstance().isMainThread()) {
                this.mDelegate.playTts(tts);
            } else {
                DelegateTaskExecutor.getInstance().postToMainThread(() -> mDelegate.playTts(tts));
            }
        }
    }

    @Override
    public void playUrl(String url) {
        //判断是否是合法的url资源
        if (isUrlValid(url)) {
            if (null != this.mDelegate) {
                if (DelegateTaskExecutor.getInstance().isMainThread()) {
                    this.mDelegate.playUrl(url);
                } else {
                    DelegateTaskExecutor.getInstance().postToMainThread(() -> mDelegate.playUrl(url));
                }
            }
        }
    }

    @Override
    public void playAudioResource(final int rawId) {
        if (null != this.mDelegate) {
            if (DelegateTaskExecutor.getInstance().isMainThread()) {
                this.mDelegate.playAudioResource(rawId);
            } else {
                DelegateTaskExecutor.getInstance().postToMainThread(() -> mDelegate.playAudioResource(rawId));
            }
        }
    }

    @Override
    public void setPlayStateListener(final PlayStateListener arg0) {
        if (null != this.mDelegate) {
            this.mDelegate.setPlayStateListener(arg0);
        }
    }

    private static final class Singleton {
        static final AudioService INSTANCE = new AudioService();
    }

    public boolean isUrlValid(String url) {
        try {
            Uri uri = Uri.parse(url);
            return uri.getScheme() != null && uri.getHost() != null;
        } catch (Exception e) {
            return false;
        }
    }
}

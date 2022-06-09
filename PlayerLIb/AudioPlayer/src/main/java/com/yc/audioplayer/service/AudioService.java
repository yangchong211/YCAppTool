package com.yc.audioplayer.service;

import android.content.Context;

import com.yc.audioplayer.bean.AudioPlayData;
import com.yc.audioplayer.manager.AudioManager;

public final class AudioService implements AudioServiceProvider {

    private AudioServiceProvider mDelegate;

    private AudioService() {
        if (mDelegate == null){
            mDelegate = new AudioServiceImpl();
        }
    }

    public static AudioService getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public final void init(final Context arg0) {
        if (null != this.mDelegate) {
            this.mDelegate.init(arg0);
        }
    }

    @Override
    public final boolean isInit() {
        return null != this.mDelegate && this.mDelegate.isInit();
    }

    @Override
    public final void stop() {
        if (null != this.mDelegate) {
            this.mDelegate.stop();
        }
    }

    @Override
    public final void pause() {
        if (null != this.mDelegate) {
            this.mDelegate.pause();
        }
    }

    @Override
    public final void resume() {
        if (null != this.mDelegate) {
            this.mDelegate.resume();
        }
    }

    @Override
    public final void release() {
        if (null != this.mDelegate) {
            this.mDelegate.release();
        }
    }

    @Override
    public final boolean isPlaying() {
        return null != this.mDelegate && this.mDelegate.isPlaying();
    }

    @Override
    public final void play(final AudioPlayData arg0) {
        if (null != this.mDelegate) {
            this.mDelegate.play(arg0);
        }
    }

    @Override
    public final void playTts(final String arg0) {
        if (null != this.mDelegate) {
            this.mDelegate.playTts(arg0);
        }
    }

    @Override
    public final void playAudioResource(final int arg0) {
        if (null != this.mDelegate) {
            this.mDelegate.playAudioResource(arg0);
        }
    }

    @Override
    public final void setPlayStateListener(final AudioManager.PlayStateListener arg0) {
        if (null != this.mDelegate) {
            this.mDelegate.setPlayStateListener(arg0);
        }
    }

    private static final class Singleton {
        static final AudioService INSTANCE = new AudioService();
    }
}

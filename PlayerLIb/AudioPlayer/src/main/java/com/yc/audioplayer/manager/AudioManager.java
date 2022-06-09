package com.yc.audioplayer.manager;

import android.content.Context;

import com.yc.audioplayer.delegate.AudioServiceDelegate;
import com.yc.audioplayer.player.DefaultTtsPlayer;
import com.yc.audioplayer.player.MediaAudioPlayer;
import com.yc.audioplayer.wrapper.AbstractAudioWrapper;
import com.yc.audioplayer.bean.AudioPlayData;
import com.yc.audioplayer.inter.InterAudio;
import com.yc.audioplayer.inter.InterPlayListener;


public class AudioManager extends AbstractAudioWrapper {

    private final InterAudio mTtsEngine;
    private final InterAudio mMediaPlayer;
    private AudioPlayData mCurrentData;
    private InterAudio mCurrentAudio;
    private PlayStateListener mPlayStateListener;

    public AudioManager(Context context) {
        //创建tts播放器
        DefaultTtsPlayer defaultTtsPlayer = new DefaultTtsPlayer(context);
        //常见tts代理类对象
        mTtsEngine = new AudioServiceDelegate(defaultTtsPlayer);
        //创建音频播放器
        mMediaPlayer = new MediaAudioPlayer();
    }

    @Override
    public void init(InterPlayListener next, Context context) {
        mTtsEngine.init(next, context);
        mMediaPlayer.init(next, context);
    }

    @Override
    public void play(AudioPlayData data) {
        if (null != mPlayStateListener) {
            mPlayStateListener.onStartPlay();
        }
        this.mCurrentData = data;
        //判断是否播放tts
        if (data.mPlayTts){
            this.mCurrentAudio = mTtsEngine;
        } else {
            this.mCurrentAudio = mMediaPlayer;
        }
        this.mCurrentAudio.play(data);
    }

    /**
     * 暂停播放内容
     */
    @Override
    public void stop() {
        if (mCurrentAudio != null) {
            mCurrentAudio.stop();
            mCurrentData = null;
            synchronized (mMutex) {
                mMutex.notifyAll();
            }
        }
    }

    @Override
    public void release() {
        mTtsEngine.release();
        mMediaPlayer.release();
    }

    @Override
    public void pause() {
        mCurrentAudio.pause();
    }

    @Override
    public void resumeSpeaking() {
        mCurrentAudio.resumeSpeaking();
    }

    @Override
    public boolean isPlaying() {
        return mCurrentAudio != null && mCurrentAudio.isPlaying();
    }

    @Override
    public void onCompleted() {
        if (mCurrentData != null && mCurrentData.getNext() != null) {
            mCurrentData = mCurrentData.getNext();
            play(mCurrentData);
        } else {
            synchronized (mMutex) {
                mMutex.notifyAll();
            }
            if (null != mPlayStateListener) {
                mPlayStateListener.onCompletePlay();
            }
        }
    }

    @Override
    public void onError(String error) {

    }

    public void setPlayStateListener(PlayStateListener playStateListener) {
        this.mPlayStateListener = playStateListener;
    }

    public interface PlayStateListener {

        void onStartPlay();

        void onCompletePlay();
    }
}

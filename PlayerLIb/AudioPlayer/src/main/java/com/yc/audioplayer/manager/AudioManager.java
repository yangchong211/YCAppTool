package com.yc.audioplayer.manager;

import android.content.Context;

import com.yc.audioplayer.delegate.AudioServiceDelegate;
import com.yc.audioplayer.inter.PlayStateListener;
import com.yc.audioplayer.player.DefaultTtsPlayer;
import com.yc.audioplayer.player.ExoAudioPlayer;
import com.yc.audioplayer.player.MediaAudioPlayer;
import com.yc.audioplayer.wrapper.AbstractAudioWrapper;
import com.yc.audioplayer.bean.AudioPlayData;
import com.yc.audioplayer.inter.InterAudio;
import com.yc.audioplayer.inter.InterPlayListener;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     GitHub : https://github.com/yangchong211/YCVideoPlayer
 *     time  : 2018/8/6
 *     desc  : 音频播放器
 *     revise:
 * </pre>
 */
public class AudioManager extends AbstractAudioWrapper {

    private final InterAudio mTtsEngine;
    private final InterAudio mMediaPlayer;
    private final InterAudio mExoPlayer;
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
        //创建谷歌音频播放器
        mExoPlayer = new ExoAudioPlayer();
    }

    @Override
    public void init(InterPlayListener next, Context context) {
        mTtsEngine.init(next, context);
        mMediaPlayer.init(next, context);
        mExoPlayer.init(next, context);
    }

    @Override
    public void play(AudioPlayData data) {
        if (null != mPlayStateListener) {
            mPlayStateListener.onStartPlay();
        }
        this.mCurrentData = data;
        switch (data.audioTtsMode){
            case TTS:
                this.mCurrentAudio = mTtsEngine;
                break;
            case URL:
                this.mCurrentAudio = mExoPlayer;
                break;
            case RAW_ID:
                this.mCurrentAudio = mMediaPlayer;
                break;
            default:
                break;
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
        if (null != mPlayStateListener) {
            mPlayStateListener.onStopPlay();
        }
    }

    @Override
    public void release() {
        mTtsEngine.release();
        mMediaPlayer.release();
        mExoPlayer.release();
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
            //播放完成，开始播放下一个
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

}

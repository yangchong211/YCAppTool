package com.yc.audioplayer.powerful.service;

import android.content.Context;

import com.yc.audioplayer.powerful.inter.PlayStateListener;
import com.yc.audioplayer.powerful.player.DefaultTtsPlayer;
import com.yc.audioplayer.powerful.player.ExoAudioPlayer;
import com.yc.audioplayer.powerful.player.MediaAudioPlayer;
import com.yc.audioplayer.powerful.inter.AbstractAudioWrapper;
import com.yc.audioplayer.powerful.bean.AudioPlayData;
import com.yc.audioplayer.powerful.inter.InterAudio;
import com.yc.audioplayer.powerful.inter.InterPlayListener;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     GitHub : <a href="https://github.com/yangchong211/YCVideoPlayer">...</a>
 *     time  : 2018/8/6
 *     desc  : 音频播放器
 *     revise:
 * </pre>
 */
public class AudioManager extends AbstractAudioWrapper {

    private final InterAudio mTtsPlayer;
    private final InterAudio mMediaPlayer;
    private final InterAudio mExoPlayer;
    private AudioPlayData mCurrentData;
    private InterAudio mCurrentAudio;
    private PlayStateListener mPlayStateListener;

    public AudioManager(Context context) {
        //创建tts播放器
        mTtsPlayer = new DefaultTtsPlayer(context);
        //创建音频播放器
        mMediaPlayer = new MediaAudioPlayer();
        //创建谷歌音频播放器
        mExoPlayer = new ExoAudioPlayer();
    }

    @Override
    public void init(InterPlayListener next, Context context) {
        mTtsPlayer.init(next, context);
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
                this.mCurrentAudio = mTtsPlayer;
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
        mTtsPlayer.release();
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

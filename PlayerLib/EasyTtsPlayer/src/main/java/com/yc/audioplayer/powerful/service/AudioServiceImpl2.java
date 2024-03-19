package com.yc.audioplayer.powerful.service;

import android.content.Context;

import com.yc.audioplayer.powerful.bean.AudioPlayData;
import com.yc.audioplayer.powerful.bean.TtsPlayerConfig;
import com.yc.audioplayer.powerful.inter.InterPlayListener;
import com.yc.audioplayer.powerful.inter.PlayStateListener;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     GitHub : https://github.com/yangchong211/YCVideoPlayer
 *     time  : 2018/8/6
 *     desc  : provider接口具体实现类2
 *             支持每次播放最新的tts(会覆盖之前的)
 *     revise:
 * </pre>
 */
public class AudioServiceImpl2 implements AudioServiceProvider , InterPlayListener {

    private AudioManager mAudioManager;
    private boolean mReady = false;
    private static TtsPlayerConfig mConfig;
    private PlayStateListener mPlayStateListener;

    @Override
    public void init(Context context, TtsPlayerConfig config) {
        mConfig = config;
        mAudioManager = new AudioManager(context);
        mAudioManager.init(this, context);
        mReady = true;
    }

    @Override
    public boolean isInit() {
        return mReady;
    }

    @Override
    public TtsPlayerConfig getConfig() {
        return mConfig;
    }

    @Override
    public void stop() {
        if (mReady) {
            mAudioManager.stop();
        }
    }

    @Override
    public void pause() {
        if (mReady) {
            mAudioManager.pause();
        }
    }

    @Override
    public void resume() {
        if (mReady) {
            mAudioManager.resumeSpeaking();
        }
    }

    @Override
    public void release() {
        mAudioManager.release();
        mReady = false;
    }

    @Override
    public boolean isPlaying() {
        return mReady && mAudioManager.isPlaying();
    }

    @Override
    public void play(AudioPlayData data) {
        if (data == null) {
            return;
        }
        if (!mReady) {
            TtsPlayerConfig config = AudioService.getInstance().getConfig();
            config.getLogger().log("audio not init!");
            return;
        }
        mAudioManager.stop();
        mAudioManager.play(data);
    }

    @Override
    public void playTts(String tts) {
        AudioPlayData data = new AudioPlayData
                .Builder()
                .tts(tts)
                .build();
        play(data);
    }

    @Override
    public void playUrl(String url) {
        AudioPlayData data = new AudioPlayData
                .Builder()
                .url(url)
                .build();
        play(data);
    }

    @Override
    public void playAudioResource(int rawId) {
        AudioPlayData data = new AudioPlayData
                .Builder()
                .rawId(rawId)
                .build();
        play(data);
    }

    @Override
    public void setPlayStateListener(PlayStateListener playStateListener) {
        this.mPlayStateListener = playStateListener;
        if (null != mAudioManager) {
            mAudioManager.setPlayStateListener(playStateListener);
        }
    }

    @Override
    public void onCompleted() {
        if (mPlayStateListener != null){
            mPlayStateListener.onCompletePlay();
        }
    }

    @Override
    public void onError(String error) {

    }
}

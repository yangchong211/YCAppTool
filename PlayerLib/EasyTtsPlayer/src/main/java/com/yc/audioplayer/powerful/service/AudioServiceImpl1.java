package com.yc.audioplayer.powerful.service;

import android.content.Context;

import com.yc.audioplayer.powerful.bean.TtsPlayerConfig;
import com.yc.audioplayer.powerful.dispatch.AudioTaskDispatcher;
import com.yc.audioplayer.powerful.bean.AudioPlayData;
import com.yc.audioplayer.powerful.inter.PlayStateListener;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     GitHub : https://github.com/yangchong211/YCVideoPlayer
 *     time  : 2018/8/6
 *     desc  : provider接口具体实现类1
 *             支持tts按照顺序和优先级播放
 *     revise:
 * </pre>
 */
public class AudioServiceImpl1 implements AudioServiceProvider {

    private AudioManager mAudioManager;
    private final AudioTaskDispatcher mAudioTaskDispatcher = AudioTaskDispatcher.getInstance();
    private boolean mReady = false;
    private static TtsPlayerConfig mConfig;

    @Override
    public void init(Context context, TtsPlayerConfig config) {
        mConfig = config;
        mAudioManager = new AudioManager(context);
        mAudioManager.init(mAudioTaskDispatcher, context);
        mAudioTaskDispatcher.initialize(mAudioManager);
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
        mAudioTaskDispatcher.release();
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
        //将播放内容任务添加到分发器中
        mAudioTaskDispatcher.addTask(data);
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
        if (null != mAudioManager) {
            mAudioManager.setPlayStateListener(playStateListener);
        }
    }
}

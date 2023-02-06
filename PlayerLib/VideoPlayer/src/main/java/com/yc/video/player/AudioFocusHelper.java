/*
Copyright 2017 yangchong211（github.com/yangchong211）

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package com.yc.video.player;

import android.content.Context;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import java.lang.ref.WeakReference;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/11/9
 *     desc  : 音频焦点改变监听
 *     revise:
 * </pre>
 */
public final class AudioFocusHelper implements AudioManager.OnAudioFocusChangeListener {

    private Handler mHandler = new Handler(Looper.getMainLooper());
    private final WeakReference<VideoPlayer> mWeakVideoView;
    private final AudioManager mAudioManager;
    private boolean mStartRequested = false;
    private boolean mPausedForLoss = false;
    private int mCurrentFocus = 0;

    public AudioFocusHelper(@NonNull VideoPlayer videoView) {
        mWeakVideoView = new WeakReference<>(videoView);
        mAudioManager = (AudioManager) videoView.getContext().getApplicationContext()
                .getSystemService(Context.AUDIO_SERVICE);
    }

    @Override
    public void onAudioFocusChange(final int focusChange) {
        if (mCurrentFocus == focusChange) {
            return;
        }

        //由于onAudioFocusChange有可能在子线程调用，
        //故通过此方式切换到主线程去执行
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                //处理音频焦点抢占
                handleAudioFocusChange(focusChange);
            }
        });
        mCurrentFocus = focusChange;
    }

    private void handleAudioFocusChange(int focusChange) {
        final VideoPlayer videoView = mWeakVideoView.get();
        if (videoView == null) {
            return;
        }
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_GAIN:
                //重新获得焦点
            case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT:
                //暂时获得焦点
                if (mStartRequested || mPausedForLoss) {
                    videoView.start();
                    mStartRequested = false;
                    mPausedForLoss = false;
                }
                if (!videoView.isMute()) {
                    //恢复音量
                    videoView.setVolume(1.0f, 1.0f);
                }
                break;
            case AudioManager.AUDIOFOCUS_LOSS:
                //焦点丢失，这个是永久丢失焦点，如被其他播放器抢占
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                //焦点暂时丢失，，如来电
                if (videoView.isPlaying()) {
                    mPausedForLoss = true;
                    videoView.pause();
                }
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                //此时需降低音量，瞬间丢失焦点，如通知
                if (videoView.isPlaying() && !videoView.isMute()) {
                    videoView.setVolume(0.1f, 0.1f);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 请求获取音频焦点
     */
    public void requestFocus() {
        if (mCurrentFocus == AudioManager.AUDIOFOCUS_GAIN) {
            //如果已经是获得焦点，则直接返回
            return;
        }
        if (mAudioManager == null) {
            return;
        }
        //请求重新获取焦点
        int status = mAudioManager.requestAudioFocus(this,
                AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        //焦点更改请求成功
        if (AudioManager.AUDIOFOCUS_REQUEST_GRANTED == status) {
            mCurrentFocus = AudioManager.AUDIOFOCUS_GAIN;
            return;
        }
        mStartRequested = true;
    }

    /**
     * 请求系统放下音频焦点
     */
    public void abandonFocus() {
        if (mAudioManager == null) {
            return;
        }
        mStartRequested = false;
        mAudioManager.abandonAudioFocus(this);
    }

    /**
     * 销毁资源
     */
    public void release(){
        abandonFocus();
        if (mHandler!=null){
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
    }

}
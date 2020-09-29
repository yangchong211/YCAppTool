package com.ycbjie.music.manager;

import android.annotation.SuppressLint;
import android.media.AudioManager;
import android.os.Build;
import android.support.annotation.NonNull;


import com.ycbjie.music.service.PlayService;

import static android.content.Context.AUDIO_SERVICE;

public class AudioSoundManager  {


    private AudioManager mAudioManager;


    /**
     * 初始化操作
     * @param content           playService对象
     */
    public AudioSoundManager(@NonNull PlayService content) {
        mAudioManager = (AudioManager) content.getSystemService(AUDIO_SERVICE);
    }


    /**
     * 切换到外放
     */
    public void changeToSpeaker(){
        mAudioManager.setMode(AudioManager.MODE_NORMAL);
        mAudioManager.setSpeakerphoneOn(true);
    }


    /**
     * 切换到耳机模式
     */
    public void changeToHeadset(){
        mAudioManager.setSpeakerphoneOn(false);
    }


    /**
     * 切换到听筒
     */
    @SuppressLint("ObsoleteSdkInt")
    public void changeToReceiver(){
        mAudioManager.setSpeakerphoneOn(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            mAudioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        } else {
            mAudioManager.setMode(AudioManager.MODE_IN_CALL);
        }
    }


}

package com.yc.audiofocus.audio;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.os.Build;
import androidx.annotation.NonNull;

import static android.content.Context.AUDIO_SERVICE;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/11/9
 *     desc  : audio操作类
 *     revise:
 * </pre>
 */
public class AudioSoundManager  {

    private final AudioManager mAudioManager;

    /**
     * 初始化操作
     * @param content           playService对象
     */
    public AudioSoundManager(@NonNull Context content) {
        mAudioManager = (AudioManager) content.getSystemService(AUDIO_SERVICE);
    }


    /**
     * 切换到外放
     */
    public void changeToSpeaker(){
        mAudioManager.setMode(AudioManager.MODE_NORMAL);
        // 为true打开喇叭扩音器；为false关闭喇叭扩音器.
        mAudioManager.setSpeakerphoneOn(true);
    }


    /**
     * 切换到耳机模式
     */
    public void changeToHeadset(){
        // 为true打开喇叭扩音器；为false关闭喇叭扩音器.
        mAudioManager.setSpeakerphoneOn(false);
    }


    /**
     * 切换到听筒
     */
    @SuppressLint("ObsoleteSdkInt")
    public void changeToReceiver(){
        // 为true打开喇叭扩音器；为false关闭喇叭扩音器.
        mAudioManager.setSpeakerphoneOn(false);
        // 在android5.0以上我们设置听筒模式audioManager.setMode(AudioManager.MODE_IN_CALL)没有效果;
        // 后来各种查阅资料,发现android5.0以上Google官方文档推荐使用
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            mAudioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        } else {
            mAudioManager.setMode(AudioManager.MODE_IN_CALL);
        }
    }

    /**
     * 检查A2DPAudio是否通过蓝牙耳机
     * @return
     */
    public boolean isBluetoothA2dpOn(){
        return mAudioManager.isBluetoothA2dpOn();
    }

    /**
     * 检查扬声器是否打开
     * @return
     */
    public boolean isSpeakerphoneOn(){
        return mAudioManager.isSpeakerphoneOn();
    }

    /**
     * 检查线控耳机是否连着；注意这个方法只是用来判断耳机是否是插入状态
     * 并不能用它的结果来判定当前的Audio是通过耳机输出的，这还依赖于其他条件。
     * @return
     */
    public boolean isWiredHeadsetOn(){
        return mAudioManager.isWiredHeadsetOn();
    }


    /**
     * 打开扬声器
     */
    public void openSpeaker() {
        //mAudioManager.setMode(AudioManager.ROUTE_SPEAKER);
        mAudioManager.setMode(AudioManager.MODE_IN_CALL);
        //currVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL);
        if(!mAudioManager.isSpeakerphoneOn()) {
            // 为true打开喇叭扩音器；为false关闭喇叭扩音器.
            mAudioManager.setSpeakerphoneOn(true);
            mAudioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL,
                    mAudioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL),
                    AudioManager.STREAM_VOICE_CALL);
        }
    }


    /**
     * 关闭扬声器
     */
    public void closeSpeaker() {
        if(mAudioManager != null) {
            int currVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL);
            if(mAudioManager.isSpeakerphoneOn()) {
                // 为true打开喇叭扩音器；为false关闭喇叭扩音器.
                mAudioManager.setSpeakerphoneOn(false);
                // 在android5.0以上我们设置听筒模式audioManager.setMode(AudioManager.MODE_IN_CALL)没有效果;
                // 后来各种查阅资料,发现android5.0以上Google官方文档推荐使用
                mAudioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
                mAudioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL,
                        currVolume, AudioManager.STREAM_VOICE_CALL);
            }
        }
    }


}

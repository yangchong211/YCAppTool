package com.yc.bellsvibrations

import android.content.Context
import android.media.AudioManager

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     GitHub : https://github.com/yangchong211/YCServerLib
 *     time  : 2018/11/9
 *     desc  : 手机声音设置帮助类
 *     doc  :
 * </pre>
 */
class AudioVolumeHelper(private val context: Context) : IAudioVolume{

    /**
     * 管理这些铃声音量的工具是AudioManager，对象从系统服务AUDIO_SERVICE中获取
     */
    private val audioManager: AudioManager? by lazy {
        context.applicationContext.getSystemService(Context.AUDIO_SERVICE) as? AudioManager
    }

    /**
     * 获取指定类型铃声的响铃模式。
     */
    override fun getRingerMode(): Int? {
        return audioManager?.ringerMode
    }

    /**
     * 获取最大多媒体音量
     */
    override fun getMediaMaxVolume(type: Int): Int {
        when (type) {
            //通话声音
            1 -> {
                return audioManager?.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL) ?: 0
            }
            //系统音
            2 -> {
                return audioManager?.getStreamMaxVolume(AudioManager.STREAM_SYSTEM) ?: 0
            }
            //铃音，来电与收短信的铃声
            3 -> {
                return audioManager?.getStreamMaxVolume(AudioManager.STREAM_RING) ?: 0
            }
            //媒体音	，音视频，游戏等
            4 -> {
                return audioManager?.getStreamMaxVolume(AudioManager.STREAM_MUSIC) ?: 0
            }
            //闹钟音
            5 -> {
                return audioManager?.getStreamMaxVolume(AudioManager.STREAM_ALARM) ?: 0
            }
            //通知声音
            6 -> {
                return audioManager?.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION) ?: 0
            }
        }
        return audioManager?.getStreamMaxVolume(AudioManager.STREAM_MUSIC) ?: 0
    }

    /**
     * 获取指定类型铃声的当前音量
     */
    override fun getMediaVolume(type: Int): Int {
        when (type) {
            //通话声音
            1 -> {
                return audioManager?.getStreamVolume(AudioManager.STREAM_VOICE_CALL) ?: 0
            }
            //系统音
            2 -> {
                return audioManager?.getStreamVolume(AudioManager.STREAM_SYSTEM) ?: 0
            }
            //铃音，来电与收短信的铃声
            3 -> {
                return audioManager?.getStreamVolume(AudioManager.STREAM_RING) ?: 0
            }
            //媒体音	，音视频，游戏等
            4 -> {
                return audioManager?.getStreamVolume(AudioManager.STREAM_MUSIC) ?: 0
            }
            //闹钟音
            5 -> {
                return audioManager?.getStreamVolume(AudioManager.STREAM_ALARM) ?: 0
            }
            //通知声音
            6 -> {
                return audioManager?.getStreamVolume(AudioManager.STREAM_NOTIFICATION) ?: 0
            }
        }
        return audioManager?.getStreamVolume(AudioManager.STREAM_MUSIC) ?: 0
    }

    /**
     * 设置指定类型铃声的当前音量
     */
    override fun setMediaVolume(type: Int, volume: Int){
        when (type) {
            //通话声音
            1 -> {
                audioManager?.setStreamVolume(AudioManager.STREAM_VOICE_CALL,
                    volume, AudioManager.FLAG_PLAY_SOUND
                            or AudioManager.FLAG_SHOW_UI)
            }
            //系统音
            2 -> {
                audioManager?.setStreamVolume(AudioManager.STREAM_SYSTEM,
                    volume, AudioManager.FLAG_PLAY_SOUND
                            or AudioManager.FLAG_SHOW_UI)
            }
            //铃音，来电与收短信的铃声
            3 -> {
                audioManager?.setStreamVolume(AudioManager.STREAM_RING,
                    volume, AudioManager.FLAG_PLAY_SOUND
                            or AudioManager.FLAG_SHOW_UI)
            }
            //媒体音	，音视频，游戏等
            4 -> {
                audioManager?.setStreamVolume(AudioManager.STREAM_MUSIC,
                    volume, AudioManager.FLAG_PLAY_SOUND
                            or AudioManager.FLAG_SHOW_UI)
            }
            //闹钟音
            5 -> {
                audioManager?.setStreamVolume(AudioManager.STREAM_ALARM,
                    volume, AudioManager.FLAG_PLAY_SOUND
                            or AudioManager.FLAG_SHOW_UI)
            }
            //通知声音
            6 -> {
                audioManager?.setStreamVolume(AudioManager.STREAM_NOTIFICATION,
                    volume, AudioManager.FLAG_PLAY_SOUND
                            or AudioManager.FLAG_SHOW_UI)
            }
        }
    }

    /**
     * 设置指定类型铃声的响铃模式
     */
    override fun setRingerMode(mode : Int) {
        audioManager?.ringerMode = mode
    }


    /**
     * 是否支持铃音
     */
    override fun enableRingtone(): Boolean {
        //AudioManager类的响铃模式
        return when (getRingerMode()) {
            //静音
            AudioManager.RINGER_MODE_SILENT -> false
            //震动
            AudioManager.RINGER_MODE_VIBRATE -> false
            //正常
            AudioManager.RINGER_MODE_NORMAL -> true
            else -> false
        }
    }

    /**
     * 是否支持震动
     */
    override fun enableVibrate(): Boolean {
        return when (getRingerMode()) {
            //静音
            AudioManager.RINGER_MODE_SILENT -> false
            //震动
            AudioManager.RINGER_MODE_VIBRATE -> true
            //正常
            AudioManager.RINGER_MODE_NORMAL -> true
            else -> false
        }
    }


}
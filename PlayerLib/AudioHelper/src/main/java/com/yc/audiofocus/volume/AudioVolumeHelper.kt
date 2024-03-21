package com.yc.audiofocus.volume

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
class AudioVolumeHelper(private val context: Context) : IAudioVolume {

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
    override fun getMediaVolume(@DataSource type: Int): Int {
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
     * 调用setStreamVolume方法，第三个参数是flag标签
     * AudioManager.FLAG_SHOW_UI 调整音量时显示系统音量进度条 , 0 则不显示
     * AudioManager.FLAG_ALLOW_RINGER_MODES 是否铃声模式
     * AudioManager.FLAG_VIBRATE 是否震动模式
     * AudioManager.FLAG_SHOW_VIBRATE_HINT 震动提示
     * AudioManager.FLAG_SHOW_SILENT_HINT 静音提示
     * AudioManager.FLAG_PLAY_SOUND 调整音量时播放声音
     */
    override fun setMediaVolume(type: Int, volume: Int) {
        when (type) {
            //通话声音
            DataSource.STREAM_VOICE_CALL -> {
                audioManager?.setStreamVolume(
                    AudioManager.STREAM_VOICE_CALL,
                    volume, AudioManager.FLAG_PLAY_SOUND
                            or AudioManager.FLAG_SHOW_UI
                )
            }
            //系统音
            DataSource.STREAM_SYSTEM -> {
                audioManager?.setStreamVolume(
                    AudioManager.STREAM_SYSTEM,
                    volume, AudioManager.FLAG_PLAY_SOUND
                            or AudioManager.FLAG_SHOW_UI
                )
            }
            //铃音，来电与收短信的铃声
            DataSource.STREAM_RING -> {
                audioManager?.setStreamVolume(
                    AudioManager.STREAM_RING,
                    volume, AudioManager.FLAG_PLAY_SOUND
                            or AudioManager.FLAG_SHOW_UI
                )
            }
            //媒体音	，音视频，游戏等
            DataSource.STREAM_MUSIC -> {
                audioManager?.setStreamVolume(
                    AudioManager.STREAM_MUSIC,
                    volume, AudioManager.FLAG_PLAY_SOUND
                            or AudioManager.FLAG_SHOW_UI
                )
            }
            //闹钟音
            DataSource.STREAM_ALARM -> {
                audioManager?.setStreamVolume(
                    AudioManager.STREAM_ALARM,
                    volume, AudioManager.FLAG_PLAY_SOUND
                            or AudioManager.FLAG_SHOW_UI
                )
            }
            //通知声音
            DataSource.STREAM_NOTIFICATION -> {
                audioManager?.setStreamVolume(
                    AudioManager.STREAM_NOTIFICATION,
                    volume, AudioManager.FLAG_PLAY_SOUND
                            or AudioManager.FLAG_SHOW_UI
                )
            }
        }
    }

    /**
     * 设置指定类型铃声的当前音量
     * adjustStreamVolume是以当前音量为基础，然后调大、调小或调静音。
     * 第二个参数 direction ：
     * AudioManager.ADJUST_RAISE 音量逐渐递增
     * AudioManager.ADJUST_LOWER 音量逐渐递减
     * AudioManager.ADJUST_SAME 不变
     */
    override fun setAdjustStreamVolume(type: Int, volume: Int) {
        when (type) {
            //通话声音
            DataSource.STREAM_VOICE_CALL -> {
                audioManager?.adjustStreamVolume(
                    AudioManager.STREAM_VOICE_CALL,
                    volume, AudioManager.FLAG_PLAY_SOUND
                            or AudioManager.FLAG_SHOW_UI
                )
            }
            //系统音
            DataSource.STREAM_SYSTEM -> {
                audioManager?.adjustStreamVolume(
                    AudioManager.STREAM_SYSTEM,
                    volume, AudioManager.FLAG_PLAY_SOUND
                            or AudioManager.FLAG_SHOW_UI
                )
            }
            //铃音，来电与收短信的铃声
            DataSource.STREAM_RING -> {
                audioManager?.adjustStreamVolume(
                    AudioManager.STREAM_RING,
                    volume, AudioManager.FLAG_PLAY_SOUND
                            or AudioManager.FLAG_SHOW_UI
                )
            }
            //媒体音	，音视频，游戏等
            DataSource.STREAM_MUSIC -> {
                audioManager?.adjustStreamVolume(
                    AudioManager.STREAM_MUSIC,
                    volume, AudioManager.FLAG_PLAY_SOUND
                            or AudioManager.FLAG_SHOW_UI
                )
            }
            //闹钟音
            DataSource.STREAM_ALARM -> {
                audioManager?.adjustStreamVolume(
                    AudioManager.STREAM_ALARM,
                    volume, AudioManager.FLAG_PLAY_SOUND
                            or AudioManager.FLAG_SHOW_UI
                )
            }
            //通知声音
            DataSource.STREAM_NOTIFICATION -> {
                audioManager?.adjustStreamVolume(
                    AudioManager.STREAM_NOTIFICATION,
                    volume, AudioManager.FLAG_PLAY_SOUND
                            or AudioManager.FLAG_SHOW_UI
                )
            }
        }
    }

    /**
     * 设置指定类型铃声的响铃模式
     */
    override fun setRingerMode(mode: Int) {
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

    /**
     * 按照比例调整声音
     */
    override fun dynamicChangeVolume(type: Int, rate: Float) {
        //获取最大音量
        val mediaMaxVolume = getMediaMaxVolume(type)
        //获取当前音量
        val mediaVolume = getMediaVolume(type)
        //获取比例音量 = 最大音量 * 比例
        val targetVolume = mediaMaxVolume * rate
        if (mediaVolume < targetVolume) {
            //如果当前音量小于获取比例音量，则修改声音
            setMediaVolume(type, targetVolume.toInt())
        }
    }

    /**
     * 关闭/打开扬声器播放
     */
    fun setSpeakerStatus(on: Boolean) {
        if (on) {
            //扬声器
            audioManager?.isSpeakerphoneOn = true
            audioManager?.mode = AudioManager.MODE_NORMAL
        } else {
            // 设置最大音量
            val max = audioManager?.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL) ?: 0
            audioManager?.setStreamVolume(AudioManager.STREAM_VOICE_CALL, max, AudioManager.STREAM_VOICE_CALL)
            // 设置成听筒模式
            audioManager?.mode = AudioManager.MODE_IN_COMMUNICATION
            // 关闭扬声器
            audioManager?.isSpeakerphoneOn = false
            audioManager?.setRouting(AudioManager.MODE_NORMAL, AudioManager.ROUTE_EARPIECE, AudioManager.ROUTE_ALL)
        }
    }

    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class DataSource {
        companion object {
            var STREAM_VOICE_CALL = 1   //通话声音
            var STREAM_SYSTEM = 2       //系统音
            var STREAM_RING = 3         //铃音，来电与收短信的铃声
            var STREAM_MUSIC = 4        //媒体音	，音视频，游戏等
            var STREAM_ALARM = 5        //闹钟音
            var STREAM_NOTIFICATION = 6 //通知声音
        }
    }

}
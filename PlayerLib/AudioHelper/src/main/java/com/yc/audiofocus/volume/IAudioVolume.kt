package com.yc.audiofocus.volume

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     GitHub : https://github.com/yangchong211/YCServerLib
 *     time  : 2018/11/9
 *     desc  : 手机声音设置帮助类接口
 *     doc  :
 * </pre>
 */
interface IAudioVolume {

    /**
     * 获取指定类型铃声的响铃模式
     */
    fun getRingerMode(): Int?

    /**
     * 获取最大多媒体音量
     */
    fun getMediaMaxVolume(type: Int): Int

    /**
     * 获取指定类型铃声的当前音量
     */
    fun getMediaVolume(type: Int): Int

    /**
     * 设置指定类型铃声的当前音量
     * setStreamVolume直接将音量调整到目标值，通常与拖动条配合使用；
     */
    fun setMediaVolume(type: Int, volume: Int)

    /**
     * 设置指定类型铃声的当前音量
     * adjustStreamVolume是以当前音量为基础，然后调大、调小或调静音。
     */
    fun setAdjustStreamVolume(type: Int, volume: Int)

    /**
     * 设置指定类型铃声的响铃模式
     */
    fun setRingerMode(mode: Int)

    /**
     * 是否支持铃音
     */
    fun enableRingtone(): Boolean

    /**
     * 是否支持震动
     */
    fun enableVibrate(): Boolean

    /**
     * 按照比例调整声音
     */
    fun dynamicChangeVolume(type: Int, rate: Float)
}
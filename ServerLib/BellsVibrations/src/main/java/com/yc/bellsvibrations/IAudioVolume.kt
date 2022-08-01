package com.yc.bellsvibrations

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
     */
    fun setMediaVolume(type: Int, volume: Int)

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
}
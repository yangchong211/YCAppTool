package com.yc.easymediaplayer.media

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri

/**
 * @author yangchong
 * email  : yangchong211@163.com
 * time  : 2018/8/6
 * desc  : 音频播放player
 * revise:
 */
class MediaAudioPlayer(private val context: Context) {

    private var mMediaPlayer: MediaPlayer? = null
    private var mPause = false

    /**
     * 播放raw资源
     */
    fun play(@DataSource type: Int, data: Any?) {
        playLoop(type, data , false)
    }

    /**
     * 播放raw资源
     */
    fun playLoop(@DataSource type: Int, data: Any?, isLoop : Boolean = true) {
        if (data == null) {
            return
        }
        if (type == DataSource.DATA_SOURCE_URI) {
            check(data is Int) { "data must be Integer" }
        }
        if (type == DataSource.DATA_SOURCE_ASSET) {
            check(data is String) { "data must be String" }
        }
        if (type == DataSource.DATA_SOURCE_NET) {
            check(data is String) { "data must be String" }
        }
        if (mMediaPlayer == null) {
            mMediaPlayer = MediaPlayer()
            mMediaPlayer?.setAudioStreamType(AudioManager.STREAM_MUSIC)
            mMediaPlayer?.setOnCompletionListener { player ->
                if (mMediaPlayer != null && player != null && mMediaPlayer === player) {
                    try {
                        mMediaPlayer?.stop()
                        mMediaPlayer?.release()
                    } catch (e: IllegalStateException) {
                        e.printStackTrace()
                    } finally {
                        mMediaPlayer = null
                    }
                }
            }
            mMediaPlayer?.setOnPreparedListener {
                try {
                    mMediaPlayer?.isLooping = isLoop
                    mMediaPlayer?.start()
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
        }
        try {
            when (type) {
                1 -> {
                    //播放asset文件
                    val afd = context.assets.openFd((data as String?)!!)
                    mMediaPlayer?.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                }
                2 -> {
                    //res/raw文件中的url地址。
                    val sound = Uri.parse("android.resource://" + context.packageName + "/" + data)
                    mMediaPlayer?.setDataSource(context, sound)
                }
                3 -> {
                    //播放网络文件
                    mMediaPlayer?.setDataSource(data as String?)
                }
                else -> {

                }
            }
            mMediaPlayer?.prepare()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    /**
     * 停止播放
     */
    fun stop() {
        if (mMediaPlayer != null) {
            synchronized(mMediaPlayer!!) {
                try {
                    mMediaPlayer?.stop()
                    mMediaPlayer?.release()
                } catch (e: IllegalStateException) {
                    e.printStackTrace()
                } finally {
                    mMediaPlayer = null
                }
            }
        }
    }

    fun release() {
        if (mMediaPlayer != null) {
            mMediaPlayer?.release()
            mMediaPlayer = null
        }
    }

    fun pause() {
        if (mMediaPlayer != null && mMediaPlayer!!.isPlaying) {
            mMediaPlayer?.pause()
            mPause = true
        }
    }

    fun resume() {
        if (mMediaPlayer != null && mPause) {
            mMediaPlayer?.start()
            mPause = false
        }
    }

    val isPlaying: Boolean
        get() = mMediaPlayer != null && mMediaPlayer!!.isPlaying

    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class DataSource {
        companion object {
            var DATA_SOURCE_ASSET = 1
            var DATA_SOURCE_URI = 2
            var DATA_SOURCE_NET = 3
        }
    }
    
}
package com.yc.appserver.vibrator

import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yc.appserver.R
import com.yc.audiofocus.focus.AudioFocusHelper
import com.yc.audiofocus.focus.BecomeNoiseHelper
import com.yc.audiofocus.focus.OnAudioFocusChangeListener
import com.yc.audiofocus.audio.AudioSoundManager
import com.yc.audiofocus.volume.AudioVolumeHelper
import com.yc.easymediaplayer.MediaAudioPlayer
import com.yc.audiofocus.vibrator.VibratorHelper
import com.yc.logclient.LogUtils
import com.yc.roundcorner.view.RoundTextView
import com.yc.toastutils.ToastUtils


class VibratorTestActivity : AppCompatActivity() {

    private lateinit var tvTest1: RoundTextView
    private lateinit var tvTest2: RoundTextView
    private lateinit var tvTest3: RoundTextView
    private lateinit var tvTest4: RoundTextView
    private lateinit var tvTest5: RoundTextView
    private lateinit var tvTest6: RoundTextView
    private lateinit var tvTest7: RoundTextView
    private lateinit var  tvMedia1:RoundTextView
    private lateinit var  tvMedia2:RoundTextView
    private lateinit var  tvMedia3:RoundTextView
    private lateinit var  tvMedia4:RoundTextView
    private lateinit var  tvMedia5:RoundTextView
    private var vibratorHelper: VibratorHelper?= null
    private var mediaAudioPlayer: com.yc.easymediaplayer.MediaAudioPlayer?= null
    private var audioVolumeHelper: AudioVolumeHelper?= null
    private var audioSoundManager: AudioSoundManager?= null
    private var audioFocus: AudioFocusHelper?=null
    private var becomeNoiseHelper: BecomeNoiseHelper?=null

    companion object{
        fun startActivity(context: Context) {
            try {
                val target = Intent()
                target.setClass(context, VibratorTestActivity::class.java)
                context.startActivity(target)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vibrator_main)
        vibratorHelper = VibratorHelper(this)
        mediaAudioPlayer = com.yc.easymediaplayer.MediaAudioPlayer(this)
        audioVolumeHelper = AudioVolumeHelper(this)
        audioSoundManager =
            AudioSoundManager(this.application)
        initView()
        initListener()
        audioFocus = AudioFocusHelper(
            this.applicationContext,
            object : OnAudioFocusChangeListener {
                override fun onLoss() {
                    LogUtils.i("Log test AudioFocusHelper : 音频焦点永久性丢失（此时应暂停播放）")
                }

                override fun onLossTransient() {
                    LogUtils.i("Log test AudioFocusHelper : 音频焦点暂时性丢失（此时应暂停播放）")
                }

                override fun onLossTransientCanDuck() {
                    LogUtils.i("Log test AudioFocusHelper : 音频焦点暂时性丢失（此时只需降低音量，不需要暂停播放）")
                }

                override fun onGain(lossTransient: Boolean, lossTransientCanDuck: Boolean) {
                    LogUtils.i("Log test AudioFocusHelper : 重新获取到音频焦点 , $lossTransient , $lossTransientCanDuck")
                }
            })
        becomeNoiseHelper = BecomeNoiseHelper(this,
            object :
                BecomeNoiseHelper.OnBecomeNoiseListener {
                override fun onBecomeNoise() {
                    LogUtils.i("Log test AudioFocusHelper : 监听 become noise 事件")
                }
            })
        becomeNoiseHelper?.registerBecomeNoiseReceiver()
        LogUtils.i("Log test info : LogTestActivity is create")
    }

    override fun onDestroy() {
        super.onDestroy()
        becomeNoiseHelper?.unregisterBecomeNoiseReceiver()
    }

    private fun initView() {
        tvTest1 = findViewById(R.id.tv_vibrator_1)
        tvTest2 = findViewById(R.id.tv_vibrator_2)
        tvTest3 = findViewById(R.id.tv_vibrator_3)
        tvMedia1 = findViewById(R.id.tv_media_1)
        tvMedia2 = findViewById(R.id.tv_media_2)
        tvMedia3 = findViewById(R.id.tv_media_3)
        tvMedia4 = findViewById(R.id.tv_media_4)
        tvMedia5 = findViewById(R.id.tv_media_5)
    }

    private fun initListener() {
        tvTest1.setOnClickListener {
            ToastUtils.showRoundRectToast("开始震动")
            vibratorHelper?.vibrate(longArrayOf(300L, 800L), 0)
        }
        tvTest2.setOnClickListener {
            ToastUtils.showRoundRectToast("取消震动")
            vibratorHelper?.cancel()
        }
        tvTest3.setOnClickListener {
            ToastUtils.showRoundRectToast("震动3秒")
            vibratorHelper?.vibrate(3000)
        }
        tvMedia1.setOnClickListener {
            ToastUtils.showRoundRectToast("开始播放raw")
            //audioSoundManager?.changeToReceiver()
            audioFocus?.requestAudioFocus()
            mediaAudioPlayer?.playLoop(com.yc.easymediaplayer.MediaAudioPlayer.DataSource.DATA_SOURCE_URI,R.raw.audio_call_ring,true)
        }
        tvMedia2.setOnClickListener {
            ToastUtils.showRoundRectToast("开始播放asset")
            //audioSoundManager?.changeToSpeaker()
            audioFocus?.requestAudioFocus()
            mediaAudioPlayer?.playLoop(com.yc.easymediaplayer.MediaAudioPlayer.DataSource.DATA_SOURCE_ASSET,"audio_call_ring.mp3")
        }
        tvMedia3.setOnClickListener {
            ToastUtils.showRoundRectToast("取消播放")
            mediaAudioPlayer?.stop()
            audioFocus?.abandonAudioFocus()
        }
        tvMedia4.setOnClickListener {
            if (audioVolumeHelper?.enableRingtone() == true){
                ToastUtils.showRoundRectToast("设置播放媒体声音变大1")
                audioVolumeHelper?.dynamicChangeVolume(4,0.5f)
            } else {
                ToastUtils.showRoundRectToast("不支持铃声")
            }
        }
        tvMedia5.setOnClickListener {
            ToastUtils.showRoundRectToast("设置播放媒体声音变大2")
            val maxVolume = audioVolumeHelper?.getMediaMaxVolume(4)
            maxVolume?.let {
                audioVolumeHelper?.setMediaVolume(4, (it * 0.5f).toInt())
            }
        }
    }

    private fun test(){
        //获取最大多媒体音量
        val mediaMaxVolume = audioVolumeHelper?.getMediaMaxVolume(4)
        //是否支持铃音
        val enableRingtone = audioVolumeHelper?.enableRingtone()
        //是否支持震动
        val enableVibrate = audioVolumeHelper?.enableVibrate()
        //获取指定类型铃声的当前音量
        val mediaVolume = audioVolumeHelper?.getMediaVolume(4)
        //获取指定类型铃声的响铃模式
        val ringerMode = audioVolumeHelper?.getRingerMode()
        //设置指定类型铃声的响铃模式
        audioVolumeHelper?.setRingerMode(AudioManager.RINGER_MODE_SILENT)
        //设置指定类型铃声的当前音量
        audioVolumeHelper?.setMediaVolume(4,5)
        //设置指定类型铃声的当前音量
        audioVolumeHelper?.setAdjustStreamVolume(4,5)
        //按照比例调整声音
        audioVolumeHelper?.dynamicChangeVolume(4,0.5f)
    }
}
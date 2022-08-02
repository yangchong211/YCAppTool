package com.yc.appserver.vibrator

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.yc.appserver.R
import com.yc.bellsvibrations.MediaAudioPlayer
import com.yc.bellsvibrations.VibratorHelper
import com.yc.logclient.LogUtils
import com.yc.monitorfilelib.FileExplorerActivity
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
    private var vibratorHelper: VibratorHelper?= null
    private var mediaAudioPlayer: MediaAudioPlayer?= null

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
        mediaAudioPlayer = MediaAudioPlayer(this)
        initView()
        initListener()
        LogUtils.i("Log test info : LogTestActivity is create")
    }

    private fun initView() {
        tvTest1 = findViewById(R.id.tv_vibrator_1)
        tvTest2 = findViewById(R.id.tv_vibrator_2)
        tvTest3 = findViewById(R.id.tv_vibrator_3)
        tvMedia1 = findViewById(R.id.tv_media_1)
        tvMedia2 = findViewById(R.id.tv_media_2)
        tvMedia3 = findViewById(R.id.tv_media_3)
        tvMedia4 = findViewById(R.id.tv_media_4)
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
            mediaAudioPlayer?.play(MediaAudioPlayer.DataSource.DATA_SOURCE_URI,R.raw.audio_call_ring)
        }
        tvMedia2.setOnClickListener {
            ToastUtils.showRoundRectToast("开始播放asset")
            mediaAudioPlayer?.play(MediaAudioPlayer.DataSource.DATA_SOURCE_ASSET,"audio_call_ring.mp3")
        }
        tvMedia3.setOnClickListener {
            ToastUtils.showRoundRectToast("取消播放")
            mediaAudioPlayer?.stop()
        }
        tvMedia4.setOnClickListener {
            ToastUtils.showRoundRectToast("设置播放媒体声音变大")

        }
    }


}
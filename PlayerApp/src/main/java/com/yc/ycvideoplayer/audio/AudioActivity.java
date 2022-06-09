package com.yc.ycvideoplayer.audio;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yc.audioplayer.manager.AudioManager;
import com.yc.audioplayer.service.AudioService;
import com.yc.audioplayer.bean.AudioPlayData;
import com.yc.audioplayer.bean.AudioTtsPriority;

import com.yc.ycvideoplayer.R;

public class AudioActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnInit;
    private Button btnSpeakTts;
    private Button btnSpeakMedia;
    private Button btnMixPlay;
    private Button btnPause;
    private Button btnResume;
    private Button btnStop;
    private Button btnHighPriority;
    private Button btnRelease;
    private Button btnBrazil;
    private Button btnTts;
    private Button btnTtsDemo;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);

        btnInit = findViewById(R.id.btn_init);
        btnSpeakTts = findViewById(R.id.btn_speak_tts);
        btnSpeakMedia = findViewById(R.id.btn_speak_media);
        btnMixPlay = findViewById(R.id.btn_mix_play);
        btnPause = findViewById(R.id.btn_pause);
        btnResume = findViewById(R.id.btn_resume);
        btnStop = findViewById(R.id.btn_stop);
        btnHighPriority = findViewById(R.id.btn_high_priority);
        btnRelease = findViewById(R.id.btn_release);
        btnBrazil = findViewById(R.id.btn_brazil);
        btnTts = findViewById(R.id.btn_tts);
        btnTtsDemo = findViewById(R.id.btn_tts_demo);

        btnInit.setOnClickListener(this);
        btnSpeakTts.setOnClickListener(this);
        btnSpeakMedia.setOnClickListener(this);
        btnMixPlay.setOnClickListener(this);
        btnPause.setOnClickListener(this);
        btnResume.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        btnHighPriority.setOnClickListener(this);
        btnRelease.setOnClickListener(this);
        btnBrazil.setOnClickListener(this);
        btnTts.setOnClickListener(this);
        btnTtsDemo.setOnClickListener(this);

        AudioService.getInstance().init(this);
    }


    @Override
    public void onClick(View v) {
        if (v == btnInit){
            AudioService.getInstance().setPlayStateListener(new AudioManager.PlayStateListener() {
                @Override
                public void onStartPlay() {

                }

                @Override
                public void onCompletePlay() {

                }
            });
        } else if (v == btnSpeakTts){
            AudioPlayData playData = new AudioPlayData.Builder()
                    .tts("开始播放语音，这个是一段文字，逗比。Your goals are hindered by financial strictures.")
                    .build();
            AudioService.getInstance().play(playData);
        } else if (v == btnSpeakMedia){
            AudioPlayData playData = new AudioPlayData.Builder()
                    .rawId(R.raw.timeout)
                    .build();
            AudioService.getInstance().play(playData);
        } else if (v == btnMixPlay){
            AudioPlayData data = new AudioPlayData.Builder(AudioTtsPriority.HIGH_PRIORITY)
                    .tts("我是一个混合的协议")
                    .rawId(R.raw.timeout)
                    .tts("Hello TTS Service").build();
            AudioService.getInstance().play(data);
        } else if (v == btnPause){
            AudioService.getInstance().pause();
        } else if (v == btnResume){
            AudioService.getInstance().resume();
        } else if (v == btnStop){
            AudioService.getInstance().stop();
        } else if (v == btnHighPriority){
            AudioPlayData playData =new AudioPlayData.Builder(AudioTtsPriority.HIGH_PRIORITY)
                    .tts("It sets targets for reduction of greenhouse-gas emissions. ")
                    .build();
            AudioService.getInstance().play(playData);
        } else if (v == btnRelease){
            AudioService.getInstance().release();
        } else if (v == btnBrazil){
            //法鱼
            AudioPlayData playData = new AudioPlayData.Builder()
                    .tts("Dans tout ce que nous faisons, nous devons être persévérants")
                    .build();
            AudioService.getInstance().play(playData);
        } else if (v == btnTts){
            AudioService.getInstance().playTts("逗比，这个是tts");
        } else if (v == btnTtsDemo){
            startActivity(new Intent(this,TTSAudioActivity.class));
        }
    }
}

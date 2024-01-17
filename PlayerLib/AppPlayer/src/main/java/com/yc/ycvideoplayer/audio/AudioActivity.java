package com.yc.ycvideoplayer.audio;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yc.appcommoninter.ILogger;
import com.yc.audioplayer.bean.TtsPlayerConfig;
import com.yc.audioplayer.inter.DefaultStateListener;
import com.yc.audioplayer.inter.PlayStateListener;
import com.yc.audioplayer.manager.AudioManager;
import com.yc.audioplayer.service.AudioService;
import com.yc.audioplayer.bean.AudioPlayData;
import com.yc.audioplayer.bean.AudioTtsPriority;

import com.yc.toolutils.AppLogUtils;
import com.yc.ycvideoplayer.R;

public class AudioActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnTtsState1;
    private Button btnTtsState2;
    private Button btnSpeakTts1;
    private Button btnSpeakTts2;
    private Button btnSpeakTts3;
    private Button btnSpeakMedia;
    private Button btnMixPlay;
    private Button btnPause;
    private Button btnResume;
    private Button btnStop;
    private Button btnHighPriority;
    private Button btnRelease;
    private Button btnBrazil;
    private Button btnTts;
    private Button btnUrl;
    private Button btnUrl2;
    private Button btnTtsDemo;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);
        AppLogUtils.setShowLog(true);

        btnTtsState1 = findViewById(R.id.btn_tts_state1);
        btnTtsState2 = findViewById(R.id.btn_tts_state2);
        btnSpeakTts1 = findViewById(R.id.btn_speak_tts1);
        btnSpeakTts2 = findViewById(R.id.btn_speak_tts2);
        btnSpeakTts3 = findViewById(R.id.btn_speak_tts3);
        btnSpeakMedia = findViewById(R.id.btn_speak_media);
        btnMixPlay = findViewById(R.id.btn_mix_play);
        btnPause = findViewById(R.id.btn_pause);
        btnResume = findViewById(R.id.btn_resume);
        btnStop = findViewById(R.id.btn_stop);
        btnHighPriority = findViewById(R.id.btn_high_priority);
        btnRelease = findViewById(R.id.btn_release);
        btnBrazil = findViewById(R.id.btn_brazil);
        btnTts = findViewById(R.id.btn_tts);
        btnUrl = findViewById(R.id.btn_url);
        btnUrl2 = findViewById(R.id.btn_url2);
        btnTtsDemo = findViewById(R.id.btn_tts_demo);

        btnTtsState1.setOnClickListener(this);
        btnTtsState2.setOnClickListener(this);
        btnSpeakTts1.setOnClickListener(this);
        btnSpeakTts2.setOnClickListener(this);
        btnSpeakTts3.setOnClickListener(this);
        btnSpeakMedia.setOnClickListener(this);
        btnMixPlay.setOnClickListener(this);
        btnPause.setOnClickListener(this);
        btnResume.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        btnHighPriority.setOnClickListener(this);
        btnRelease.setOnClickListener(this);
        btnBrazil.setOnClickListener(this);
        btnTts.setOnClickListener(this);
        btnUrl.setOnClickListener(this);
        btnUrl2.setOnClickListener(this);
        btnTtsDemo.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v == btnTtsState1){
            TtsPlayerConfig config = new TtsPlayerConfig.Builder().setTtsDeque(true).build();
            AudioService.getInstance().init(this,config);
            AudioService.getInstance().setPlayStateListener(new DefaultStateListener() {
                @Override
                public void onStartPlay() {
                    AppLogUtils.d("tts player state start");
                }

                @Override
                public void onStopPlay() {
                    super.onStopPlay();
                    AppLogUtils.d("tts player state stop");
                }

                @Override
                public void onCompletePlay() {
                    AppLogUtils.d("tts player state complete");
                }
            });
        } else if (v == btnTtsState2){
            TtsPlayerConfig config = new TtsPlayerConfig.Builder()
                    .setTtsDeque(false)
                    .setLogger(new ILogger() {

                        @Override
                        public void log(String log) {

                        }

                        @Override
                        public void error(String error) {

                        }
                    })
                    .build();
            AudioService.getInstance().init(this,config);
            AudioService.getInstance().setPlayStateListener(new DefaultStateListener() {
                @Override
                public void onStartPlay() {
                    AppLogUtils.d("tts player state start");
                }
                @Override
                public void onStopPlay() {
                    super.onStopPlay();
                    AppLogUtils.d("tts player state stop");
                }
                @Override
                public void onCompletePlay() {
                    AppLogUtils.d("tts player state complete");
                }
            });
        }else if (v == btnSpeakTts1){
            AudioService.getInstance().playTts("开始播放语音，这个是一段文字，逗比。Your goals are hindered by financial strictures.");
        } else if (v == btnSpeakTts2){
            AudioService.getInstance().playAudioResource(R.raw.ns_no_answer_call_later);
        } else if (v == btnSpeakTts3){
            AudioService.getInstance().playUrl("https://asraudio.cdnjtzy.com/eb93cfd82d0044a1a9ce047c3aeafb8c.mp3");
        } else if (v == btnSpeakMedia){
            AudioPlayData playData = new AudioPlayData.Builder()
                    .rawId(R.raw.timeout)
                    .build();
            AudioService.getInstance().play(playData);
        } else if (v == btnMixPlay){
            AudioPlayData data = new AudioPlayData.Builder(AudioTtsPriority.HIGH_PRIORITY)
                    .tts("我是一个混合的协议的语音播报")
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
        } else if (v == btnUrl){
            AudioPlayData playData =new AudioPlayData.Builder(AudioTtsPriority.HIGH_PRIORITY)
                    .url("https://asraudio.cdnjtzy.com/eb93cfd82d0044a1a9ce047c3aeafb8c.mp3")
                    .url("https://asraudio.cdnjtzy.com/52bdab34457e4d9ca14a5a7feee94a23.mp3")
                    .url("https://asraudio.cdnjtzy.com/eb93cfd82d0044a1a9ce047c3aeafb8c.mp3")
                    .url("https://asraudio.cdnjtzy.com/eb93cfd82d0044a1a9ce047c3aeafb8c.mp3")
                    .url("https://asraudio.cdnjtzy.com/52bdab34457e4d9ca14a5a7feee94a23.mp3")
                    .url("https://asraudio.cdnjtzy.com/eb93cfd82d0044a1a9ce047c3aeafb8c.mp3")
                    .build();
            AudioService.getInstance().play(playData);
        }  else if (v == btnUrl2){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    AudioService.getInstance().playUrl("https://asraudio.cdnjtzy.com/52bdab34457e4d9ca14a5a7feee94a23.mp3");
                }
            }).start();
        } else if (v == btnTtsDemo){
            startActivity(new Intent(this,TTSAudioActivity.class));
        }
    }
}

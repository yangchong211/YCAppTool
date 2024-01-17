package com.yc.ycvideoplayer.audio;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.yc.toolutils.AppLogUtils;

import com.yc.ycvideoplayer.R;

import java.util.Locale;

public class TTSAudioActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnInit;
    private Button btnSpeakTts1;
    private Button btnSpeakTts2;
    private Button btnSpeakTts3;
    private Button btnSpeakTts4;
    private Button btnStop;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_tts);

        btnInit = findViewById(R.id.btn_init);
        btnSpeakTts1 = findViewById(R.id.btn_speak_tts1);
        btnSpeakTts2 = findViewById(R.id.btn_speak_tts2);
        btnSpeakTts3 = findViewById(R.id.btn_speak_tts3);
        btnSpeakTts4 = findViewById(R.id.btn_speak_tts4);
        btnStop = findViewById(R.id.btn_stop);

        btnInit.setOnClickListener(this);
        btnSpeakTts1.setOnClickListener(this);
        btnSpeakTts2.setOnClickListener(this);
        btnSpeakTts3.setOnClickListener(this);
        btnSpeakTts4.setOnClickListener(this);
        btnStop.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v == btnInit){
            init();
        } else if (v == btnSpeakTts1){
            if (textToSpeech!=null){
                textToSpeech.speak("简单播放tts", TextToSpeech.QUEUE_FLUSH, null);
            }
        } else if (v == btnSpeakTts2){
            if (textToSpeech!=null){
                for (int i=0 ; i<5 ; i++){
                    textToSpeech.speak("简单播放tts，"+i, TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        } else if (v == btnSpeakTts3){
            if (textToSpeech!=null){
                for (int i=0 ; i<5 ; i++){
                    if (!textToSpeech.isSpeaking()){
                        textToSpeech.speak("简单播放tts，"+i, TextToSpeech.QUEUE_FLUSH, null);
                    }
                }
            }
        } else if (v == btnSpeakTts4){
            if (textToSpeech!=null){
                for (int i=0 ; i<5 ; i++){
                    textToSpeech.speak("简单播放，"+i, TextToSpeech.QUEUE_ADD, null);
                }
            }
        } else if (v == btnStop){
            if (textToSpeech!=null){
                textToSpeech.stop();
            }
        }
    }

    private TextToSpeech textToSpeech;
    private void init(){
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if ((TextToSpeech.SUCCESS == status) && textToSpeech != null) {
                    AppLogUtils.i("Initialize TTS success");
                    //获取locale
                    final Locale locale = TTSAudioActivity.this.getApplicationContext()
                            .getResources().getConfiguration().locale;
                    if (locale != null) {
                        AppLogUtils.i("tts isLanguageAvailable " + textToSpeech.isLanguageAvailable(locale) +
                                "; variant is " + locale.getVariant() +
                                "; locale is " + locale + " ; country  is " + locale.getCountry());
                    }
                    //设置朗读语言
                    int setLanguage = textToSpeech.setLanguage(null != locale ? locale : Locale.getDefault());
                    switch (setLanguage) {
                        case TextToSpeech.LANG_MISSING_DATA:
                            AppLogUtils.i("TTS set language: Language missing data");
                            break;
                        case TextToSpeech.LANG_NOT_SUPPORTED:
                            AppLogUtils.i("TTS set language: Language not supported");
                            break;
                        case TextToSpeech.LANG_AVAILABLE:
                            AppLogUtils.i("TTS set language: Language available");
                            break;
                        case TextToSpeech.LANG_COUNTRY_AVAILABLE:
                            AppLogUtils.i("TTS set language: Language country available");
                            break;
                        case TextToSpeech.LANG_COUNTRY_VAR_AVAILABLE:
                            AppLogUtils.i("TTS set language: Language country var available");
                            break;
                        default:
                            AppLogUtils.i("TTS set language: Unknown error");
                            break;
                    }
                } else if (TextToSpeech.ERROR == status) {
                    AppLogUtils.i("Initialize TTS error");
                } else {
                    AppLogUtils.i("Initialize TTS error");
                }
            }
        });
    }

}

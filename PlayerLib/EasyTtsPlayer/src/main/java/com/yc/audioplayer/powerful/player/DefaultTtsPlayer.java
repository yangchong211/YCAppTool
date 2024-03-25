package com.yc.audioplayer.powerful.player;

import android.app.Application;
import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;


import com.yc.audioplayer.powerful.bean.TtsPlayerConfig;
import com.yc.audioplayer.powerful.service.AudioService;
import com.yc.audioplayer.powerful.inter.AbstractAudioWrapper;
import com.yc.audioplayer.powerful.bean.AudioPlayData;
import com.yc.audioplayer.powerful.inter.InterPlayListener;

import java.util.HashMap;
import java.util.Locale;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     GitHub : <a href="https://github.com/yangchong211/YCVideoPlayer">...</a>
 *     time  : 2018/8/6
 *     desc  : tts播放player
 *     revise: 使用TextToSpeech
 * </pre>
 */
public class DefaultTtsPlayer extends AbstractAudioWrapper implements TextToSpeech.OnInitListener {

    private TextToSpeech mTts;

    /**
     * 初始化是否完成
     */
    private volatile boolean mReady = false;
    private final Context mContext;
    private InterPlayListener mPlayListener;
    /**
     * 创建tts监听
     */
    private final OnCompleteListener mOnCompleteListener = new OnCompleteListener();

    public DefaultTtsPlayer(Context context) {
        if (context instanceof Application) {
            mContext = context;
        } else {
            mContext = context.getApplicationContext();
        }
    }

    @Override
    public void init(InterPlayListener next, Context context) {
        mPlayListener = next;
        //初始化 TTS 引擎：在使用 TTS 功能之前，需要初始化 TTS 引擎。
        //通过创建 TextToSpeech 对象，并传递初始化完成的监听器，可以初始化 TTS 引擎。一旦引擎初始化完成，就可以开始使用 TTS 功能。
        this.mTts = new TextToSpeech(context, this);
    }

    @Override
    public boolean isPlaying() {
        return mTts.isSpeaking();
    }

    @Override
    public void onCompleted() {
        if (mPlayListener != null) {
            mPlayListener.onCompleted();
        }
        mReady = true;
    }

    @Override
    public void onError(String error) {

    }

    /**
     * 初始化引擎回调
     *
     * @param status {@link TextToSpeech#SUCCESS} or {@link TextToSpeech#ERROR}.
     */
    @Override
    public void onInit(final int status) {
        TtsPlayerConfig config = AudioService.getInstance().getConfig();
        try {
            setLanguage(status, config);
        } catch (Exception e) {
            e.printStackTrace();
            config.getLogger().log(e.getMessage());
        }
    }

    private void setLanguage(int status, TtsPlayerConfig config) {
        if (!mReady && (TextToSpeech.SUCCESS == status) && this.mTts != null) {
            config.getLogger().log("Initialize TTS success");
            //获取locale
            final Locale locale = mContext.getApplicationContext()
                    .getResources().getConfiguration().locale;
            if (locale != null) {
                config.getLogger().log("tts isLanguageAvailable " + mTts.isLanguageAvailable(locale) +
                        "; variant is " + locale.getVariant() +
                        "; locale is " + locale + " ; country  is " + locale.getCountry());
            }
            //设置朗读语言
            //TTS 引擎支持多种语言，包括但不限于英语、中文、法语、德语等。
            //可以通过 setLanguage() 方法设置要使用的语言。需要注意的是，不同的 TTS 引擎可能支持的语言范围有所不同。
            int setLanguage = this.mTts.setLanguage(null != locale ? locale : Locale.getDefault());
            switch (setLanguage) {
                case TextToSpeech.LANG_MISSING_DATA:
                    config.getLogger().log("TTS set language: Language missing data");
                    break;
                case TextToSpeech.LANG_NOT_SUPPORTED:
                    config.getLogger().log("TTS set language: Language not supported");
                    break;
                case TextToSpeech.LANG_AVAILABLE:
                    config.getLogger().log("TTS set language: Language available");
                    break;
                case TextToSpeech.LANG_COUNTRY_AVAILABLE:
                    config.getLogger().log("TTS set language: Language country available");
                    break;
                case TextToSpeech.LANG_COUNTRY_VAR_AVAILABLE:
                    config.getLogger().log("TTS set language: Language country var available");
                    break;
                default:
                    config.getLogger().log("TTS set language: Unknown error");
                    break;
            }
        } else if (TextToSpeech.ERROR == status) {
            config.getLogger().log("Initialize TTS error");
        } else {
            config.getLogger().log("Initialize TTS error");
        }
    }

    @Override
    public void pause() {
        //停止tts播放
        mTts.stop();
    }

    @Override
    public void play(AudioPlayData data) {
        synchronized (this) {
            //如果是在说话中，则先停止
            if (this.mTts.isSpeaking()) {
                this.mTts.stop();
            }

            String tts = data.getTts();
            //添加tts监听。监听器提供了回调方法，如 onStart()、onDone()、onError() 等，用于处理语音合成的开始、完成和错误情况。
            this.mTts.setOnUtteranceProgressListener(mOnCompleteListener);
            HashMap<String, String> map = new HashMap<>();
            map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, tts);
            //tts播报
            //第一个参数播放的内容
            //第二个参数表示
            //第三个参数表示
            //使用 TextToSpeech 类的 speak() 方法可以将文本转换为语音。
            //可以指定要转换的文本内容、语音合成的队列模式、播放速度、音量等参数。
            //TTS 引擎将根据指定的参数将文本转换为语音，并通过设备的扬声器播放出来。
            this.mTts.speak(tts, TextToSpeech.QUEUE_FLUSH, map);
        }
    }

    /**
     * 在不需要使用 TTS 功能时，应及时停止语音合成并释放相关资源，以避免资源浪费。
     * 可以使用 stop() 方法停止当前的语音合成，使用 shutdown() 方法释放 TTS 引擎。
     * 释放了引擎后需要再次初始化引擎才可以播放tts
     */
    @Override
    public void release() {
        //先停止播放
        stop();
        //关闭TTS
        this.mTts.shutdown();
        this.mReady = false;
    }

    @Override
    public void resumeSpeaking() {

    }

    @Override
    public void stop() {
        //停止播报
        mTts.stop();
    }

    private final class OnCompleteListener extends UtteranceProgressListener {

        OnCompleteListener() {

        }

        /**
         * 播放完成。这个是播报完毕的时候 每一次播报完毕都会走
         *
         * @param utteranceId 话语id
         */
        @Override
        public void onDone(final String utteranceId) {
            TtsPlayerConfig config = AudioService.getInstance().getConfig();
            config.getLogger().log("TTSPlayer OnCompleteListener onDone");
            onCompleted();
        }

        /**
         * 播放异常
         *
         * @param utteranceId 话语id
         */
        @Override
        public void onError(final String utteranceId) {
            TtsPlayerConfig config = AudioService.getInstance().getConfig();
            config.getLogger().log("TTSPlayer OnCompleteListener onError");
            stop();
            onError("TTSPlayer has play fail : " + utteranceId);
            onCompleted();
        }

        /**
         * 播放开始。这个是开始的时候。是先发声之后才会走这里
         * 调用isSpeaking()方法在这为true
         *
         * @param utteranceId 话语id
         */
        @Override
        public void onStart(final String utteranceId) {
            TtsPlayerConfig config = AudioService.getInstance().getConfig();
            config.getLogger().log("TTSPlayer OnCompleteListener onStart");
        }
    }

}

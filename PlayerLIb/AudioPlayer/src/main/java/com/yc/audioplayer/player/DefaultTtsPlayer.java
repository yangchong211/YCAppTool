package com.yc.audioplayer.player;

import android.app.Application;
import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;


import com.yc.audioplayer.wrapper.AbstractAudioWrapper;
import com.yc.audioplayer.bean.AudioPlayData;
import com.yc.audioplayer.inter.InterPlayListener;
import com.yc.videotool.VideoLogUtils;

import java.util.HashMap;
import java.util.Locale;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     GitHub : https://github.com/yangchong211/YCVideoPlayer
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

    @Override
    public void onInit(final int status) {
        try {
            if (!mReady && (TextToSpeech.SUCCESS == status) && this.mTts != null) {
                VideoLogUtils.i("Initialize TTS success");
                //获取locale
                final Locale locale = mContext.getApplicationContext()
                        .getResources().getConfiguration().locale;
                if (locale != null) {
                    VideoLogUtils.i("tts isLanguageAvailable " + mTts.isLanguageAvailable(locale) +
                            "; variant is " + locale.getVariant() +
                            "; locale is " + locale + " ; country  is " + locale.getCountry());
                }
                //设置朗读语言
                int setLanguage = this.mTts.setLanguage(null != locale ? locale : Locale.getDefault());
                switch (setLanguage) {
                    case TextToSpeech.LANG_MISSING_DATA:
                        VideoLogUtils.i("TTS set language: Language missing data");
                        break;
                    case TextToSpeech.LANG_NOT_SUPPORTED:
                        VideoLogUtils.i("TTS set language: Language not supported");
                        break;
                    case TextToSpeech.LANG_AVAILABLE:
                        VideoLogUtils.i("TTS set language: Language available");
                        break;
                    case TextToSpeech.LANG_COUNTRY_AVAILABLE:
                        VideoLogUtils.i("TTS set language: Language country available");
                        break;
                    case TextToSpeech.LANG_COUNTRY_VAR_AVAILABLE:
                        VideoLogUtils.i("TTS set language: Language country var available");
                        break;
                    default:
                        VideoLogUtils.i("TTS set language: Unknown error");
                        break;
                }
            } else if (TextToSpeech.ERROR == status) {
                VideoLogUtils.i("Initialize TTS error");
            } else {
                VideoLogUtils.i("Initialize TTS error");
            }
        } catch (Exception e) {
            e.printStackTrace();
            VideoLogUtils.i(e.getMessage());
        }
    }

    @Override
    public void pause() {
        //停止tts播放
        mTts.stop();
    }

    @Override
    public void play(AudioPlayData data) {
        synchronized (this.mTts) {
            //如果是在说话中，则先停止
            if (this.mTts.isSpeaking()) {
                this.mTts.stop();
            }

            String tts = data.getTts();
            //添加tts监听
            this.mTts.setOnUtteranceProgressListener(mOnCompleteListener);
            HashMap<String, String> map = new HashMap<>();
            map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, tts);
            //tts播报
            //第一个参数播放的内容
            //第二个参数表示
            //第三个参数表示
            this.mTts.speak(tts, TextToSpeech.QUEUE_FLUSH, map);
        }
    }

    @Override
    public void release() {
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
            VideoLogUtils.i("TTSPlayer OnCompleteListener onDone");
            onCompleted();
        }

        /**
         * 播放异常
         *
         * @param utteranceId 话语id
         */
        @Override
        public void onError(final String utteranceId) {
            VideoLogUtils.i("TTSPlayer OnCompleteListener onError");
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

        }
    }

}

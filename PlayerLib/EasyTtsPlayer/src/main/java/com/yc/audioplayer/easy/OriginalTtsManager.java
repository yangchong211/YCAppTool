/*
Copyright 2017 yangchong211（github.com/yangchong211）

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package com.yc.audioplayer.easy;


import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.HashMap;

/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/12/23
 *     desc  : tts工具类
 *     revise:
 * </pre>
 */
public class OriginalTtsManager implements InterTtsPlayer {

    private static final String TAG = "OriginalTtsManager";
    private static volatile InterTtsPlayer whyTTS = null;
    private final TextToSpeech mSpeech;
    private String residenceContent;
    private final HashMap<Integer, Long> sentenceStep = new HashMap<>();
    private long startTime = 0;
    private long charStep = 225;

    private OriginalTtsManager(Context context) {
        mSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                Log.e(TAG, "onInit: success");
                mSpeech.setSpeechRate(1.0f);
            }
        });
    }

    public static InterTtsPlayer getInstance(Context context) {
        if (whyTTS == null) {
            synchronized (OriginalTtsManager.class) {
                if (whyTTS == null) {
                    whyTTS = new OriginalTtsManager(context);
                }
            }
        }
        return whyTTS;
    }

    @Override
    public void speak(final String content) {
        residenceContent = content;
        sentenceStep.clear();
        //getSentenceStep();
        startTime = System.currentTimeMillis();
        mSpeech.speak(content, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    public void pause() {
        long duration = System.currentTimeMillis() - startTime;
        residenceContent = getResidenceByDuration(duration);
        mSpeech.speak("", TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    public void resume() {
        speak(residenceContent);
    }

    @Override
    public void setSpeechRate(float newRate) {
        if (mSpeech != null) {
            mSpeech.setSpeechRate(newRate);
            charStep = charStep + (long) (newRate - 1.0f) * charStep;
        }
    }

    @Override
    public void setSpeechPitch(float newPitch) {
        if (mSpeech != null) {
            mSpeech.setSpeechRate(newPitch);
        }
    }

    private String getResidenceByDuration(long duration) {
        int tempIndex = (int) (duration / charStep);
        if (duration > (charStep * residenceContent.length())) {
            return "";
        }
        residenceContent = residenceContent.substring(tempIndex - 1);
        return residenceContent;
    }

    /**
     * 根据朗读时间计算当前读到句子索引
     *
     * @param duration
     */
    private int findSentenceIndex(long duration) {
        for (int i = 0; i < sentenceStep.size() - 1; i++) {
            if (duration <= sentenceStep.get(i + 1) && duration >= sentenceStep.get(i)) {
                return i + 1;
            }
        }
        if (duration > sentenceStep.get(sentenceStep.size() - 1)) {
            return -1;
        }
        return -2;
    }

    /**
     * get the mark index list
     */
    private void getSentenceStep() {
        String[] array = residenceContent.split("，");
        if (array.length <= 1) {
            return;
        } else {
            for (int i = 0; i < array.length; i++) {
                long tempTime = 0;
                for (int j = 0; j <= i; j++) {
                    tempTime += array[j].length() * charStep;
                }
                long markStep = 300;
                tempTime += (i + 1) * markStep;
                sentenceStep.put(i, tempTime);
            }
        }
    }

    @Override
    public void release() {
        mSpeech.shutdown();
        mSpeech.stop();
    }

}

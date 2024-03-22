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
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import androidx.annotation.RequiresApi;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;

/**
 * <pre>
 *     @author 杨充
 *     blog  : <a href="https://github.com/yangchong211">...</a>
 *     time  : 2019/12/23
 *     desc  : 自定义LiveData<T>
 *     revise:
 * </pre>
 */
public class MediaTtsManager implements InterTtsPlayer {

    private static final String TAG = "MediaTtsManager";
    private static volatile InterTtsPlayer manager = null;
    private static TextToSpeech mSpeech = null;
    private final Context mContext;
    private final String wavPath;
    private final MediaPlayer player;
    private final HashMap<String, String> myHashRender = new HashMap<>();

    @RequiresApi(api = Build.VERSION_CODES.M)
    private MediaTtsManager(Context context) {
        this.mContext = context;
        wavPath = Environment.getExternalStorageDirectory() + "/temp.wav";
        player = new MediaPlayer();
        initSpeech();
    }

    /**
     * Init TTS and set params
     */
    private void initSpeech() {
        mSpeech = new TextToSpeech(mContext, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                 mSpeech.setLanguage(Locale.getDefault());
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static InterTtsPlayer getInstance(Context context) {
        if (manager == null) {
            synchronized (MediaTtsManager.class) {
                if (manager == null) {
                    manager = new MediaTtsManager(context);
                }
            }
        }
        return manager;
    }


    @Override
    public void speak(String content) {
        Log.e(TAG, "speak content: " + content);
        myHashRender.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, content);
        //使用指定的参数将给定的文本合成为文件。
        //该方法是异步的，即该方法只是将请求添加到TTS请求队列中，然后返回。
        //当此方法返回时，合成可能还没有完成(甚至还没有开始!)。
        int r = mSpeech.synthesizeToFile(content, myHashRender, wavPath);
        if (r == TextToSpeech.SUCCESS) {
            Log.e(TAG, "save success" + wavPath);
        } else {
            Log.e(TAG, "save fail");
        }
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            player.reset();
            player.setDataSource(wavPath);
            player.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        player.start();
    }


    /**
     * pause the TTS
     */
    @Override
    public void pause() {
        if (player.isPlaying()) {
            player.pause();
        }
    }

    /**
     * reset the TTS
     */
    @Override
    public void resume() {
        player.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void setSpeechRate(float newRate) {
        //6.0+可以设置
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            player.setPlaybackParams(player.getPlaybackParams().setSpeed(newRate));
        } else {
            Log.e(TAG, "setSpeechRate: 版本过低，接口不可用");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void setSpeechPitch(float newPitch) {
        //6.0+可以设置
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            player.setPlaybackParams(player.getPlaybackParams().setPitch(newPitch));
        } else {
            Log.e(TAG, "setSpeechPitch: 版本过低，接口不可用");
        }
    }

    /**
     * stop the TTS
     */
    @Override
    public void release() {
        player.stop();
        player.release();
        mSpeech.shutdown();
        mSpeech.stop();
    }

}

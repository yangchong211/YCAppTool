package com.yc.apphardware.card;

import android.media.MediaPlayer;
import com.yc.appcontextlib.AppToolUtils;
import com.yc.apphardware.R;

/**
 * @author: Administrator
 * @date: 2023/10/10
 */

public class ToolUtils {

    //播放beep
    private static MediaPlayer mediaPlayer;
    public static void playBeep() {
        if(mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(AppToolUtils.getApp(), R.raw.beep);
        }
        if(mediaPlayer != null) {
            mediaPlayer.setLooping(false);
            mediaPlayer.start();
        }
    }
    //释放MediaPlayer资源
    public static void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }


}
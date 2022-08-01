package com.yc.bellsvibrations;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time  : 2018/8/6
 *     desc  : 音频播放player
 *     revise:
 * </pre>
 */
public final class MediaAudioPlayer {

    private final Context mContext;
    private MediaPlayer mMediaPlayer;
    private boolean mPause = false;

    public MediaAudioPlayer(Context context) {
        this.mContext = context;
    }

    /**
     * 播放raw资源
     */
    public void play(@DataSourceType int type, Object data) {
        if (data == null) {
            return;
        }
        if (type == DataSource.DATA_SOURCE_URI){
            if (!(data instanceof Integer)){
                throw new IllegalStateException("data must be Integer");
            }
        }
        if (type == DataSource.DATA_SOURCE_ASSET){
            if (!(data instanceof String)){
                throw new IllegalStateException("data must be String");
            }
        }
        if (type == DataSource.DATA_SOURCE_NET){
            if (!(data instanceof String)){
                throw new IllegalStateException("data must be String");
            }
        }
        if (mMediaPlayer == null){
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer player) {
                    if (mMediaPlayer != null && player != null && mMediaPlayer == player) {
                        try {
                            mMediaPlayer.stop();
                            mMediaPlayer.release();
                        } catch (IllegalStateException e) {
                            e.printStackTrace();
                        } finally {
                            mMediaPlayer = null;
                        }
                    }
                }
            });
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    try {
                        mMediaPlayer.start();
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        if (mMediaPlayer != null) {
            try {
                switch (type) {
                    case 1:
                        //播放asset文件
                        AssetFileDescriptor afd = mContext.getAssets().openFd((String) data);
                        mMediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                        break;
                    case 2:
                        //res/raw文件中的url地址。
                        Uri sound = Uri.parse("android.resource://" + mContext.getPackageName() + "/" + data);
                        mMediaPlayer.setDataSource(mContext, sound);
                        break;
                    case 3:
                        //播放网络文件
                        mMediaPlayer.setDataSource((String) data);
                        break;
                    default:
                        break;
                }
                mMediaPlayer.prepare();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 停止播放
     */
    public void stop() {
        if (mMediaPlayer != null) {
            synchronized (mMediaPlayer) {
                try {
                    mMediaPlayer.stop();
                    mMediaPlayer.release();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } finally {
                    mMediaPlayer = null;
                }
            }
        }
    }

    public void release() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    public void pause() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            mPause = true;
        }
    }

    public void resume() {
        if (mMediaPlayer != null && mPause) {
            mMediaPlayer.start();
            mPause = false;
        }
    }

    public boolean isPlaying() {
        return mMediaPlayer != null && mMediaPlayer.isPlaying();
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface DataSource {
        int DATA_SOURCE_ASSET = 1;
        int DATA_SOURCE_URI = 2;
        int DATA_SOURCE_NET = 3;
    }

    @IntDef({DataSource.DATA_SOURCE_URI,DataSource.DATA_SOURCE_ASSET,DataSource.DATA_SOURCE_NET})
    @Retention(RetentionPolicy.SOURCE)
    public @interface DataSourceType{}
}

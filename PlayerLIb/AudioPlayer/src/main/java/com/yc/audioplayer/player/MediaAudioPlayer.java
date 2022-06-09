package com.yc.audioplayer.player;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

import com.yc.audioplayer.wrapper.AbstractAudioWrapper;
import com.yc.audioplayer.bean.AudioPlayData;
import com.yc.audioplayer.inter.InterPlayListener;
import com.yc.videotool.VideoLogUtils;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time  : 2018/8/6
 *     desc  : 音频播放player
 *     revise:
 * </pre>
 */
public class MediaAudioPlayer extends AbstractAudioWrapper {

    private InterPlayListener mPlayListener;
    private Context mContext;
    private MediaPlayer mMediaPlayer;
    private boolean mPause = false;

    /**
     * 完成/出错时的监听接口
     */
    private final OnCompletionListener mOnCompletionListener = new OnCompletionListener() {
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
            onCompleted();
        }
    };

    public MediaAudioPlayer() {

    }

    @Override
    public void init(InterPlayListener next, Context context) {
        this.mPlayListener = next;
        this.mContext = context;
    }

    /**
     * 播放raw资源
     */
    @Override
    public void play(AudioPlayData data) {
        VideoLogUtils.d("MediaPlay: play resourceId is" + data.getRawId());
        if (data.getRawId() <= 0) {
            return;
        }
        if (mMediaPlayer == null) {
            try {
                mMediaPlayer = new MediaPlayer();
                AssetFileDescriptor afd = mContext.getResources().openRawResourceFd(data.getRawId());

//                Uri sound = Uri.parse("android.resource://" + mContext.getPackageName() + "/" + data.getRawId());//  res/raw文件中的url地址。
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC); //set streaming according to ur needs
                mMediaPlayer.setOnCompletionListener(mOnCompletionListener);
//                mMediaPlayer.reset();
                mMediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
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
                mMediaPlayer.prepare();
            } catch (Throwable e) {
                //e.printStackTrace();
                VideoLogUtils.d("MediaPlay: play fail");
                onError("MediaPlayer has play fail : " + e.getMessage());
                onCompleted();
            }
        }
    }

    /**
     * 停止播放
     */
    @Override
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

    @Override
    public void release() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    @Override
    public void pause() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            mPause = true;
        }
    }

    @Override
    public void resumeSpeaking() {
        if (mMediaPlayer != null && mPause) {
            mMediaPlayer.start();
            mPause = false;
        }
    }

    @Override
    public boolean isPlaying() {
        return mMediaPlayer != null && mMediaPlayer.isPlaying();
    }

    @Override
    public void onCompleted() {
        if (mPlayListener != null) {
            mPlayListener.onCompleted();
        }
    }

    @Override
    public void onError(String error) {

    }

}

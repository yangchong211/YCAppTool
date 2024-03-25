package com.yc.audioplayer.powerful.player;

import android.app.Application;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

import com.yc.audioplayer.powerful.bean.TtsPlayerConfig;
import com.yc.audioplayer.powerful.bean.AudioPlayData;
import com.yc.audioplayer.powerful.inter.AbstractAudioWrapper;
import com.yc.audioplayer.powerful.inter.InterPlayListener;
import com.yc.audioplayer.powerful.service.AudioService;


/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     GitHub : <a href="https://github.com/yangchong211/YCVideoPlayer">...</a>
 *     time  : 2018/8/6
 *     desc  : 音频播放player，使用原生media。本地资源可以使用这个
 *     revise:
 * </pre>
 */
public class MediaAudioPlayer extends AbstractAudioWrapper {

    private InterPlayListener mPlayListener;
    private Context mContext;
    private MediaPlayer mMediaPlayer;
    private boolean mPause = false;

    public MediaAudioPlayer() {

    }

    @Override
    public void init(InterPlayListener next, Context context) {
        this.mPlayListener = next;
        if (context instanceof Application) {
            mContext = context;
        } else {
            mContext = context.getApplicationContext();
        }
    }

    /**
     * 播放raw资源
     */
    @Override
    public void play(AudioPlayData data) {
        TtsPlayerConfig config = AudioService.getInstance().getConfig();
        config.getLogger().log("MediaPlay: play resourceId is" + data.getRawId());
        if (data.getRawId() <= 0) {
            return;
        }
        if (mMediaPlayer == null) {
            try {
                mMediaPlayer = new MediaPlayer();
                AssetFileDescriptor afd = mContext.getResources().openRawResourceFd(data.getRawId());
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mMediaPlayer.setOnCompletionListener(mOnCompletionListener);
                mMediaPlayer.setOnErrorListener(onErrorListener);
                mMediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                mMediaPlayer.setOnPreparedListener(mp -> {
                    try {
                        mMediaPlayer.start();
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                });
                mMediaPlayer.prepare();
            } catch (Throwable e) {
                config.getLogger().error("MediaPlay: play fail " + e.getMessage());
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
        if (mPlayListener != null) {
            mPlayListener.onError(error);
        }
    }

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

    private final MediaPlayer.OnErrorListener onErrorListener = new MediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            stop();
            MediaAudioPlayer.this.onError("监听异常"+ what + ", extra: " + extra);
            //如果播放异常，则先回调异常，然后再完成。
            onCompleted();
            return true;
        }
    };
}

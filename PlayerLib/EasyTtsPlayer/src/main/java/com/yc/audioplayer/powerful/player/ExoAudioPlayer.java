package com.yc.audioplayer.powerful.player;

import android.app.Application;
import android.content.Context;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.util.Util;
import com.yc.audioplayer.powerful.bean.AudioPlayData;
import com.yc.audioplayer.powerful.bean.TtsPlayerConfig;
import com.yc.audioplayer.powerful.inter.InterPlayListener;
import com.yc.audioplayer.powerful.service.AudioService;
import com.yc.audioplayer.powerful.inter.AbstractAudioWrapper;


/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     GitHub : <a href="https://github.com/yangchong211/YCVideoPlayer">...</a>
 *     time  : 2018/8/6
 *     desc  : 音频播放player，使用谷歌exo
 *     revise:
 * </pre>
 */
public class ExoAudioPlayer extends AbstractAudioWrapper {

    private InterPlayListener mPlayListener;
    private Context mContext;
    private SimpleExoPlayer mMediaPlayer;
    protected MediaSource mMediaSource;
    private boolean mPause = false;


    public ExoAudioPlayer() {

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
     * 播放网络tts资源
     */
    @Override
    public void play(AudioPlayData data) {
        TtsPlayerConfig config = AudioService.getInstance().getConfig();
        config.getLogger().log("MediaPlay: play resourceId is" + data.getTts());
        String tts = data.getTts();
        if (tts==null || tts.length()==0 || !tts.startsWith("http")) {
            return;
        }
        if (mMediaPlayer == null) {
            mMediaPlayer = new SimpleExoPlayer.Builder(mContext).setLooper(Util.getLooper()).build();
        }
        mMediaSource = ExoMediaSourceHelper.getInstance(mContext).getMediaSource(tts);
        mMediaPlayer.prepare(mMediaSource);
        mMediaPlayer.setPlayWhenReady(true);
        mMediaPlayer.addListener(mEventListener);
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
            mMediaPlayer.removeListener(mEventListener);
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    @Override
    public void pause() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.setPlayWhenReady(false);
            mPause = true;
        }
    }

    @Override
    public void resumeSpeaking() {
        if (mMediaPlayer != null && mPause) {
            mMediaPlayer.setPlayWhenReady(true);
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
     * event事件监听listener
     */
    private final Player.DefaultEventListener mEventListener = new Player.DefaultEventListener() {
        @Override
        public void onPlayerError(ExoPlaybackException error) {
            super.onPlayerError(error);
            stop();
            onError(error.getMessage());
            //如果播放异常，则先回调异常，然后再完成。
            onCompleted();
        }

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            super.onPlayerStateChanged(playWhenReady, playbackState);
            switch (playbackState) {
                //最开始调用的状态
                case Player.STATE_IDLE:
                    break;
                //开始缓充
                case Player.STATE_BUFFERING:
                    break;
                //开始播放
                case Player.STATE_READY:
                    break;
                //播放器已经播放完了媒体
                case Player.STATE_ENDED:
                    if (mMediaPlayer != null) {
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
                    break;
                default:
                    break;
            }
        }
    };
}

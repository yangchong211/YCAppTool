package com.yc.kernel.impl.exo;

import android.app.Application;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.os.Handler;
import android.view.Surface;
import android.view.SurfaceHolder;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.analytics.AnalyticsCollector;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MediaSourceEventListener;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.util.Clock;
import com.google.android.exoplayer2.util.Util;
import com.yc.easyexecutor.DelegateTaskExecutor;
import com.yc.kernel.inter.AbstractVideoPlayer;
import com.yc.kernel.inter.VideoPlayerListener;
import com.yc.kernel.utils.PlayerConstant;
import com.yc.toolutils.AppLogUtils;

import java.util.Map;

import static com.google.android.exoplayer2.ExoPlaybackException.TYPE_SOURCE;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/11/9
 *     desc  : exo视频播放器实现类
 *     revise:
 * </pre>
 */
public class ExoMediaPlayer extends AbstractVideoPlayer {

    protected Context mAppContext;
    protected SimpleExoPlayer mInternalPlayer;
    protected MediaSource mMediaSource;
    protected ExoMediaSourceHelper mMediaSourceHelper;
    private PlaybackParameters mSpeedPlaybackParameters;
    private int mLastReportedPlaybackState = Player.STATE_IDLE;
    private boolean mLastReportedPlayWhenReady = false;
    private boolean mIsPreparing;
    private boolean mIsBuffering;
    private DefaultAnalyticsListener mAnalyticsListener;
    private LoadControl mLoadControl;
    private RenderersFactory mRenderersFactory;
    private TrackSelector mTrackSelector;

    /**
     * 创建对象操作
     *
     * @param context 上下文
     */
    public ExoMediaPlayer(Context context) {
        if (context instanceof Application) {
            mAppContext = context;
        } else {
            mAppContext = context.getApplicationContext();
        }
        mMediaSourceHelper = ExoMediaSourceHelper.getInstance(context);
    }

    /**
     * 初始化谷歌播放器
     */
    @Override
    public void initPlayer() {
        // 创建带宽，默认的
        DefaultBandwidthMeter bandwidthMeter = DefaultBandwidthMeter.getSingletonInstance(mAppContext);
        // 创建轨道选择器实例
        if (mTrackSelector == null) {
            //DefaultTrackSelector TrackSelector是一种适用于大多数用例的灵活方式。
            mTrackSelector = new DefaultTrackSelector(mAppContext);
        }
        if (mRenderersFactory == null) {
            mRenderersFactory = new DefaultRenderersFactory(mAppContext);
        }
        if (mLoadControl == null) {
            mLoadControl = new DefaultLoadControl();
        }
        AnalyticsCollector analyticsCollector = new AnalyticsCollector(Clock.DEFAULT);
        boolean useLazyPreparation = true;
        //创建exo播放器
        mInternalPlayer = new SimpleExoPlayer.Builder(
                mAppContext,
                mRenderersFactory,
                mTrackSelector,
                mLoadControl,
                bandwidthMeter,
                Util.getLooper(),
                analyticsCollector,
                useLazyPreparation,
                Clock.DEFAULT
        )
                .build();
        //设置配置
        setOptions();
        //播放器日志
        initListener();
    }

    /**
     * exo视频播放器监听listener
     */
    private void initListener() {
        if (AppLogUtils.isShowLog() && mTrackSelector instanceof MappingTrackSelector) {
            //设置使用分析监听器
            mAnalyticsListener = new DefaultAnalyticsListener(
                    (MappingTrackSelector) mTrackSelector);
            //提供的额外日志有助于了解播放器正在做什么，以及调试播放问题。
            mInternalPlayer.addAnalyticsListener(mAnalyticsListener);
        }
        //状态变化和播放错误等事件会报告给已注册的 Player.Listener实例
        //设置视频播放器监听
        mInternalPlayer.addListener(mEventListener);
        //设置视频video监听
        //onVideoSizeChanged        视频size发生了变化
        //onSurfaceSizeChanged      视频surface变化
        //onRenderedFirstFrame      视频第一帧
        mInternalPlayer.addVideoListener(mVideoListener);
    }

    /**
     * 设置自定义轨道选择器，目前使用默认的
     *
     * @param trackSelector 轨道选择器
     */
    public void setTrackSelector(TrackSelector trackSelector) {
        mTrackSelector = trackSelector;
    }

    public void setRenderersFactory(RenderersFactory renderersFactory) {
        mRenderersFactory = renderersFactory;
    }

    public void setLoadControl(LoadControl loadControl) {
        mLoadControl = loadControl;
    }

    /**
     * 设置播放地址
     *
     * @param path    播放地址
     * @param headers 播放地址请求头
     */
    @Override
    public void setDataSource(String path, Map<String, String> headers) {
        // 设置dataSource
        if (path == null || path.length() == 0) {
            if (mPlayerEventListener != null) {
                mPlayerEventListener.onInfo(PlayerConstant.MEDIA_INFO_URL_NULL, 0);
            }
            return;
        }
        //获取媒体来源
        mMediaSource = mMediaSourceHelper.getMediaSource(path, headers);
    }

    @Override
    public void setDataSource(AssetFileDescriptor fd) {
        //no support
    }

    /**
     * 准备开始播放（异步）
     */
    @Override
    public void prepareAsync() {
        if (mInternalPlayer == null) {
            return;
        }
        if (mMediaSource == null) {
            return;
        }
        if (mSpeedPlaybackParameters != null) {
            mInternalPlayer.setPlaybackParameters(mSpeedPlaybackParameters);
        }
        mIsPreparing = true;
        mMediaSource.addEventListener(new Handler(), mMediaSourceEventListener);
        //准备播放
        mInternalPlayer.prepare(mMediaSource);
    }

    /**
     * 播放
     */
    @Override
    public void start() {
        if (mInternalPlayer == null) {
            return;
        }
        mInternalPlayer.setPlayWhenReady(true);
    }

    /**
     * 暂停
     */
    @Override
    public void pause() {
        if (mInternalPlayer == null) {
            return;
        }
        mInternalPlayer.setPlayWhenReady(false);
    }

    /**
     * 停止
     */
    @Override
    public void stop() {
        if (mInternalPlayer == null) {
            return;
        }
        mInternalPlayer.stop();
    }

    private final MediaSourceEventListener mMediaSourceEventListener = new MediaSourceEventListener() {
        @Override
        public void onReadingStarted(int windowIndex, MediaSource.MediaPeriodId mediaPeriodId) {
            if (mPlayerEventListener != null && mIsPreparing) {
                mPlayerEventListener.onPrepared();
            }
        }
    };

    /**
     * 重置播放器
     */
    @Override
    public void reset() {
        if (mInternalPlayer != null) {
            mInternalPlayer.stop(true);
            mInternalPlayer.setVideoSurface(null);
            mIsPreparing = false;
            mIsBuffering = false;
            mLastReportedPlaybackState = Player.STATE_IDLE;
            mLastReportedPlayWhenReady = false;
        }
    }

    /**
     * 是否正在播放
     */
    @Override
    public boolean isPlaying() {
        if (mInternalPlayer == null) {
            return false;
        }
        int state = mInternalPlayer.getPlaybackState();
        switch (state) {
            case Player.STATE_BUFFERING:
            case Player.STATE_READY:
                return mInternalPlayer.getPlayWhenReady();
            case Player.STATE_IDLE:
            case Player.STATE_ENDED:
            default:
                return false;
        }
    }

    /**
     * 调整进度
     */
    @Override
    public void seekTo(long time) {
        if (mInternalPlayer == null) {
            return;
        }
        mInternalPlayer.seekTo(time);
    }

    /**
     * 释放播放器
     */
    @Override
    public void release() {
        if (mInternalPlayer != null) {
            mInternalPlayer.removeListener(mEventListener);
            mInternalPlayer.removeVideoListener(mVideoListener);
            mInternalPlayer.removeAnalyticsListener(mAnalyticsListener);
            final SimpleExoPlayer player = mInternalPlayer;
            mInternalPlayer = null;
            DelegateTaskExecutor.getInstance().executeOnCpu(new Runnable() {
                @Override
                public void run() {
                    //异步释放，防止卡顿
                    player.release();
                }
            });
        }

        mIsPreparing = false;
        mIsBuffering = false;
        mLastReportedPlaybackState = Player.STATE_IDLE;
        mLastReportedPlayWhenReady = false;
        mSpeedPlaybackParameters = null;
    }

    /**
     * 获取当前播放的位置
     */
    @Override
    public long getCurrentPosition() {
        if (mInternalPlayer == null) {
            return 0;
        }
        return mInternalPlayer.getCurrentPosition();
    }

    /**
     * 获取视频总时长
     */
    @Override
    public long getDuration() {
        if (mInternalPlayer == null) {
            return 0;
        }
        return mInternalPlayer.getDuration();
    }

    /**
     * 获取缓冲百分比
     */
    @Override
    public int getBufferedPercentage() {
        return mInternalPlayer == null ? 0 : mInternalPlayer.getBufferedPercentage();
    }

    /**
     * 设置渲染视频的View,主要用于SurfaceView
     */
    @Override
    public void setSurface(Surface surface) {
        if (surface != null) {
            try {
                if (mInternalPlayer != null) {
                    mInternalPlayer.setVideoSurface(surface);
                }
            } catch (Exception e) {
                mPlayerEventListener.onError(PlayerConstant.ErrorType.TYPE_UNEXPECTED, e.getMessage());
            }
        }
    }

    @Override
    public void setDisplay(SurfaceHolder holder) {
        if (holder == null) {
            setSurface(null);
        } else {
            setSurface(holder.getSurface());
        }
    }

    /**
     * 设置音量
     */
    @Override
    public void setVolume(float leftVolume, float rightVolume) {
        if (mInternalPlayer != null) {
            mInternalPlayer.setVolume((leftVolume + rightVolume) / 2);
        }
    }

    /**
     * 设置是否循环播放
     * Player.REPEAT_MODE_OFF：播放列表不重复，播放列表中Player.STATE_ENDED的最后一个项目播放完后播放器将切换到。
     * Player.REPEAT_MODE_ONE：当前项目在无限循环中重复。类似的方法Player.seekToNextMediaItem将忽略这一点并寻找列表中的下一个项目，然后将在无限循环中重复。
     * Player.REPEAT_MODE_ALL：整个播放列表在无限循环中重复。
     */
    @Override
    public void setLooping(boolean isLooping) {
        if (mInternalPlayer != null) {
            mInternalPlayer.setRepeatMode(isLooping ? Player.REPEAT_MODE_ALL : Player.REPEAT_MODE_OFF);
        }
    }

    @Override
    public void setOptions() {
        //准备好就开始播放
        mInternalPlayer.setPlayWhenReady(true);
    }

    /**
     * 设置播放速度
     */
    @Override
    public void setSpeed(float speed) {
        PlaybackParameters playbackParameters = new PlaybackParameters(speed);
        mSpeedPlaybackParameters = playbackParameters;
        if (mInternalPlayer != null) {
            mInternalPlayer.setPlaybackParameters(playbackParameters);
        }
    }

    /**
     * 获取播放速度
     */
    @Override
    public float getSpeed() {
        if (mSpeedPlaybackParameters != null) {
            return mSpeedPlaybackParameters.speed;
        }
        return 1f;
    }

    /**
     * 获取当前缓冲的网速
     */
    @Override
    public long getTcpSpeed() {
        // no support
        return 0;
    }

    @Override
    public void setPlayerEventListener(VideoPlayerListener playerEventListener) {
        super.setPlayerEventListener(playerEventListener);
    }

    /**
     * event事件监听listener
     */
    private final DefaultEventListener mEventListener = new DefaultEventListener() {

        @Override
        public void onPlayerError(ExoPlaybackException error) {
            super.onPlayerError(error);
            if (mPlayerEventListener != null) {
                int type = error.type;
                if (type == TYPE_SOURCE) {
                    //错误的链接
                    mPlayerEventListener.onError(PlayerConstant.ErrorType.TYPE_SOURCE, error.getMessage());
                } else if (
                        //渲染器异常
                        type == ExoPlaybackException.TYPE_RENDERER
                                //意想不到
                        || type == ExoPlaybackException.TYPE_UNEXPECTED
                                //远程
                        || type == ExoPlaybackException.TYPE_REMOTE
                                //内存OOM
                        || type == ExoPlaybackException.TYPE_OUT_OF_MEMORY) {
                    mPlayerEventListener.onError(PlayerConstant.ErrorType.TYPE_UNEXPECTED, error.getMessage());
                }
            }
        }

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            super.onPlayerStateChanged(playWhenReady, playbackState);
            if (mPlayerEventListener == null) {
                return;
            }
            if (mIsPreparing) {
                return;
            }
            if (mLastReportedPlayWhenReady != playWhenReady || mLastReportedPlaybackState != playbackState) {
                switch (playbackState) {
                    //最开始调用的状态
                    case Player.STATE_IDLE:
                        break;
                    //开始缓充
                    case Player.STATE_BUFFERING:
                        mPlayerEventListener.onInfo(PlayerConstant.MEDIA_INFO_BUFFERING_START, getBufferedPercentage());
                        mIsBuffering = true;
                        break;
                    //开始播放
                    case Player.STATE_READY:
                        if (mIsBuffering) {
                            mPlayerEventListener.onInfo(PlayerConstant.MEDIA_INFO_BUFFERING_END, getBufferedPercentage());
                            mIsBuffering = false;
                        }
                        break;
                    //播放器已经播放完了媒体
                    case Player.STATE_ENDED:
                        mPlayerEventListener.onCompletion();
                        break;
                    default:
                        break;
                }
                mLastReportedPlaybackState = playbackState;
                mLastReportedPlayWhenReady = playWhenReady;
            }
        }
    };

    private final DefaultVideoListener mVideoListener = new DefaultVideoListener(){
        @Override
        public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
            super.onVideoSizeChanged(width, height, unappliedRotationDegrees, pixelWidthHeightRatio);
            if (mPlayerEventListener != null) {
                mPlayerEventListener.onVideoSizeChanged(width, height);
                if (unappliedRotationDegrees > 0) {
                    mPlayerEventListener.onInfo(PlayerConstant.MEDIA_INFO_VIDEO_ROTATION_CHANGED, unappliedRotationDegrees);
                }
            }
        }

        @Override
        public void onRenderedFirstFrame() {
            super.onRenderedFirstFrame();
            if (mPlayerEventListener != null && mIsPreparing) {
                mPlayerEventListener.onInfo(PlayerConstant.MEDIA_INFO_VIDEO_RENDERING_START, 0);
                mIsPreparing = false;
            }
        }
    };

}

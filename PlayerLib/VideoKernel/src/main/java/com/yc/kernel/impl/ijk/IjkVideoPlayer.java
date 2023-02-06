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
package com.yc.kernel.impl.ijk;

import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Surface;
import android.view.SurfaceHolder;

import com.yc.easyexecutor.DelegateTaskExecutor;
import com.yc.kernel.inter.AbstractVideoPlayer;
import com.yc.kernel.inter.VideoPlayerListener;
import com.yc.kernel.utils.PlayerConstant;
import com.yc.toolutils.AppLogUtils;

import java.util.Map;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import tv.danmaku.ijk.media.player.IjkTimedText;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/11/9
 *     desc  : ijk视频播放器实现类
 *     revise:
 * </pre>
 */
public class IjkVideoPlayer extends AbstractVideoPlayer {

    protected IjkMediaPlayer mMediaPlayer;
    private int mBufferedPercent;
    private final Context mAppContext;

    public IjkVideoPlayer(Context context) {
        if (context instanceof Application){
            mAppContext = context;
        } else {
            mAppContext = context.getApplicationContext();
        }
    }

    @Override
    public void initPlayer() {
        mMediaPlayer = new IjkMediaPlayer();
        //native日志
        IjkMediaPlayer.native_setLogLevel(AppLogUtils.isShowLog()
                ? IjkMediaPlayer.IJK_LOG_INFO : IjkMediaPlayer.IJK_LOG_SILENT);
        setOptions();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        initListener();
    }

    @Override
    public void setOptions() {
        /*int player = IjkMediaPlayer.OPT_CATEGORY_PLAYER;
        int codec = IjkMediaPlayer.OPT_CATEGORY_CODEC;
        int format = IjkMediaPlayer.OPT_CATEGORY_FORMAT;

        //设置ijkPlayer播放器的硬件解码相关参数
        //设置播放前的最大探测时间
        mMediaPlayer.setOption(format, "analyzemaxduration", 100L);
        //设置播放前的探测时间 1,达到首屏秒开效果
        mMediaPlayer.setOption(format, "analyzeduration", 1L);
        //播放前的探测Size，默认是1M, 改小一点会出画面更快
        mMediaPlayer.setOption(format, "probesize", 10240L);
        //设置是否开启变调isModifyTone?0:1
        mMediaPlayer.setOption(player,"soundtouch",0);
        //每处理一个packet之后刷新io上下文
        mMediaPlayer.setOption(format, "flush_packets", 1L);
        //是否开启预缓冲，一般直播项目会开启，达到秒开的效果，不过带来了播放丢帧卡顿的体验
        mMediaPlayer.setOption(player, "packet-buffering", 0L);
        //播放重连次数
        mMediaPlayer.setOption(player, "reconnect", 5);
        //最大缓冲大小,单位kb
        mMediaPlayer.setOption(player, "max-buffer-size", 10240L);
        //跳帧处理,放CPU处理较慢时，进行跳帧处理，保证播放流程，画面和声音同步
        mMediaPlayer.setOption(player, "framedrop", 1L);
        //最大fps
        mMediaPlayer.setOption(player, "max-fps", 30L);
        //SeekTo设置优化
        mMediaPlayer.setOption(player, "enable-accurate-seek", 1L);
        mMediaPlayer.setOption(player, "opensles", 0);
        mMediaPlayer.setOption(player, "overlay-format",
                IjkMediaPlayer.SDL_FCC_RV32);
        mMediaPlayer.setOption(player, "framedrop", 1);
        mMediaPlayer.setOption(player, "start-on-prepared", 0);
        mMediaPlayer.setOption(format, "http-detect-range-support", 0);
        //设置是否开启环路过滤: 0开启，画面质量高，解码开销大，48关闭，画面质量差点，解码开销小
        mMediaPlayer.setOption(codec, "skip_loop_filter", 48);

        //jkPlayer支持硬解码和软解码。
        //软解码时不会旋转视频角度这时需要你通过onInfo的what == IMediaPlayer.MEDIA_INFO_VIDEO_ROTATION_CHANGED去获取角度，自己旋转画面。
        //或者开启硬解硬解码，不过硬解码容易造成黑屏无声（硬件兼容问题），下面是设置硬解码相关的代码
        mMediaPlayer.setOption(player, "mediacodec", 0);
        mMediaPlayer.setOption(player, "mediacodec-auto-rotate", 1);
        mMediaPlayer.setOption(player, "mediacodec-handle-resolution-change", 1);*/
    }

    /**
     * ijk视频播放器监听listener
     */
    private void initListener() {
        // 设置监听，可以查看ijk中的IMediaPlayer源码监听事件
        // 设置视频错误监听器
        mMediaPlayer.setOnErrorListener(onErrorListener);
        // 设置视频播放完成监听事件
        mMediaPlayer.setOnCompletionListener(onCompletionListener);
        // 设置视频信息监听器
        mMediaPlayer.setOnInfoListener(onInfoListener);
        // 设置视频缓冲更新监听事件
        mMediaPlayer.setOnBufferingUpdateListener(onBufferingUpdateListener);
        // 设置准备视频播放监听事件
        mMediaPlayer.setOnPreparedListener(onPreparedListener);
        // 设置视频大小更改监听器
        mMediaPlayer.setOnVideoSizeChangedListener(onVideoSizeChangedListener);
        // 设置视频seek完成监听事件
        mMediaPlayer.setOnSeekCompleteListener(onSeekCompleteListener);
        // 设置时间文本监听器
        mMediaPlayer.setOnTimedTextListener(onTimedTextListener);
        mMediaPlayer.setOnNativeInvokeListener(new IjkMediaPlayer.OnNativeInvokeListener() {
            @Override
            public boolean onNativeInvoke(int i, Bundle bundle) {
                return true;
            }
        });
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
        if(path==null || path.length()==0){
            if (mPlayerEventListener!=null){
                mPlayerEventListener.onInfo(PlayerConstant.MEDIA_INFO_URL_NULL, 0);
            }
            return;
        }
        try {
            //解析path
            Uri uri = Uri.parse(path);
            String scheme = uri.getScheme();
            if (ContentResolver.SCHEME_ANDROID_RESOURCE.equals(scheme)) {
                //处理本地资源
                RawDataSourceProvider rawDataSourceProvider = RawDataSourceProvider.create(mAppContext, uri);
                if (rawDataSourceProvider != null){
                    mMediaPlayer.setDataSource(rawDataSourceProvider);
                } else {
                    mPlayerEventListener.onError(PlayerConstant.ErrorType.TYPE_UNEXPECTED,"path no find");
                }
            } else {
                //处理UA问题
                if (headers != null) {
                    String userAgent = headers.get("User-Agent");
                    if (!TextUtils.isEmpty(userAgent)) {
                        mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "user_agent", userAgent);
                    }
                }
                mMediaPlayer.setDataSource(mAppContext, uri, headers);
            }
        } catch (Exception e) {
            mPlayerEventListener.onError(PlayerConstant.ErrorType.TYPE_PARSE,e.getMessage());
        }
    }

    /**
     * 用于播放raw和asset里面的视频文件
     */
    @Override
    public void setDataSource(AssetFileDescriptor fd) {
        try {
            mMediaPlayer.setDataSource(new RawDataSourceProvider(fd));
        } catch (Exception e) {
            mPlayerEventListener.onError(PlayerConstant.ErrorType.TYPE_UNEXPECTED,e.getMessage());
        }
    }

    /**
     * 设置渲染视频的View,主要用于TextureView
     * @param surface                           surface
     */
    @Override
    public void setSurface(Surface surface) {
        if (surface!=null){
            try {
                mMediaPlayer.setSurface(surface);
            } catch (Exception e) {
                mPlayerEventListener.onError(PlayerConstant.ErrorType.TYPE_UNEXPECTED,e.getMessage());
            }
        }
    }

    /**
     * 准备开始播放（异步）
     */
    @Override
    public void prepareAsync() {
        try {
            mMediaPlayer.prepareAsync();
        } catch (IllegalStateException e) {
            mPlayerEventListener.onError(PlayerConstant.ErrorType.TYPE_UNEXPECTED,e.getMessage());
        }
    }

    /**
     * 暂停
     */
    @Override
    public void pause() {
        try {
            mMediaPlayer.pause();
        } catch (IllegalStateException e) {
            mPlayerEventListener.onError(PlayerConstant.ErrorType.TYPE_UNEXPECTED,e.getMessage());
        }
    }

    /**
     * 播放
     */
    @Override
    public void start() {
        try {
            mMediaPlayer.start();
        } catch (IllegalStateException e) {
            mPlayerEventListener.onError(PlayerConstant.ErrorType.TYPE_UNEXPECTED,e.getMessage());
        }
    }

    /**
     * 停止
     */
    @Override
    public void stop() {
        try {
            mMediaPlayer.stop();
        } catch (IllegalStateException e) {
            mPlayerEventListener.onError(PlayerConstant.ErrorType.TYPE_UNEXPECTED,e.getMessage());
        }
    }

    /**
     * 重置播放器
     */
    @Override
    public void reset() {
        mMediaPlayer.reset();
        mMediaPlayer.setOnVideoSizeChangedListener(onVideoSizeChangedListener);
        setOptions();
    }

    /**
     * 是否正在播放
     */
    @Override
    public boolean isPlaying() {
        return mMediaPlayer.isPlaying();
    }


    /**
     * 调整进度
     */
    @Override
    public void seekTo(long time) {
        try {
            mMediaPlayer.seekTo((int) time);
        } catch (IllegalStateException e) {
            mPlayerEventListener.onError(PlayerConstant.ErrorType.TYPE_UNEXPECTED,e.getMessage());
        }
    }

    /**
     * 释放播放器
     */
    @Override
    public void release() {
        mMediaPlayer.setOnErrorListener(null);
        mMediaPlayer.setOnCompletionListener(null);
        mMediaPlayer.setOnInfoListener(null);
        mMediaPlayer.setOnBufferingUpdateListener(null);
        mMediaPlayer.setOnPreparedListener(null);
        mMediaPlayer.setOnVideoSizeChangedListener(null);
        DelegateTaskExecutor.getInstance().executeOnCpu(new Runnable() {
            @Override
            public void run() {
                try {
                    //异步释放，防止卡顿
                    mMediaPlayer.release();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 获取当前播放的位置
     */
    @Override
    public long getCurrentPosition() {
        return mMediaPlayer.getCurrentPosition();
    }

    /**
     * 获取视频总时长
     */
    @Override
    public long getDuration() {
        return mMediaPlayer.getDuration();
    }

    /**
     * 获取缓冲百分比
     */
    @Override
    public int getBufferedPercentage() {
        return mBufferedPercent;
    }

    /**
     * 设置渲染视频的View,主要用于SurfaceView
     */
    @Override
    public void setDisplay(SurfaceHolder holder) {
        mMediaPlayer.setDisplay(holder);
    }

    /**
     * 设置音量
     */
    @Override
    public void setVolume(float v1, float v2) {
        mMediaPlayer.setVolume(v1, v2);
    }

    /**
     * 设置是否循环播放
     */
    @Override
    public void setLooping(boolean isLooping) {
        mMediaPlayer.setLooping(isLooping);
    }

    /**
     * 设置播放速度
     */
    @Override
    public void setSpeed(float speed) {
        mMediaPlayer.setSpeed(speed);
    }

    /**
     * 获取播放速度
     */
    @Override
    public float getSpeed() {
        return mMediaPlayer.getSpeed(0);
    }

    /**
     * 获取当前缓冲的网速
     */
    @Override
    public long getTcpSpeed() {
        return mMediaPlayer.getTcpSpeed();
    }

    /**
     * 设置视频错误监听器
     * int MEDIA_INFO_VIDEO_RENDERING_START = 3;//视频准备渲染
     * int MEDIA_INFO_BUFFERING_START = 701;//开始缓冲
     * int MEDIA_INFO_BUFFERING_END = 702;//缓冲结束
     * int MEDIA_INFO_VIDEO_ROTATION_CHANGED = 10001;//视频选择信息
     * int MEDIA_ERROR_SERVER_DIED = 100;//视频中断，一般是视频源异常或者不支持的视频类型。
     * int MEDIA_ERROR_IJK_PLAYER = -10000,//一般是视频源有问题或者数据格式不支持，比如音频不是AAC之类的
     * int MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK = 200;//数据错误没有有效的回收
     */
    private final IMediaPlayer.OnErrorListener onErrorListener = new IMediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(IMediaPlayer iMediaPlayer, int framework_err, int impl_err) {
            mPlayerEventListener.onError(PlayerConstant.ErrorType.TYPE_UNEXPECTED,"监听异常"+ framework_err + ", extra: " + impl_err);
            AppLogUtils.d("IjkVideoPlayer----listener---------onError ——> STATE_ERROR ———— what：" + framework_err + ", extra: " + impl_err);
            return true;
        }
    };

    /**
     * 设置视频播放完成监听事件
     */
    private final IMediaPlayer.OnCompletionListener onCompletionListener = new IMediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(IMediaPlayer iMediaPlayer) {
            mPlayerEventListener.onCompletion();
            AppLogUtils.d("IjkVideoPlayer----listener---------onCompletion ——> STATE_COMPLETED");
        }
    };


    /**
     * 设置视频信息监听器
     */
    private final IMediaPlayer.OnInfoListener onInfoListener = new IMediaPlayer.OnInfoListener() {
        @Override
        public boolean onInfo(IMediaPlayer iMediaPlayer, int what, int extra) {
            mPlayerEventListener.onInfo(what, extra);
            AppLogUtils.d("IjkVideoPlayer----listener---------onInfo ——> ———— what：" + what + ", extra: " + extra);
            return true;
        }
    };

    /**
     * 设置视频缓冲更新监听事件
     */
    private final IMediaPlayer.OnBufferingUpdateListener onBufferingUpdateListener = new IMediaPlayer.OnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(IMediaPlayer iMediaPlayer, int percent) {
            mBufferedPercent = percent;
        }
    };


    /**
     * 设置准备视频播放监听事件
     */
    private final IMediaPlayer.OnPreparedListener onPreparedListener = new IMediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(IMediaPlayer iMediaPlayer) {
            mPlayerEventListener.onPrepared();
            AppLogUtils.d("IjkVideoPlayer----listener---------onPrepared ——> STATE_PREPARED");
        }
    };

    /**
     * 设置视频大小更改监听器
     */
    private final IMediaPlayer.OnVideoSizeChangedListener onVideoSizeChangedListener = new IMediaPlayer.OnVideoSizeChangedListener() {
        @Override
        public void onVideoSizeChanged(IMediaPlayer iMediaPlayer, int width, int height,
                                       int sar_num, int sar_den) {
            int videoWidth = iMediaPlayer.getVideoWidth();
            int videoHeight = iMediaPlayer.getVideoHeight();
            if (videoWidth != 0 && videoHeight != 0) {
                mPlayerEventListener.onVideoSizeChanged(videoWidth, videoHeight);
            }
            AppLogUtils.d("IjkVideoPlayer----listener---------onVideoSizeChanged ——> WIDTH：" + width + "， HEIGHT：" + height);
        }
    };

    /**
     * 设置时间文本监听器
     */
    private final IMediaPlayer.OnTimedTextListener onTimedTextListener = new IMediaPlayer.OnTimedTextListener() {
        @Override
        public void onTimedText(IMediaPlayer iMediaPlayer, IjkTimedText ijkTimedText) {

        }
    };

    /**
     * 设置视频seek完成监听事件
     */
    private final IMediaPlayer.OnSeekCompleteListener onSeekCompleteListener = new IMediaPlayer.OnSeekCompleteListener() {
        @Override
        public void onSeekComplete(IMediaPlayer iMediaPlayer) {

        }
    };

    @Override
    public void setPlayerEventListener(VideoPlayerListener playerEventListener) {
        super.setPlayerEventListener(playerEventListener);
    }

}

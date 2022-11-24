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
package com.yc.video.config;


import android.content.Context;

import androidx.annotation.Nullable;

import com.yc.kernel.factory.PlayerFactory;
import com.yc.kernel.factory.MediaPlayerFactory;

import com.yc.toolutils.AppLogUtils;
import com.yc.video.player.ProgressManager;
import com.yc.videosurface.SurfaceFactory;
import com.yc.videosurface.TextureViewFactory;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/11/9
 *     desc  : 播放器全局配置
 *     revise: 构建者设计模式
 * </pre>
 */
public final class VideoPlayerConfig {

    public static Builder newBuilder() {
        return new Builder();
    }

    public final static class Builder {

        private Context mContext;
        /**
         * 默认是关闭日志的
         */
        private boolean mIsEnableLog = false;
        /**
         * 在移动环境下调用start()后是否继续播放，默认不继续播放
         */
        private boolean mPlayOnMobileNetwork;
        /**
         * 是否监听设备方向来切换全屏/半屏， 默认不开启
         */
        private boolean mEnableOrientation;
        /**
         * 是否开启AudioFocus监听， 默认开启
         */
        private boolean mEnableAudioFocus = true;
        /**
         * 设置进度管理器，用于保存播放进度
         */
        private ProgressManager mProgressManager;
        /**
         * 自定义播放核心
         */
        private PlayerFactory mPlayerFactory;
        /**
         * 自定义视频全局埋点事件
         */
        private BuriedPointEvent mBuriedPointEvent;
        /**
         * 设置视频比例
         */
        private int mScreenScaleType;
        /**
         * 自定义RenderView
         */
        private SurfaceFactory mRenderViewFactory;
        /**
         * 是否适配刘海屏，默认适配
         */
        private boolean mAdaptCutout = true;
        /**
         * 是否设置倒计时n秒吐司
         */
        private boolean mIsShowToast = false;
        /**
         * 倒计时n秒时间
         */
        private long mShowToastTime = 5;

        /**
         * 是否监听设备方向来切换全屏/半屏， 默认不开启
         */
        public Builder setContext(Context context) {
            mContext = context;
            return this;
        }

        /**
         * 是否监听设备方向来切换全屏/半屏， 默认不开启
         */
        public Builder setEnableOrientation(boolean enableOrientation) {
            mEnableOrientation = enableOrientation;
            return this;
        }

        /**
         * 在移动环境下调用start()后是否继续播放，默认不继续播放
         */
        public Builder setPlayOnMobileNetwork(boolean playOnMobileNetwork) {
            mPlayOnMobileNetwork = playOnMobileNetwork;
            return this;
        }

        /**
         * 是否开启AudioFocus监听， 默认开启
         */
        public Builder setEnableAudioFocus(boolean enableAudioFocus) {
            mEnableAudioFocus = enableAudioFocus;
            return this;
        }

        /**
         * 设置进度管理器，用于保存播放进度
         */
        public Builder setProgressManager(@Nullable ProgressManager progressManager) {
            mProgressManager = progressManager;
            return this;
        }

        /**
         * 是否打印日志
         */
        public Builder setLogEnabled(boolean enableLog) {
            mIsEnableLog = enableLog;
            return this;
        }

        /**
         * 自定义播放核心
         */
        public Builder setPlayerFactory(PlayerFactory playerFactory) {
            mPlayerFactory = playerFactory;
            return this;
        }

        /**
         * 自定义视频全局埋点事件
         */
        public Builder setBuriedPointEvent(BuriedPointEvent buriedPointEvent) {
            mBuriedPointEvent = buriedPointEvent;
            return this;
        }

        /**
         * 设置视频比例
         */
        public Builder setScreenScaleType(int screenScaleType) {
            mScreenScaleType = screenScaleType;
            return this;
        }

        /**
         * 自定义RenderView
         */
        public Builder setRenderViewFactory(SurfaceFactory renderViewFactory) {
            mRenderViewFactory = renderViewFactory;
            return this;
        }

        /**
         * 是否适配刘海屏，默认适配
         */
        public Builder setAdaptCutout(boolean adaptCutout) {
            mAdaptCutout = adaptCutout;
            return this;
        }

        /**
         * 是否设置倒计时n秒吐司
         */
        public Builder setIsShowToast(boolean isShowToast) {
            mIsShowToast = isShowToast;
            return this;
        }

        /**
         * 倒计时n秒时间
         */
        public Builder setShowToastTime(long showToastTime) {
            mShowToastTime = showToastTime;
            return this;
        }

        public VideoPlayerConfig build() {
            //创建builder对象
            return new VideoPlayerConfig(this);
        }
    }

    public final Context mContext;
    public final boolean mPlayOnMobileNetwork;
    public final boolean mEnableOrientation;
    public final boolean mEnableAudioFocus;
    public final boolean mIsEnableLog;
    public final ProgressManager mProgressManager;
    public final PlayerFactory mPlayerFactory;
    public final BuriedPointEvent mBuriedPointEvent;
    public final int mScreenScaleType;
    public final SurfaceFactory mRenderViewFactory;
    public final boolean mAdaptCutout;
    public final boolean mIsShowToast ;
    public final long mShowToastTime;

    private VideoPlayerConfig(Builder builder) {
        mIsEnableLog = builder.mIsEnableLog;
        mEnableOrientation = builder.mEnableOrientation;
        mPlayOnMobileNetwork = builder.mPlayOnMobileNetwork;
        mEnableAudioFocus = builder.mEnableAudioFocus;
        mProgressManager = builder.mProgressManager;
        mScreenScaleType = builder.mScreenScaleType;
        if (builder.mPlayerFactory == null) {
            //默认为AndroidMediaPlayer
            mPlayerFactory = MediaPlayerFactory.create();
        } else {
            mPlayerFactory = builder.mPlayerFactory;
        }
        mBuriedPointEvent = builder.mBuriedPointEvent;
        if (builder.mRenderViewFactory == null) {
            //默认使用TextureView渲染视频
            mRenderViewFactory = TextureViewFactory.create();
        } else {
            mRenderViewFactory = builder.mRenderViewFactory;
        }
        mAdaptCutout = builder.mAdaptCutout;
        mContext = builder.mContext;
        mIsShowToast = builder.mIsShowToast;
        mShowToastTime = builder.mShowToastTime;
        AppLogUtils.setShowLog(mIsEnableLog);
    }


}

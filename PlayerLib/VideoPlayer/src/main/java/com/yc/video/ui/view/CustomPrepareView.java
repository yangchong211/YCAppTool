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
package com.yc.video.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.yc.video.config.ConstantKeys;
import com.yc.video.bridge.ControlWrapper;
import com.yc.video.player.VideoViewManager;

import com.yc.video.R;
import com.yc.video.inter.IControlView;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/11/9
 *     desc  : 预加载准备播放页面视图
 *     revise:
 * </pre>
 */
public class CustomPrepareView extends FrameLayout implements IControlView {

    private Context mContext;
    private ControlWrapper mControlWrapper;
    private ImageView mIvThumb;
    private ImageView mIvStartPlay;
    private ProgressBar mPbLoading;
    private FrameLayout mFlNetWarning;
    private TextView mTvMessage;
    private TextView mTvStart;

    public CustomPrepareView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public CustomPrepareView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomPrepareView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context){
        this.mContext = context;
        View view = LayoutInflater.from(getContext()).inflate(
                R.layout.custom_video_player_prepare, this, true);
        initFindViewById(view);
        initListener();
    }

    private void initFindViewById(View view) {
        mIvThumb = view.findViewById(R.id.iv_thumb);
        mIvStartPlay = view.findViewById(R.id.iv_start_play);
        mPbLoading = view.findViewById(R.id.pb_loading);
        mFlNetWarning = view.findViewById(R.id.fl_net_warning);
        mTvMessage = view.findViewById(R.id.tv_message);
        mTvStart = view.findViewById(R.id.tv_start);
    }

    private void initListener() {
        mTvStart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mFlNetWarning.setVisibility(GONE);
                VideoViewManager.instance().setPlayOnMobileNetwork(true);
                mControlWrapper.start();
            }
        });
    }

    /**
     * 设置点击此界面开始播放
     */
    public void setClickStart() {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mControlWrapper.start();
            }
        });
    }

    @Override
    public void attach(@NonNull ControlWrapper controlWrapper) {
        mControlWrapper = controlWrapper;
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void onVisibilityChanged(boolean isVisible, Animation anim) {

    }

    @Override
    public void onPlayStateChanged(int playState) {
        switch (playState) {
            case ConstantKeys.CurrentState.STATE_PREPARING:
                bringToFront();
                setVisibility(VISIBLE);
                mIvStartPlay.setVisibility(View.GONE);
                mFlNetWarning.setVisibility(GONE);
                mPbLoading.setVisibility(View.VISIBLE);
                break;
            case ConstantKeys.CurrentState.STATE_PLAYING:
            case ConstantKeys.CurrentState.STATE_PAUSED:
            case ConstantKeys.CurrentState.STATE_ERROR:
            case ConstantKeys.CurrentState.STATE_BUFFERING_PAUSED:
            case ConstantKeys.CurrentState.STATE_COMPLETED:
            case ConstantKeys.CurrentState.STATE_BUFFERING_PLAYING:
            case ConstantKeys.CurrentState.STATE_ONCE_LIVE:
                setVisibility(GONE);
                break;
            case ConstantKeys.CurrentState.STATE_IDLE:
                setVisibility(VISIBLE);
                bringToFront();
                mPbLoading.setVisibility(View.GONE);
                mFlNetWarning.setVisibility(GONE);
                mIvStartPlay.setVisibility(View.VISIBLE);
                mIvThumb.setVisibility(View.VISIBLE);
                break;
            case ConstantKeys.CurrentState.STATE_START_ABORT:
                setVisibility(VISIBLE);
                mFlNetWarning.setVisibility(VISIBLE);
                mFlNetWarning.bringToFront();
                break;
        }
    }

    @Override
    public void onPlayerStateChanged(int playerState) {

    }

    @Override
    public void setProgress(int duration, int position) {

    }

    @Override
    public void onLockStateChanged(boolean isLocked) {

    }

    public ImageView getThumb() {
        return mIvThumb;
    }
}

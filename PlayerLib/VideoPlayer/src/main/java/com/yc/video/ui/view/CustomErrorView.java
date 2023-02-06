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

import android.app.Activity;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.content.pm.ActivityInfo;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yc.video.config.ConstantKeys;
import com.yc.video.bridge.ControlWrapper;

import com.yc.video.R;
import com.yc.video.tool.PlayerUtils;
import com.yc.video.inter.IControlView;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/11/9
 *     desc  : 出错提示界面
 *     revise:
 * </pre>
 */
public class CustomErrorView extends LinearLayout implements IControlView, View.OnClickListener {

    private Context mContext;
    private float mDownX;
    private float mDownY;
    private TextView mTvMessage;
    private TextView mTvRetry;
    private ImageView mIvStopFullscreen;

    private ControlWrapper mControlWrapper;

    public CustomErrorView(Context context) {
        super(context);
        init(context);
    }

    public CustomErrorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomErrorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context){
        this.mContext = context;
        setVisibility(GONE);
        View view = LayoutInflater.from(getContext()).inflate(
                R.layout.custom_video_player_error, this, true);
        initFindViewById(view);
        initListener();
        setClickable(true);
    }

    private void initFindViewById(View view) {
        mTvMessage = view.findViewById(R.id.tv_message);
        mTvRetry = view.findViewById(R.id.tv_retry);
        mIvStopFullscreen = view.findViewById(R.id.iv_stop_fullscreen);
    }

    private void initListener() {
        mTvRetry.setOnClickListener(this);
        mIvStopFullscreen.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v == mTvRetry){
            setVisibility(GONE);
            mControlWrapper.replay(false);
        } else if (v == mIvStopFullscreen){
            //点击返回键
            if (mControlWrapper.isFullScreen()) {
                Activity activity = PlayerUtils.scanForActivity(mContext);
                if (activity != null && !activity.isFinishing()) {
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    mControlWrapper.stopFullScreen();
                }
            }
        }
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
        if (playState == ConstantKeys.CurrentState.STATE_ERROR) {
            bringToFront();
            setVisibility(VISIBLE);
            mIvStopFullscreen.setVisibility(mControlWrapper.isFullScreen() ? VISIBLE : GONE);
            mTvMessage.setText("视频播放异常");
        } if (playState == ConstantKeys.CurrentState.STATE_NETWORK_ERROR) {
            bringToFront();
            setVisibility(VISIBLE);
            mIvStopFullscreen.setVisibility(mControlWrapper.isFullScreen() ? VISIBLE : GONE);
            mTvMessage.setText("无网络，请检查网络设置");
        } if (playState == ConstantKeys.CurrentState.STATE_PARSE_ERROR) {
            bringToFront();
            setVisibility(VISIBLE);
            mIvStopFullscreen.setVisibility(mControlWrapper.isFullScreen() ? VISIBLE : GONE);
            //mTvMessage.setText("视频解析异常");
            mTvMessage.setText("视频加载错误");
        } else if (playState == ConstantKeys.CurrentState.STATE_IDLE) {
            setVisibility(GONE);
        } else if (playState == ConstantKeys.CurrentState.STATE_ONCE_LIVE) {
            setVisibility(GONE);
        }
    }

    @Override
    public void onPlayerStateChanged(int playerState) {

    }

    @Override
    public void setProgress(int duration, int position) {

    }

    @Override
    public void onLockStateChanged(boolean isLock) {

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = ev.getX();
                mDownY = ev.getY();
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                float absDeltaX = Math.abs(ev.getX() - mDownX);
                float absDeltaY = Math.abs(ev.getY() - mDownY);
                if (absDeltaX > ViewConfiguration.get(getContext()).getScaledTouchSlop() ||
                        absDeltaY > ViewConfiguration.get(getContext()).getScaledTouchSlop()) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

}

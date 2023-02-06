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
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.yc.video.config.ConstantKeys;
import com.yc.video.bridge.ControlWrapper;
import com.yc.video.tool.PlayerUtils;

import com.yc.video.R;
import com.yc.video.inter.IControlView;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/11/9
 *     desc  : 顶部标题栏视图
 *     revise:
 * </pre>
 */
public class CustomTitleView extends FrameLayout implements IControlView, View.OnClickListener {

    private Context mContext;
    private ControlWrapper mControlWrapper;
    private LinearLayout mLlTitleContainer;
    private ImageView mIvBack;
    private TextView mTvTitle;
    private ImageView mIvBattery;
    private TextView mTvSysTime;

    private BatteryReceiver mBatteryReceiver;
    private boolean mIsRegister;//是否注册BatteryReceiver

    public CustomTitleView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public CustomTitleView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomTitleView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        this.mContext = context;
        setVisibility(GONE);
        View view = LayoutInflater.from(mContext).inflate(
                R.layout.custom_video_player_top, this, true);
        initFindViewById(view);
        initListener();
        //电量
        mBatteryReceiver = new BatteryReceiver(mIvBattery);
    }

    private void initFindViewById(View view) {
        mLlTitleContainer = view.findViewById(R.id.ll_title_container);
        mIvBack = view.findViewById(R.id.iv_back);
        mTvTitle = view.findViewById(R.id.tv_title);
        mIvBattery = view.findViewById(R.id.iv_battery);
        mTvSysTime = view.findViewById(R.id.tv_sys_time);

    }

    private void initListener() {
        mIvBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mIvBack){
            //点击返回键
            Activity activity = PlayerUtils.scanForActivity(getContext());
            if (activity != null && mControlWrapper.isFullScreen()) {
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                mControlWrapper.stopFullScreen();
                return;
            }
            //如果不是全屏模式，则直接关闭页面activity
            if (PlayerUtils.isActivityLiving(activity)){
                activity.finish();
            }
        }
    }


    public void setTitle(String title) {
        if (title!=null && title.length()>0){
            mTvTitle.setText(title);
        } else {
            mTvTitle.setText("");
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mIsRegister) {
            getContext().unregisterReceiver(mBatteryReceiver);
            mIsRegister = false;
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!mIsRegister) {
            getContext().registerReceiver(mBatteryReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
            mIsRegister = true;
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
        //只在全屏时才有效
        if (isVisible) {
            if (getVisibility() == GONE) {
                mTvSysTime.setText(PlayerUtils.getCurrentSystemTime());
                setVisibility(VISIBLE);
                if (anim != null) {
                    startAnimation(anim);
                }
            }
        } else {
            if (getVisibility() == VISIBLE) {
                setVisibility(GONE);
                if (anim != null) {
                    startAnimation(anim);
                }
            }
        }
        if (getVisibility() == VISIBLE) {
            if (mControlWrapper.isFullScreen()) {
                //显示电量
                mIvBattery.setVisibility(VISIBLE);
                mTvSysTime.setVisibility(VISIBLE);
            } else {
                //不显示电量
                mIvBattery.setVisibility(GONE);
                mTvSysTime.setVisibility(GONE);
            }
        }
    }

    @Override
    public void onPlayStateChanged(int playState) {
        switch (playState) {
            case ConstantKeys.CurrentState.STATE_IDLE:
            case ConstantKeys.CurrentState.STATE_START_ABORT:
            case ConstantKeys.CurrentState.STATE_PREPARING:
            case ConstantKeys.CurrentState.STATE_PREPARED:
            case ConstantKeys.CurrentState.STATE_ERROR:
            case ConstantKeys.CurrentState.STATE_BUFFERING_PLAYING:
                setVisibility(GONE);
                break;
        }
    }

    @Override
    public void onPlayerStateChanged(int playerState) {
        if (playerState == ConstantKeys.PlayMode.MODE_FULL_SCREEN) {
            if (mControlWrapper.isShowing() && !mControlWrapper.isLocked()) {
                setVisibility(VISIBLE);
                mTvSysTime.setText(PlayerUtils.getCurrentSystemTime());
            }
            mTvTitle.setSelected(true);
        } else {
            setVisibility(GONE);
            mTvTitle.setSelected(false);
        }

        Activity activity = PlayerUtils.scanForActivity(mContext);
        if (activity != null && mControlWrapper.hasCutout()) {
            int orientation = activity.getRequestedOrientation();
            int cutoutHeight = mControlWrapper.getCutoutHeight();
            //设置屏幕的变化是，标题的值。后期有需要在暴露给开发者设置
            if (orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                //切换成竖屏的时候调用
                mLlTitleContainer.setPadding(PlayerUtils.dp2px(mContext,12),
                        PlayerUtils.dp2px(mContext,10), PlayerUtils.dp2px(mContext,12), 0);
            } else if (orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                //切换成横屏的时候调用
                mLlTitleContainer.setPadding(cutoutHeight, 0, PlayerUtils.dp2px(mContext,12), 0);
            } else if (orientation == ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE) {
                mLlTitleContainer.setPadding(0, 0, cutoutHeight, 0);
            } else {
                mLlTitleContainer.setPadding(PlayerUtils.dp2px(mContext,12),
                        PlayerUtils.dp2px(mContext,10), PlayerUtils.dp2px(mContext,12), 0);
            }
        }
    }

    @Override
    public void setProgress(int duration, int position) {

    }

    @Override
    public void onLockStateChanged(boolean isLocked) {
        if (isLocked) {
            setVisibility(GONE);
        } else {
            setVisibility(VISIBLE);
            mTvSysTime.setText(PlayerUtils.getCurrentSystemTime());
        }
    }


    private static class BatteryReceiver extends BroadcastReceiver {

        private ImageView pow;

        public BatteryReceiver(ImageView pow) {
            this.pow = pow;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle extras = intent.getExtras();
            if (extras == null) return;
            int current = extras.getInt("level");// 获得当前电量
            int total = extras.getInt("scale");// 获得总电量
            int percent = current * 100 / total;
            pow.getDrawable().setLevel(percent);
        }
    }
}

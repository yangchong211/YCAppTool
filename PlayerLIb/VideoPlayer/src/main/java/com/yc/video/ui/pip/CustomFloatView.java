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
package com.yc.video.ui.pip;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.yc.video.config.ConstantKeys;
import com.yc.video.bridge.ControlWrapper;

import com.yc.video.R;

import com.yc.video.inter.IControlView;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/11/9
 *     desc  : 悬浮窗视图
 *     revise:
 * </pre>
 */
public class CustomFloatView extends FrameLayout implements IControlView, View.OnClickListener {

    private ControlWrapper mControlWrapper;
    private Context mContext;
    private ImageView mIvStartPlay;
    private ProgressBar mPbLoading;
    private ImageView mIvClose;
    private ImageView mIvSkip;
    private ProgressBar mPbBottomProgress;
    private boolean mIsShowBottomProgress = true;

    public CustomFloatView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public CustomFloatView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomFloatView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        this.mContext = context;
        View view = LayoutInflater.from(getContext()).inflate(
                R.layout.custom_video_player_float, this, true);
        initFindViewById(view);
        initListener();
        //5.1以下系统SeekBar高度需要设置成WRAP_CONTENT
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
            mPbBottomProgress.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
    }

    private void initFindViewById(View view) {
        mIvStartPlay = view.findViewById(R.id.iv_start_play);
        mPbLoading = view.findViewById(R.id.pb_loading);
        mIvClose = view.findViewById(R.id.iv_close);
        mIvSkip = view.findViewById(R.id.iv_skip);
        mPbBottomProgress = view.findViewById(R.id.pb_bottom_progress);
    }

    private void initListener() {
        mIvStartPlay.setOnClickListener(this);
        mIvClose.setOnClickListener(this);
        mIvSkip.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v == mIvClose) {
            FloatVideoManager.getInstance(mContext).stopFloatWindow();
            FloatVideoManager.getInstance(mContext).reset();
        } else if (v == mIvStartPlay) {
            mControlWrapper.togglePlay();
        } else if (v == mIvSkip) {
            if (FloatVideoManager.getInstance(mContext).getActClass() != null) {
                Intent intent = new Intent(getContext(), FloatVideoManager.getInstance(mContext).getActClass());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);
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
        if (isVisible) {
            if (mIvStartPlay.getVisibility() == VISIBLE){
                return;
            }
            mIvStartPlay.setVisibility(VISIBLE);
            mIvStartPlay.startAnimation(anim);
            if (mIsShowBottomProgress) {
                mPbBottomProgress.setVisibility(GONE);
            }
        } else {
            if (mIvStartPlay.getVisibility() == GONE){
                return;
            }
            mIvStartPlay.setVisibility(GONE);
            mIvStartPlay.startAnimation(anim);
            if (mIsShowBottomProgress) {
                mPbBottomProgress.setVisibility(VISIBLE);
                AlphaAnimation animation = new AlphaAnimation(0f, 1f);
                animation.setDuration(300);
                mPbBottomProgress.startAnimation(animation);
            }
        }
    }

    @Override
    public void onPlayStateChanged(int playState) {
        switch (playState) {
            case ConstantKeys.CurrentState.STATE_IDLE:
                mIvStartPlay.setSelected(false);
                mIvStartPlay.setVisibility(VISIBLE);
                mPbLoading.setVisibility(GONE);
                break;
            case ConstantKeys.CurrentState.STATE_PLAYING:
                mIvStartPlay.setSelected(true);
                mIvStartPlay.setVisibility(GONE);
                mPbLoading.setVisibility(GONE);
                if (mIsShowBottomProgress) {
                    if (mControlWrapper.isShowing()) {
                        mPbBottomProgress.setVisibility(GONE);
                    } else {
                        mPbBottomProgress.setVisibility(VISIBLE);
                    }
                }
                //开始刷新进度
                mControlWrapper.startProgress();
                break;
            case ConstantKeys.CurrentState.STATE_PAUSED:
                mIvStartPlay.setSelected(false);
                mIvStartPlay.setVisibility(VISIBLE);
                mPbLoading.setVisibility(GONE);
                break;
            case ConstantKeys.CurrentState.STATE_PREPARING:
                mIvStartPlay.setVisibility(GONE);
                mIvStartPlay.setVisibility(VISIBLE);
                break;
            case ConstantKeys.CurrentState.STATE_PREPARED:
                mIvStartPlay.setVisibility(GONE);
                mPbLoading.setVisibility(GONE);
                break;
            case ConstantKeys.CurrentState.STATE_ERROR:
                mPbLoading.setVisibility(GONE);
                mIvStartPlay.setVisibility(GONE);
                bringToFront();
                break;
            case ConstantKeys.CurrentState.STATE_BUFFERING_PAUSED:
                mIvStartPlay.setVisibility(GONE);
                mPbLoading.setVisibility(VISIBLE);
                break;
            case ConstantKeys.CurrentState.STATE_COMPLETED:
                mIvStartPlay.setVisibility(GONE);
                mPbLoading.setVisibility(GONE);
                mIvStartPlay.setSelected(mControlWrapper.isPlaying());
                break;
            case ConstantKeys.CurrentState.STATE_BUFFERING_PLAYING:
                bringToFront();
                mPbBottomProgress.setProgress(0);
                mPbBottomProgress.setSecondaryProgress(0);
                break;
        }
    }

    @Override
    public void onPlayerStateChanged(int playerState) {

    }

    @Override
    public void setProgress(int duration, int position) {
        if (duration > 0) {
            int pos = (int) (position * 1.0 / duration * mPbBottomProgress.getMax());
            mPbBottomProgress.setProgress(pos);
        }
        int percent = mControlWrapper.getBufferedPercentage();
        if (percent >= 95) {
            //解决缓冲进度不能100%问题
            mPbBottomProgress.setSecondaryProgress(mPbBottomProgress.getMax());
        } else {
            mPbBottomProgress.setSecondaryProgress(percent * 10);
        }
    }


    @Override
    public void onLockStateChanged(boolean isLocked) {

    }

}

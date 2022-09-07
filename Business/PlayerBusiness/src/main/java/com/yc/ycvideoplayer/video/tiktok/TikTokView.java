package com.yc.ycvideoplayer.video.tiktok;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.yc.toolutils.AppLogUtils;

import com.yc.ycvideoplayer.R;

import com.yc.video.bridge.ControlWrapper;
import com.yc.video.config.ConstantKeys;
import com.yc.video.inter.IControlView;

public class TikTokView extends FrameLayout implements IControlView {

    private ImageView thumb;
    private ImageView mPlayBtn;

    private ControlWrapper mControlWrapper;
    private int mScaledTouchSlop;
    private int mStartX, mStartY;

    public TikTokView(@NonNull Context context) {
        super(context);
    }

    public TikTokView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TikTokView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_tiktok_controller, this, true);
        thumb = findViewById(R.id.iv_thumb);
        mPlayBtn = findViewById(R.id.play_btn);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mControlWrapper.togglePlay();
            }
        });
        mScaledTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    /**
     * 解决点击和VerticalViewPager滑动冲突问题
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mStartX = (int) event.getX();
                mStartY = (int) event.getY();
                return true;
            case MotionEvent.ACTION_UP:
                int endX = (int) event.getX();
                int endY = (int) event.getY();
                if (Math.abs(endX - mStartX) < mScaledTouchSlop
                        && Math.abs(endY - mStartY) < mScaledTouchSlop) {
                    performClick();
                }
                break;
        }
        return false;
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
            case ConstantKeys.CurrentState.STATE_IDLE:
                AppLogUtils.e("STATE_IDLE " + hashCode());
                thumb.setVisibility(VISIBLE);
                break;
            case ConstantKeys.CurrentState.STATE_PLAYING:
                AppLogUtils.e("STATE_PLAYING " + hashCode());
                thumb.setVisibility(GONE);
                mPlayBtn.setVisibility(GONE);
                break;
            case ConstantKeys.CurrentState.STATE_PAUSED:
                AppLogUtils.e("STATE_PAUSED " + hashCode());
                thumb.setVisibility(GONE);
                mPlayBtn.setVisibility(VISIBLE);
                break;
            case ConstantKeys.CurrentState.STATE_PREPARED:
                AppLogUtils.e("STATE_PREPARED " + hashCode());
                break;
            case ConstantKeys.CurrentState.STATE_ERROR:
                AppLogUtils.e("STATE_ERROR " + hashCode());
                Toast.makeText(getContext(), R.string.error_message, Toast.LENGTH_SHORT).show();
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
}

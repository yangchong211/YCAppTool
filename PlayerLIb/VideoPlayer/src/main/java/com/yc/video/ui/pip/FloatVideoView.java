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

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;

import com.yc.video.tool.PlayerUtils;

import com.yc.video.R;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/11/9
 *     desc  : 悬浮窗控件（解决滑动冲突）
 *     revise:
 * </pre>
 */
@SuppressLint("ViewConstructor")
public class FloatVideoView extends FrameLayout{

    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mParams;

    private int mDownRawX, mDownRawY;//手指按下时相对于屏幕的坐标
    private int mDownX, mDownY;//手指按下时相对于悬浮窗的坐标

    public FloatVideoView(@NonNull Context context, int x, int y) {
        super(context);
        mDownX = x;
        mDownY = y;
        init();
    }


    private void init() {
        setBackgroundResource(R.drawable.shape_float_window_bg);
        int padding = PlayerUtils.dp2px(getContext(), 1);
        setPadding(padding, padding, padding, padding);
        initWindow();
    }

    private void initWindow() {
        mWindowManager = PlayerUtils.getWindowManager(getContext().getApplicationContext());
        mParams = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            mParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }else {
            mParams.type =  WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        // 设置图片格式，效果为背景透明
        mParams.format = PixelFormat.TRANSLUCENT;
        mParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mParams.windowAnimations = R.style.FloatWindowAnimation;
        mParams.gravity = Gravity.START | Gravity.TOP; // 调整悬浮窗口至右下角
        // 设置悬浮窗口长宽数据
        int width = PlayerUtils.dp2px(getContext(), 250);
        mParams.width = width;
        mParams.height = width * 9 / 16;
        mParams.x = mDownX;
        mParams.y = mDownY;
    }

    /**
     * 添加至窗口
     */
    public boolean addToWindow() {
        if (mWindowManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (!isAttachedToWindow()) {
                    mWindowManager.addView(this, mParams);
                    return true;
                } else {
                    return false;
                }
            } else {
                try {
                    if (getParent() == null) {
                        mWindowManager.addView(this, mParams);
                    }
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }
        } else {
            return false;
        }
    }

    /**
     * 从窗口移除
     */
    public boolean removeFromWindow() {
        if (mWindowManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (isAttachedToWindow()) {
                    mWindowManager.removeViewImmediate(this);
                    return true;
                } else {
                    return false;
                }
            } else {
                try {
                    if (getParent() != null) {
                        mWindowManager.removeViewImmediate(this);
                    }
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercepted = false;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                intercepted = false;
                mDownRawX = (int) ev.getRawX();
                mDownRawY = (int) ev.getRawY();
                mDownX = (int) ev.getX();
                mDownY = (int) (ev.getY() + PlayerUtils.getStatusBarHeight(getContext()));
                break;
            case MotionEvent.ACTION_MOVE:
                float absDeltaX = Math.abs(ev.getRawX() - mDownRawX);
                float absDeltaY = Math.abs(ev.getRawY() - mDownRawY);
                intercepted = absDeltaX > ViewConfiguration.get(getContext()).getScaledTouchSlop() ||
                        absDeltaY > ViewConfiguration.get(getContext()).getScaledTouchSlop();
                break;
            default:
                break;
        }
        return intercepted;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                int x = (int) event.getRawX();
                int y = (int) event.getRawY();
                mParams.x = x - mDownX;
                mParams.y = y - mDownY;
                mWindowManager.updateViewLayout(this, mParams);
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }
}

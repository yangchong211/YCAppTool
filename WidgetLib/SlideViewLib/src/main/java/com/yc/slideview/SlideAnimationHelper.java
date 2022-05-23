package com.yc.slideview;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/7/3
 * 描    述：帮助类
 * 修订历史：
 * 项目地址：https://github.com/yangchong211/YCSlideView
 * ================================================
 */
public class SlideAnimationHelper {

    //关闭时的动画时间
    static final int STATE_CLOSE = 20000;
    //打开时的动画时间
    static final int STATE_OPEN = 30000;
    //关闭还是打开的状态
    private static int mCurrentState = STATE_CLOSE;

    private ValueAnimator mValueAnimator;

    SlideAnimationHelper() {}

    public int getState() {
        return mCurrentState;
    }

    public ValueAnimator getAnimation() {
        if (mValueAnimator == null) {
            mValueAnimator = new ValueAnimator();
            mValueAnimator.setFloatValues(0.0f, 1.0f);
        }
        return mValueAnimator;
    }

    public void openAnimation(long duration, AnimatorUpdateListener animatorUpdateListener,
                              Animator.AnimatorListener listener, float... values) {
        mCurrentState = STATE_OPEN;
        setValueAnimator(duration, animatorUpdateListener, listener, values);
    }

    public void closeAnimation(long duration, AnimatorUpdateListener animatorUpdateListener,
                               Animator.AnimatorListener listener, float... values) {
        mCurrentState = STATE_CLOSE;
        setValueAnimator(duration, animatorUpdateListener, listener, values);
    }

    public void openAnimation(long duration, AnimatorUpdateListener animatorUpdateListener,
                              float... values) {
        mCurrentState = STATE_OPEN;
        setValueAnimator(duration, animatorUpdateListener, null, values);
    }

    public void closeAnimation(long duration, AnimatorUpdateListener animatorUpdateListener,
                               float... values) {
        mCurrentState = STATE_CLOSE;
        setValueAnimator(duration, animatorUpdateListener, null, values);
    }

    public void openAnimation(long duration, AnimatorUpdateListener animatorUpdateListener,
                              Animator.AnimatorListener listener) {
        mCurrentState = STATE_OPEN;
        setValueAnimator(duration, animatorUpdateListener, listener);
    }

    public void closeAnimation(long duration, AnimatorUpdateListener animatorUpdateListener,
                               Animator.AnimatorListener listener) {
        mCurrentState = STATE_CLOSE;
        setValueAnimator(duration, animatorUpdateListener, listener);
    }

    public void openAnimation(long duration, AnimatorUpdateListener animatorUpdateListener) {
        mCurrentState = STATE_OPEN;
        setValueAnimator(duration, animatorUpdateListener, null);
    }

    public void closeAnimation(long duration, AnimatorUpdateListener animatorUpdateListener) {
        mCurrentState = STATE_CLOSE;
        setValueAnimator(duration, animatorUpdateListener, null);
    }

    private void setValueAnimator(long duration, AnimatorUpdateListener animatorUpdateListener,
                                  Animator.AnimatorListener listener) {
        mValueAnimator = getAnimation();
        mValueAnimator.setDuration(duration);

        if (animatorUpdateListener != null) {
            mValueAnimator.addUpdateListener(animatorUpdateListener);
        }
        if (listener != null) {
            mValueAnimator.addListener(listener);
        }
        start();
    }

    private void setValueAnimator(long duration, AnimatorUpdateListener animatorUpdateListener,
                                  Animator.AnimatorListener listener, float... values) {
        mValueAnimator = getAnimation();
        mValueAnimator.setDuration(duration);
        mValueAnimator.setFloatValues(values);
        if (animatorUpdateListener != null) {
            mValueAnimator.addUpdateListener(animatorUpdateListener);
        }
        if (listener != null) {
            mValueAnimator.addListener(listener);
        }
        start();
    }

    private void start() {
        if (mValueAnimator != null && !mValueAnimator.isRunning()) {
            mValueAnimator.start();
        }
    }

    static int getOffset(Context context, int offset) {
        return (int) (context.getResources().getDisplayMetrics().density * offset + 0.5f);
    }

}

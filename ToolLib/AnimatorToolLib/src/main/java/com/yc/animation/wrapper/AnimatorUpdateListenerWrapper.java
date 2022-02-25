package com.yc.animation.wrapper;

import android.animation.ValueAnimator;

import com.yc.animation.animator.Animator;
import com.yc.animation.listener.AnimatorUpdateListener;

/**
 * 对{@link AnimatorUpdateListener}的包装类，适配系统API到自定义的update listener
 * @author 杨充
 * @date 2017/8/11 14:25
 */
public class AnimatorUpdateListenerWrapper implements ValueAnimator.AnimatorUpdateListener {
    private Animator mAnimator;
    private AnimatorUpdateListener mListener;

    public AnimatorUpdateListenerWrapper(Animator animator, AnimatorUpdateListener listener) {
        this.mAnimator = animator;
        this.mListener = listener;
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        if (mListener != null) {
            mListener.onAnimationUpdate(mAnimator, animation.getAnimatedValue());
        }
    }
}

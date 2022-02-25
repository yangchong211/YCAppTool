package com.yc.animation.wrapper;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;

import com.yc.animation.listener.AnimatorListener;

/**
 * 对 {@link android.animation.AnimatorSet} 设置 listener 的包装类
 * 区别于 {@link AnimatorListenerWrapper}的是：对整个动画序列来说，没有 target 的概念
 * @author 杨充
 * @date 2017/6/15 19:02
 */
public class AnimatorSetListenerWrapper extends AnimatorListenerAdapter {

    private com.yc.animation.animator.Animator mAnimator;
    private AnimatorListener mListener;

    public AnimatorSetListenerWrapper(com.yc.animation.animator.Animator animator, AnimatorListener listener) {
        this.mAnimator = animator;
        this.mListener = listener;
    }

    @Override
    public void onAnimationStart(Animator animation) {
        super.onAnimationStart(animation);
        if (mListener != null) {
            mListener.onAnimationStart(mAnimator, null);
        }
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        super.onAnimationEnd(animation);
        animation.removeListener(this);
        if (mListener != null) {
            mListener.onAnimationEnd(mAnimator, null);
        }
    }
}

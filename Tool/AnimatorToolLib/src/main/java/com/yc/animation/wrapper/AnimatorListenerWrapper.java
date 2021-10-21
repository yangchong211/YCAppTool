package com.yc.animation.wrapper;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;

import com.yc.animation.event.PropertyBuilder;
import com.yc.animation.listener.AnimatorListener;

/**
 * 对 {@link android.animation.Animator} 设置 listener 的包装类
 * 动画序列中的每个子动画有 target 的概念，这里会返回给调用者，以便加以区分
 * @author 杨充
 * @date 2017/6/15 19:15
 */
public class AnimatorListenerWrapper extends AnimatorListenerAdapter {
    private com.yc.animation.animator.Animator mAnimator;
    private PropertyBuilder mPropertyBuilder;
    private AnimatorListener mListener;

    public AnimatorListenerWrapper(com.yc.animation.animator.Animator animator, PropertyBuilder propertyBuilder, AnimatorListener listener) {
        this.mAnimator = animator;
        this.mPropertyBuilder = propertyBuilder;
        this.mListener = listener;
    }

    @Override
    public void onAnimationStart(Animator animation) {
        super.onAnimationStart(animation);
        if (mListener != null) {
            mListener.onAnimationStart(mAnimator, mPropertyBuilder.getTarget());
        }
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        super.onAnimationEnd(animation);
        animation.removeListener(this);
        if (mListener != null) {
            mListener.onAnimationEnd(mAnimator, mPropertyBuilder.getTarget());
        }
    }
}

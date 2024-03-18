package com.yc.animhelper.transition;

import android.animation.Animator;

import java.lang.ref.WeakReference;

/**
 * <pre>
 *     @author : yangchong
 *     email  : yangchong211@163.com
 *     time   : 2018/8/11
 *     desc   : 动画监听类
 *     revise :
 * </pre>
 */
public class TransitionAnimation implements Animator.AnimatorListener {

    private final WeakReference<TransitionCallback> weakCallback;

    public TransitionAnimation(TransitionCallback transitionCallback) {
        weakCallback = new WeakReference<>(transitionCallback);
    }

    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {
        TransitionCallback callback = weakCallback.get();
        if (callback != null) {
            callback.onTransitionStop();
        }
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }
}

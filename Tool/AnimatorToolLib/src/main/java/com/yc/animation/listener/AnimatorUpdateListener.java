package com.yc.animation.listener;

import com.yc.animation.animator.Animator;


public interface AnimatorUpdateListener {
    void onAnimationUpdate(Animator animator, Object animatedValue);
}

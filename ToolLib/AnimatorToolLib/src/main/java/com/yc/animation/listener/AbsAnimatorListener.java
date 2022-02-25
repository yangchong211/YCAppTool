package com.yc.animation.listener;

import androidx.annotation.Nullable;
import android.view.View;

import com.yc.animation.animator.Animator;


/**
 * <pre>
 *     @author  yangchong
 *     email  : yangchong211@163.com
 *     time  :  2017/5/26
 *     desc  :  动画监听接口抽象类
 *     revise:
 * </pre>
 */
public abstract class AbsAnimatorListener implements AnimatorListener {

    @Override
    public void onAnimationStart(Animator animator, @Nullable View view) {

    }

    @Override
    public void onAnimationEnd(Animator animator, @Nullable View view) {

    }
}

package com.yc.animation.listener;

import androidx.annotation.Nullable;
import android.view.View;

import com.yc.animation.animator.Animator;


/**
 * <pre>
 *     @author  yangchong
 *     email  : yangchong211@163.com
 *     time  :  2017/5/26
 *     desc  :  动画监听，为了兼容 Fake animation 的处理，不直接使用系统接口
 *     revise:
 * </pre>
 */
public interface AnimatorListener {

    void onAnimationStart(Animator animator, @Nullable View view);

    void onAnimationEnd(Animator animator, @Nullable View view);
}

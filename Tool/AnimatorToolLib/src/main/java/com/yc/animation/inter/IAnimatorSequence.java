package com.yc.animation.inter;


import com.yc.animation.animator.Animator;

/**
 * <pre>
 *     @author  yangchong
 *     email  : yangchong211@163.com
 *     time  :  2017/5/26
 *     desc  :  动画序列接口
 *     revise:
 * </pre>
 */
public interface IAnimatorSequence {

    /**
     * 同时启动多个动画
     */
    Animator playTogether(Animator... animators);

    /**
     * 顺序启动多个动画
     */
    Animator playSequentially(Animator... animators);
}

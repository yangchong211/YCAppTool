package com.yc.animation.animator.fake;

import android.animation.TypeEvaluator;
import android.view.animation.Interpolator;
import com.yc.animation.animator.AnimatorBuilder;

/**
 * 降级处理的动画构建器，对重复设置、时间、插值器、启动延迟时间、缓动函数设置都做空处理
 * @author 杨充
 * @date 2017/5/27 12:07
 */
public class FakeAnimatorBuilder extends AnimatorBuilder {

    public FakeAnimatorBuilder() {
        this.mAnimator = new FakeAnimator();
    }

    /*Repeat*/
    @Override
    public AnimatorBuilder repeatCount(int count) {
        return this;
    }

    @Override
    public AnimatorBuilder repeatInfinite() {
        return this;
    }

    @Override
    public AnimatorBuilder repeatRestart() {
        return this;
    }

    @Override
    public AnimatorBuilder repeatReverse() {
        return this;
    }

    /*Animation duration*/
    @Override
    public AnimatorBuilder duration(int duration) {
        return this;
    }

    /*Interpolator*/
    @Override
    public AnimatorBuilder accelerateDecelerate() {
        return this;
    }

    @Override
    public AnimatorBuilder accelerate() {
        return this;
    }

    @Override
    public AnimatorBuilder bounce() {
        return this;
    }

    @Override
    public AnimatorBuilder decelerate() {
        return this;
    }

    @Override
    public AnimatorBuilder overshoot() {
        return this;
    }

    @Override
    public AnimatorBuilder anticipate() {
        return this;
    }

    @Override
    public AnimatorBuilder anticipateOvershoot() {
        return this;
    }

    /* Custom interpolator*/
    @Override
    public AnimatorBuilder interpolator(Interpolator interpolator) {
        return this;
    }

    /* Custom evaluator*/
    @Override
    public AnimatorBuilder evaluator(TypeEvaluator evaluator) {
        return this;
    }

    /*Start delay time*/
    @Override
    public AnimatorBuilder startDelay(long delay) {
        return this;
    }

    /*Easing function*/
    @Override
    public AnimatorBuilder bounceEaseOut() {
        return this;
    }

    @Override
    public AnimatorBuilder elasticEaseOut() {
        return this;
    }
}

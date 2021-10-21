package com.yc.animation.animator.view;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;

import com.yc.animation.animator.AnimatorBuilder;
import com.yc.animation.easing.BounceEaseOut;
import com.yc.animation.easing.ElasticEaseOut;

/**
 * 真实动画的构建器
 * @author 杨充
 * @date 2017/5/22 11:40
 */
public class ViewAnimatorBuilder extends AnimatorBuilder {

    public ViewAnimatorBuilder() {
        this.mAnimator = new ViewAnimator();
    }

    @Override
    public AnimatorBuilder repeatCount(int count) {
        this.mCurPropertyBuilder.setRepeatCount(count);
        return this;
    }

    @Override
    public AnimatorBuilder repeatInfinite() {
        this.mCurPropertyBuilder.setRepeatCount(ValueAnimator.INFINITE);
        return this;
    }

    @Override
    public AnimatorBuilder repeatRestart() {
        this.mCurPropertyBuilder.setRepeatMode(ValueAnimator.RESTART);
        return this;
    }

    @Override
    public AnimatorBuilder repeatReverse() {
        this.mCurPropertyBuilder.setRepeatMode(ValueAnimator.REVERSE);
        return this;
    }

    @Override
    public AnimatorBuilder duration(int duration) {
        this.mCurPropertyBuilder.setDuration(duration);
        return this;
    }

    @Override
    public AnimatorBuilder accelerateDecelerate() {
        this.mCurPropertyBuilder.setInterpolator(new AccelerateDecelerateInterpolator());
        return this;
    }

    @Override
    public AnimatorBuilder accelerate() {
        this.mCurPropertyBuilder.setInterpolator(new AccelerateInterpolator());
        return this;
    }

    @Override
    public AnimatorBuilder bounce() {
        this.mCurPropertyBuilder.setInterpolator(new BounceInterpolator());
        return this;
    }

    @Override
    public AnimatorBuilder decelerate() {
        this.mCurPropertyBuilder.setInterpolator(new DecelerateInterpolator());
        return this;
    }

    @Override
    public AnimatorBuilder overshoot() {
        this.mCurPropertyBuilder.setInterpolator(new OvershootInterpolator());
        return this;
    }

    @Override
    public AnimatorBuilder anticipate() {
        this.mCurPropertyBuilder.setInterpolator(new AnticipateInterpolator());
        return this;
    }

    @Override
    public AnimatorBuilder anticipateOvershoot() {
        this.mCurPropertyBuilder.setInterpolator(new AnticipateOvershootInterpolator());
        return this;
    }

    @Override
    public AnimatorBuilder interpolator(Interpolator interpolator) {
        this.mCurPropertyBuilder.setInterpolator(interpolator);
        return this;
    }

    @Override
    public AnimatorBuilder evaluator(TypeEvaluator evaluator) {
        this.mCurPropertyBuilder.setEvaluator(evaluator);
        return this;
    }

    @Override
    public AnimatorBuilder startDelay(long delay) {
        this.mCurPropertyBuilder.setStartDelay(delay);
        return this;
    }

    /*Easing function*/
    @Override
    public AnimatorBuilder bounceEaseOut() {
        this.mCurPropertyBuilder.setEvaluator(new BounceEaseOut(0));
        return this;
    }

    @Override
    public AnimatorBuilder elasticEaseOut() {
        this.mCurPropertyBuilder.setEvaluator(new ElasticEaseOut(0));
        return this;
    }
}

package com.yc.animation.animator.value;

import android.animation.TypeEvaluator;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

import com.yc.animation.easing.BounceEaseOut;
import com.yc.animation.inter.IEasingFunction;
import com.yc.animation.easing.ElasticEaseOut;
import com.yc.animation.animator.Animator;
import com.yc.animation.inter.IAnimatorSequence;
import com.yc.animation.listener.AnimatorUpdateListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 构建ValueAnimator，没有指定的target，由外面提供的values构建动画实例
 * 适用于需要根据动画执行过程中的值来做自定义处理的场景（同android API中的AnimatorUpdateListener使用场景）
 * @author 杨充
 * @date 2017/8/11 11:32
 */
public class ValueAnimatorBuilder implements IEasingFunction<ValueAnimatorBuilder>, IAnimatorSequence {

    private final int DEFAULT_ANIMATION_DURATION = 1000;
    private final long DEFAULT_START_DELAY = 0;

    /** Default values is from 0 to 1 */
    float[] values = {0f, 1f};
    int mDuration = DEFAULT_ANIMATION_DURATION;
    int mRepeatCount = 0;
    int mRepeatMode = android.animation.ValueAnimator.RESTART;
    Interpolator mInterpolator = new LinearInterpolator();
    long mStartDelay = DEFAULT_START_DELAY;
    TypeEvaluator mEvaluator = null;
    List<AnimatorUpdateListener> mListeners = new ArrayList<>();

    private ValueAnimator mAnimator;

    public ValueAnimatorBuilder() {
        this.mAnimator = new ValueAnimator();
    }

    public ValueAnimatorBuilder values(float... values) {
        this.values = values;
        return this;
    }

    /**
     * 设置动画的重复次数
     */
    public ValueAnimatorBuilder repeatCount(int count) {
        this.mRepeatCount = count;
        return this;
    }

    /**
     * 动画无限次重复执行
     */
    public ValueAnimatorBuilder repeatInfinite() {
        this.mRepeatCount = android.animation.ValueAnimator.INFINITE;
        return this;
    }

    /**
     * 动画重复时从头开始
     */
    public ValueAnimatorBuilder repeatRestart() {
        this.mRepeatMode = android.animation.ValueAnimator.RESTART;
        return this;
    }

    /**
     * 动画重复时反向开始
     */
    public ValueAnimatorBuilder repeatReverse() {
        this.mRepeatMode = android.animation.ValueAnimator.REVERSE;
        return this;
    }

    /**
     * 动画的执行时间
     * @param duration 单位：ms
     */
    public ValueAnimatorBuilder duration(int duration) {
        this.mDuration = duration;
        return this;
    }

    /*Interpolator*/
    public ValueAnimatorBuilder accelerateDecelerate() {
        this.mInterpolator = new AccelerateDecelerateInterpolator();
        return this;
    }

    public ValueAnimatorBuilder accelerate() {
        this.mInterpolator = new AccelerateInterpolator();
        return this;
    }

    public ValueAnimatorBuilder bounce() {
        this.mInterpolator = new BounceInterpolator();
        return this;
    }

    public ValueAnimatorBuilder decelerate() {
        this.mInterpolator = new DecelerateInterpolator();
        return this;
    }

    public ValueAnimatorBuilder overshoot() {
        this.mInterpolator = new OvershootInterpolator();
        return this;
    }

    public ValueAnimatorBuilder anticipate() {
        this.mInterpolator = new AnticipateInterpolator();
        return this;
    }

    public ValueAnimatorBuilder anticipateOvershoot() {
        this.mInterpolator = new AnticipateOvershootInterpolator();
        return this;
    }

    /** Custom interpolator*/
    public ValueAnimatorBuilder interpolator(Interpolator interpolator) {
        this.mInterpolator = interpolator;
        return this;
    }

    /** Custom evaluator*/
    public ValueAnimatorBuilder evaluator(TypeEvaluator evaluator) {
        this.mEvaluator = evaluator;
        return this;
    }

    /**
     * 动画启动的延迟时间
     * @param delay 单位：ms
     */
    public ValueAnimatorBuilder startDelay(long delay) {
        this.mStartDelay = delay;
        return this;
    }

    public ValueAnimatorBuilder withListener(AnimatorUpdateListener listener) {
        if (listener != null) {
            this.mListeners.add(listener);
        }
        return this;
    }

    @Override
    public ValueAnimatorBuilder bounceEaseOut() {
        this.mEvaluator = new BounceEaseOut(0);
        return this;
    }

    @Override
    public ValueAnimatorBuilder elasticEaseOut() {
        this.mEvaluator = new ElasticEaseOut(0);
        return this;
    }

    @Override
    public Animator playTogether(Animator... animators) {
        this.mAnimator.playTogether(animators);
        return this.mAnimator;
    }

    @Override
    public Animator playSequentially(Animator... animators) {
        this.mAnimator.playSequentially(animators);
        return this.mAnimator;
    }

    public Animator build() {
        if (this.mListeners.isEmpty()) {
            throw new IllegalStateException("Value animator must have one AnimatorUpdateListener at least, do you forget it? ");
        }
        this.mAnimator.build(this);
        return this.mAnimator;
    }
}

package com.yc.animation.animator.value;

import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;

import com.yc.animation.wrapper.AnimatorUpdateListenerWrapper;
import com.yc.animation.utils.LoggerUtil;
import com.yc.animation.animator.Animator;
import com.yc.animation.event.PropertyBuilder;
import com.yc.animation.listener.AnimatorUpdateListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 杨充
 * @date 2017/8/11 11:28
 */
public class ValueAnimator extends Animator {
    private static final String TAG = "ValueAnimator";

    private AnimatorSet mAnimatorSet;

    ValueAnimator() {
        /*this.mAnimatorSet = new AnimatorSet();*/
    }

    private AnimatorSet getInternalAnimatorSet() {
        return this.mAnimatorSet;
    }

    private List<android.animation.Animator> collectAnimators(Animator... animators) {
        List<android.animation.Animator> list = new ArrayList<>();
        for (Animator animator : animators) {
            if (animator instanceof ValueAnimator) {
                list.add(((ValueAnimator) animator).getInternalAnimatorSet());
            } else {
                LoggerUtil.e(TAG, "collectAnimators: unsupported Animator type: " + animator.getClass().getSimpleName());
            }
        }
        return list;
    }

    @Override
    public Animator playTogether(Animator... animators) {
        this.mAnimatorSet = new AnimatorSet();
        this.mAnimatorSet.playTogether(collectAnimators(animators));
        return this;
    }

    @Override
    public Animator playSequentially(Animator... animators) {
        this.mAnimatorSet = new AnimatorSet();
        this.mAnimatorSet.playSequentially(collectAnimators(animators));
        return this;
    }

    @Override
    public void play(PropertyBuilder builder) {

    }

    @Override
    public void with(PropertyBuilder builder) {

    }

    @Override
    public void before(PropertyBuilder builder) {

    }

    @Override
    public void after(PropertyBuilder builder) {

    }

    @Override
    public void build() {}

    @Override
    public Animator start() {
        mAnimatorSet.start();
        return this;
    }

    @Override
    public void stop() {
        mAnimatorSet.end();
    }

    @Override
    public boolean isRunning() {
        return mAnimatorSet != null && mAnimatorSet.isRunning();
    }

    @Override
    public boolean isStarted() {
        return mAnimatorSet != null && mAnimatorSet.isStarted();
    }

    public void build(ValueAnimatorBuilder builder) {
        if (mAnimatorSet != null) {
            LoggerUtil.d(TAG, "Please do not build animator repeatedly!");
            return;
        }
        this.mAnimatorSet = new AnimatorSet();
        final android.animation.ValueAnimator valueAnimator = createValueAnimator(builder);
        mAnimatorSet.play(valueAnimator);
    }

    private android.animation.ValueAnimator createValueAnimator(ValueAnimatorBuilder builder) {
        final android.animation.ValueAnimator valueAnimator = android.animation.ValueAnimator.ofFloat(builder.values);
        valueAnimator.setDuration(builder.mDuration);
        valueAnimator.setRepeatCount(builder.mRepeatCount);
        valueAnimator.setRepeatMode(builder.mRepeatMode);
        valueAnimator.setStartDelay(builder.mStartDelay);
        valueAnimator.setInterpolator(builder.mInterpolator);
        valueAnimator.setEvaluator(builder.mEvaluator);
        for (AnimatorUpdateListener listener : builder.mListeners) {
            valueAnimator.addUpdateListener(new AnimatorUpdateListenerWrapper(this, listener));
        }
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(android.animation.Animator animation) {
                super.onAnimationEnd(animation);
                valueAnimator.removeAllUpdateListeners();
                animation.removeListener(this);
            }
        });
        return valueAnimator;
    }
}

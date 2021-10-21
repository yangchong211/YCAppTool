package com.yc.animation.animator.view;

import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TypeEvaluator;
import android.view.View;

import com.yc.animation.model.AnimationPropertyValuesHolder;
import com.yc.animation.wrapper.AnimatorListenerWrapper;
import com.yc.animation.wrapper.AnimatorSetListenerWrapper;
import com.yc.animation.easing.BaseEasingMethod;
import com.yc.animation.utils.LoggerUtil;
import com.yc.animation.animator.Animator;
import com.yc.animation.event.PropertyBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * 真实动画实现：
 * 由 {@link ViewAnimatorBuilder} 构建，通过 {@link PropertyValuesHolder} 对每个 view 构建多属性的 ObjectAnimator；
 * 多个 view 的动画最终组合为一个 AnimatorSet
 * @author 杨充
 * @date 2017/5/9 19:22
 */
public class ViewAnimator extends Animator {
    private static final String TAG = "ViewAnimator";
    private AnimatorSet mAnimatorSet;

    private PropertyBuilder mPlayViewBuilder;
    private List<PropertyBuilder> mWithViewBuilders = new ArrayList<>();
    private List<PropertyBuilder> mBeforeViewBuilders = new ArrayList<>();
    private List<PropertyBuilder> mAfterViewBuilders = new ArrayList<>();

    private boolean mPrepared = false;
    private boolean mPending = false;

    public ViewAnimator() {
        /*this.mAnimatorSet = new AnimatorSet();*/
    }

    /**
     * 将动画属性值转换成 PropertyValueHolder，用来构建 ObjectAnimator
     */
    private PropertyValuesHolder[] convertPropertyValuesHolders(List<AnimationPropertyValuesHolder> valuesHolders) {
        final int size = valuesHolders.size();
        final PropertyValuesHolder[] results = new PropertyValuesHolder[size];
        for (int i = 0; i < size; i++) {
            results[i] = PropertyValuesHolder.ofFloat(valuesHolders.get(i).getPropertyName(), valuesHolders.get(i).getValues());
        }
        return results;
    }

    @SuppressWarnings("WrongConstant")
    private android.animation.Animator createAnimator(final PropertyBuilder builder) {
        final PropertyValuesHolder[] properties = convertPropertyValuesHolders(builder.getProperties());
        final ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(builder.getTarget(), properties);
        animator.setDuration(builder.getDuration());
        animator.setInterpolator(builder.getInterpolator());
        animator.setRepeatCount(builder.getRepeatCount());
        animator.setRepeatMode(builder.getRepeatMode());
        animator.setStartDelay(builder.getStartDelay());
        // Evaluator
        // FIXME: 2017/6/14 evaluator 需要设置给具体的属性，否则只对第一个属性有效
        TypeEvaluator evaluator = builder.getEvaluator();
        if (builder.getEvaluator() instanceof BaseEasingMethod) {
            ((BaseEasingMethod) builder.getEvaluator()).setDuration(builder.getDuration());
        }
        animator.setEvaluator(evaluator);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(android.animation.Animator animation) {
                logMsg("onAnimationStart animation: " + animation);
                super.onAnimationStart(animation);
                final View target = builder.getTarget();
                if (target == null) {
                    LoggerUtil.e(TAG, "onAnimationStart error: target is null!!");
                    return;
                }
                target.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(android.animation.Animator animation) {
                super.onAnimationEnd(animation);
                animation.removeListener(this);
                logMsg("onAnimationEnd animation.getListeners(): " + animation.getListeners());
            }
        });
        if (builder.getListener() != null) {
            animator.addListener(new AnimatorListenerWrapper(this, builder, builder.getListener()));
        }

        return animator;
    }

    private void composeAnimation() {
        this.mAnimatorSet = new AnimatorSet();
        logMsg("createAnimation mPlayViewBuilder: " + mPlayViewBuilder
                + " mWithViewBuilders: " + mWithViewBuilders + " mAfterViewBuilders: " + mAfterViewBuilders);
        if (mPlayViewBuilder != null) {
            final AnimatorSet.Builder mAnimatorSetBuilder = this.mAnimatorSet.play(createAnimator(mPlayViewBuilder));
            if (!mWithViewBuilders.isEmpty()) {
                for (PropertyBuilder builder : mWithViewBuilders) {
                    mAnimatorSetBuilder.with(createAnimator(builder));
                }
            }
            if (!mBeforeViewBuilders.isEmpty()) {
                for (PropertyBuilder builder : mBeforeViewBuilders) {
                    mAnimatorSetBuilder.before(createAnimator(builder));
                }
            }
            if (!mAfterViewBuilders.isEmpty()) {
                for (PropertyBuilder builder : mAfterViewBuilders) {
                    mAnimatorSetBuilder.after(createAnimator(builder));
                }
            }
        }
        if (this.getListener() != null) {
            this.mAnimatorSet.addListener(new AnimatorSetListenerWrapper(this, this.getListener()));
        }
    }

    private void startAnimation() {
        LoggerUtil.d(TAG, "startAnimation mAnimatorSet: " + mAnimatorSet);
        if (mAnimatorSet.isStarted()) {
            mAnimatorSet.end();
        }
        mAnimatorSet.start();
    }

    /*private void reverseAnimation() {
        composeAnimation();
        for (android.animation.Animator animator : this.mAnimatorSet.getChildAnimations()) {
            final ValueAnimator valueAnimator = (ValueAnimator) animator;
            valueAnimator.reverse();
        }
    }*/

    private void logMsg(String msg) {
        LoggerUtil.d(TAG, msg);
    }

    @Override
    public void play(PropertyBuilder builder) {
        this.mPlayViewBuilder = builder;
    }

    @Override
    public void with(PropertyBuilder builder) {
        this.mWithViewBuilders.add(builder);
    }

    @Override
    public void before(PropertyBuilder builder) {
        this.mBeforeViewBuilders.add(builder);
    }

    @Override
    public void after(PropertyBuilder builder) {
        this.mAfterViewBuilders.add(builder);
    }

    @Override
    public void build() {
        if (mAnimatorSet != null) {
            logMsg("Please do not build animator repeatedly!");
            return;
        }
        prepareAnimation(new PrepareAnimationCallback() {
            @Override
            public void onPrepared() {
                composeAnimation();
                mPrepared = true;
                logMsg("build onPrepared mPending: " + mPending);
                if (mPending) {
                    mPending = false;
                    startAnimation();
                }
            }
        });
    }

    @Override
    public Animator start() {
        logMsg("start mPrepared: " + mPrepared);
        if (mPrepared) {
            startAnimation();
        } else {
            mPending = true;
        }
        return this;
    }

    @Override
    public void stop() {
        logMsg("stop mAnimatorSet: " + this.mAnimatorSet
                + " isStarted: " + (this.mAnimatorSet != null && this.mAnimatorSet.isStarted())
                + " isRunning: " + (this.mAnimatorSet != null && this.mAnimatorSet.isRunning()));
        if (this.mAnimatorSet == null) {
            LoggerUtil.e(TAG, "Stop: this animator has not built yet!");
            return;
        }
        this.mAnimatorSet.end();
    }

    @Override
    public boolean isRunning() {
        return this.mAnimatorSet != null && this.mAnimatorSet.isRunning();
    }

    @Override
    public boolean isStarted() {
        return this.mAnimatorSet != null && this.mAnimatorSet.isStarted();
    }

    private AnimatorSet getInternalAnimatorSet() {
        return this.mAnimatorSet;
    }

    private List<android.animation.Animator> collectAnimators(Animator... animators) {
        List<android.animation.Animator> list = new ArrayList<>();
        for (Animator animator : animators) {
            if (animator instanceof ViewAnimator) {
                list.add(((ViewAnimator) animator).getInternalAnimatorSet());
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
        this.mPrepared = true;
        return this;
    }

    @Override
    public Animator playSequentially(Animator... animators) {
        this.mAnimatorSet = new AnimatorSet();
        this.mAnimatorSet.playSequentially(collectAnimators(animators));
        this.mPrepared = true;
        return this;
    }
}

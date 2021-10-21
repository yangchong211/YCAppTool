package com.yc.animation;

import android.view.View;

import com.yc.animation.animator.Animator;
import com.yc.animation.animator.AnimatorBuilder;
import com.yc.animation.animator.value.ValueAnimator;
import com.yc.animation.animator.value.ValueAnimatorBuilder;
import com.yc.animation.animator.view.ViewAnimatorBuilder;
import com.yc.animation.constant.AnimationLevel;
import com.yc.animation.animator.fake.FakeAnimatorBuilder;

/**
 * 根据当前设备所能支持的动画等级创建不同的动画构建器，实现不同设备的兼容处理
 * @author 杨充
 * @date 2017/5/11 11:08
 */
public class AnimatorCompat {
    private static final String TAG = "AnimatorCompat";

    private static int sAnimationLevel = AnimationLevel.LEVEL_REAL;

    private AnimatorCompat() {}

    public static void doRealAnimation() {
        sAnimationLevel = AnimationLevel.LEVEL_REAL;
    }

    public static void doFakeAnimation() {
        sAnimationLevel = AnimationLevel.LEVEL_FAKE;
    }

    private static AnimatorBuilder createAnimatorBuilder() {
        AnimatorBuilder builder;
        switch (sAnimationLevel) {
            case AnimationLevel.LEVEL_REAL:
                builder = new ViewAnimatorBuilder();
                break;
            case AnimationLevel.LEVEL_FAKE:
                builder = new FakeAnimatorBuilder();
                break;
            default:
                builder = new ViewAnimatorBuilder();
                break;
        }
        return builder;
    }

    private static ValueAnimatorBuilder createValueAnimatorBuilder() {
        return new ValueAnimatorBuilder();
    }

    public static AnimatorBuilder play(final View view){
        AnimatorBuilder builder = createAnimatorBuilder();
        builder.play(view);
        return builder;
    }

    public static ValueAnimatorBuilder valueAnimation() {
        ValueAnimatorBuilder builder = createValueAnimatorBuilder();
        return builder;
    }

    public static Animator playTogether(Animator... animators) {
        if (animators == null || animators.length == 0) {
            throw new IllegalArgumentException("No animator to play!");
        }
        if (animators[0] instanceof ValueAnimator) {
            return createValueAnimatorBuilder().playTogether(animators);
        } else {
            return createAnimatorBuilder().playTogether(animators);
        }
    }

    public static Animator playSequentially(Animator... animators) {
        if (animators == null || animators.length == 0) {
            throw new IllegalArgumentException("No animator to play!");
        }
        if (animators[0] instanceof ValueAnimator) {
            return createValueAnimatorBuilder().playSequentially(animators);
        } else {
            return createAnimatorBuilder().playSequentially(animators);
        }
    }
}

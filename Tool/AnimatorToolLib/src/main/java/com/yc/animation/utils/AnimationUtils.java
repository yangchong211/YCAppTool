package com.yc.animation.utils;

import android.view.View;
import android.view.ViewGroup;
import com.yc.animation.animator.AnimatorBuilder;

/**
 * 常用动画效果辅助实现类，基于 {@link AnimatorBuilder} 设置 view 的多个属性值
 * @author 杨充
 * @date 2017/5/11 12:12
 */
public class AnimationUtils/* implements CommonAnimation*/ {
    private static final String TAG = "CommonAnimationHelper";

    private static float[] sAnimationValueIn = {0f, 1f};
    private static float[] sAnimationValueOut = {1f, 0f};

    public static AnimatorBuilder fadeIn(AnimatorBuilder builder) {
        return builder.alpha(sAnimationValueIn).decelerate();
    }

    public static AnimatorBuilder fadeOut(AnimatorBuilder builder) {
        return builder.alpha(sAnimationValueOut).decelerate();
    }

    private static float[] getTranslateInFromTopValues(View view) {
        final float valueFrom = -view.getHeight();
        float temp = 0;
        final float y = view.getY();
        LoggerUtil.d(TAG, "getTranslateInFromTopValues view: " + view + "[y: " + y + "]");
        // The view is shown completely
        if (y >= 0) {
            temp = y;
        } else {
            // Get parent's top padding as target value
            if (view.getParent() instanceof ViewGroup) {
                final ViewGroup parent = (ViewGroup) view.getParent();
                LoggerUtil.d(TAG, "getTranslateInFromTopValues parent: " + parent + "[top padding: " + parent.getPaddingTop() + "]");
                temp = parent.getPaddingTop();
            }
        }
        final float valueTo = temp;
        LoggerUtil.d(TAG, "translateInFromTop valueFrom: " + valueFrom + " valueTo: " + valueTo);
        return new float[]{valueFrom, valueTo};
    }

    public static AnimatorBuilder translateInFromTop(final AnimatorBuilder builder) {
        final View target = builder.getTarget();
        if (target == null) {
            return builder;
        }

        if (target.getVisibility() != View.GONE) {
            builder.y(getTranslateInFromTopValues(target));
        } else {
            builder.waitForViewShown(new Runnable() {
                @Override
                public void run() {
                    builder.y(getTranslateInFromTopValues(target));
                }
            });
        }
        builder.decelerate();
        return builder;
    }

    private static float[] getTranslateOutToTopValues(View view) {
        final float valueFrom = view.getY();
        final float valueTo = -view.getHeight();
        LoggerUtil.d(TAG, "translateOutToTop valueFrom: " + valueFrom + " valueTo: " + valueTo);
        return new float[]{valueFrom, valueTo};
    }

    public static AnimatorBuilder translateOutToTop(final AnimatorBuilder builder) {
        final View target = builder.getTarget();
        if (target == null) {
            return builder;
        }

        if (target.getVisibility() != View.GONE) {
            builder.y(getTranslateOutToTopValues(target));
        } else {
            builder.waitForViewShown(new Runnable() {
                @Override
                public void run() {
                    builder.y(getTranslateOutToTopValues(target));
                }
            });
        }
        builder.decelerate();
        return builder;
    }

    private static float[] getTranslateInFromBottomValues(View view) {
        final View rootView = view.getRootView();

        final float valueFrom = rootView.getHeight();
        final float valueTo = view.getY();
        LoggerUtil.d(TAG, "translateInFromBottom valueFrom: " + valueFrom + " valueTo: " + valueTo);
        return new float[]{valueFrom, valueTo};
    }

    public static AnimatorBuilder translateInFromBottom(final AnimatorBuilder builder) {
        final View target = builder.getTarget();
        if (target == null) {
            return builder;
        }

        if (target.getVisibility() != View.GONE) {
            builder.y(getTranslateInFromBottomValues(target));
        } else {
            builder.waitForViewShown(new Runnable() {
                @Override
                public void run() {
                    builder.y(getTranslateInFromBottomValues(target));
                }
            });
        }
        builder.decelerate();
        return builder;
    }

    private static float[] getTranslateOutToBottomValues(View view) {
        final View rootView = view.getRootView();

        final float valueFrom = view.getY();
        final float valueTo = rootView.getHeight();
        LoggerUtil.d(TAG, "translateOutToBottom valueFrom: " + valueFrom + " valueTo: " + valueTo);
        return new float[]{valueFrom, valueTo};
    }

    public static AnimatorBuilder translateOutToBottom(final AnimatorBuilder builder) {
        final View target = builder.getTarget();
        if (target == null) {
            return builder;
        }

        if (target.getVisibility() != View.GONE) {
            builder.y(getTranslateOutToBottomValues(target));
        } else {
            builder.waitForViewShown(new Runnable() {
                @Override
                public void run() {
                    builder.y(getTranslateOutToBottomValues(target));
                }
            });
        }
        builder.decelerate();
        return builder;
    }

    public static AnimatorBuilder scaleIn(AnimatorBuilder builder) {
        return builder.scaleX(sAnimationValueIn).scaleY(sAnimationValueIn);
    }

    public static AnimatorBuilder scaleOut(AnimatorBuilder builder) {
        return builder.scaleX(sAnimationValueOut).scaleY(sAnimationValueOut);
    }

    public static AnimatorBuilder rotateCW(AnimatorBuilder builder) {
        return builder.rotate(0f, 360f);
    }

    public static AnimatorBuilder rotateCCW(AnimatorBuilder builder) {
        return builder.rotate(360f, 0f);
    }
}

package com.yc.animhelper.transition;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.os.Build;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.ViewTreeObserver;


/**
 * <pre>
 *     @author : yangchong
 *     email  : yangchong211@163.com
 *     time   : 2018/8/11
 *     desc   : 动画控制器
 *     revise :
 * </pre>
 */
public final class TransitionController {

    /**
     * 动画控件
     */
    private final View animView;
    /**
     * 动画时长
     */
    private final long duration;
    /**
     * 插值器
     */
    private final TimeInterpolator timeInterpolator;
    /**
     * 转场动画参数
     */
    private TransitionParam transitionParam;
    /**
     * 目标的宽和高
     */
    private int targetWidth, targetHeight;
    /**
     * 动画
     */
    private ViewPropertyAnimator viewAnimator;

    private TransitionController(View animView, long duration, int targetWidth, 
                                 int targetHeight, TimeInterpolator timeInterpolator) {
        this.animView = animView;
        this.duration = duration;
        this.targetWidth = targetWidth;
        this.targetHeight = targetHeight;
        this.timeInterpolator = timeInterpolator;
    }

    /**
     * 进入和退出的动画
     * @param enterAnimation                是否是进入
     * @param animatorListener              动画监听
     */
    private void transitionStart(boolean enterAnimation, Animator.AnimatorListener animatorListener) {
        //标识我们点击的View在屏幕中可见的高度
        int visibleHeight = transitionParam.bottom - transitionParam.top;

        //计算缩放的宽和高起点,需要和外部的控件宽高看起来一致才比较细腻
        float scaleXStart = (float) transitionParam.width / targetWidth;
        float scaleYStart = (float) transitionParam.height / targetHeight;

        animView.setPivotX(0);
        animView.setPivotY(0);

        int startTransX = transitionParam.left;
        int startTransY;
        if (transitionParam.bottom == targetHeight) {
            //滑动到屏幕底部去了,这时候以点击的控件顶部为位移起始点
            startTransY = transitionParam.top;
        } else if (visibleHeight < transitionParam.height) {
            //滑动到屏幕顶部去了
            startTransY = transitionParam.bottom - transitionParam.height;
        } else {
            startTransY = transitionParam.top;
        }

        if (enterAnimation) {
            //显示动画设置移动的起始位置,关闭动画只指定目标位移位置
            animView.setTranslationX(startTransX);
            animView.setTranslationY(startTransY);
        }

        //设置缩放点
        animView.setScaleX(enterAnimation ? scaleXStart : 1.0F);
        animView.setScaleY(enterAnimation ? scaleYStart : 1.0F);

        viewAnimator = animView.animate();
        //头条参数
        viewAnimator.setInterpolator(timeInterpolator);
        animView.setVisibility(View.VISIBLE);
        viewAnimator.setDuration(duration)
                .setListener(animatorListener)
                .scaleX(enterAnimation ? 1.0F : scaleXStart)
                .scaleY(enterAnimation ? 1.0F : scaleYStart)
                //translationX，translationY：控制View的位置，值是相对于View容器左上角坐标的偏移。
                .translationX(enterAnimation ? 0.0F : startTransX)
                .translationY(enterAnimation ? 0.0F : startTransY)
                .start();
    }

    /**
     * 进入的转场动画
     * @param param                         参数
     * @param transitionCallback            回调
     */
    public void transitionEnter(TransitionParam param, final TransitionCallback transitionCallback) {
        this.transitionParam = param;
        animView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    animView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    animView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
                //获取
                targetWidth = animView.getMeasuredWidth();
                targetHeight = animView.getMeasuredHeight();
                //开始执行动画
                transitionStart(true, new TransitionAnimation(transitionCallback));
            }
        });
    }

    /**
     * 退出的转场动画
     * @param transitionCallback            回调
     */
    public void transitionExit(TransitionCallback transitionCallback) {
        transitionStart(false, new TransitionAnimation(transitionCallback));
    }

    /**
     * 释放动画
     */
    public void transitionRelease() {
        if (viewAnimator != null) {
            viewAnimator.setListener(null);
            viewAnimator.cancel();
            viewAnimator = null;
        }
    }

    public static class Builder {
        /**
         * 动画控件
         */
        private View animView;
        /**
         * 动画时长
         */
        private long duration;

        /**
         * 插值器
         */
        private TimeInterpolator timeInterpolator;

        /**
         * 目标的宽和高
         */
        private int targetWidth, targetHeight;

        public Builder with(View animView) {
            this.animView = animView;
            return this;
        }

        public Builder duration(long duration) {
            this.duration = duration;
            return this;
        }

        public Builder setInterpolator(TimeInterpolator interpolator) {
            this.timeInterpolator = interpolator;
            return this;
        }

        public Builder targetWH(int width, int height) {
            this.targetWidth = width;
            this.targetHeight = height;
            return this;
        }

        public TransitionController build() {
            return new TransitionController(animView, duration, targetWidth, 
                    targetHeight, timeInterpolator);
        }

    }

}

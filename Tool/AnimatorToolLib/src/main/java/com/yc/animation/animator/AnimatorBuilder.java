package com.yc.animation.animator;

import android.animation.TypeEvaluator;
import androidx.annotation.Nullable;
import android.view.View;
import android.view.animation.Interpolator;

import com.yc.animation.event.PropertyBuilder;
import com.yc.animation.inter.IEasingFunction;
import com.yc.animation.inter.ICommonAnimation;
import com.yc.animation.inter.IAnimatorSequence;
import com.yc.animation.listener.AnimatorListener;
import com.yc.animation.utils.AnimationUtils;
import com.yc.animation.wrapper.RunnableWrapper;

/**
 * 动画构建器
 * 1. 支持（多个 view 组合动画的场景中）对多个动画排序
 * 2. 包装 {@link PropertyBuilder} 的API
 * 3. 增加常用动效及组合功能
 * 4. 支持缓动函数效果（http://easings.net/zh-cn）
 * @author 杨充
 * @date 2017/5/22 11:41
 */
public abstract class AnimatorBuilder implements ICommonAnimation<AnimatorBuilder>,
        IEasingFunction<AnimatorBuilder>, IAnimatorSequence {

    public PropertyBuilder mCurPropertyBuilder;
    public Animator mAnimator;

    public PropertyBuilder createPropertyBuilder(View target){
        return new PropertyBuilder(target);
    }

    /**
     * 基于传入的 view ，创建一个新的动画序列
     * @param target 执行动画的 view
     * @return AnimatorBuilder 本身
     */
    public AnimatorBuilder play(View target) {
        // Create a new property builder to hold properties for target view
        final PropertyBuilder propertyBuilder = createPropertyBuilder(target);
        this.mAnimator.play(propertyBuilder);
        this.mCurPropertyBuilder = propertyBuilder;
        return this;
    }

    /**
     * 在当前动画序列中增加一个动画，与设置给 {@link AnimatorBuilder#play(View)} 的 view 同时执行
     * @param target 执行动画的 view
     * @return AnimatorBuilder 本身
     */
    public AnimatorBuilder with(View target) {
        final PropertyBuilder propertyBuilder = createPropertyBuilder(target);
        this.mAnimator.with(propertyBuilder);
        this.mCurPropertyBuilder = propertyBuilder;
        return this;
    }

    /**
     * 在当前动画序列中增加一个动画，设置给 {@link AnimatorBuilder#play(View)} 的 view 动画
     * 在当前 view 的动画执行前执行
     * @param target 执行动画的 view
     * @return AnimatorBuilder 本身
     */
    public AnimatorBuilder before(View target) {
        final PropertyBuilder propertyBuilder = createPropertyBuilder(target);
        this.mAnimator.before(propertyBuilder);
        this.mCurPropertyBuilder = propertyBuilder;
        return this;
    }

    /**
     * 在当前动画序列中增加一个动画，设置给 {@link AnimatorBuilder#play(View)} 的 view 动画
     * 在当前 view 的动画执行结束后执行
     * @param target 执行动画的 view
     * @return AnimatorBuilder 本身
     */
    public AnimatorBuilder after(View target) {
        final PropertyBuilder propertyBuilder = createPropertyBuilder(target);
        this.mAnimator.after(propertyBuilder);
        this.mCurPropertyBuilder = propertyBuilder;
        return this;
    }

    // Wrap all property APIs of PropertyBuilder
    public AnimatorBuilder x(float... values) {
        this.mCurPropertyBuilder.x(values);
        return this;
    }

    public AnimatorBuilder y(float... values) {
        this.mCurPropertyBuilder.y(values);
        return this;
    }

    public AnimatorBuilder translateX(float... values) {
        this.mCurPropertyBuilder.translateX(values);
        return this;
    }

    public AnimatorBuilder translateY(float... values) {
        this.mCurPropertyBuilder.translateY(values);
        return this;
    }

    public AnimatorBuilder rotate(float... values) {
        this.mCurPropertyBuilder.rotate(values);
        return this;
    }

    public AnimatorBuilder scale(float... values) {
        this.mCurPropertyBuilder.scaleX(values);
        this.mCurPropertyBuilder.scaleY(values);
        return this;
    }

    public AnimatorBuilder scaleX(float... values) {
        this.mCurPropertyBuilder.scaleX(values);
        return this;
    }

    public AnimatorBuilder scaleY(float... values) {
        this.mCurPropertyBuilder.scaleY(values);
        return this;
    }

    public AnimatorBuilder alpha(float... values) {
        this.mCurPropertyBuilder.alpha(values);
        return this;
    }

    public AnimatorBuilder property(String propertyName, float... values) {
        this.mCurPropertyBuilder.property(propertyName, values);
        return this;
    }

    /**
     * 设置动画的重复次数
     */
    public abstract AnimatorBuilder repeatCount(int count);

    /**
     * 动画无限次重复执行
     */
    public abstract AnimatorBuilder repeatInfinite();

    /**
     * 动画重复时从头开始
     */
    public abstract AnimatorBuilder repeatRestart();

    /**
     * 动画重复时反向开始
     */
    public abstract AnimatorBuilder repeatReverse();

    /**
     * 动画的执行时间
     * @param duration 单位：ms
     */
    public abstract AnimatorBuilder duration(int duration);

    /*Interpolator*/
    public abstract AnimatorBuilder accelerateDecelerate();

    public abstract AnimatorBuilder accelerate();

    public abstract AnimatorBuilder bounce();

    public abstract AnimatorBuilder decelerate();

    public abstract AnimatorBuilder overshoot();

    public abstract AnimatorBuilder anticipate();

    public abstract AnimatorBuilder anticipateOvershoot();

    /* Custom interpolator*/
    public abstract AnimatorBuilder interpolator(Interpolator interpolator);

    /* Custom evaluator*/
    public abstract AnimatorBuilder evaluator(TypeEvaluator evaluator);

    /**
     * 动画启动的延迟时间
     * @param delay 单位：ms
     */
    public abstract AnimatorBuilder startDelay(long delay);

    public AnimatorBuilder withListener(AnimatorListener listener) {
        this.mCurPropertyBuilder.setListener(listener);
        return this;
    }

    public Animator build() {
        this.mAnimator.build();
        return this.mAnimator;
    }

    /**
     * 构建动画，并附带整个动画序列的一个 listener
     * @param listener 监听整个动画序列播放事件的 listener
     * @return 当前动画的控制实例 {@link Animator}
     */
    public Animator buildWithListener(AnimatorListener listener) {
        this.mAnimator.setListener(listener);
        return build();
    }

    @Nullable
    public View getTarget() {
        return this.mCurPropertyBuilder.getTarget();
    }

    /*Common animation*/
    @Override
    public AnimatorBuilder fadeIn() {
        return AnimationUtils.fadeIn(this);
    }

    @Override
    public AnimatorBuilder fadeOut() {
        return AnimationUtils.fadeOut(this);
    }

    @Override
    public AnimatorBuilder translateInFromTop() {
        return AnimationUtils.translateInFromTop(this);
    }

    @Override
    public AnimatorBuilder translateOutToTop() {
        return AnimationUtils.translateOutToTop(this);
    }

    @Override
    public AnimatorBuilder translateInFromBottom() {
        return AnimationUtils.translateInFromBottom(this);
    }

    @Override
    public AnimatorBuilder translateOutToBottom() {
        return AnimationUtils.translateOutToBottom(this);
    }

    @Override
    public AnimatorBuilder scaleIn() {
        return AnimationUtils.scaleIn(this);
    }

    @Override
    public AnimatorBuilder scaleOut() {
        return AnimationUtils.scaleOut(this);
    }

    @Override
    public AnimatorBuilder rotateCW() {
        return AnimationUtils.rotateCW(this);
    }

    @Override
    public AnimatorBuilder rotateCCW() {
        return AnimationUtils.rotateCCW(this);
    }

    public AnimatorBuilder waitForViewShown(Runnable runnable) {
        this.mAnimator.waitForViewShown(new RunnableWrapper(this.mCurPropertyBuilder.getTarget(), runnable));
        return this;
    }

    @Override
    public Animator playTogether(Animator... animators) {
        return this.mAnimator.playTogether(animators);
    }

    @Override
    public Animator playSequentially(Animator... animators) {
        return this.mAnimator.playSequentially(animators);
    }
}

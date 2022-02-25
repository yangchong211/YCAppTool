package com.yc.animation.animator.fake;

import android.view.View;

import com.yc.animation.model.AnimationPropertyValuesHolder;
import com.yc.animation.utils.LoggerUtil;
import com.yc.animation.model.TargetValueHolder;
import com.yc.animation.animator.Animator;
import com.yc.animation.constant.Property;
import com.yc.animation.event.PropertyBuilder;
import com.yc.animation.listener.AnimatorListener;
import java.util.ArrayList;
import java.util.List;



/**
 * 动画的降级处理实现：
 * 由 {@link FakeAnimatorBuilder} 构建，通过 {@link TargetValueHolder} 记录每个 view 对应属性的目标值，执行动画的过程为：直接更新 view 属性为目标值
 * 多个 view 的组合动画统一处理，不再有序列的概念
 * @author 杨充
 * @date 2017/5/11 11:16
 */
public class FakeAnimator extends Animator {
    private static final String TAG = "FakeAnimator";

    private List<PropertyBuilder> mAnimations = new ArrayList<>();

    private boolean mRunning = false;
    private boolean mStarted = false;

    /**
     * 将动画属性值处理为降级方案中需要的值
     */
    private TargetValueHolder[] convertPropertyValuesHolders(List<AnimationPropertyValuesHolder> valuesHolders) {
        final int size = valuesHolders.size();
        final TargetValueHolder[] results = new TargetValueHolder[size];
        for (int i = 0; i < size; i++) {
            final float[] values = valuesHolders.get(i).getValues();
            // 取属性值中的最后一个作为属性的目标值
            results[i] = TargetValueHolder.create(valuesHolders.get(i).getPropertyName(), values[values.length - 1]);
        }
        return results;
    }

    private void startAnimation() {
        this.mRunning = true;
        AnimatorListener animatorListener = this.getListener();
        if (animatorListener != null) {
            animatorListener.onAnimationStart(this, null);
        }
        for (PropertyBuilder builder : mAnimations) {
            AnimatorListener propertyListener = builder.getListener();
            if (propertyListener != null) {
                propertyListener.onAnimationStart(this, builder.getTarget());
            }
            final View target = builder.getTarget();
            if (target == null) {
                continue;
            }
            final TargetValueHolder[] valueHolders = convertPropertyValuesHolders(builder.getProperties());
            animateView(target, valueHolders);
            target.setVisibility(View.VISIBLE);
            if (propertyListener != null) {
                propertyListener.onAnimationEnd(this, builder.getTarget());
            }
        }
        if (animatorListener != null) {
            animatorListener.onAnimationEnd(this, null);
        }
        this.mRunning = false;
    }

    private void animateView(View view, TargetValueHolder[] valueHolders) {
        for (TargetValueHolder valueHolder : valueHolders) {
            switch (valueHolder.getPropertyName()) {
                case Property.PROPERTY_X:
                    view.setX(valueHolder.getValue());
                    break;
                case Property.PROPERTY_Y:
                    view.setY(valueHolder.getValue());
                    break;
                case Property.PROPERTY_TRANSLATION_X:
                    view.setTranslationX(valueHolder.getValue());
                    break;
                case Property.PROPERTY_TRANSLATION_Y:
                    view.setTranslationY(valueHolder.getValue());
                    break;
                case Property.PROPERTY_ROTATION:
                    view.setRotation(valueHolder.getValue());
                    break;
                case Property.PROPERTY_SCALE_X:
                    view.setScaleX(valueHolder.getValue());
                    break;
                case Property.PROPERTY_SCALE_Y:
                    view.setScaleY(valueHolder.getValue());
                    break;
                case Property.PROPERTY_ALPHA:
                    view.setAlpha(valueHolder.getValue());
                    break;
                default:
                    // Custom property here
                    FakeAnimatorHelper.invokeCustomSetter(view, valueHolder.getPropertyName(), valueHolder.getValue());
//                    Log.e(TAG, "Unsupported property!");
                    break;
            }
        }
    }

    @Override
    public void play(PropertyBuilder builder) {
        this.mAnimations.add(builder);
    }

    @Override
    public void with(PropertyBuilder builder) {
        this.mAnimations.add(builder);
    }

    @Override
    public void before(PropertyBuilder builder) {
        this.mAnimations.add(builder);
    }

    @Override
    public void after(PropertyBuilder builder) {
        this.mAnimations.add(builder);
    }

    @Override
    public void build() {
        // For fake animation, build action has completed on play/with/before/after methods invoke
    }

    @Override
    public Animator start() {
        mStarted = true;
        prepareAnimation(new PrepareAnimationCallback() {
            @Override
            public void onPrepared() {
                startAnimation();
                mStarted = false;
            }
        });
        return this;
    }

    @Override
    public void stop() {
        // For fake animation, stop is unsupported
    }

    @Override
    public boolean isRunning() {
        return this.mRunning;
    }

    @Override
    public boolean isStarted() {
        return this.mStarted;
    }

    private List<PropertyBuilder> getInternalAnimations() {
        return this.mAnimations;
    }

    private List<PropertyBuilder> collectAnimations(Animator[] animators) {
        final List<PropertyBuilder> list = new ArrayList<>();
        for (Animator animator : animators) {
            if (animator instanceof FakeAnimator) {
                list.addAll(((FakeAnimator) animator).getInternalAnimations());
            } else {
                LoggerUtil.e(TAG, "collectAnimations: unsupported Animator type: " + animator.getClass().getSimpleName());
                break;
            }
        }
        return list;
    }

    @Override
    public Animator playTogether(Animator... animators) {
        this.mAnimations.clear();
        this.mAnimations.addAll(collectAnimations(animators));
        return this;
    }

    @Override
    public Animator playSequentially(Animator... animators) {
        return playTogether(animators);
    }
}

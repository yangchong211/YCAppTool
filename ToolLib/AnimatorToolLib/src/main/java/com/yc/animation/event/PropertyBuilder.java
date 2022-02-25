package com.yc.animation.event;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import androidx.annotation.Nullable;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.yc.animation.model.AnimationPropertyValuesHolder;
import com.yc.animation.constant.Property;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


/**
 * <pre>
 *     @author  yangchong
 *     email  : yangchong211@163.com
 *     time  :  2017/5/26
 *     desc  :  动画的属性构建器，保存动画对象的属性、动画时间、插值器、重复次数、重复模式、启动延迟时间、估值器等
 *     revise:
 * </pre>
 */
public class PropertyBuilder extends AnimatorEvent {
    private final int DEFAULT_ANIMATION_DURATION = 1000;
    private final long DEFAULT_START_DELAY = 0;

//    private View mTarget;
    private WeakReference<View> mTarget;
    private List<AnimationPropertyValuesHolder> mProperties = new ArrayList<>();

    /** Animation attributes */
    private int mDuration = DEFAULT_ANIMATION_DURATION;
    private int mRepeatCount = 0;
    private int mRepeatMode = ValueAnimator.RESTART;
    private Interpolator mInterpolator = new LinearInterpolator();
    private long mStartDelay = DEFAULT_START_DELAY;
    private TypeEvaluator mEvaluator = null;

    public PropertyBuilder(View view) {
        this.mTarget = view == null ? null : new WeakReference<>(view);
    }

    private void animateProperty(String propertyName, float... values) {
        final AnimationPropertyValuesHolder valuesHolder = AnimationPropertyValuesHolder.create(propertyName, values);
        mProperties.add(valuesHolder);
    }

    /** Object properties */
    public PropertyBuilder x(float... values) {
        animateProperty(Property.PROPERTY_X, values);
        return this;
    }

    public PropertyBuilder y(float... values) {
        animateProperty(Property.PROPERTY_Y, values);
        return this;
    }

    public PropertyBuilder translateX(float... values) {
        animateProperty(Property.PROPERTY_TRANSLATION_X, values);
        return this;
    }

    public PropertyBuilder translateY(float... values) {
        animateProperty(Property.PROPERTY_TRANSLATION_Y, values);
        return this;
    }

    public PropertyBuilder rotate(float... values) {
        animateProperty(Property.PROPERTY_ROTATION, values);
        return this;
    }

    public PropertyBuilder scaleX(float... values) {
        animateProperty(Property.PROPERTY_SCALE_X, values);
        return this;
    }

    public PropertyBuilder scaleY(float... values) {
        animateProperty(Property.PROPERTY_SCALE_Y, values);
        return this;
    }

    public PropertyBuilder alpha(float... values) {
        animateProperty(Property.PROPERTY_ALPHA, values);
        return this;
    }

    public PropertyBuilder property(String propertyName, float... values) {
        animateProperty(propertyName, values);
        return this;
    }

    public List<AnimationPropertyValuesHolder> getProperties() {
        return this.mProperties;
    }

    @Nullable
    public View getTarget() {
        return this.mTarget == null ? null : this.mTarget.get();
    }

    public int getDuration() {
        return this.mDuration;
    }

    public void setDuration(int duration) {
        if (duration <= 0) {
            this.mDuration = DEFAULT_ANIMATION_DURATION;
        } else {
            this.mDuration = duration;
        }
    }

    public int getRepeatMode() {
        return this.mRepeatMode;
    }

    public void setRepeatMode(int mode) {
        this.mRepeatMode = mode;
    }

    public int getRepeatCount() {
        return this.mRepeatCount;
    }

    public void setRepeatCount(int count) {
        this.mRepeatCount = count;
    }

    public Interpolator getInterpolator() {
        return this.mInterpolator;
    }

    public void setInterpolator(Interpolator interpolator) {
        this.mInterpolator = interpolator;
    }

    public long getStartDelay() {
        return this.mStartDelay;
    }

    public void setStartDelay(long startDelay) {
        if (startDelay < 0) {
            this.mStartDelay = DEFAULT_START_DELAY;
        } else {
            this.mStartDelay = startDelay;
        }
    }

    public TypeEvaluator getEvaluator() {
        return this.mEvaluator;
    }

    public void setEvaluator(TypeEvaluator evaluator) {
        this.mEvaluator = evaluator;
    }
}

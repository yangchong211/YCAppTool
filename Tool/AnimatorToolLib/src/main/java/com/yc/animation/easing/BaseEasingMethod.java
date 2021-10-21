package com.yc.animation.easing;

import android.animation.TypeEvaluator;

/**
 * 来自 https://github.com/daimajia/AnimationEasingFunctions
 * @author 杨充
 * @date 2017/6/12 17:46
 */
public abstract class BaseEasingMethod  implements TypeEvaluator<Number> {
    private float mDuration;

    public BaseEasingMethod(float duration){
        mDuration = duration;
    }

    public void setDuration(float duration) {
        mDuration = duration;
    }

    @Override
    public final Float evaluate(float fraction, Number startValue, Number endValue){
        float t = mDuration * fraction;
        float b = startValue.floatValue();
        float c = endValue.floatValue() - startValue.floatValue();
        float d = mDuration;
        return calculate(t,b,c,d);
    }

    protected abstract Float calculate(float t, float b, float c, float d);
}

package com.yc.animation.easing;

import com.yc.animation.easing.BaseEasingMethod;

/**
 * 来自 https://github.com/daimajia/AnimationEasingFunctions
 * @author 杨充
 * @date 2017/6/12 17:44
 */
public class BounceEaseOut extends BaseEasingMethod {

    public BounceEaseOut(float duration) {
        super(duration);
    }

    @Override
    protected Float calculate(float t, float b, float c, float d) {
        if ((t/=d) < (1/2.75f)) {
            return c*(7.5625f*t*t) + b;
        } else if (t < (2/2.75f)) {
            return c*(7.5625f*(t-=(1.5f/2.75f))*t + .75f) + b;
        } else if (t < (2.5/2.75)) {
            return c*(7.5625f*(t-=(2.25f/2.75f))*t + .9375f) + b;
        } else {
            return c*(7.5625f*(t-=(2.625f/2.75f))*t + .984375f) + b;
        }
    }
}

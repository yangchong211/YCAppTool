package com.yc.animation.easing;

/**
 * 来自 https://github.com/daimajia/AnimationEasingFunctions
 * @author 杨充
 * @date 2017/6/12 17:56
 */
public class ElasticEaseOut extends BaseEasingMethod {

    public ElasticEaseOut(float duration) {
        super(duration);
    }

    @Override
    public Float calculate(float t, float b, float c, float d) {
        if (t==0) return b;  if ((t/=d)==1) return b+c;
        float p=d*.3f;
        float a=c;
        float s=p/4;
        return (a*(float)Math.pow(2,-10*t) * (float)Math.sin( (t*d-s)*(2*(float)Math.PI)/p ) + c + b);
    }
}

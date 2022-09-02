package com.yc.zoomimagelib;

import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

/**
 * <pre>
 *     @author yangchong
 *     GitHub : https://github.com/yangchong211/YCWidgetLib
 *     email  : yangchong211@163.com
 *     time  : 2018/7/10
 *     desc  : 差值器代理类
 *     revise:
 * </pre>
 */
class InterpolatorProxy implements Interpolator {

    private Interpolator mTarget;

    public InterpolatorProxy() {
        mTarget = new DecelerateInterpolator();
    }

    public void setTargetInterpolator(Interpolator interpolator) {
        mTarget = interpolator;
    }

    @Override
    public float getInterpolation(float input) {
        if (mTarget != null) {
            return mTarget.getInterpolation(input);
        }
        return input;
    }
}

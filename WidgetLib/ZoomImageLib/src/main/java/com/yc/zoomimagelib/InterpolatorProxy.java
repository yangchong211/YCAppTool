package com.yc.zoomimagelib;

import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

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

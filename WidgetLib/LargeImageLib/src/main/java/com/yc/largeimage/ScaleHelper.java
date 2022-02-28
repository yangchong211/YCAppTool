package com.yc.largeimage;

import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;

public class ScaleHelper {

    private long mStartTime;
    private Interpolator mInterpolator;
    private float mScale;
    private float mToScale;
    private int mStartX;
    private int mDuration;
    private boolean mFinished = true;
    private int mStartY;

    public void startScale(float scale, float toScale, int x, int y, Interpolator interpolator) {
        mStartTime = AnimationUtils.currentAnimationTimeMillis();
        mInterpolator = interpolator;
        mScale = scale;
        mToScale = toScale;
        mStartX = x;
        mStartY = y;
        float d;
        if (toScale > scale) {
            d = toScale / scale;
        } else {
            d = scale / toScale;
        }
        if (d > 4) {
            d = 4;
        }
        //倍数差值越大 执行时间越久 220 - 340
        mDuration = (int) (220 + Math.sqrt(d * 3600));
        mFinished = false;
    }

    /**
     * Call this when you want to know the new location. If it returns true, the
     * animation is not yet finished.
     */
    public boolean computeScrollOffset() {
        if (isFinished()) {
            return false;
        }
        long time = AnimationUtils.currentAnimationTimeMillis();
        // Any scroller can be used for time, since they were started
        // together in scroll mode. We use X here.
        final long elapsedTime = time - mStartTime;

        final int duration = mDuration;
        if (elapsedTime < duration) {
            final float q = mInterpolator.getInterpolation(elapsedTime / (float) duration);
            mScale = mScale + q * (mToScale - mScale);
        } else {
            mScale = mToScale;
            mFinished = true;
        }
        return true;
    }

    private boolean isFinished() {
        return mFinished;
    }

    public float getCurScale() {
        return mScale;
    }

    public int getStartX() {
        return mStartX;
    }

    public int getStartY() {
        return mStartY;
    }

}

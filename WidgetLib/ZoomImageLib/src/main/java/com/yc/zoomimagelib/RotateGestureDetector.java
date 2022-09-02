package com.yc.zoomimagelib;

import android.view.MotionEvent;

/**
 * <pre>
 *     @author yangchong
 *     GitHub : https://github.com/yangchong211/YCWidgetLib
 *     email  : yangchong211@163.com
 *     time  : 2018/7/10
 *     desc  : 旋转操作类
 *     revise:
 * </pre>
 */
public class RotateGestureDetector {

    private static final int MAX_DEGREES_STEP = 120;
    private final OnRotateListener mListener;
    private float mPrevSlope;
    private float mCurrSlope;
    private float x1;
    private float y1;
    private float x2;
    private float y2;

    public RotateGestureDetector(OnRotateListener listener) {
        mListener = listener;
    }

    public void onTouchEvent(MotionEvent event) {
        final int action = event.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_POINTER_UP:
                if (event.getPointerCount() == MotionEvent.ACTION_MOVE) {
                    mPrevSlope = calculateSlope(event);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (event.getPointerCount() > MotionEvent.ACTION_UP) {
                    mCurrSlope = calculateSlope(event);
                    double currDegrees = Math.toDegrees(Math.atan(mCurrSlope));
                    double prevDegrees = Math.toDegrees(Math.atan(mPrevSlope));
                    double deltaSlope = currDegrees - prevDegrees;
                    if (Math.abs(deltaSlope) <= MAX_DEGREES_STEP) {
                        mListener.onRotate((float) deltaSlope, (x2 + x1) / 2, (y2 + y1) / 2);
                    }
                    mPrevSlope = mCurrSlope;
                }
                break;
            default:
                break;
        }
    }

    private float calculateSlope(MotionEvent event) {
        x1 = event.getX(0);
        y1 = event.getY(0);
        x2 = event.getX(1);
        y2 = event.getY(1);
        return (y2 - y1) / (x2 - x1);
    }
}



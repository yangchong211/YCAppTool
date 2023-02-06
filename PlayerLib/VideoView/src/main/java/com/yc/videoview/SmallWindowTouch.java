package com.yc.videoview;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/8/29
 *     desc  : 小窗口触摸移动
 *     revise:
 * </pre>
 */
public class SmallWindowTouch implements View.OnTouchListener {

    private int mDownX, mDownY;
    private final int mMarginLeft;
    private final int mMarginTop;
    private int _xDelta, _yDelta;
    private final View mView;

    public SmallWindowTouch(View view, int marginLeft, int marginTop) {
        super();
        mMarginLeft = marginLeft;
        mMarginTop = marginTop;
        mView = view;
    }


    @Override
    public boolean onTouch(View view, MotionEvent event) {
        final int X = (int) event.getRawX();
        final int Y = (int) event.getRawY();
        Log.e("onTouch","---X---"+X + "---Y---"+Y);
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mDownX = X;
                mDownY = Y;

                FrameLayout.LayoutParams lParams = (FrameLayout.LayoutParams) mView.getLayoutParams();
                _xDelta = X - lParams.leftMargin;
                _yDelta = Y - lParams.topMargin;
                break;
            case MotionEvent.ACTION_UP:
                if (Math.abs(mDownY - Y) < 5 && Math.abs(mDownX - X) < 5) {
                    return false;
                } else {
                    return true;
                }
            case MotionEvent.ACTION_MOVE:
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mView.getLayoutParams();
                layoutParams.leftMargin = X - _xDelta;
                layoutParams.topMargin = Y - _yDelta;
                if (layoutParams.leftMargin >= mMarginLeft) {
                    layoutParams.leftMargin = mMarginLeft;
                }

                if (layoutParams.topMargin >= mMarginTop) {
                    layoutParams.topMargin = mMarginTop;
                }

                if (layoutParams.leftMargin <= 0) {
                    layoutParams.leftMargin = 0;
                }

                if (layoutParams.topMargin <= 0) {
                    layoutParams.topMargin = 0;
                }
                mView.setLayoutParams(layoutParams);
                break;
            default:
                break;
        }
        return false;
    }

}
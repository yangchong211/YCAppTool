package com.yc.window;

import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.View;


final class ViewTouchWrapper implements View.OnTouchListener {

    private final FloatWindow<?> mToast;
    private final FloatWindow.OnTouchListener mListener;

    ViewTouchWrapper(FloatWindow<?> toast, FloatWindow.OnTouchListener listener) {
        mToast = toast;
        mListener = listener;
    }

    @SuppressLint("ClickableViewAccessibility")
    @SuppressWarnings("unchecked")
    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if (mListener == null) {
            return false;
        }
        return mListener.onTouch(mToast, view, event);
    }
}
package com.yc.window.event;

import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.View;

import com.yc.window.FloatWindow;


public final class ViewTouchWrapper implements View.OnTouchListener {

    private final FloatWindow<?> mToast;
    private final FloatWindow.OnTouchListener mListener;

    public ViewTouchWrapper(FloatWindow<?> toast, FloatWindow.OnTouchListener listener) {
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
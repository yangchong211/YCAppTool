package com.yc.window.wrapper;

import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.View;

import com.yc.window.FloatWindow;
import com.yc.window.inter.ITouchListener;


public final class ViewTouchWrapper implements View.OnTouchListener {

    private final FloatWindow mToast;
    private final ITouchListener mListener;

    public ViewTouchWrapper(FloatWindow toast, ITouchListener listener) {
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
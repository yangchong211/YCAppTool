package com.yc.window.event;

import android.view.View;

import com.yc.window.FloatWindow;


public final class ViewClickWrapper implements View.OnClickListener {

    private final FloatWindow<?> mToast;
    private final FloatWindow.OnClickListener mListener;

    public ViewClickWrapper(FloatWindow<?> toast, FloatWindow.OnClickListener listener) {
        mToast = toast;
        mListener = listener;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final void onClick(View view) {
        if (mListener == null) {
            return;
        }
        mListener.onClick(mToast, view);
    }
}
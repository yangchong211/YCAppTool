package com.yc.window.wrapper;

import android.view.View;

import com.yc.window.FloatWindow;


public final class ViewLongClickWrapper implements View.OnLongClickListener {

    private final FloatWindow<?> mToast;
    private final FloatWindow.OnLongClickListener mListener;

    public ViewLongClickWrapper(FloatWindow<?> toast, FloatWindow.OnLongClickListener listener) {
        mToast = toast;
        mListener = listener;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final boolean onLongClick(View view) {
        if (mListener == null) {
            return false;
        }
        return mListener.onLongClick(mToast, view);
    }
}
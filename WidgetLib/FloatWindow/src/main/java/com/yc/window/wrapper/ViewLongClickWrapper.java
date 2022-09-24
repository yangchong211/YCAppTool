package com.yc.window.wrapper;

import android.view.View;

import com.yc.window.FloatWindow;
import com.yc.window.inter.ILongClickListener;


public final class ViewLongClickWrapper implements View.OnLongClickListener {

    private final FloatWindow<?> mToast;
    private final ILongClickListener mListener;

    public ViewLongClickWrapper(FloatWindow<?> floatWindow, ILongClickListener listener) {
        mToast = floatWindow;
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
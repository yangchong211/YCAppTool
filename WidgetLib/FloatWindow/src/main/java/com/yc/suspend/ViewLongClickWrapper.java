package com.yc.suspend;

import android.view.View;


final class ViewLongClickWrapper implements View.OnLongClickListener {

    private final FloatWindow<?> mToast;
    private final FloatWindow.OnLongClickListener mListener;

    ViewLongClickWrapper(FloatWindow<?> toast, FloatWindow.OnLongClickListener listener) {
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
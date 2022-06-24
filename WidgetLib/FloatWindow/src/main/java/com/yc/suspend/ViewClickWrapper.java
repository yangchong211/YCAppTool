package com.yc.suspend;

import android.view.View;


final class ViewClickWrapper implements View.OnClickListener {

    private final FloatWindow<?> mToast;
    private final FloatWindow.OnClickListener mListener;

    ViewClickWrapper(FloatWindow<?> toast, FloatWindow.OnClickListener listener) {
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
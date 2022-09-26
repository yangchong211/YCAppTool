package com.yc.window.wrapper;

import android.view.View;

import com.yc.window.FloatWindow;
import com.yc.window.inter.IClickListener;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/08/18
 *     desc  : 点击事件包装对象
 *     revise:
 * </pre>
 */
public final class ViewClickWrapper implements View.OnClickListener {

    private final FloatWindow mFloatWindow;
    private final IClickListener mListener;

    public ViewClickWrapper(FloatWindow floatWindow, IClickListener listener) {
        mFloatWindow = floatWindow;
        mListener = listener;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final void onClick(View view) {
        if (mListener == null) {
            return;
        }
        mListener.onClick(mFloatWindow, view);
    }
}
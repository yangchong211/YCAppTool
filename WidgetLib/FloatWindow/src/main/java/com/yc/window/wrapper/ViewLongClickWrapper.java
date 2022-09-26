package com.yc.window.wrapper;

import android.view.View;

import com.yc.window.FloatWindow;
import com.yc.window.inter.ILongClickListener;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/08/18
 *     desc  : 长按事件包装对象
 *     revise:
 * </pre>
 */
public final class ViewLongClickWrapper implements View.OnLongClickListener {

    private final FloatWindow mFloatWindow;
    private final ILongClickListener mListener;

    public ViewLongClickWrapper(FloatWindow floatWindow, ILongClickListener listener) {
        mFloatWindow = floatWindow;
        mListener = listener;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final boolean onLongClick(View view) {
        if (mListener == null) {
            return false;
        }
        return mListener.onLongClick(mFloatWindow, view);
    }
}
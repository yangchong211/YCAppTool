package com.yc.window.wrapper;

import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.View;

import com.yc.window.FloatWindow;
import com.yc.window.inter.ITouchListener;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/08/18
 *     desc  : 触摸事件包装对象
 *     revise:
 * </pre>
 */
public final class ViewTouchWrapper implements View.OnTouchListener {

    private final FloatWindow mFloatWindow;
    private final ITouchListener mListener;

    public ViewTouchWrapper(FloatWindow floatWindow, ITouchListener listener) {
        mFloatWindow = floatWindow;
        mListener = listener;
    }

    @SuppressLint("ClickableViewAccessibility")
    @SuppressWarnings("unchecked")
    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if (mListener == null) {
            return false;
        }
        return mListener.onTouch(mFloatWindow, view, event);
    }
}
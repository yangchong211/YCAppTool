package com.yc.window.inter;

import android.view.MotionEvent;
import android.view.View;

import com.yc.window.FloatWindow;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/08/18
 *     desc  : 定义自定义触摸事件
 *     revise:
 * </pre>
 */
public interface ITouchListener<V extends View> {

    /**
     * 触摸回调
     */
    boolean onTouch(FloatWindow floatWindow, V view, MotionEvent event);
}

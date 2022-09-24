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
public interface ITouchListener {

    /**
     * 触摸回调
     * @param floatWindow           悬浮窗view
     * @param view                  孩子view
     * @param event                 event事件
     * @return
     */
    boolean onTouch(FloatWindow floatWindow, View view, MotionEvent event);
}

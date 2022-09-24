package com.yc.window.inter;

import android.view.View;

import com.yc.window.FloatWindow;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/08/18
 *     desc  : 定义自定义长按事件
 *     revise:
 * </pre>
 */
public interface ILongClickListener<V extends View> {

    /**
     * 长按回调
     */
    boolean onLongClick(FloatWindow<?> floatWindow, V view);
}

package com.yc.window.inter;

import android.view.View;

import com.yc.window.FloatWindow;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/08/18
 *     desc  : 定义自定义点击事件
 *     revise:
 * </pre>
 */
public interface IClickListener<V extends View> {

    /**
     * 点击回调
     */
    void onClick(FloatWindow<?> toast, V view);
}

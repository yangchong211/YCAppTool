package com.yc.window.inter;

import com.yc.window.FloatWindow;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/10/21
 *     desc  : 定义生命周期接口类
 *     revise:
 * </pre>
 */
public interface ILifecycleListener {

    /**
     * 显示回调
     */
    void onShow(FloatWindow floatWindow);

    /**
     * 消失回调
     */
    void onDismiss(FloatWindow floatWindow);

    /**
     * 回收回调
     */
    void onRecycler(FloatWindow floatWindow);
}

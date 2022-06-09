package com.yc.videoview.inter;

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

    void onShow();

    void onHide();

    void onPostHide();
}

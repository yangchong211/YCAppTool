package com.yc.appstatuslib.helper.phone;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     GitHub : https://github.com/yangchong211/YCCommonLib
 *     time  : 2018/11/9
 *     desc  : 电话状态监听listener
 *     revise:
 * </pre>
 */
public interface OnPhoneListener {

    /**
     * 挂断
     */
    void callIdle();

    /**
     * 接听
     */
    void callOffHook();

    /**
     * 响铃
     */
    void callRunning();

}

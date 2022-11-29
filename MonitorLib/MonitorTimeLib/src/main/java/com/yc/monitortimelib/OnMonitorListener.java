package com.yc.monitortimelib;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time  : 2020/7/10
 *     desc  : 回调接口，开发者可自己使用
 *     revise:
 * </pre>
 */
public interface OnMonitorListener {
    /**
     * 回调
     * @param processName       进程名称
     * @param result            返回数据
     */
    void onMonitorResult(String processName, String result);
}

package com.yc.appstatuslib.listener;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time   : 2017/5/18
 *     desc   : 日志回调
 *     revise :
 * </pre>
 */
public interface AppLogListener {
    /**
     * 日志监听
     * @param info                          info信息
     */
    void log(String info);
}

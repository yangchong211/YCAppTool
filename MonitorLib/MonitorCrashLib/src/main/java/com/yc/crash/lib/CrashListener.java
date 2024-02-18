package com.yc.crash.lib;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time  : 2020/7/10
 *     desc  : 接口监听listener
 *     revise:
 * </pre>
 */
public interface CrashListener {


    /**
     * 自定义上传crash，支持开发者上传自己捕获的crash数据
     *
     * @param ex         当前异常
     * @param crashCount 当前异常出现的次数
     */
    void recordException(Throwable ex, int crashCount);

}

package com.yc.toollib.crash;

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
     * 重启app
     */
    void againStartApp();

    /**
     * 自定义上传crash，支持开发者上传自己捕获的crash数据
     * @param ex                        ex
     */
    void recordException(Throwable ex);


}

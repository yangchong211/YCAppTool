package com.ycbjie.live.config;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/9/19
 *     desc  : 需要保活的服务
 *     revise:
 * </pre>
 */
public interface KeepLiveService {
    /**
     * 运行中
     * 由于服务可能会多次自动启动，该方法可能重复调用
     */
    void onWorking();

    /**
     * 服务终止
     * 由于服务可能会被多次终止，该方法可能重复调用，需同onWorking配套使用，如注册和注销
     */
    void onStop();
}

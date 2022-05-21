package com.yc.applicationlib;

import android.app.Application;

import com.yc.spi.annotation.ServiceProviderInterface;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/11/9
 *     desc  : Application生命周期接口定义
 *     revise:
 * </pre>
 */
@ServiceProviderInterface
public interface AppLifecycleListener {

    void onCreate(Application app);

}

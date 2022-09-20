package com.yc.applicationlib;

import android.app.Application;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/11/9
 *     desc  : Application生命周期接口定义
 *     revise:
 * </pre>
 */
public interface AppLifecycleListener {

    void onCreate(Application app);

}

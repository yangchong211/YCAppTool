package com.yc.activitymanager.application;

import android.content.Context;
import android.content.res.Configuration;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/11/9
 *     desc  : Application生命周期接口定义
 *     revise:
 * </pre>
 */
public interface IApplicationHelper {
    void onCreate();

    void onTerminate();

    void onConfigurationChanged(final Configuration config);

    void onLowMemory();

    void onTrimMemory(final int level);
}

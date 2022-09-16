package com.yc.applicationlib;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

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
public interface IApplicationHelper {

    void attachBaseContext(Context base);

    void onCreate(final Application app);

    void onTerminate(final Application app);

    void onConfigurationChanged(final Application app, final Configuration config);

    void onLowMemory(final Application app);

    void onTrimMemory(final Application app, final int level);
}

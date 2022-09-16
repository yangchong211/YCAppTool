package com.yc.applicationlib;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import com.yc.spi.annotation.ServiceProvider;
import com.yc.spi.loader.ServiceLoader;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/11/9
 *     desc  : 分发 lifecycle 时确保各个 listener 出现异常时不相互影响
 *     revise:
 * </pre>
 */
public class LaunchApplicationListener extends BaseApplicationHelper {

    private final Iterable<IApplicationHelper> mApplicationListener = ServiceLoader.load(IApplicationHelper.class);
    private final Iterable<AppLifecycleListener> mAppListener = ServiceLoader.load(AppLifecycleListener.class);


    @Override
    public void attachBaseContext(Context base) {
        for (IApplicationHelper applicationLifecycleListener : mApplicationListener) {
            applicationLifecycleListener.attachBaseContext(base);
        }
    }

    @Override
    public void onConfigurationChanged(Application app, Configuration config) {
        for (IApplicationHelper applicationLifecycleListener : mApplicationListener) {
            applicationLifecycleListener.onConfigurationChanged(app, config);
        }
    }

    @Override
    public void onCreate(Application app) {
        for (IApplicationHelper applicationLifecycleListener : mApplicationListener) {
            applicationLifecycleListener.onCreate(app);
        }
        for (AppLifecycleListener appLifecycleListener : mAppListener) {
            appLifecycleListener.onCreate(app);
        }
        dispatcherOnCreate(app);
    }

    @Override
    public void onLowMemory(Application app) {
        for (IApplicationHelper applicationLifecycleListener : mApplicationListener) {
            applicationLifecycleListener.onLowMemory(app);
        }
    }

    @Override
    public void onTerminate(Application app) {
        for (IApplicationHelper applicationLifecycleListener : mApplicationListener) {
            applicationLifecycleListener.onTerminate(app);
        }
    }

    @Override
    public void onTrimMemory(Application app, int level) {
        for (IApplicationHelper applicationLifecycleListener : mApplicationListener) {
            applicationLifecycleListener.onTrimMemory(app, level);
        }
    }

    private void dispatcherOnCreate(Application app) {
        for (IApplicationHelper applicationLifecycleListener : mApplicationListener) {
            applicationLifecycleListener.onCreate(app);
        }
    }

}

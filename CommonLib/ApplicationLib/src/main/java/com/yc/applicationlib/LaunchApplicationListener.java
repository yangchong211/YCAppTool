package com.yc.applicationlib;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import com.yc.api.getIt.ServiceProvider;

import java.util.ServiceLoader;


/**
 * 分发 lifecycle 时确保各个 listener 出现异常时不相互影响
 */
@ServiceProvider(value = ApplicationListener.class, priority = -1)
public class LaunchApplicationListener extends AbstractLifecycleListener {

    private final Iterable<ApplicationListener> mApplicationListener =
            ServiceLoader.load(ApplicationListener.class);
    private final Iterable<AppLifecycleListener> mAppListener =
            ServiceLoader.load(AppLifecycleListener.class);


    @Override
    public void attachBaseContext(Context base) {
        try {
            for (ApplicationListener applicationLifecycleListener : mApplicationListener) {
                applicationLifecycleListener.attachBaseContext(base);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConfigurationChanged(Application app, Configuration config) {
        try {
            for (ApplicationListener applicationLifecycleListener : mApplicationListener) {
                applicationLifecycleListener.onConfigurationChanged(app, config);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(Application app) {
        try {
            for (ApplicationListener applicationLifecycleListener : mApplicationListener) {
                applicationLifecycleListener.onCreate(app);
            }
            for (AppLifecycleListener appLifecycleListener : mAppListener) {
                appLifecycleListener.onCreate(app);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        dispatcherOnCreate(app);
    }

    @Override
    public void onLowMemory(Application app) {
        try {
            for (ApplicationListener applicationLifecycleListener : mApplicationListener) {
                applicationLifecycleListener.onLowMemory(app);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTerminate(Application app) {
        try {
            for (ApplicationListener applicationLifecycleListener : mApplicationListener) {
                applicationLifecycleListener.onTerminate(app);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTrimMemory(Application app, int level) {
        try {
            for (ApplicationListener applicationLifecycleListener : mApplicationListener) {
                applicationLifecycleListener.onTrimMemory(app, level);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void dispatcherOnCreate(Application app) {
        try {
            for (ApplicationListener applicationLifecycleListener : mApplicationListener) {
                applicationLifecycleListener.onCreate(app);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

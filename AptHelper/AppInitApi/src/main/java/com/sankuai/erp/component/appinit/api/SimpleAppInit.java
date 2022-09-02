package com.sankuai.erp.component.appinit.api;

import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.ComponentCallbacks2;
import android.content.res.Configuration;

import com.sankuai.erp.component.appinit.common.IAppInit;


/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time   : 2019/5/11
 *     desc   : 所有方法都只会运行在你注册的进程
 *     revise :
 *     GitHub : https://github.com/yangchong211/YCAppTool
 * </pre>
 */
public abstract class SimpleAppInit implements IAppInit {
    @SuppressWarnings("checkstyle:MemberName")
    protected final String TAG;
    protected Application mApplication;
    protected boolean mIsDebug;

    public SimpleAppInit() {
        TAG = this.getClass().getSimpleName();
        mApplication = AppInitManager.get().getApplication();
        mIsDebug = AppInitManager.get().isDebug();
    }

    /**
     * 是否需要异步初始化，默认为 false
     */
    @Override
    public boolean needAsyncInit() {
        return false;
    }

    /**
     * {@link Application#onCreate()} 时调用
     */
    @Override
    public void onCreate() {
    }

    @Override
    public void asyncOnCreate() {
    }

    /**
     * {@link Application#onTerminate()} 时调用
     */
    @Override
    public void onTerminate() {
    }

    /**
     * {@link Application#onConfigurationChanged(Configuration)} 时调用
     *
     * @param newConfig The new device configuration.
     * @see ComponentCallbacks#onConfigurationChanged(Configuration)
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
    }

    /**
     * {@link Application#onLowMemory()} 时调用
     *
     * @see ComponentCallbacks#onLowMemory()
     */
    @Override
    public void onLowMemory() {
    }

    /**
     * {@link Application#onTrimMemory(int)} 时调用
     *
     * @param level The context of the trim, giving a hint of the amount of trimming the application may like to perform.
     * @see ComponentCallbacks2#onTrimMemory(int)
     */
    @Override
    public void onTrimMemory(int level) {
    }
}

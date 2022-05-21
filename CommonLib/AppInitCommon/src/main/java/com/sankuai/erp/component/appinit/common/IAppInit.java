package com.sankuai.erp.component.appinit.common;

import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.ComponentCallbacks2;
import android.content.res.Configuration;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time   : 2019/5/11
 *     desc   : 抽象初始化接口
 *     revise :
 *     GitHub : https://github.com/yangchong211/YCAppTool
 * </pre>
 */
public interface IAppInit {
    /**
     * 是否需要异步初始化，默认为 false
     */
    boolean needAsyncInit();

    /**
     * {@link Application#onCreate()} 时调用
     */
    void onCreate();

    void asyncOnCreate();

    /**
     * {@link Application#onTerminate()} 时调用
     */
    void onTerminate();

    /**
     * {@link Application#onConfigurationChanged(Configuration)} 时调用
     *
     * @param newConfig The new device configuration.
     * @see ComponentCallbacks#onConfigurationChanged(Configuration)
     */
    void onConfigurationChanged(Configuration newConfig);

    /**
     * {@link Application#onLowMemory()} 时调用
     *
     * @see ComponentCallbacks#onLowMemory()
     */
    void onLowMemory();

    /**
     * {@link Application#onTrimMemory(int)} 时调用
     *
     * @param level The context of the trim, giving a hint of the amount of trimming the application may like to perform.
     * @see ComponentCallbacks2#onTrimMemory(int)
     */
    void onTrimMemory(int level);
}

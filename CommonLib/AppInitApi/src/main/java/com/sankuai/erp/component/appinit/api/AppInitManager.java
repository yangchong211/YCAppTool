package com.sankuai.erp.component.appinit.api;

import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.ComponentCallbacks2;
import android.content.res.Configuration;

import androidx.annotation.NonNull;

import com.sankuai.erp.component.appinit.common.AppInitCallback;
import com.sankuai.erp.component.appinit.common.AppInitCommonUtils;
import com.sankuai.erp.component.appinit.common.AppInitItem;
import com.sankuai.erp.component.appinit.common.AppInitLogger;
import com.sankuai.erp.component.appinit.common.ChildInitTable;
import com.sankuai.erp.component.appinit.common.IAppInit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;



/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time   : 2019/5/11
 *     desc   : 初始化管理
 *     revise :
 *     GitHub : https://github.com/yangchong211/YCAppTool
 * </pre>
 */
public final class AppInitManager {
    private AppInitDispatcher mAppInitDispatcher;
    private boolean mIsDebug;
    private Application mApplication;
    private List<ChildInitTable> mChildInitTableList = new ArrayList<>();
    private List<AppInitItem> mAppInitItemList = new ArrayList<>();
    private AppInitCallback mAppInitCallback;
    private boolean mAbortOnNotExist = true;

    private AppInitManager() {
    }

    private static class SingletonHolder {
        private static final AppInitManager INSTANCE = new AppInitManager();
    }

    public static AppInitManager get() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 获取当前应用是否处于 debug 模式
     */
    public boolean isDebug() {
        return mIsDebug;
    }

    /**
     * 获取当前应用程序上下文
     */
    public Application getApplication() {
        return mApplication;
    }

    /**
     * 在插件中注入 ChildInitTableLis
     */
    private void injectChildInitTableList() {
    }

    /**
     * 在插件中注入 AppInitItemList
     */
    private void injectAppInitItemList() {
    }

    /**
     * 初始化 AppInitManager。在 {@link Application#onCreate()} 中调用
     *
     * @param application     AndroidManifest 中注册的 Application
     * @param appInitCallback 初始化配置回调接口
     */
    public void init(@NonNull Application application, @NonNull AppInitCallback appInitCallback) {
        mApplication = application;
        mAppInitCallback = appInitCallback;

        mAppInitCallback.onInitStart(AppInitApiUtils.isMainProcess(), AppInitApiUtils.getProcessName());
        mIsDebug = mAppInitCallback.isDebug();
        AppInitLogger.sLogger = new Logger();

        injectChildInitTableList();
        injectAppInitItemList();

        AppInitCommonUtils.timeStr("initSort ", () -> {
            // 接入方运行时自定义了排序
            Map<String, String> coordinateAheadOfMap = mAppInitCallback.getCoordinateAheadOfMap();
            if (coordinateAheadOfMap != null && !coordinateAheadOfMap.isEmpty()) {

                mAppInitItemList = AppInitCommonUtils.sortAppInitItem(mAbortOnNotExist, mChildInitTableList, coordinateAheadOfMap, new StringBuilder());
                for (AppInitItem appInitItem : mAppInitItemList) {
                    try {
                        appInitItem.appInit = (IAppInit) Class.forName(appInitItem.appInitClassName).newInstance();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            // 实例化分发器
            mAppInitDispatcher = new AppInitDispatcher(mAppInitItemList);
        });
        onCreate();
    }

    /**
     * 在 {@link Application#onCreate()} 中调用
     */
    private void onCreate() {
        AppInitCommonUtils.timeStr("总的 onCreate ", () -> mAppInitDispatcher.onCreate());

        mAppInitCallback.onInitFinished(AppInitApiUtils.isMainProcess(), AppInitApiUtils.getProcessName(),
                new ArrayList<>(mChildInitTableList), new ArrayList<>(mAppInitItemList));
    }

    /**
     * 在 {@link Application#onTerminate()} 中调用
     */
    public void onTerminate() {
        AppInitCommonUtils.timeStr("总的 onTerminate ", () -> mAppInitDispatcher.onTerminate());
    }

    /**
     * 在 {@link Application#onConfigurationChanged(Configuration)} 中调用
     *
     * @param newConfig The new device configuration.
     * @see ComponentCallbacks#onConfigurationChanged(Configuration)
     */
    public void onConfigurationChanged(Configuration newConfig) {
        AppInitCommonUtils.timeStr("总的 onConfigurationChanged ", () -> mAppInitDispatcher.onConfigurationChanged(newConfig));
    }

    /**
     * 在 {@link Application#onLowMemory()} 中调用
     *
     * @see ComponentCallbacks#onLowMemory()
     */
    public void onLowMemory() {
        AppInitCommonUtils.timeStr("总的 onLowMemory ", () -> mAppInitDispatcher.onLowMemory());
    }

    /**
     * 在 {@link Application#onTrimMemory(int)} 中调用
     *
     * @param level The context of the trim, giving a hint of the amount of trimming the application may like to perform.
     * @see ComponentCallbacks2#onTrimMemory(int)
     */
    public void onTrimMemory(int level) {
        AppInitCommonUtils.timeStr("总的 onTrimMemory ", () -> mAppInitDispatcher.onTrimMemory(level));
    }
}

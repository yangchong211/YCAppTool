package com.sankuai.erp.component.appinit.api;

import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.ComponentCallbacks2;
import android.content.res.Configuration;
import android.os.AsyncTask;

import com.sankuai.erp.component.appinit.common.AppInitCommonUtils;
import com.sankuai.erp.component.appinit.common.AppInitItem;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;


/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time   : 2019/5/11
 *     desc   : 每个模块生成一张字表
 *     revise :
 *     GitHub : https://github.com/yangchong211/YCAppTool
 * </pre>
 */
final class AppInitDispatcher {

    private static final int CONST_100 = 100;
    private List<AppInitItem> mAppInitItemList;
    private boolean mIsMainProcess;

    private BlockingQueue<AppInitItem> mAsyncOnCreateQueuedInit;
    private volatile boolean mAsyncOnCreateQueuedInitFinished;

    AppInitDispatcher(List<AppInitItem> appInitItemList) {
        mAppInitItemList = appInitItemList;
        mIsMainProcess = AppInitApiUtils.isMainProcess();
    }

    /**
     * 初始化列表是否为空
     */
    private boolean isAppInitEmpty() {
        return mAppInitItemList == null || mAppInitItemList.size() <= 0;
    }

    /**
     * 是否忽略该初始化
     */
    private boolean isIgnoreDispatch(AppInitItem appInitItem) {
        if (appInitItem.onlyForDebug && !AppInitManager.get().isDebug()) {
            return true;
        }
        boolean dispatch = (mIsMainProcess && appInitItem.isForMainProcess()) || (!mIsMainProcess && appInitItem.isNotForMainProcess());
        return !dispatch;
    }

    /**
     * 在 {@link Application#onCreate()} 中调用
     */
    void onCreate() {
        if (isAppInitEmpty()) {
            return;
        }
        mAsyncOnCreateQueuedInit = new ArrayBlockingQueue<>(mAppInitItemList.size());
        AsyncTask.THREAD_POOL_EXECUTOR.execute(this::asyncOnCreate);
        dispatch(appInitItem -> {
            appInitItem.time += AppInitCommonUtils.time(appInitItem.toString() + " onCreate ", () -> appInitItem.appInit.onCreate());
            if (appInitItem.appInit.needAsyncInit()) {
                mAsyncOnCreateQueuedInit.add(appInitItem);
            }
        });
        mAsyncOnCreateQueuedInitFinished = true;
    }

    private void asyncOnCreate() {
        AppInitItem appInitItem;
        try {
            while (true) {
                appInitItem = mAsyncOnCreateQueuedInit.poll(CONST_100, TimeUnit.MILLISECONDS);
                if (appInitItem == null) {
                    if (mAsyncOnCreateQueuedInitFinished) {
                        break;
                    } else {
                        continue;
                    }
                }

                dispatchAsyncOnCreate(appInitItem);

                if (mAsyncOnCreateQueuedInitFinished && mAsyncOnCreateQueuedInit.isEmpty()) {
                    break;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void dispatchAsyncOnCreate(AppInitItem appInitItem) {
        if (isIgnoreDispatch(appInitItem)) {
            return;
        }
        AppInitCommonUtils.time(appInitItem.toString() + " asyncOnCreate ", () -> appInitItem.appInit.asyncOnCreate());
    }

    /**
     * 在 {@link Application#onTerminate()} 中调用
     */
    void onTerminate() {
        dispatch(appInitItem -> AppInitCommonUtils.time(appInitItem.toString() + " onTerminate ", () -> appInitItem.appInit.onTerminate()));
    }

    /**
     * 在 {@link Application#onConfigurationChanged(Configuration)} 中调用
     *
     * @param newConfig The new device configuration.
     * @see ComponentCallbacks#onConfigurationChanged(Configuration)
     */
    void onConfigurationChanged(Configuration newConfig) {
        dispatch(appInitItem -> AppInitCommonUtils.time(appInitItem.toString() + " onConfigurationChanged ",
                () -> appInitItem.appInit.onConfigurationChanged(newConfig)));
    }

    /**
     * 在 {@link Application#onLowMemory()} 中调用
     *
     * @see ComponentCallbacks#onLowMemory()
     */
    void onLowMemory() {
        dispatch(appInitItem -> AppInitCommonUtils.time(appInitItem.toString() + " onLowMemory ", () -> appInitItem.appInit.onLowMemory()));
    }

    /**
     * 在 {@link Application#onTrimMemory(int)} 中调用
     *
     * @param level The context of the trim, giving a hint of the amount of trimming the application may like to perform.
     * @see ComponentCallbacks2#onTrimMemory(int)
     */
    void onTrimMemory(int level) {
        dispatch(appInitItem -> AppInitCommonUtils.time(appInitItem.toString() + " onTrimMemory ", () -> appInitItem.appInit.onTrimMemory(level)));
    }

    private void dispatch(DispatchCallback dispatchCallback) {
        if (isAppInitEmpty()) {
            return;
        }
        for (AppInitItem appInitItem : mAppInitItemList) {
            if (isIgnoreDispatch(appInitItem)) {
                continue;
            }
            dispatchCallback.dispatch(appInitItem);
        }
    }

    private interface DispatchCallback {
        void dispatch(AppInitItem appInitItem);
    }
}

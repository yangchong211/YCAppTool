package com.yc.activitymanager.application;

import android.app.Application;
import android.util.Log;


public class AppInitHelper {

    private final static String TAG = AppInitHelper.class.getSimpleName();
    private static volatile AppInitHelper singleton = null;
    //当前的操作能力
    private IApplicationHelper mAppInit;
    private Application application;

    /**
     * 获取单例
     *
     * @return 单例
     */
    public static AppInitHelper getInstance() {
        if (singleton == null) {
            synchronized (AppInitHelper.class) {
                if (singleton == null) {
                    singleton = new AppInitHelper();
                }
            }
        }
        return singleton;
    }

    public void init(Application application){
        this.application = application;
    }

    /**
     * 构造函数
     */
    private AppInitHelper() {
        //如果已存在，直接抛出异常，保证只会被new 一次，解决反射破坏单例的方案
        if (singleton != null) {
            throw new RuntimeException("对象已存在不可重复创建");
        }
        mAppInit = new BaseApplicationHelper(application);
        Log.i(TAG , "AppInitHelper");
    }


    public IApplicationHelper getAppInit() {
        return mAppInit;
    }

    public void setAppInit(IApplicationHelper globalInit) {
        this.mAppInit = globalInit;
    }
}

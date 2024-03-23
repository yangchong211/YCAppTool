package com.yc.activitymanager.application;

import android.app.Application;
import android.content.res.Configuration;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.yc.toolutils.system.AppLogUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/11/9
 *     desc  : 应用app
 *     revise:
 * </pre>
 */
public abstract class LibBaseApplication extends Application {

    private static Application mApplication;
    private static final String TAG = "BaseApplication";
    /**
     * 各个Module中针对各个Module自身特有的初始化业务；
     */
    private final List<IApplicationHelper> mApplicationHelperList = new ArrayList<>();

    public static Application getInstance() {
        return mApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        AppInitHelper.getInstance().init(mApplication);
        AppInitHelper.getInstance().getAppInit().onCreate();
        // 在基类中初始化一次各module通用的功能，
        initComponentCommonService();
        AppLogUtils.d("onCreate#mApplication=" + mApplication.getClass().getName());
        // 针对各个Module自身特有的业务，在分开打包时，各个module可以重写些方法，进行初始化业务；
        initComponentSpecificService();
        // 各个Module必须要进行的初始化业务；
        handleAllApplicationHelpersCreate();
    }

    /**
     * 初始化各个module通用的业务；
     */
    private void initComponentCommonService() {
        AppLogUtils.d("initComponentCommonService#初始化各个组件公共的业务");
    }

    /**
     * 对于各个module在单独编译时，如果有必要的业务需要处理则重写此方法；
     */
    protected void initComponentSpecificService() {
        AppLogUtils.d("initComponentSpecificService#初始化各个组件自身特有的业务#" + TAG + "里定义的空实现");
    }

    /**
     * 处理注册初始化功能的业务实例，及调用各实例的onCreate()方法；
     */
    private void handleAllApplicationHelpersCreate() {
        AppLogUtils.d("handleAllApplicationHelpersCreate");
        // 首先注册具体实例；
        registerApplicationHelper();
        // 调用各实例的初始化方法
        for (int i= 0 ; i< mApplicationHelperList.size() ; i++){
            IApplicationHelper iApplicationHelper = mApplicationHelperList.get(i);
            AppLogUtils.d("handleAllApplicationHelpersCreate#applicationHelper=" +
                    iApplicationHelper.getClass().getSimpleName());
            iApplicationHelper.onCreate();
        }
    }

    /**
     * 注册各个Module中用于初始化功能的业务实例；
     */
    protected abstract void registerApplicationHelper();

    /**
     * 根据全路径名，通过反射注册相关实例；各组件单独编译时，其它组件会报异常
     *
     * @param className
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected void registerTargetApplicationHelper(String className) {
        BaseApplicationHelper applicationHelper = null;
        try {
            Class<? extends BaseApplicationHelper> clazz = (Class<? extends BaseApplicationHelper>) Class.forName(className);
            //获取构造函数
            Constructor<? extends BaseApplicationHelper> constructor = clazz.getConstructor(Application.class);
            //创建对象
            applicationHelper = constructor.newInstance(LibBaseApplication.this);
        } catch (IllegalAccessException | InstantiationException 
                | ClassNotFoundException | NoSuchMethodException 
                | InvocationTargetException e) {
            e.printStackTrace();
            AppLogUtils.e("registerApplicationHelper#e=" + e.getMessage());
        } finally {
            if (applicationHelper != null) {
                mApplicationHelperList.add(applicationHelper);
            }
        }
    }

    /**
     * 根据全路径名的数组，通过反射注册相关实例；各组件单独编译时，其它组件会报异常
     *
     * @param classNameArray        数组
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected void registerApplicationHelperArray(String[] classNameArray) {
        for (String className : classNameArray) {
            BaseApplicationHelper applicationHelper = null;
            try {
                Class<? extends BaseApplicationHelper> clazz = (Class<? extends BaseApplicationHelper>) Class.forName(className);
                Constructor<? extends BaseApplicationHelper> constructor = clazz.getConstructor(Application.class);
                applicationHelper = constructor.newInstance(LibBaseApplication.this);
            } catch (IllegalAccessException | InstantiationException | ClassNotFoundException 
                    | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
                AppLogUtils.e("registerApplicationHelperArray#e=" + e.getMessage());
            } finally {
                if (applicationHelper != null) {
                    mApplicationHelperList.add(applicationHelper);
                }
            }
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        for (int i= 0 ; i< mApplicationHelperList.size() ; i++){
            IApplicationHelper iApplicationHelper = mApplicationHelperList.get(i);
            iApplicationHelper.onTerminate();
        }
        AppInitHelper.getInstance().getAppInit().onTerminate();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        for (int i= 0 ; i< mApplicationHelperList.size() ; i++){
            IApplicationHelper iApplicationHelper = mApplicationHelperList.get(i);
            iApplicationHelper.onLowMemory();
        }
        AppInitHelper.getInstance().getAppInit().onLowMemory();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        for (int i= 0 ; i< mApplicationHelperList.size() ; i++){
            IApplicationHelper iApplicationHelper = mApplicationHelperList.get(i);
            iApplicationHelper.onConfigurationChanged(newConfig);
        }
        AppInitHelper.getInstance().getAppInit().onConfigurationChanged(newConfig);
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        for (int i= 0 ; i< mApplicationHelperList.size() ; i++){
            IApplicationHelper iApplicationHelper = mApplicationHelperList.get(i);
            iApplicationHelper.onTrimMemory(level);
        }
        AppInitHelper.getInstance().getAppInit().onTrimMemory(level);
    }
}

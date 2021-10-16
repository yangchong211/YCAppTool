package com.yc.toolutils.activity;

import android.app.Activity;
import android.app.Application;

import java.util.Stack;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time  : 2026/8/10
 *     desc  : 堆栈管理
 *     revise:
 * </pre>
 */
public class ActivityManager implements IActivityManager<Activity> {

    private static ActivityManager sInstance;
    private final Stack<Activity> mActivityStacks = new Stack<>();
    private final ProxyActivityListener mProxyActivityListener =
            new ProxyActivityListener(this);

    public static ActivityManager getInstance() {
        if (sInstance == null) {
            synchronized (ActivityManager.class) {
                if (sInstance == null) {
                    sInstance = new ActivityManager();
                }
            }
        }
        return sInstance;
    }

    /**
     * 主界面Activity是否存在
     */
    @Override
    public boolean isExist(Class clazz) {
        if (mActivityStacks == null || mActivityStacks.empty()) {
            return false;
        }

        for (int i = 0; i < mActivityStacks.size(); i++) {
            if (mActivityStacks.get(i).getClass().getSimpleName()
                    .equals(clazz.getSimpleName())) {
                return true;
            }
        }
        return false;
    }


    /**
     * 查找指定的Activity
     *
     * @param clazz 目标Activity
     */
    @Override
    public Activity get(Class clazz) {
        if (mActivityStacks == null || mActivityStacks.empty()) {
            return null;
        }

        for (int i = 0; i < mActivityStacks.size(); i++) {
            if (mActivityStacks.get(i).getClass().getSimpleName()
                    .equals(clazz.getSimpleName())) {
                return mActivityStacks.get(i);
            }
        }
        return null;
    }

    public void init(Application application) {
        application.registerActivityLifecycleCallbacks(mProxyActivityListener);
    }

    @Override
    public boolean isEmpty() {
        return mActivityStacks == null || mActivityStacks.empty();
    }

    /**
     * 获取栈顶的Activity
     *
     * @return 可能为空
     */
    @Override
    public Activity peek() {
        if (mActivityStacks == null || mActivityStacks.size() == 0) {
            return null;
        }
        return mActivityStacks.peek();
    }

    /**
     * 移除栈顶的activity
     */
    @Override
    public void pop() {
        if (mActivityStacks == null || mActivityStacks.size() == 0) {
            return;
        }

        Activity activity = mActivityStacks.pop();
        if (activity != null) {
            activity.finish();
        }
    }

    /**
     * 注册activity的声明周期变换
     *
     * @param clazz             目标activity
     * @param lifecycleListener 对应的监听器
     */
    @Override
    public void registerActivityLifecycleListener(Class clazz,
                                                  ActivityLifecycleListener lifecycleListener) {
        if (clazz == null || lifecycleListener == null) {
            return;
        }
        mProxyActivityListener.registerActivityLifecycleListener(clazz, lifecycleListener);
    }

    @Override
    public void unregisterActivityLifecycleListener(Class clazz,
                                                    ActivityLifecycleListener lifecycleListener) {
        if (clazz == null || lifecycleListener == null) {
            return;
        }
        mProxyActivityListener.unregisterActivityLifecycleListener(clazz, lifecycleListener);
    }

    /**
     * 添加 activity
     *
     * @param activity {@link Activity}
     */
    @Override
    public void add(Activity activity) {
        if (activity != null) {
            mActivityStacks.add(activity);
        }
    }

    /**
     * 移除 activity
     * @param activity {@link Activity}
     */
    @Override
    public void remove(Activity activity) {
        if (activity != null) {
            mActivityStacks.remove(activity);
        }
    }

    /**
     * 结束指定的Activity
     * @param activity {@link Activity}
     */
    @Override
    public void finish(Activity activity) {
        if (activity != null && !activity.isFinishing()) {
            mActivityStacks.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    @Override
    public void remove(Class<?> cls) {
        for (Activity activity : mActivityStacks) {
            if (activity.getClass().equals(cls)) {
                finish(activity);
            }
        }
    }


    /**
     * 结束所有Activity
     */
    @Override
    public void finishAll() {
        for (int i = 0, size = mActivityStacks.size(); i < size; i++) {
            if (null != mActivityStacks.get(i)) {
                mActivityStacks.get(i).finish();
            }
        }
        mActivityStacks.clear();
    }

    /**
     * 退出应用程序
     */
    @Override
    public void appExist() {
        try {
            //finish所有activity
            finishAll();
            //杀死进程
            android.os.Process.killProcess(android.os.Process.myPid());
        } catch (Exception ignored) {
            //ignored.printStackTrace();
        } finally {
            System.exit(0);
        }
    }

}

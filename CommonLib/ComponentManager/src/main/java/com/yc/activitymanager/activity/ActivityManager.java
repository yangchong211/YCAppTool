/*
Copyright 2017 yangchong211（github.com/yangchong211）

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package com.yc.activitymanager.activity;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     GitHub : <a href="https://github.com/yangchong211/YCCommonLib">...</a>
 *     time  : 2016/8/10
 *     desc  : 堆栈管理
 *     revise:
 * </pre>
 */
public class ActivityManager implements IActivityManager<Activity> {

    /**
     * 单例对象
     */
    private volatile static ActivityManager sInstance;
    /**
     * 记录activity任务栈
     */
    private final Stack<Activity> mActivityStacks = new Stack<>();
    /**
     * 常见代理类监听
     */
    private final ProxyActivityListener mProxyActivityListener =
            new ProxyActivityListener(this);
    /**
     * app退出监听
     */
    private OnExitListener onExitListener;
    /**
     * 是否初始化
     */
    private boolean mInit = false;

    /**
     * 单例模式
     *
     * @return ActivityManager对象
     */
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
     * 判断界面Activity是否存在
     */
    @Override
    public boolean isExist(Class<?> clazz) {
        if (mActivityStacks.empty()) {
            return false;
        }

        for (int i = 0; i < mActivityStacks.size(); i++) {
            String simpleName = clazz.getSimpleName();
            if (mActivityStacks.get(i).getClass().getSimpleName().equals(simpleName)) {
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
    public Activity get(Class<?> clazz) {
        if (mActivityStacks.empty()) {
            return null;
        }

        for (int i = 0; i < mActivityStacks.size(); i++) {
            Activity activity = mActivityStacks.get(i);
            if (activity.getClass().getSimpleName().equals(clazz.getSimpleName())) {
                return mActivityStacks.get(i);
            }
        }
        return null;
    }

    /**
     * 初始化操作
     *
     * @param application 全局上下文
     */
    protected void init(Application application) {
        if (mInit) {
            return;
        }
        if (application == null) {
            throw new NullPointerException("init activity manager , context must be null");
        }
        //注册全局监听
        application.registerActivityLifecycleCallbacks(mProxyActivityListener);
        mInit = true;
    }

    /**
     * 判断activity任务栈是否为空
     *
     * @return 是否为空
     */
    @Override
    public boolean isEmpty() {
        return mActivityStacks.empty();
    }

    /**
     * 获取栈顶的Activity
     *
     * @return 可能为空
     */
    @Override
    public Activity peek() {
        if (mActivityStacks.size() == 0) {
            return null;
        }
        return mActivityStacks.peek();
    }

    /**
     * 移除栈顶的activity
     */
    @Override
    public void pop() {
        if (mActivityStacks.size() == 0) {
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
                                                  AbsLifecycleListener lifecycleListener) {
        if (clazz == null || lifecycleListener == null) {
            return;
        }
        mProxyActivityListener.registerActivityLifecycleListener(clazz, lifecycleListener);
    }

    /**
     * 解绑注册activity的声明周期变换
     *
     * @param clazz             目标activity
     * @param lifecycleListener 对应的监听器
     */
    @Override
    public void unregisterActivityLifecycleListener(Class clazz,
                                                    AbsLifecycleListener lifecycleListener) {
        if (clazz == null || lifecycleListener == null) {
            return;
        }
        mProxyActivityListener.unregisterActivityLifecycleListener(clazz, lifecycleListener);
    }

    /**
     * 添加 activity 入栈
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
     * 移除 activity 出栈
     *
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
     *
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
        if (mActivityStacks.size() == 0) {
            return;
        }
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
            Activity activity = peek();
            if (activity != null && !activity.isDestroyed()) {
                //先回退到桌面，然后在杀死进程
                activity.moveTaskToBack(true);
            }
            //finish所有activity
            finishAll();
            //做一些资源释放工作
            if (getOnExitListener() != null) {
                getOnExitListener().deInit();
            }
        } catch (Exception ignored) {
            //ignored.printStackTrace();
        }
        killCurrentProcess(false);
    }


    /**
     * 杀死进程操作，默认为异常退出
     * System.exit(0)是正常退出程序，而System.exit(1)或者说非0表示非正常退出程序
     * System.exit(1)一般放在catch块中，当捕获到异常，需要停止程序。这个status=1是用来表示这个程序是非正常退出。
     * <p>
     * 为何要杀死进程：如果不主动退出进程，重启后会一直黑屏，所以加入主动杀掉进程
     *
     * @param isThrow 是否是异常退出
     */
    public void killCurrentProcess(boolean isThrow) {
        //需要杀掉原进程，否则崩溃的app处于黑屏,卡死状态
        android.os.Process.killProcess(android.os.Process.myPid());
        if (isThrow) {
            System.exit(10);
        } else {
            System.exit(0);
        }
    }


    /**
     * 返回AndroidManifest.xml中注册的所有Activity的class
     *
     * @param context     环境
     * @param packageName 包名
     * @param excludeList 排除class列表
     * @return
     */
    public List<Class> getActivitiesClass(Context context, String packageName,
                                          List<Class> excludeList) {
        List<Class> returnClassList = new ArrayList<>();
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(
                    packageName, PackageManager.GET_ACTIVITIES);
            if (packageInfo.activities != null) {
                for (ActivityInfo ai : packageInfo.activities) {
                    Class c;
                    try {
                        c = Class.forName(ai.name);
                        if (Activity.class.isAssignableFrom(c)) {
                            returnClassList.add(c);
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                if (excludeList != null) {
                    returnClassList.removeAll(excludeList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnClassList;
    }

    /**
     * 判断activity是否处于栈顶
     *
     * @return true在栈顶false不在栈顶
     */
    public boolean isActivityTop(Context context, String activityName) {
        if (TextUtils.isEmpty(activityName)) {
            return false;
        }
        android.app.ActivityManager manager = (android.app.ActivityManager)
                context.getSystemService(Context.ACTIVITY_SERVICE);
        if (manager.getRunningTasks(1) == null || (manager.getRunningTasks(1)
                != null && manager.getRunningTasks(1).size() <= 0)) {
            return false;
        }
        if ((manager.getRunningTasks(1).get(0) == null
                || (manager.getRunningTasks(1).get(0) != null
                && manager.getRunningTasks(1).get(0).topActivity == null))) {
            return false;
        }
        String name = manager.getRunningTasks(1).get(0).topActivity.getClassName();
        if (TextUtils.isEmpty(name)) {
            return false;
        }
        return name.equals(activityName);
    }

    public OnExitListener getOnExitListener() {
        return onExitListener;
    }

    public void setOnExitListener(OnExitListener onExitListener) {
        this.onExitListener = onExitListener;
    }
}

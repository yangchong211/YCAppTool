package com.yc.leak;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.SystemClock;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.yc.easyexecutor.DelegateTaskExecutor;
import com.yc.fragmentmanager.FragmentLifecycleListener;
import com.yc.fragmentmanager.FragmentManager;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

public final class AppLeakHelper {

    private static volatile AppLeakHelper INSTANCE = null;
    private final WeakHashMap<Activity,String> activityWeakHashMap = new WeakHashMap<>();
    private final WeakHashMap<androidx.fragment.app.Fragment,String> fragmentWeakHashMap = new WeakHashMap<>();
    private final WeakHashMap<Object,String> objectWeakHashMap = new WeakHashMap<>();
    private boolean isOpen = true;
    //延迟检测时间
    private static final int DELAY_TIME = 5 * 1000;
    private final Handler mHandler;
    private Runnable lastRunnable;

    public static AppLeakHelper getInstance() {
        if (INSTANCE == null) {
            synchronized (AppLeakHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new AppLeakHelper();
                }
            }
        }
        return INSTANCE;
    }


    /**
     * 构造函数
     */
    private AppLeakHelper() {
        HandlerThread mLeakThread = new HandlerThread("leakThread");
        mLeakThread.start();
        mHandler = new Handler(mLeakThread.getLooper());
    }

    /**
     * 是否开启检测
     * @return 结果
     */
    public boolean isOpen() {
        return isOpen;
    }

    /**
     * 设置是否要检测
     * @param open 是否检测
     */
    public AppLeakHelper setOpen(boolean open) {
        isOpen = open;
        return INSTANCE;
    }

    /**
     * 内存泄漏检测
     */
    public AppLeakHelper startWork(Application application){
        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                if (activity instanceof FragmentActivity){
                    FragmentManager fragmentManager = FragmentManager.Companion.getInstance();
                    fragmentManager.registerActivityLifecycleListener(
                            (FragmentActivity) activity,lifecycleListener);
                }
            }
            @Override
            public void onActivityStarted(Activity activity) {}
            @Override
            public void onActivityResumed(Activity activity) {}
            @Override
            public void onActivityPaused(Activity activity) {}
            @Override
            public void onActivityStopped(Activity activity) {}
            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {}

            @Override
            public void onActivityDestroyed(Activity activity) {
                //UI线程空闲的时候才会执行，不会造成卡顿
                monitorActivity(activity);
                if (activity instanceof FragmentActivity){
                    FragmentManager.Companion.getInstance().unregisterActivityLifecycleListener(
                            (FragmentActivity) activity,lifecycleListener);
                }
            }
        });
        return INSTANCE;
    }

    private final FragmentLifecycleListener lifecycleListener = new FragmentLifecycleListener() {
        @Override
        public void onFragmentCreated(@NotNull androidx.fragment.app.FragmentManager fm,
                                      @NotNull androidx.fragment.app.Fragment f,
                                      @Nullable Bundle savedInstanceState) {
            super.onFragmentCreated(fm, f, savedInstanceState);
        }

        @Override
        public void onFragmentDestroyed(@NotNull androidx.fragment.app.FragmentManager fm,
                                        @NotNull androidx.fragment.app.Fragment f) {
            super.onFragmentDestroyed(fm, f);
            monitorFragment(f);
        }
    };

    /**
     * 监控Fragment的泄漏，要在onDestroy中调用
     * @param fragment 监控对象
     */
    public void monitorFragment(androidx.fragment.app.Fragment fragment){
        fragmentWeakHashMap.put(fragment,fragment.getClass().getSimpleName());
        //UI线程空闲的时候才会执行，不会造成卡顿
        monitor(fragmentWeakHashMap);
    }

    /**
     * 监控带生命周期对象的泄漏，要在需要释放的时候调用
     * @param object 监控对象
     */
    public void monitorObject(Object object){
        objectWeakHashMap.put(object,object.getClass().getSimpleName());
        //UI线程空闲的时候才会执行，不会造成卡顿
        monitor(objectWeakHashMap);
    }

    /**
     * 注意：UI线程空闲的时候才会执行，不会造成卡顿
     */
    private void monitorActivity(Activity activity) {
        activityWeakHashMap.put(activity,activity.getClass().getSimpleName());
        monitor(activityWeakHashMap);
    }

    /**
     * 开始监控
     * @param weakHashMap 监控的对象
     * @param <T> 类型
     */
    private  <T> void monitor(WeakHashMap<T,String> weakHashMap){
        //如果不开启监控的话直接返回
        if(!isOpen){
            return;
        }
        //UI线程空闲的时候才会执行，不会造成卡顿
        Looper.myQueue().addIdleHandler(() -> {
            //开始时间
            long begin = System.currentTimeMillis();
            //开始启动一次gc，正常耗时20ms
            Runtime.getRuntime().gc();
            //保证多次触发，只会执行最后一次检测
            if(lastRunnable != null){
                mHandler.removeCallbacks(lastRunnable);
            }
            lastRunnable = () -> {
                try {
                    //执行一次回收，大概耗时20ms
                    Runtime.getRuntime().gc();
                    //延迟时间
                    long delayTime = 100;
                    //gc之后延迟一定时间，耗时100ms
                    SystemClock.sleep(delayTime);
                    //调用所有对象的finalize方法，释放对象，耗时1ms
                    System.runFinalization();
                    //开始检测泄漏
                    HashMap<String, Integer> hashMap = new HashMap<>();
                    for (Map.Entry<T, String> activityStringEntry : weakHashMap.entrySet()) {
                        String name = activityStringEntry.getKey().getClass().getName();
                        Integer value = hashMap.get(name);
                        //统计一个泄漏出现的次数
                        if (value == null) {
                            hashMap.put(name, 1);
                        } else {
                            hashMap.put(name, value + 1);
                        }
                    }
                    onLeaksNext(System.currentTimeMillis() - begin - delayTime - DELAY_TIME,hashMap);
                }catch (Exception e){
                    e.printStackTrace();
                }
            };
            //延迟一定时间之后开始检测泄漏的情况
            mHandler.postDelayed(lastRunnable, DELAY_TIME);
            return false;
        });
    }

    private OnLeaksListener onLeaksListener;

    //供外部调用的set方法
    public AppLeakHelper setOnLeaksListener(OnLeaksListener onLeaksListener) {
        this.onLeaksListener = onLeaksListener;
        return INSTANCE;
    }

    private void onLeaksNext(long cosTime,HashMap<String, Integer> hashMap){
        if(this.onLeaksListener != null){
            DelegateTaskExecutor.getInstance().postToMainThread(new Runnable() {
                @Override
                public void run() {
                    onLeaksListener.onLeaks(cosTime,hashMap);
                }
            });
        }
    }

}

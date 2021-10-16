package com.yc.toolutils.activity;

public interface IActivityManager<T> {

    T get(Class clazz);

    T peek();

    void pop();

    boolean isEmpty();

    void add(T activity);

    void remove(T activity);

    void remove(Class<?> cls);

    void finish(T activity);

    void finishAll();

    boolean isExist(Class clazz);

    void appExist();

    void registerActivityLifecycleListener(Class clazz, ActivityLifecycleListener lifecycleListener);

    void unregisterActivityLifecycleListener(Class clazz, ActivityLifecycleListener lifecycleListener);

}

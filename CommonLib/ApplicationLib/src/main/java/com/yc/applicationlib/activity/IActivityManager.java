package com.yc.applicationlib.activity;

public interface IActivityManager<T> {

    T get(Class<T> clazz);

    T peek();

    void pop();

    boolean isEmpty();

    void add(T activity);

    void remove(T activity);

    void remove(Class<?> cls);

    void finish(T activity);

    void finishAll();

    boolean isExist(Class<T> clazz);

    void appExist();

    void registerActivityLifecycleListener(Class<T> clazz, ActivityLifecycleListener lifecycleListener);

    void unregisterActivityLifecycleListener(Class<T> clazz, ActivityLifecycleListener lifecycleListener);

}

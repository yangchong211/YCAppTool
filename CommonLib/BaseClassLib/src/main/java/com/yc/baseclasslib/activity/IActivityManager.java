package com.yc.baseclasslib.activity;
/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/11/9
 *     desc  : 接口
 *     revise:
 * </pre>
 */
public interface IActivityManager<T> {

    /**
     *
     * @param clazz
     * @return
     */
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

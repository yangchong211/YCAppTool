package com.yc.activitymanager;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     GitHub : https://github.com/yangchong211/YCCommonLib
 *     time  : 2018/11/9
 *     desc  : 接口
 *     revise:
 * </pre>
 */
public interface IActivityManager<T> {

    /**
     * 获取某个class
     *
     * @param clazz clazz
     * @return T
     */
    T get(Class<T> clazz);

    /**
     * 获取栈顶的class
     *
     * @return T
     */
    T peek();

    /**
     * pop元素
     */
    void pop();

    /**
     * 判断是否为空
     *
     * @return true表示为空
     */
    boolean isEmpty();

    /**
     * 添加元素
     *
     * @param activity activity
     */
    void add(T activity);

    /**
     * 移除
     *
     * @param activity activity
     */
    void remove(T activity);

    /**
     * 移除
     *
     * @param cls cls
     */
    void remove(Class<?> cls);

    /**
     * 关闭某个页面
     *
     * @param activity activity
     */
    void finish(T activity);

    /**
     * 关闭所有的页面
     */
    void finishAll();

    /**
     * 判断某个页面是否存在
     *
     * @param clazz clazz
     * @return true表示存在
     */
    boolean isExist(Class<T> clazz);

    /**
     * 推出app
     */
    void appExist();

    /**
     * 注册activity生命周期监听
     *
     * @param clazz             clazz
     * @param lifecycleListener 监听
     */
    void registerActivityLifecycleListener(Class<T> clazz,
                                           ActivityLifecycleListener lifecycleListener);

    /**
     * 移除activity生命周期监听
     *
     * @param clazz             clazz
     * @param lifecycleListener 监听
     */
    void unregisterActivityLifecycleListener(Class<T> clazz,
                                             ActivityLifecycleListener lifecycleListener);

}

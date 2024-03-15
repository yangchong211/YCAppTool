package com.yc.store.lru.lib;

import java.util.Set;


/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time  : 2020/8/6
 *     desc  : 缓存接口
 *     revise: 内存缓存，主要使用到了淘汰算法
 * </pre>
 */
public interface ILruCache<K, V> {

    /**
     * 重制大小
     *
     * @param maxSize 大小
     */
    void resize(int maxSize);

    /**
     * 获取元素
     *
     * @param key key
     * @return V
     */
    V get(K key);

    /**
     * 插入元素
     *
     * @param key   key
     * @param value value
     * @return
     */
    V put(K key, V value);

    /**
     * 移除元素
     *
     * @param key key
     * @return V
     */
    V remove(K key);

    /**
     * 判断是否包含key元素
     *
     * @param key key
     * @return V
     */
    boolean containsKey(K key);

    /**
     * 获取所有元素的键
     *
     * @return set集合
     */
    Set<K> keySet();

    /**
     * 最大值
     *
     * @return size
     */
    int maxSize();

    /**
     * 当前缓存大小
     *
     * @return size
     */
    int size();

    /**
     * 清除缓存中所有的内容
     */
    void clear();
}

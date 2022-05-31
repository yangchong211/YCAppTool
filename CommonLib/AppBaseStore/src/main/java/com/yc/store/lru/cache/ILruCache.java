package com.yc.store.lru.cache;

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

    void resize(int maxSize);

    V get(K key);

    V put(K key, V value);

    V remove(K key);

    boolean containsKey(K key);

    Set<K> keySet();

    int maxSize();

    int size();

    /**
     * 清除缓存中所有的内容
     */
    void clear();
}

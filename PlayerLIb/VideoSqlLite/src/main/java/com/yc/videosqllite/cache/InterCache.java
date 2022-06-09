package com.yc.videosqllite.cache;

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
public interface InterCache<K, V> {

    /**
     * 返回当前缓存已占用的总 size
     *
     * @return {@code size}
     */
    int size();

    /**
     * 返回当前缓存所能允许的最大 size
     *
     * @return {@code maxSize}
     */
    int getMaxSize();

    /**
     * 返回这个 {@code key} 在缓存中对应的 {@code value}, 如果返回 {@code null} 说明这个 {@code key} 没有对应的 {@code value}
     *
     * @param key {@code key}
     * @return {@code value}
     */
    V get(K key);

    /**
     * 将 {@code key} 和 {@code value} 以条目的形式加入缓存,如果这个 {@code key} 在缓存中已经有对应的 {@code value}
     * 则此 {@code value} 被新的 {@code value} 替换并返回,如果为 {@code null} 说明是一个新条目
     *
     * @param key   {@code key}
     * @param value {@code value}
     * @return 如果这个 {@code key} 在容器中已经储存有 {@code value}, 则返回之前的 {@code value} 否则返回 {@code null}
     */
    V put(K key, V value);

    /**
     * 移除缓存中这个 {@code key} 所对应的条目,并返回所移除条目的 value
     * 如果返回为 {@code null} 则有可能时因为这个 {@code key} 对应的 value 为 {@code null} 或条目不存在
     *
     * @param key {@code key}
     * @return 如果这个 {@code key} 在容器中已经储存有 {@code value} 并且删除成功则返回删除的 {@code value}, 否则返回 {@code null}
     */
    V remove(K key);

    /**
     * 如果这个 {@code key} 在缓存中有对应的 value 并且不为 {@code null}, 则返回 {@code true}
     *
     * @param key {@code key}
     * @return {@code true} 为在容器中含有这个 {@code key}, 否则为 {@code false}
     */
    boolean containsKey(K key);

    /**
     * 返回当前缓存中含有的所有 {@code key}
     *
     * @return {@code keySet}
     */
    Set<K> keySet();

    /**
     * 清除缓存中所有的内容
     */
    void clear();
}

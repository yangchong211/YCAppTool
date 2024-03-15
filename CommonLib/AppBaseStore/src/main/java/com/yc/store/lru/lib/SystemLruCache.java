package com.yc.store.lru.lib;

import android.annotation.SuppressLint;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;


/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time  : 2020/8/6
 *     desc  : LruCache
 *     revise:
 * </pre>
 */
public class SystemLruCache<K, V> implements ILruCache<K, V> {

    /**
     * LRU 控制
     */
    private final LinkedHashMap<K, V> map;
    /**
     * 当前缓存占用
     */
    private int size;
    /**
     * 最大缓存容量
     */
    private int maxSize;

    // 以下属性用于数据统计
    /**
     * 设置数据次数
     */
    private int putCount;
    /**
     * 创建数据次数
     */
    private int createCount;
    /**
     * 淘汰数据次数
     */
    private int evictionCount;
    /**
     * 缓存命中次数
     */
    private int hitCount;
    /**
     * 缓存未命中数
     */
    private int missCount;

    /**
     * 由于缓存空间不可能设置无限大，所以开发者需要在构造方法中设置缓存的最大内存容量 maxSize。
     * @param maxSize       最大值
     */
    public SystemLruCache(int maxSize) {
        if (maxSize <= 0) {
            throw new IllegalArgumentException("maxSize <= 0");
        }
        this.maxSize = maxSize;
        //创建map集合
        //第一个参数：
        //第二个参数：
        //第三个参数：
        // 创建 LinkedHashMap 对象，并使用 LRU 排序模式
        this.map = new LinkedHashMap<K, V>(0, 0.75f, true);
    }

    @Override
    public void resize(int maxSize) {
        if (maxSize <= 0) {
            throw new IllegalArgumentException("maxSize <= 0");
        }

        synchronized (this) {
            this.maxSize = maxSize;
        }
        trimToSize(maxSize);
    }

    /**
     * 具体来说，判断map中是否含有key值value值，若存在，则hitCount（击中元素数量）自增，并返回Value值，
     * 若没有击中，则执行create(key)方法，这里看到create方法是一个空的实现方法，返回值为null，
     * 所以可以重写该方法，在调用get（key）的时候若没有找到value值，则自动创建一个value值并压入map中。
     * @param key   key
     * @return
     */
    @Override
    public final V get(K key) {
        if (key == null) {
            throw new NullPointerException("key == null");
        }

        V mapValue;
        synchronized (this) {
            mapValue = map.get(key);
            if (mapValue != null) {
                hitCount++;
                return mapValue;
            }
            missCount++;
        }

        V createdValue = create(key);
        if (createdValue == null) {
            return null;
        }

        synchronized (this) {
            createCount++;
            mapValue = map.put(key, createdValue);

            if (mapValue != null) {
                // There was a conflict so undo that last put
                map.put(key, mapValue);
            } else {
                size += safeSizeOf(key, createdValue);
            }
        }

        if (mapValue != null) {
            entryRemoved(false, key, createdValue, mapValue);
            return mapValue;
        } else {
            trimToSize(maxSize);
            return createdValue;
        }
    }

    /**
     * 插入一个键值对数据
     * @param key   key
     * @param value value
     * @return  V数据
     */
    @Override
    public final V put(K key, V value) {
        if (key == null || value == null) {
            throw new NullPointerException("key == null || value == null");
        }

        V previous;
        synchronized (this) {
            putCount++;
            size += safeSizeOf(key, value);
            //这里判断map.put方法的返回值是否为空
            previous = map.put(key, value);
            if (previous != null) {
                //如果不为空的话，则说明我们刚刚并没有将我么你的键值对压入Map中，所以这里的size需要自减；
                size -= safeSizeOf(key, previous);
            }
        }

        //这里判断previous是否为空，如果不为空的话，调用了一个空的实现方法entryRemoved()
        //也就是说我们可以实现自己的LruCache并在添加缓存的时候若存在该缓存可以重写这个方法；
        if (previous != null) {
            entryRemoved(false, key, previous, value);
        }

        trimToSize(maxSize);
        return previous;
    }

    /**
     * 自动淘汰数据
     * 该方法主要是判断该Map的大小是否已经达到阙值，若达到，则将Map队尾的元素（最不常使用的元素）remove掉。
     * @param maxSize   最大值
     */
    private void trimToSize(int maxSize) {
        // 淘汰数据直到不超过最大容量限制
        //循环遍历
        while (true) {
            K key;
            V value;
            synchronized (this) {
                //如果为空，则抛出异常
                if (size < 0 || (map.isEmpty() && size != 0)) {
                    throw new IllegalStateException(getClass().getName()
                            + ".sizeOf() is reporting inconsistent results!");
                }
                // 不超过最大容量限制，跳出
                if (size <= maxSize) {
                    break;
                }

                //取最早的数据
                Map.Entry<K, V> toEvict = null;
                Set<Map.Entry<K, V>> entries = map.entrySet();
                for (Map.Entry<K, V> entry : entries) {
                    toEvict = entry;
                    break;
                }
                // toEvict 为 null 说明没有更多数据
                if (toEvict == null) {
                    break;
                }

                key = toEvict.getKey();
                value = toEvict.getValue();
                //移除
                map.remove(key);
                //逐次减去1，减去旧 Value 内存占用
                size -= safeSizeOf(key, value);
                //统计淘汰计数
                evictionCount++;
            }

            //数据移除回调（value -> null）
            entryRemoved(true, key, value, null);
        }
    }

    @Override
    public final V remove(K key) {
        if (key == null) {
            throw new NullPointerException("key == null");
        }

        V previous;
        synchronized (this) {
            previous = map.remove(key);
            if (previous != null) {
                size -= safeSizeOf(key, previous);
            }
        }

        if (previous != null) {
            entryRemoved(false, key, previous, null);
        }

        return previous;
    }

    /**
     * 这里是参考glide中的lru缓存策略，低内存的时候清除
     * @param level             level级别
     */
    public void trimMemory(int level) {
        if (level >= android.content.ComponentCallbacks2.TRIM_MEMORY_BACKGROUND) {
            // Entering list of cached background apps
            // Evict our entire bitmap cache
            clearMemory();
        } else if (level >= android.content.ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN || level == android.content.ComponentCallbacks2.TRIM_MEMORY_RUNNING_CRITICAL) {
            // The app's UI is no longer visible, or app is in the foreground but system is running
            // critically low on memory
            // Evict oldest half of our bitmap cache
            trimToSize(maxSize() / 2);
        }
    }

    /**
     * 清除掉所有的内存数据
     */
    public void clearMemory() {
        trimToSize(0);
    }

    @Override
    public boolean containsKey(K key) {
        return map.containsKey(key);
    }

    @Override
    public Set<K> keySet() {
        return map.keySet();
    }

    protected void entryRemoved(boolean evicted, K key, V oldValue, V newValue) {

    }

    protected V create(K key) {
        return null;
    }

    /**
     * 测量数据单元的内存占用
     * @param key
     * @param value
     * @return
     */
    private int safeSizeOf(K key, V value) {
        // 如果开发者重写的 sizeOf 返回负数，则抛出异常
        int result = sizeOf(key, value);
        if (result < 0) {
            throw new IllegalStateException("Negative size: " + key + "=" + value);
        }
        return result;
    }

    /**
     * 测量缓存单元的内存占用
     * @param key       key
     * @param value     value
     * @return
     */
    protected int sizeOf(K key, V value) {
        return 1;
    }

    public final void evictAll() {
        // -1 will evict 0-sized elements
        trimToSize(-1);
    }

    @Override
    public synchronized final int size() {
        return size;
    }

    @Override
    public synchronized final int maxSize() {
        return maxSize;
    }

    public synchronized final int hitCount() {
        return hitCount;
    }

    public synchronized final int missCount() {
        return missCount;
    }

    public synchronized final int createCount() {
        return createCount;
    }

    public synchronized final int putCount() {
        return putCount;
    }

    public synchronized final int evictionCount() {
        return evictionCount;
    }

    public synchronized final Map<K, V> snapshot() {
        return new LinkedHashMap<K, V>(map);
    }

    @Override
    public synchronized final void clear(){
        map.clear();
    }

    @SuppressLint("DefaultLocale")
    @Override
    public synchronized final String toString() {
        int accesses = hitCount + missCount;
        int hitPercent = accesses != 0 ? (100 * hitCount / accesses) : 0;
        return String.format("LruCache[maxSize=%d,hits=%d,misses=%d,hitRate=%d%%]",
                maxSize, hitCount, missCount, hitPercent);
    }
}

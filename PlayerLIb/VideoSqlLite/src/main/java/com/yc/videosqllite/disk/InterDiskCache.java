package com.yc.videosqllite.disk;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time  : 2020/8/6
 *     desc  : 磁盘缓存接口
 *     revise:
 * </pre>
 */
public interface InterDiskCache {

    String get(String key);

    void put(String key, String data);

    boolean remove(String key);

    boolean containsKey(String key);

    void clear();

}

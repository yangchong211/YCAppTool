package com.yc.videosqllite.model;

import com.yc.videosqllite.cache.VideoLruCache;
import com.yc.videosqllite.manager.CacheConfig;
import com.yc.videosqllite.manager.LocationManager;
import com.yc.videotool.VideoLogUtils;
import com.yc.videotool.VideoMd5Utils;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time  : 2020/8/6
 *     desc  : url校验以及缓存
 *     revise: 内存+磁盘缓存需要md5两次，使用该类后只需要一次即可。
 * </pre>
 */
public class SafeKeyGenerator {

    private final VideoLruCache<Integer, String> loadIdToSafeHash = new VideoLruCache<>(1000);

    public SafeKeyGenerator() {

    }

    public String getSafeKey(String url) {
        if (url==null || url.length()==0){
            return null;
        }
        String safeKey;
        //获取链接的hash算法值
        int hashCode = url.hashCode();
        synchronized (loadIdToSafeHash) {
            safeKey = loadIdToSafeHash.get(hashCode);
            VideoLogUtils.d("SafeKeyGenerator-----获取缓存key-"+safeKey);
        }
        if (safeKey == null || safeKey.length()==0) {
            CacheConfig cacheConfig = LocationManager.getInstance().getCacheConfig();
            safeKey = VideoMd5Utils.encryptMD5ToString(url, cacheConfig.getSalt());
            VideoLogUtils.d("SafeKeyGenerator-----md5转化key-"+safeKey);
        }
        synchronized (loadIdToSafeHash) {
            loadIdToSafeHash.put(hashCode, safeKey);
            VideoLogUtils.d("SafeKeyGenerator-----存储key-"+safeKey);
        }
        return safeKey;
    }

}

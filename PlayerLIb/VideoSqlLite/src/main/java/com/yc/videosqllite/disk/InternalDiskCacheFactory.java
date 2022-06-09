package com.yc.videosqllite.disk;

import android.content.Context;

import java.io.File;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time  : 2020/8/6
 *     desc  : 创建一个基于内部的磁盘缓存磁盘缓存目录
 *     revise:
 * </pre>
 */
public final class InternalDiskCacheFactory extends DiskLruCacheFactory {

    public InternalDiskCacheFactory(Context context) {
        this(context, InterDiskFactory.DEFAULT_DISK_CACHE_DIR,
                InterDiskFactory.DEFAULT_DISK_CACHE_SIZE);
    }

    public InternalDiskCacheFactory(Context context, long diskCacheSize) {
        this(context, InterDiskFactory.DEFAULT_DISK_CACHE_DIR, diskCacheSize);
    }

    public InternalDiskCacheFactory(final Context context, final String diskCacheName,
                                    long diskCacheSize) {
        super(new CacheDirectoryGetter() {
            @Override
            public File getCacheDirectory() {
                File cacheDirectory = context.getCacheDir();
                if (cacheDirectory == null) {
                    return null;
                }
                if (diskCacheName != null) {
                    return new File(cacheDirectory, diskCacheName);
                }
                return cacheDirectory;
            }
        }, diskCacheSize);
    }
}

package com.yc.videosqllite.disk;

import android.content.Context;

import androidx.annotation.Nullable;

import java.io.File;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time  : 2020/8/6
 *     desc  : 创建一个基于外部的磁盘缓存目录，如果没有外部存储，则返回到内部磁盘缓存可用。
 *             如果曾经退回到内部磁盘缓存，将从那一刻起使用那个缓存。
 *     revise:
 * </pre>
 */
public final class ExternalDiskCacheFactory extends DiskLruCacheFactory {

    public ExternalDiskCacheFactory(Context context) {
        this(context, InterDiskFactory.DEFAULT_DISK_CACHE_DIR, InterDiskFactory.DEFAULT_DISK_CACHE_SIZE);
    }

    public ExternalDiskCacheFactory(Context context, long diskCacheSize) {
        this(context, InterDiskFactory.DEFAULT_DISK_CACHE_DIR, diskCacheSize);
    }

    public ExternalDiskCacheFactory(final Context context, final String diskCacheName, final long diskCacheSize) {
        super(new CacheDirectoryGetter() {
            @Nullable
            private File getInternalCacheDirectory() {
                File cacheDirectory = context.getCacheDir();
                if (cacheDirectory == null) {
                    return null;
                }
                if (diskCacheName != null) {
                    return new File(cacheDirectory, diskCacheName);
                }
                return cacheDirectory;
            }

            @Override
            public File getCacheDirectory() {
                File internalCacheDirectory = getInternalCacheDirectory();

                // Already used internal cache, so keep using that one,
                // thus avoiding using both external and internal with transient errors.
                if ((null != internalCacheDirectory) && internalCacheDirectory.exists()) {
                    return internalCacheDirectory;
                }

                File cacheDirectory = context.getExternalCacheDir();

                // Shared storage is not available.
                if ((cacheDirectory == null) || (!cacheDirectory.canWrite())) {
                    return internalCacheDirectory;
                }
                if (diskCacheName != null) {
                    return new File(cacheDirectory, diskCacheName);
                }
                return cacheDirectory;
            }
        }, diskCacheSize);
    }
}


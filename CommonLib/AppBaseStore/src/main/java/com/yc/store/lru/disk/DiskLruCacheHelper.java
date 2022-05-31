package com.yc.store.lru.disk;


import androidx.annotation.Nullable;

import com.yc.store.StoreToolHelper;

import java.io.File;
import java.io.IOException;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time  : 2020/8/6
 *     desc  : 磁盘缓存实现类
 *     revise:
 * </pre>
 */
public class DiskLruCacheHelper {

    private static final int APP_VERSION = 1;
    private static final int VALUE_COUNT = 1;
    private DiskLruCache diskLruCache;
    private File directory;
    private static final int maxSize = 100;

    private synchronized DiskLruCache getDiskCache() throws IOException {
        if (diskLruCache == null) {
            String path = StoreToolHelper.INSTANCE.getApp().getCacheDir().getPath();
            directory = new File(path + File.pathSeparator + "disk");
            diskLruCache = DiskLruCache.open(directory, APP_VERSION, VALUE_COUNT, maxSize);
        }
        return diskLruCache;
    }


    private synchronized void resetDiskCache() {
        diskLruCache = null;
    }

}

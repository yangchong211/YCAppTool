package com.yc.store.disk.lib;


import android.util.Log;

import com.yc.appcommoninter.report.ExceptionReporter;
import com.yc.appcommoninter.report.LoggerReporter;

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
    private final DiskCacheWriteLocker writeLocker = new DiskCacheWriteLocker();

    private synchronized DiskLruCache getDiskCache() throws IOException {
        if (diskLruCache == null) {
            String baseCachePath = DiskHelperUtils.getBaseCachePath();
            int maxLruSize = DiskHelperUtils.getMaxLruSize();
            File directory = new File(baseCachePath + File.separator + "disk");
            Log.d("CacheHelper : " , "lru disk file path : " + directory.getPath());
            LoggerReporter.report("DiskLruCacheHelper" , "lru disk file path : " + directory.getPath());
            //路径：/storage/emulated/0/Android/data/你的包名/cache/ycCache/disk
            diskLruCache = DiskLruCache.open(directory, APP_VERSION, VALUE_COUNT, maxLruSize);
        }
        return diskLruCache;
    }

    public boolean put(String key ,Object value){
        writeLocker.acquire(key);
        try {
            DiskLruCache diskCache = getDiskCache();
            DiskLruCache.Value current = diskCache.get(key);
            if (current != null) {
                return false;
            }
            DiskLruCache.Editor editor = diskCache.edit(key);
            if (editor == null) {
                throw new IllegalStateException("Had two simultaneous puts for: " + key);
            }
            try {
                editor.set(0, value.toString());
                editor.commit();
            } finally {
                editor.abortUnlessCommitted();
            }
        } catch (IOException e) {
            ExceptionReporter.report(
                    "Unable to put from disk cache-", e);
        } finally {
            try {
                getDiskCache().flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            writeLocker.release(key);
        }
        return false;
    }

    public String get(String key) {
        String result = null;
        try {
            final DiskLruCache.Value value = getDiskCache().get(key);
            if (value != null) {
                result = value.getString(0);
            }
        } catch (IOException e) {
            ExceptionReporter.report("Unable to get from disk cache-", e);
        } finally {
            try {
                getDiskCache().flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public boolean remove(String key) {
        boolean isRemove;
        try {
            isRemove = getDiskCache().remove(key);
        } catch (IOException e) {
            ExceptionReporter.report(
                    "Unable to delete from disk cache-", e);
            isRemove = false;
        } finally {
            try {
                getDiskCache().flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return isRemove;
    }

    public long getSize(){
        return diskLruCache.getSize();
        //return diskLruCache.size();
    }

    private synchronized void resetDiskCache() {
        diskLruCache = null;
    }

    public void clear(){
        try {
            getDiskCache().delete();
        } catch (IOException e) {
            ExceptionReporter.report(
                    "Unable to clear disk cache or disk cache cleared externally-", e);
        } finally {
            resetDiskCache();
            LoggerReporter.report("DiskLruCacheHelper clear cache");
        }
    }
}

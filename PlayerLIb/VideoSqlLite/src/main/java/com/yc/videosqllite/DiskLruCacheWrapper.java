package com.yc.videosqllite;


import androidx.annotation.Nullable;

import com.yc.applrudisk.DiskCacheWriteLocker;
import com.yc.applrudisk.DiskLruCache;
import com.yc.toolutils.AppLogUtils;

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
public class DiskLruCacheWrapper implements InterDiskCache {

    private static final int APP_VERSION = 1;
    private static final int VALUE_COUNT = 1;
    private static DiskLruCacheWrapper wrapper;
    public final SafeKeyGenerator safeKeyGenerator;
    private final File directory;
    private final long maxSize;
    private final DiskCacheWriteLocker writeLocker = new DiskCacheWriteLocker();
    private DiskLruCache diskLruCache;

    @SuppressWarnings("deprecation")
    public static synchronized InterDiskCache get(File directory, SafeKeyGenerator safeKeyGenerator) {
        if (wrapper == null) {
            wrapper = new DiskLruCacheWrapper(directory,safeKeyGenerator);
        }
        return wrapper;
    }

    @SuppressWarnings("deprecation")
    public static InterDiskCache create(File directory, SafeKeyGenerator safeKeyGenerator) {
        return new DiskLruCacheWrapper(directory,safeKeyGenerator);
    }

    @Deprecated
    @SuppressWarnings({"WeakerAccess", "DeprecatedIsStillUsed"})
    protected DiskLruCacheWrapper(File directory,SafeKeyGenerator safeKeyGenerator) {
        CacheConfig cacheConfig = LocationManager.getInstance().getCacheConfig();
        this.directory = directory;
        this.safeKeyGenerator = safeKeyGenerator;
        this.maxSize = cacheConfig.getCacheMax();
    }

    private synchronized DiskLruCache getDiskCache() throws IOException {
        if (diskLruCache == null) {
            diskLruCache = DiskLruCache.open(directory, APP_VERSION, VALUE_COUNT, maxSize);
        }
        return diskLruCache;
    }

    @Nullable
    @Override
    public String get(String key) {
        String safeKey = safeKeyGenerator.getSafeKey(key);
        String result = null;
        try {
            final DiskLruCache.Value value = getDiskCache().get(safeKey);
            if (value != null) {
                result = value.getString(0);
            }
        } catch (IOException e) {
            AppLogUtils.d("DiskLruCacheWrapper-----Unable to get from disk cache-"+e);
        } finally {
            try {
                getDiskCache().flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public void put(String key, String data) {
        String safeKey = safeKeyGenerator.getSafeKey(key);
        writeLocker.acquire(safeKey);
        try {
            AppLogUtils.d("DiskLruCacheWrapper-----Put: Obtained:"+ safeKey + " for for Key: " + key);
            try {
                DiskLruCache diskCache = getDiskCache();
                DiskLruCache.Value current = diskCache.get(safeKey);
                if (current != null) {
                    return;
                }
                DiskLruCache.Editor editor = diskCache.edit(safeKey);
                if (editor == null) {
                    throw new IllegalStateException("Had two simultaneous puts for: " + safeKey);
                }
                try {
                    editor.set(0,data);
                    editor.commit();
                } finally {
                    editor.abortUnlessCommitted();
                }
            } catch (IOException e) {
                AppLogUtils.d("DiskLruCacheWrapper-----Unable to put from disk cache-"+e);
            } finally {
                try {
                    getDiskCache().flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } finally {
            writeLocker.release(safeKey);
        }
    }

    @Override
    public boolean remove(String key) {
        String safeKey = safeKeyGenerator.getSafeKey(key);
        boolean isRemove;
        try {
            isRemove = getDiskCache().remove(safeKey);
        } catch (IOException e) {
            AppLogUtils.d("DiskLruCacheWrapper-----Unable to delete from disk cache-"+e);
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

    @Override
    public boolean containsKey(String key) {
        String safeKey = safeKeyGenerator.getSafeKey(key);
        boolean result = false;
        try {
            final DiskLruCache.Value value = getDiskCache().get(safeKey);
            if (value != null) {
                String string = value.getString(0);
                if (string!=null && string.length() > 0){
                    result = true;
                }
            }
        } catch (IOException e) {
            AppLogUtils.d("DiskLruCacheWrapper-----Unable to get from disk cache-"+e);
        } finally {
            try {
                getDiskCache().flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public void clear() {
        try {
            getDiskCache().delete();
        } catch (IOException e) {
            AppLogUtils.d("DiskLruCacheWrapper-----Unable to clear disk cache or disk cache cleared externally-"+e);
        } finally {
            try {
                getDiskCache().flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            resetDiskCache();
        }
    }

    private synchronized void resetDiskCache() {
        diskLruCache = null;
    }

}

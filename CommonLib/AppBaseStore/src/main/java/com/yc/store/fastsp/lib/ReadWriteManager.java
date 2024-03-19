package com.yc.store.fastsp.lib;

import android.util.Log;


import com.yc.toolutils.context.AppToolUtils;
import com.yc.toolutils.file.AppFileUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;


public final class ReadWriteManager {

    private static final String TAG = "ReadWriteManager";

    public static String getFilePath(String path, String name) {
        return path + File.separator + name;
    }

    private final String filePath;
    private final String lockFilePath;

    public ReadWriteManager(String name) {
        String cachePath = AppFileUtils.getCachePath(AppToolUtils.getApp()) +   File.separator + "ycCache";
        String logDir = cachePath + File.separator + "fast";
        this.filePath = getFilePath(logDir, name);
        //路径：/storage/emulated/0/Android/data/你的包名/cache/ycCache/fast/fast_sp
        this.lockFilePath = getFilePath(logDir, name + ".lock");
        //路径：/storage/emulated/0/Android/data/你的包名/cache/ycCache/fast/fast_sp.lock
        Log.d("CacheHelper : " , "read file path : " + filePath);
        prepare();
    }

    public void write(Object obj) {
        ObjectOutputStream oos = null;
        FileOutputStream fos = null;
        Lock lock = null;
        try {
            prepare();
            lock = new Lock(lockFilePath).lock();
            Log.d(TAG, "start write file: " + filePath);
            fos = new FileOutputStream(filePath);
            oos = new ObjectOutputStream(new BufferedOutputStream(fos));
            oos.writeObject(obj);
            oos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Log.d(TAG, "finish write file: " + filePath);
            closeSilently(oos);
            closeSilently(fos);
            if (lock != null) {
                lock.release();
            }
        }
    }

    public Object read() {
        if (!isFileExist(filePath)) {
            return null;
        }
        ObjectInputStream ois = null;
        FileInputStream fis = null;
        Lock lock = null;
        try {
            lock = new Lock(lockFilePath).lock();
            fis = new FileInputStream(filePath);
            ois = new ObjectInputStream(new BufferedInputStream(fis));
            return ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            closeSilently(ois);
            closeSilently(fis);
            if (lock != null) {
                lock.release();
            }
        }
    }

    private void prepare() {
        File file = new File(filePath);
        File parent = file.getParentFile();
        if (!parent.exists()) {
            parent.mkdirs();
        }
    }

    private static class Lock {

        private static final Map<String, ReentrantLock> THREAD_LOCK_MAP = new HashMap<>();

        private static ReentrantLock getLock(String key) {
            synchronized (THREAD_LOCK_MAP) {
                if (!THREAD_LOCK_MAP.containsKey(key)) {
                    THREAD_LOCK_MAP.put(key, new ReentrantLock());
                }
                return THREAD_LOCK_MAP.get(key);
            }
        }

        private final String lockFilePath;
        private FileOutputStream fos;
        private FileChannel channel;
        private FileLock fileLock;
        private final ReentrantLock threadLock;

        public Lock(String lockFilePath) {
            this.lockFilePath = lockFilePath;
            this.threadLock = getLock(lockFilePath);
            File file = new File(this.lockFilePath);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public Lock lock() throws IOException {
            threadLock.lock();
            fos = new FileOutputStream(lockFilePath);
            channel = fos.getChannel();
            fileLock = channel.lock();
            return this;
        }

        public void release() {
            if (fileLock != null) {
                try {
                    fileLock.release();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            closeSilently(channel);
            closeSilently(fos);
            threadLock.unlock();
        }
    }

    protected static void closeSilently(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException var2) {
            }
        }
    }

    private boolean isFileExist(String path) {
        File file = new File(path);
        return file.exists();
    }
}

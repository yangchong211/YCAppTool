package com.yc.store.disk.lib;


import com.yc.toolutils.context.AppToolUtils;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.nio.charset.Charset;


/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time  : 2020/8/6
 *     desc  : 磁盘缓存工具类
 *     revise:
 * </pre>
 */
public final class DiskHelperUtils {

    static final Charset US_ASCII = Charset.forName("US-ASCII");
    static final Charset UTF_8 = Charset.forName("UTF-8");
    private static String baseCachePath = "";
    private static int maxLruSize;

    private DiskHelperUtils() {
    }

    public static String getBaseCachePath() {
        if (baseCachePath == null || baseCachePath.length()==0){
            baseCachePath = AppToolUtils.getApp().getExternalCacheDir().getAbsolutePath();
        }
        return baseCachePath;
    }

    public static void setBaseCachePath(String baseCachePath) {
        DiskHelperUtils.baseCachePath = baseCachePath;
    }

    public static int getMaxLruSize() {
        return maxLruSize;
    }

    public static void setMaxLruSize(int maxLruSize) {
        DiskHelperUtils.maxLruSize = maxLruSize;
    }

    static String readFully(Reader reader) throws IOException {
        try {
            StringWriter writer = new StringWriter();
            char[] buffer = new char[1024];
            int count;
            while ((count = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, count);
            }
            return writer.toString();
        } finally {
            reader.close();
        }
    }

    /**
     * Deletes the contents of {@code dir}. Throws an IOException if any file
     * could not be deleted, or if {@code dir} is not a readable directory.
     */
    static void deleteContents(File dir) throws IOException {
        File[] files = dir.listFiles();
        if (files == null) {
            throw new IOException("not a readable directory: " + dir);
        }
        for (File file : files) {
            if (file.isDirectory()) {
                deleteContents(file);
            }
            if (!file.delete()) {
                throw new IOException("failed to delete file: " + file);
            }
        }
    }

    static void closeQuietly(/*Auto*/Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (RuntimeException rethrown) {
                throw rethrown;
            } catch (Exception ignored) {
            }
        }
    }

}

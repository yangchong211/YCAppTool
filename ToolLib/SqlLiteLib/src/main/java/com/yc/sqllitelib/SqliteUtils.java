package com.yc.sqllitelib;

import android.content.Context;
import java.io.File;

public final class SqliteUtils {

    /**
     * 清除内部数据库
     * <p>/data/data/com.xxx.xxx/databases</p>
     *
     * @return {@code true}: 清除成功<br>{@code false}: 清除失败
     */
    public static boolean cleanInternalDbs(Context context) {
        String parent = context.getFilesDir().getParent();
        File databases = new File(parent, "databases");
        return deleteFilesInDir(databases);
    }


    private static boolean deleteFilesInDir(final File dir) {
        if (dir == null) {
            return false;
        }
        // 目录不存在返回 true
        if (!dir.exists()) {
            return true;
        }
        // 不是目录返回 false
        if (!dir.isDirectory()) {
            return false;
        }
        // 现在文件存在且是文件夹
        File[] files = dir.listFiles();
        if (files != null && files.length != 0) {
            for (File file : files) {
                if (file.isFile()) {
                    if (!file.delete()) {
                        return false;
                    }
                } else if (file.isDirectory()) {
                    if (!deleteDir(file)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }


    private static boolean deleteDir(final File dir) {
        if (dir == null) {
            return false;
        }
        // 目录不存在返回 true
        if (!dir.exists()) {
            return true;
        }
        // 不是目录返回 false
        if (!dir.isDirectory()) {
            return false;
        }
        // 现在文件存在且是文件夹
        File[] files = dir.listFiles();
        if (files != null && files.length != 0) {
            for (File file : files) {
                if (file.isFile()) {
                    if (!file.delete()) {
                        return false;
                    }
                } else if (file.isDirectory()) {
                    if (!deleteDir(file)) {
                        return false;
                    }
                }
            }
        }
        return dir.delete();
    }


}

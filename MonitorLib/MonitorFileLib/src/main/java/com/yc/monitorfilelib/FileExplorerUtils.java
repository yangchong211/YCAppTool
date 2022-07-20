package com.yc.monitorfilelib;

import android.util.Log;

import java.io.File;
import java.util.Locale;

/**
 * <pre>
 *     author : yangchong
 *     email  : yangchong211@163.com
 *     time   : 2021/8/11
 *     desc   : 工具类
 *     revise :
 * </pre>
 */
public final class FileExplorerUtils {

    public static final String DB = "db";
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String XML = ".xml";
    public static final String JSON = ".json";
    public static final boolean IS_DEBUG = true;

    private FileExplorerUtils() {

    }

    /**
     * 日志
     *
     * @param log 日志
     */
    public static void logInfo(String log) {
        if (IS_DEBUG) {
            Log.i("FileExplorer", log);
        }
    }

    /**
     * 日志
     *
     * @param log 日志
     */
    public static void logError(String log) {
        if (IS_DEBUG) {
            Log.e("FileExplorer", log);
        }
    }

    /**
     * 这个是获取文件的后缀名
     *
     * @param file file文件
     * @return 后缀名
     */
    public static String getSuffix(File file) {
        return file != null && file.exists() ?
                file.getName().substring(file.getName().lastIndexOf(".") + 1)
                        .toLowerCase(Locale.getDefault()) : "";
    }

    /**
     * 是否是db文件
     *
     * @param file 文件
     * @return
     */
    public static boolean isDB(File file) {
        if (file == null) {
            return false;
        } else {
            String suffix = getSuffix(file);
            return DB.equals(suffix);
        }
    }

    /**
     * 是否是图片文件
     *
     * @param file 文件
     * @return
     */
    public static boolean isImage(File file) {
        if (file == null) {
            return false;
        }
        String suffix = getSuffix(file);
        return "jpg".equals(suffix) || "jpeg".equals(suffix)
                || "png".equals(suffix) || "bmp".equals(suffix);
    }

    /**
     * 是否是sp文件
     *
     * @param file 文件
     * @return
     */
    public static boolean isSp(File file) {
        File parentFile = file.getParentFile();
        return parentFile != null && parentFile.getName().equals(SHARED_PREFS)
                && file.getName().contains(XML);
    }

}

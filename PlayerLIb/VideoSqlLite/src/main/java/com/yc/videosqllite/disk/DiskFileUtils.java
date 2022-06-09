package com.yc.videosqllite.disk;

import android.content.Context;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;


/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time  : 2020/7/10
 *     desc  : 保存日志的工具类
 *     revise:
 * </pre>
 */
public final class DiskFileUtils {

    /**
     * 目录地址
     * SDCard/Android/data/<application package>/cache
     * data/data/<application package>/cache
     */
    public static String getPath(Context context) {
        String path = getCachePath(context) + File.separator + "disk";
        return path;
    }

    /**
     * 目录地址
     * SDCard/Android/data/<application package>/cache
     * data/data/<application package>/cache
     * @param context                               上下文
     * @param pathName                              路径名称
     * @return
     */
    public static String getPath(Context context , String pathName) {
        String path = getCachePath(context) + File.separator + pathName;
        return path;
    }

    /**
     * 获取路径file
     * @param context                               上下文
     * @return
     */
    public static File getFilePath(Context context){
        String path = getPath(context);
        File file = new File(path);
        return file;
    }

    /**
     * 获取app缓存路径
     * 如果sd卡可以使用，sd卡路径
     * SDCard/Android/data/<application package>/cache
     *
     * 如果没有sd卡，则获取内部存储卡路径
     * data/data/<application package>/cache
     *
     * @param context                               上下文
     * @return
     */
    public static String getCachePath(Context context) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            //外部存储可用
            if (context.getExternalCacheDir()!=null){
                cachePath = context.getExternalCacheDir().getAbsolutePath();
            } else {
                cachePath = context.getCacheDir().getAbsolutePath();
            }
        } else {
            //外部存储不可用
            cachePath = context.getCacheDir().getAbsolutePath();
        }
        return cachePath;
    }

    /**
     * 获取list集合
     * @param context                               上下文
     * @return
     */
    public static List<File> getFileList(Context context) {
        File file = new File(com.yc.videosqllite.disk.DiskFileUtils.getPath(context));
        List<File> mFileList = new ArrayList<>();
        File[] fileArray = file.listFiles();
        if (fileArray == null || fileArray.length <= 0) {
            return mFileList;
        }
        for (File f : fileArray) {
            if (f.isFile()) {
                mFileList.add(f);
            }
        }
        return mFileList;
    }

    /**
     * 删除单个文件
     *
     * @param fileName 要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }


    /**
     * 删除所有的文件
     * @param root                          root目录
     */
    public static void deleteAllFiles(File root) {
        File files[] = root.listFiles();
        if (files != null)
            for (File f : files) {
                if (f.isDirectory()) { // 判断是否为文件夹
                    deleteAllFiles(f);
                    try {
                        f.delete();
                    } catch (Exception e) {
                        //不要在for循环中打印e
                    }
                } else {
                    if (f.exists()) { // 判断是否存在
                        deleteAllFiles(f);
                        try {
                            f.delete();
                        } catch (Exception e) {
                            //不要在for循环中打印e
                        }
                    }
                }
            }
    }

    /**
     * 获取文件的内容
     * @param fileName                      文件名称
     * @return
     */
    public static String readFile2String(String fileName) {
        String res = "";
        try {
            FileInputStream inputStream = new FileInputStream(fileName);
            InputStreamReader inputStreamReader = null;
            try {
                inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }
            BufferedReader reader = new BufferedReader(inputStreamReader);
            StringBuilder sb = new StringBuilder("");
            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                    sb.append("\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            res = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 重命名文件
     *
     * @param oldPath 原来的文件地址
     * @param newPath 新的文件地址
     */
    public static void renameFile(String oldPath, String newPath) {
        File oleFile = new File(oldPath);
        File newFile = new File(newPath);
        //执行重命名
        oleFile.renameTo(newFile);
    }

    /**
     * 根据文件路径拷贝文件
     *
     * @param src                           源文件
     * @param dest                          目标文件
     * @return                              boolean 成功true、失败false
     */
    public static boolean copyFile(File src, File dest) {
        boolean result = false;
        if ((src == null) || (dest == null)) {
            return result;
        }
        if (dest.exists()) {
            dest.delete(); // delete file
        }
        if (!createOrExistsDir(dest.getParentFile())) {
            return false;
        }
        try {
            dest.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileChannel srcChannel = null;
        FileChannel dstChannel = null;

        try {
            srcChannel = new FileInputStream(src).getChannel();
            dstChannel = new FileOutputStream(dest).getChannel();
            srcChannel.transferTo(0, srcChannel.size(), dstChannel);
            result = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return result;
        }
        try {
            srcChannel.close();
            dstChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static boolean deleteFile(final File file) {
        return file != null && (!file.exists() || file.isFile() && file.delete());
    }


    public static boolean createOrExistsDir(final File file) {
        return file != null && (file.exists() ? file.isDirectory() : file.mkdirs());
    }


}

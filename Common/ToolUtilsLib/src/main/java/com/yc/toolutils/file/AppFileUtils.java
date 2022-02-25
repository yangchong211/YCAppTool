package com.yc.toolutils.file;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time  : 2020/7/10
 *     desc  : 保存日志的工具类
 *     revise: 参考：https://github.com/Blankj/AndroidUtilCode
 * </pre>
 */
public final class AppFileUtils {

    /**
     * 机身内存缓存文件
     * cache-->存放缓存文件
     * code_cache-->存放运行时代码优化等产生的缓存
     * databases-->存放数据库文件
     * files-->存放一般文件
     * lib-->存放App依赖的so库 是软链接，指向/data/app/ 某个子目录下
     * shared_prefs-->存放SharedPreferences 文件
     */
    public static String getCachePath(Context context){
        File cacheDir = context.getCacheDir();
        if (cacheDir!=null && cacheDir.exists()){
            return cacheDir.getAbsolutePath();
        }
        return null;
    }

    public static String getCodeCachePath(Context context){
        File cacheDir = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            cacheDir = context.getCodeCacheDir();
        }
        if (cacheDir!=null && cacheDir.exists()){
            return cacheDir.getAbsolutePath();
        }
        return null;
    }

    public static String getFilesPath(Context context){
        File filesDir = context.getFilesDir();
        if (filesDir!=null && filesDir.exists()){
            return filesDir.getAbsolutePath();
        }
        return null;
    }

    /*------------------------------------------------------------------------------------*/

    /**
     * 机身外部存储，/storage/emulated/0/
     * App外部私有目录
     * /sdcard/Android/data/com.yc.helper
     * cache-->存放缓存文件
     * files-->存放一般文件
     */
    public static String getExternalCachePath(Context context){
        File cacheDir = context.getExternalCacheDir();
        if (cacheDir!=null && cacheDir.exists()){
            return cacheDir.getAbsolutePath();
        }
        return null;
    }

    public static String getExternalFilePath(Context context){
        File filesDir = context.getExternalFilesDir(null);
        if (filesDir!=null && filesDir.exists()){
            return filesDir.getAbsolutePath();
        }
        return null;
    }

    /**
     * 机身外部存储，/storage/emulated/0/
     * 只要拿到根目录，就可以遍历寻找其它子目录/文件。
     */
    public static String getRootFilePath(){
        File rootDir = Environment.getExternalStorageDirectory();
        if (rootDir!=null && rootDir.exists()){
            return rootDir.getAbsolutePath();
        }
        return null;
    }

    /*------------------------------------------------------------------------------------*/

    /**
     * 目录地址
     * SDCard/Android/data/<application package>/cache
     * data/data/<application package>/cache
     */
    public static String getSrcFilePath(Context context , String name) {
        String path = getAppCachePath(context) + File.separator + name;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }


    /**
     * 获取分享路径地址
     * @return                              路径
     */
    public static String getFileSharePath() {
        String path = Environment.getExternalStorageDirectory() + "";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    /**
     * 获取app缓存路径
     * SDCard/Android/data/<application package>/cache
     * data/data/<application package>/cache
     *
     * @param context                       上下文
     * @return
     */
    public static String getAppCachePath(Context context) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            //外部存储可用
            String externalCachePath = getExternalCachePath(context);
            if (externalCachePath!=null){
                cachePath = externalCachePath;
            } else {
                cachePath =  getCachePath(context);
            }
        } else {
            //外部存储不可用
            cachePath = getCachePath(context);
        }
        return cachePath;
    }

    /**
     * 获取崩溃crash的list集合
     * @param context                               上下文
     * @return                                      集合
     */
    public static List<File> getFileList(Context context ,String name) {
        File file = new File(AppFileUtils.getSrcFilePath(context,name));
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
     * 获取文件大小
     *
     * @param directory 文件
     * @return
     */
    public static long getDirectorySize(File directory) {
        long size = 0L;
        File[] listFiles = directory.listFiles();
        if (listFiles != null) {
            File[] files = listFiles;
            int length = listFiles.length;
            for (int i = 0; i < length; ++i) {
                File file = files[i];
                if (file.isDirectory()) {
                    size += getDirectorySize(file);
                } else {
                    size += file.length();
                }
            }
        } else {
            //如果不是文件目录，则获取单个文件大小
            size = directory.length();
        }
        return size;
    }

    /**
     * 文件创建时间，方便测试查看缓存文件的最后修改时间
     *
     * @param file    文件
     */
    public static String getFileTime(File file) {
        if (file != null && file.exists()) {
            long lastModified = file.lastModified();
            String time = new SimpleDateFormat("yyyy-MM-dd")
                    .format(new Date(lastModified));
            return time;
        }
        return "";
    }

    /**
     * 删除单个文件
     *
     * @param fileName                              要删除的文件的文件名
     * @return                                      单个文件删除成功返回true，否则返回false
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
     * 删除文件
     *
     * @param file 文件
     */
    public static boolean deleteDirectory(File file) {
        if (file != null) {
            if (file.isDirectory()) {
                File[] listFiles = file.listFiles();
                int length = listFiles.length;
                for (int i = 0; i < length; ++i) {
                    File f = listFiles[i];
                    deleteDirectory(f);
                }
            }
            file.delete();
        }
        // 如果删除的文件路径所对应的文件存在，并且是一个文件，则表示删除失败
        if (file != null && file.exists() && file.isFile()) {
            return false;
        } else {
            //删除成功
            return true;
        }
    }



    /**
     * 删除所有的文件
     * @param root                                  root目录
     */
    public static void deleteAllFiles(File root) {
        File files[] = root.listFiles();
        if (files != null)
            for (File f : files) {
                if (f.isDirectory()) {
                    // 判断是否为文件夹
                    deleteAllFiles(f);
                    try {
                        f.delete();
                    } catch (Exception e) {
                    }
                } else {
                    if (f.exists()) {
                        // 判断是否存在
                        deleteAllFiles(f);
                        try {
                            f.delete();
                        } catch (Exception e) {
                        }
                    }
                }
            }
    }

    /**
     * 读取file文件，转化成字符串
     * @param fileName                              文件名称
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
     * @param oldPath                               原来的文件地址
     * @param newPath                               新的文件地址
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
     * @param src  源文件
     * @param dest 目标文件
     * @return boolean 成功true、失败false
     */
    public static boolean copyFile(File src, File dest) {
        if ((src == null) || (dest == null)) {
            return false;
        }
        if (dest.exists()) {
            // delete file
            dest.delete();
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
            //当前
            srcChannel = new FileInputStream(src).getChannel();
            //
            dstChannel = new FileOutputStream(dest).getChannel();
            srcChannel.transferTo(0, srcChannel.size(), dstChannel);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (srcChannel != null) {
                    srcChannel.close();
                }
                if (dstChannel != null) {
                    dstChannel.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }


    /**
     * 删除文件
     * @param file                                  file文件
     * @return
     */
    public static boolean deleteFile(final File file) {
        return file != null && (!file.exists() || file.isFile() && file.delete());
    }

    /**
     * 判断文件是否创建，如果没有创建，则新建
     * @param file                                  file文件
     * @return
     */
    public static boolean createOrExistsDir(final File file) {
        return file != null && (file.exists() ? file.isDirectory() : file.mkdirs());
    }


}

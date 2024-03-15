package com.yc.toolutils.file;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
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
     *
     * 内部存储，举个例子：
     * cache:/data/user/0/包名/cache
     */
    public static String getCachePath(Context context){
        File cacheDir = context.getCacheDir();
        if (cacheDir!=null && cacheDir.exists()){
            return cacheDir.getAbsolutePath();
        }
        return null;
    }

    /**
     * code_cache-->存放运行时代码优化等产生的缓存
     * @param context       上下文
     * @return              路径
     */
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

    /**
     * files-->存放一般文件
     * 内部存储，举个例子：
     * file:data/user/0/包名/files
     * @param context       上下文
     * @return              路径
     */
    public static String getFilesPath(Context context){
        File filesDir = context.getFilesDir();
        if (filesDir!=null && filesDir.exists()){
            return filesDir.getAbsolutePath();
        }
        return null;
    }


    /**
     * 目录地址
     * data/data/<application package>/cache/
     */
    public static String getCacheFilePath(Context context , String name) {
        String path = getCachePath(context) + File.separator + name;
        File file = new File(path);
        if (!file.exists()) {
            //创建一个File对象所对应的目录，成功返回true，否则false。且File对象必须为路径而不是文件。
            //创建多级目录，创建路径中所有不存在的目录
            file.mkdirs();
        }
        return path;
    }

    /**
     * 目录地址
     * data/data/<application package>/files/
     */
    public static String getFilesFilePath(Context context , String name) {
        String path = getFilesPath(context) + File.separator + name;
        File file = new File(path);
        if (!file.exists()) {
            //创建一个File对象所对应的目录，成功返回true，否则false。且File对象必须为路径而不是文件。
            //创建多级目录，创建路径中所有不存在的目录
            file.mkdirs();
        }
        return path;
    }

    /*------------------------------------------------------------------------------------*/
    /*------------------------------------------------------------------------------------*/
    /*------------------------------------------------------------------------------------*/

    /**
     * 机身外部存储，/storage/emulated/0/
     * App外部私有目录
     * /sdcard/Android/data/包名
     * cache-->存放缓存文件
     * files-->存放一般文件
     *
     * 外部存储根目录，举个例子
     * files:/storage/emulated/0/Android/data/包名/files
     * cache:/storage/emulated/0/Android/data/包名/cache
     */
    public static String getExternalCachePath(Context context){
        File cacheDir = context.getExternalCacheDir();
        if (cacheDir!=null && cacheDir.exists()){
            return cacheDir.getAbsolutePath();
        }
        return null;
    }

    /**
     * 获取外部存储根目录的files文件路径
     * files:/storage/emulated/0/Android/data/包名/files
     * @param context   上下文
     * @return          路径
     */
    public static String getExternalFilePath(Context context){
        File filesDir = context.getExternalFilesDir(null);
        if (filesDir!=null && filesDir.exists()){
            return filesDir.getAbsolutePath();
        }
        return null;
    }


    /**
     * 外部存储根目录，举个例子
     * cache:/storage/emulated/0/Android/data/包名/cache
     */
    public static String getExternalCachePath(Context context , String name) {
        String path = getExternalCachePath(context) + File.separator + name;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }


    /**
     * 外部存储根目录，举个例子
     * files:/storage/emulated/0/Android/data/包名/files
     */
    public static String getExternalFilePath(Context context , String name) {
        String path = getExternalFilePath(context) + File.separator + name;
        File file = new File(path);
        if (!file.exists()) {
            //创建一个File对象所对应的目录，成功返回true，否则false。且File对象必须为路径而不是文件。
            //创建多级目录，创建路径中所有不存在的目录
            file.mkdirs();
        }
        return path;
    }


    /*------------------------------------------------------------------------------------*/
    /*------------------------------------------------------------------------------------*/
    /*------------------------------------------------------------------------------------*/

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
     * 获取app缓存路径。优先使用外部存储空间
     * SDCard/Android/data/<application package>/cache
     * data/data/<application package>/cache
     *
     * @param context                       上下文
     * @return
     */
    public static String getAppCachePath(Context context) {
        String cachePath;
        if (SdCardUtils.isMounted() || !Environment.isExternalStorageRemovable()) {
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
     * 获取file的list集合
     * @return                                      集合
     */
    public static List<File> getFileList(String path) {
        File file = new File(path);
        return getFileList(file);
    }

    /**
     * 获取某个file对应的子file列表
     *
     * @param dir file文件
     * @return
     */
    public static List<File> getFileList(File dir) {
        List<File> fileList = new ArrayList<>();
        if (dir.listFiles() != null) {
            File[] files = dir.listFiles();
            if (files == null || files.length <= 0) {
                return fileList;
            }
            int length = files.length;
            for (int i = 0; i < length; ++i) {
                File file = files[i];
                fileList.add(file);
            }
        }
        return fileList;
    }

    /**
     * 获取file文件下的文件集合
     * @param file
     * @return
     */
    public static List<File> collectAllFiles(final File file) {
        if (!file.exists()) {
            final String message = file + " does not exist";
            throw new IllegalArgumentException(message);
        }
        List<File> list = new ArrayList<>();
        if (!file.isDirectory()) {
            return list;
        }
        LinkedList<File> dirStack = new LinkedList<>();
        dirStack.push(file);
        while (!dirStack.isEmpty()) {
            File dir = dirStack.pop();
            File[] files = dir.listFiles();
            if (files == null) {
                continue;
            }
            for (File f : files) {
                if (!f.isDirectory()) {
                    list.add(f);
                } else {
                    dirStack.push(f);
                }
            }
        }
        return list;
    }


    /**
     * 文件创建时间，方便测试查看缓存文件的最后修改时间
     *
     * @param file    文件
     */
    public static long getFileTime(File file) {
        if (file != null && file.exists()) {
            return file.lastModified();
        }
        return 0L;
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
            return file.delete();
        } else {
            return false;
        }
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
     * 删除文件
     *
     * @param file 文件
     */
    public static boolean deleteDirectory(File file) {
        if (file != null) {
            if (file.isDirectory()) {
                File[] listFiles = file.listFiles();
                if (listFiles != null) {
                    for (File f : listFiles) {
                        deleteDirectory(f);
                    }
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
        File[] files = root.listFiles();
        if (files == null) {
            return;
        }
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
     * 判断文件是否创建，如果没有创建，则新建
     * @param file                                  file文件
     * @return
     */
    public static boolean createOrExistsDir(final File file) {
        return file != null && (file.exists() ? file.isDirectory() : file.mkdirs());
    }

    public static boolean createOrExistsFile(final File file) {
        if (file == null) {
            return false;
        }
        if (file.exists()) {
            return file.isFile();
        }
        if (!AppFileUtils.createOrExistsDir(file.getParentFile())) {
            return false;
        }
        try {
            return file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void searchFile(File file, List<File> fileList) {
        if (file.exists() && file.isDirectory()) {
            File[] fs = file.listFiles();
            if (fs != null) {
                for (File f : fs) {
                    searchFile(f, fileList);
                }
            }
        } else if (file.exists() && file.isFile()) {
            fileList.add(file);
        }
    }

    /**
     * 获取文件个数或者大小
     *
     * @param file     路径
     * @param isNumber 是否是获取个数个数
     * @return 结果
     */
    private long getFileLength(File file, boolean isNumber) {
        if (file == null) {
            return -1;
        }
        long size = 0;
        if (!file.isDirectory()) {
            if (isNumber) {
                size = 1;
            } else {
                size = file.length();
            }
        } else {
            for (File f : file.listFiles()) {
                size += getFileLength(f, isNumber);
            }
        }
        return size;
    }

}

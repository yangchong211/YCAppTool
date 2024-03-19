package com.yc.imageserver.utils;

import android.os.Looper;
import com.bumptech.glide.Glide;
import com.yc.toolutils.context.AppToolUtils;

import java.io.File;
import java.math.BigDecimal;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/9/9
 *     desc  : 图片清理工具类，包括缓存清理，磁盘清理，缓存大小获取等
 *     revise:
 * </pre>
 */
public class GlideCatchUtils {

    private static GlideCatchUtils instance;

    public synchronized static GlideCatchUtils getInstance() {
        if (null == instance) {
            instance = new GlideCatchUtils();
        }
        return instance;
    }

    /**
     * 获取Glide磁盘缓存大小
     * @return
     */
    public String getCacheSize() {
        try {
            return getFormatSize(getFolderSize(new File(
                    AppToolUtils.getApp().getCacheDir() + "/" + GlideConfig.GLIDE_CATCH_DIR)));
        } catch (Exception e) {
            e.printStackTrace();
            return "获取失败";
        }
    }

    /**
     * 清除Glide磁盘缓存，自己获取缓存文件夹并删除方法
     * @return
     */
    public boolean cleanCatchDisk() {
        return deleteFolderFile(AppToolUtils.getApp().getCacheDir()
                + "/" + GlideConfig.GLIDE_CATCH_DIR, true);
    }

    /**
     * 清除图片磁盘缓存，调用Glide自带方法。注意磁盘缓存和内存有很大区别，建议在子线程中进行
     * @return
     */
    public boolean clearCacheDiskSelf() {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.get(AppToolUtils.getApp()).clearDiskCache();
                    }
                }).start();
            } else {
                Glide.get(AppToolUtils.getApp()).clearDiskCache();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 清除Glide内存缓存
     * @return
     */
    public boolean clearCacheMemory() {
        try {
            //只能在主线程执行
            if (Looper.myLooper() == Looper.getMainLooper()) {
                Glide.get(AppToolUtils.getApp()).clearMemory();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 获取指定文件夹内所有文件大小的和
     * @param file                                      文件file
     * @return                                          返回文件大小
     * @throws Exception
     */
    private long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (File aFileList : fileList) {
                if (aFileList.isDirectory()) {
                    size = size + getFolderSize(aFileList);
                } else {
                    size = size + aFileList.length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 格式化单位
     * @param size                                      size大小
     * @return                                          单位
     */
    private static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "Byte";
        }
        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
        }
        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
        }
        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
    }

    /**
     * 按目录删除文件夹文件方法
     * @param filePath                                  file路径
     * @param deleteThisPath                            删除路径
     * @return
     */
    private boolean deleteFolderFile(String filePath, boolean deleteThisPath) {
        try {
            File file = new File(filePath);
            if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (File file1 : files) {
                    deleteFolderFile(file1.getAbsolutePath(), true);
                }
            }
            if (deleteThisPath) {
                if (!file.isDirectory()) {
                    file.delete();
                } else {
                    if (file.listFiles().length == 0) {
                        file.delete();
                    }
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}

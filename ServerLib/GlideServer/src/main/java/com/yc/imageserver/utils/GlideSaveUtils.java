package com.yc.imageserver.utils;


import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;


import com.yc.compress.image.ImageSaveUtils;
import com.yc.toolutils.context.AppToolUtils;
import com.yc.toolutils.file.SdCardUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * <pre>
 *     @author yangchong
 *     blog  :
 *     time  : 2017/6/9.
 *     desc  : 保存文件路径
 *     revise:
 * </pre>
 */
public final class GlideSaveUtils {


    /**
     * app保存路径
     * 视频保存位置：hwmc/video
     * 图片保存位置：hwmc/image   (包含画廊保存图片，list条目点击item按钮保存图片)
     * apk下载路径：hwmc/downApk
     * 日志路径位置：hwmc/logger
     * 崩溃记录位置：hwmc/crash
     * glide缓存位置：hwmc/GlideDisk
     */
    private final static String APP_ROOT_SAVE_PATH = "lifeHelper";
    private final static String PROPERTY = File.separator;


    /**
     * 视频系统保存文件目录
     * @param name                  自己命名
     * @return                      路径
     */
    public static String getLocalVideoPathDir(String name){
        String video = getLocalFileSavePathDir(AppToolUtils.getApp(), "video", name);
        return video;
    }

    /**
     * apk保存文件目录
     * @return                      路径
     */
    public static String getLocalApkDownSavePath(String apkName){
        String appUpdateDownApkPath = "hwmc" + File.separator + "downApk";
        String saveApkPath= appUpdateDownApkPath + File.separator;
        String sdPath = SdCardUtils.getSDCardPath();
        if (!SdCardUtils.isMounted() || TextUtils.isEmpty(sdPath)) {
            ArrayList<String> sdPathList = SdCardUtils.getExtSDCardPath();
            if (sdPathList != null && sdPathList.size() > 0 && !TextUtils.isEmpty(sdPathList.get(0))) {
                sdPath = sdPathList.get(0);
            }
        }
        String saveApkDirs = sdPath+ File.separator+saveApkPath;
        File file = new File(saveApkDirs);
        //判断文件夹是否存在，如果不存在就创建，否则不创建
        if (!file.exists()) {
            //通过file的mkdirs()方法创建目录中包含却不存在的文件夹
            //noinspection ResultOfMethodCallIgnored
            file.mkdirs();
        }
        saveApkPath = saveApkDirs + apkName+".apk";
        return saveApkPath;
    }


    /**
     * 保存bitmap到本地
     */
    public static String saveBitmap(Context context, Bitmap mBitmap, boolean isScanner) {
        String savePath = getLocalImgSavePath();
        try {
            File filePic = new File(savePath);
            if (!filePic.exists()) {
                //noinspection ResultOfMethodCallIgnored
                filePic.getParentFile().mkdirs();
                //noinspection ResultOfMethodCallIgnored
                filePic.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(filePic);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            if(isScanner){
                ImageSaveUtils.scannerImage(context,savePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return savePath;
    }


    /**
     * 获取本地图片保存路径
     * @return
     */
    public static String getLocalImgSavePath() {
        return getLocalFileSavePathDir(AppToolUtils.getApp(),"images",
                generateRandomName() + ".png");
    }

    /**
     * 保存图片/崩溃日志保存文件目录
     * @param fileName              文件名称：比如crash，image，这个是文件夹
     * @param name                  自己命名，文件名称比如图片  aa.jpg
     * @return                      路径
     */
    public static String getLocalFileSavePathDir(Context context , String fileName , String name){
        //获得SDCard 的路径,storage/sdcard
        String sdPath = SdCardUtils.getSDCardPath();

        //判断 SD 卡是否可用
        if (!SdCardUtils.isSDCardEnable(context) || TextUtils.isEmpty(sdPath)) {
            //获取 SD 卡路径
            List<String> sdPathList = SdCardUtils.getSDCardPaths(context);
            if (sdPathList != null && sdPathList.size() > 0 && !TextUtils.isEmpty(sdPathList.get(0))) {
                sdPath = sdPathList.get(0);
            }
        }
        if (TextUtils.isEmpty(sdPath)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(sdPath);
        sb.append(PROPERTY);
        sb.append(APP_ROOT_SAVE_PATH);
        sb.append(PROPERTY);
        sb.append(fileName);

        //如果name不为空，那么通过该方法是获取到文件的路径
        //否则则是获取文件夹的路径
        if(name!=null && name.length()>0){
            sb.append(PROPERTY);
            sb.append(name);
        }
        return sb.toString();
    }


    public static String generateRandomName() {
        return UUID.randomUUID().toString();
    }


}

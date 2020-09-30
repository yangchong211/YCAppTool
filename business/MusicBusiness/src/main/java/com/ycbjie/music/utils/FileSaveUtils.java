package com.ycbjie.music.utils;


import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
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
public class FileSaveUtils {


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
        String video = getLocalFileSavePathDir(Utils.getApp(), "video", name);
        return video;
    }

    /**
     * 视频系统保存文件目录
     * @param name                  自己命名
     * @return                      路径
     */
    public static String getLocalMusicPathDir(String name){
        String video = getLocalFileSavePathDir(Utils.getApp(), "music", name);
        return video;
    }

    /**
     * 获取本地图片保存路径
     * @return
     */
    public static String getLocalImgSavePath() {
        return getLocalFileSavePathDir(Utils.getApp(),"images", generateRandomName() + ".png");
    }

    /**
     * apk保存文件目录
     * @return                      路径
     */
    public static String getLocalApkDownSavePath(String apkName){
        String appUpdateDownApkPath = "hwmc" + File.separator + "downApk";
        String saveApkPath= appUpdateDownApkPath + File.separator;
        String sdPath = getSDCardPath();
        if (!isExistSDCard() || TextUtils.isEmpty(sdPath)) {
            ArrayList<String> sdPathList = getExtSDCardPath();
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
                scanner(context,savePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return savePath;
    }

    private static void scanner(Context context, String filePath) {
        MediaScannerConnection.scanFile(context,
                new String[]{filePath}, null, new MediaScannerConnection.OnScanCompletedListener() {
                    @Override
                    public void onScanCompleted(String path, Uri uri) {
                        Log.i("ExternalStorage", "Scanned " + path + ":");
                        Log.i("ExternalStorage", "-> uri=" + uri);
                    }
                });
    }


    /**
     * 保存图片/崩溃日志保存文件目录
     * @param fileName              文件名称：比如crash，image，这个是文件夹
     * @param name                  自己命名，文件名称比如图片  aa.jpg
     * @return                      路径
     */
    public static String getLocalFileSavePathDir(Context context , String fileName , String name){
        //获得SDCard 的路径,storage/sdcard
        String sdPath = getSDCardPath();

        //判断 SD 卡是否可用
        if (!isSDCardEnable(context) || TextUtils.isEmpty(sdPath)) {
            //获取 SD 卡路径
            List<String> sdPathList = getSDCardPaths(context);
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


    /*------------------------------------------------------------------------------------------*/


    /**
     * 判断SDCard是否挂载
     * Environment.MEDIA_MOUNTED,表示SDCard已经挂载
     * Environment.getExternalStorageState()，获得当前SDCard的挂载状态
     */
    private static boolean isMounted() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }


    /**
     * 获得SDCard 的路径,storage/sdcard
     * @return          路径
     */
    public static String getSDCardPath() {
        String path = null;
        if (isMounted()) {
            path = Environment.getExternalStorageDirectory().getPath();
        }
        return path;
    }


    /**
     * 判断 SD 卡是否可用
     *
     * @return true : 可用<br>false : 不可用
     */
    public static boolean isSDCardEnable(Context context) {
        return !getSDCardPaths(context).isEmpty();
    }


    /**
     * 判断是否有sd卡
     * @return                      是否有sd
     */
    public static boolean isExistSDCard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }



    /**
     * 获取 SD 卡路径
     *
     * @return SD 卡路径
     */
    @SuppressWarnings("TryWithIdenticalCatches")
    public static List<String> getSDCardPaths(Context context) {
        StorageManager storageManager = (StorageManager) context.getApplicationContext()
                .getSystemService(Context.STORAGE_SERVICE);
        List<String> paths = new ArrayList<>();
        try {
            Method getVolumePathsMethod = StorageManager.class.getMethod("getVolumePaths");
            getVolumePathsMethod.setAccessible(true);
            Object invoke = getVolumePathsMethod.invoke(storageManager);
            paths = Arrays.asList((String[]) invoke);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return paths;
    }

    /**
     * 获取外置SD卡路径
     * @return 应该就一条记录或空
     */
    public static ArrayList<String> getExtSDCardPath() {
        ArrayList<String> lResult = new ArrayList<>();
        try {
            Runtime rt = Runtime.getRuntime();
            Process process = rt.exec("mount");
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("extSdCard")) {
                    String[] arr = line.split(" ");
                    String path = arr[1];
                    File file = new File(path);
                    if (file.isDirectory()) {
                        lResult.add(path);
                    }
                }
            }
            isr.close();
        } catch (Exception ignored) {
        }
        return lResult;
    }


}

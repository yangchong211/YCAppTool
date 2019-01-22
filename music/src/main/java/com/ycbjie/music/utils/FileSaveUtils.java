package com.ycbjie.music.utils;


import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.SDCardUtils;
import com.ycbjie.library.utils.SDUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

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
     */
    private final static String APP_ROOT_SAVE_PATH = "ycPlayer";
    private final static String property = File.separator;
    public final static String music = "music";
    public final static String image = "image";
    public final static String logger = "logger";


    /**
     * 系统保存文件目录
     * @param name                  自己命名
     * @return                      路径
     */
    public static String getLocalRootSavePathDir(String name){
        if(TextUtils.isEmpty(name)){
            return "";
        }
        //获得SDCard 的路径,storage/sdcard
        String sdPath = SDUtils.getSDCardPath();

        //判断 SD 卡是否可用
        if (!SDCardUtils.isSDCardEnable() || TextUtils.isEmpty(sdPath)) {
            //获取 SD 卡路径
            List<String> sdPathList = SDCardUtils.getSDCardPaths();
            if (sdPathList != null && sdPathList.size() > 0 && !TextUtils.isEmpty(sdPathList.get(0))) {
                sdPath = sdPathList.get(0);
            }
        }
        if (TextUtils.isEmpty(sdPath)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(sdPath);
        sb.append(property);
        sb.append(APP_ROOT_SAVE_PATH);
        sb.append(property);
        sb.append(name);
        sb.append(property);
        return sb.toString();
    }


    /**
     * 保存bitmap到本地
     */
    public static String saveBitmap(final Context context, Bitmap mBitmap, String savePath, boolean isScanner) {
        if (mBitmap.isRecycled()) {
            return null;
        }
        if (TextUtils.isEmpty(savePath)) {
            savePath = getLocalRootSavePathDir(music);
        }
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
            if (isScanner) {
                scanner(context, savePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return savePath;
    }

    private static void scanner(Context context, String filePath) {
        MediaScannerConnection.scanFile(context,
                new String[]{filePath}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        Log.i("ExternalStorage", "Scanned " + path + ":");
                        Log.i("ExternalStorage", "-> uri=" + uri);
                    }
                });
    }

}

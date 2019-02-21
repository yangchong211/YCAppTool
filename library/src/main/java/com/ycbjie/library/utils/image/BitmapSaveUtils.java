package com.ycbjie.library.utils.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.util.Log;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;
import com.ycbjie.library.utils.SDUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public class BitmapSaveUtils {


    private static final String IMAGE_SAVE_PATH = "images";

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

    /**
     * 获取本地图片保存路径
     *
     * @return
     */
    public static String getLocalImgSavePath() {
        String savePath = SDUtils.getLocalRootSavePathDir(Utils.getApp(),IMAGE_SAVE_PATH)
                + generateRandomName()+".jpg";
        LogUtils.e("图片地址"+savePath);
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

    private static String generateRandomName() {
        return UUID.randomUUID().toString();
    }



}

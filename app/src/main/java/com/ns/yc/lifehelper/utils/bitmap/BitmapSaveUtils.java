package com.ns.yc.lifehelper.utils.bitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.UUID;

public class BitmapSaveUtils {


    private static final String IMAGE_SAVE_PATH = "yc" + File.separator + "share" +
            File.separator + "images";//分享时临时文件保存目录


    /**
     * 保存bitmap到本地
     * @param context
     * @param mBitmap
     * @param savePath
     * @param isScanner
     * @return
     */
    public static String saveBitmap(Context context, Bitmap mBitmap, String savePath ,boolean isScanner) {
        if (TextUtils.isEmpty(savePath)) {
            savePath = getLocalImgSavePath();
        }

        try {
            File filePic = new File(savePath);
            if (!filePic.exists()) {
                filePic.getParentFile().mkdirs();
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
        /*String sdPath = getInnerSDCardPath();
        if (!ExistSDCard() || TextUtils.isEmpty(sdPath)) {
            ArrayList<String> sdPathList = getExtSDCardPath();
            if (sdPathList != null && sdPathList.size() > 0 && !TextUtils.isEmpty(sdPathList.get(0))) {
                sdPath = sdPathList.get(0);
            }
        }
        if (TextUtils.isEmpty(sdPath)) {
            return "";
        }*/
        String savePath = getLocalImgSavePathDir() + generateRandomName()+".jpg";
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


    /**
     * 获取外置SD卡路径
     *
     * @return 应该就一条记录或空
     */
    public static ArrayList<String> getExtSDCardPath() {
        ArrayList<String> lResult = new ArrayList<String>();
        try {
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec("mount");
            InputStream is = proc.getInputStream();
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
        } catch (Exception e) {
        }
        return lResult;
    }


    private static String getLocalImgSavePathDir(){
        String sdPath = getInnerSDCardPath();
        if (!ExistSDCard() || TextUtils.isEmpty(sdPath)) {
            ArrayList<String> sdPathList = getExtSDCardPath();
            if (sdPathList != null && sdPathList.size() > 0 && !TextUtils.isEmpty(sdPathList.get(0))) {
                sdPath = sdPathList.get(0);
            }
        }
        if (TextUtils.isEmpty(sdPath)) {
            return "";
        }
        String savePath = sdPath + File.separator + IMAGE_SAVE_PATH + File.separator ;
        return savePath;
    }

    private static String generateRandomName() {
        return UUID.randomUUID().toString();
    }

    /**
     * 获取内置SD卡路径
     *
     * @return
     */
    private static String getInnerSDCardPath() {
        return Environment.getExternalStorageDirectory().getPath();
    }

    /**
     * 判断是否有sd卡
     *
     * @return
     */
    private static boolean ExistSDCard() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else{
            return false;
        }
    }


}

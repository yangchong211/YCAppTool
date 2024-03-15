/*
Copyright 2017 yangchong211（github.com/yangchong211）

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package com.yc.webviewlib.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.yc.toolutils.file.SdCardUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.List;
import java.util.UUID;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/9/10
 *     desc  : 文件管理类
 *     revise: demo地址：https://github.com/yangchong211/YCWebView
 * </pre>
 */
public final class WebFileUtils {

    /**
     * app保存路径
     * 图片保存位置：x5Web/images   (包含画廊保存图片，list条目点击item按钮保存图片)
     */
    private final static String APP_ROOT_SAVE_PATH = "WebX5";
    private static final String IMAGE_FILE_PATH = "images";
    private final static String PROPERTY = File.separator;

    /**
     * 获取本地图片保存路径
     * @return
     */
    public static String getLocalImgSavePath() {
        return getLocalFileSavePathDir(X5WebUtils.getApplication(),IMAGE_FILE_PATH, generateRandomName() + ".png");
    }

    /**
     * 获取本地图片保存路径
     * @return
     */
    public static String getLocalImagePath() {
        return getLocalFileSavePathDir(X5WebUtils.getApplication(),IMAGE_FILE_PATH,null);
    }

    /**
     * 获取name
     * @param name                          name
     * @return
     */
    public static String getImageName(String name){
        return generateRandomName() + name + ".png";
    }

    public static String getStringMd5(String str) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] array = messageDigest.digest(str.getBytes("UTF-8"));
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String generateRandomName() {
        return UUID.randomUUID().toString();
    }

    public static File getImageDir(Context context) {
        String path = null;
        try {
            if (SdCardUtils.isMounted()) {
                File extPath = context.getExternalFilesDir(null);
                if (extPath != null) {
                    path = extPath.getAbsolutePath() + PROPERTY + APP_ROOT_SAVE_PATH + PROPERTY + IMAGE_FILE_PATH;
                }
            }
        } catch (Exception e) {
            // catch accidental exception
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(path)) {
            // data storage
            path = context.getFilesDir().getAbsolutePath() + PROPERTY + APP_ROOT_SAVE_PATH + PROPERTY + IMAGE_FILE_PATH;
        }
        File file = new File(path);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                return null;
            }
        }
        return file;
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


    /**
     * 保存bitmap到本地
     */
    public static String saveBitmap(Bitmap mBitmap) {
        if (mBitmap==null){
            return null;
        }
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
        } catch (IOException e) {
            e.printStackTrace();
            //BaseToast.showRoundRectToast("稍后再试");
            return null;
        }
        return savePath;
    }


}

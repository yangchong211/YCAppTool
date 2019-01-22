package com.ycbjie.note.utils;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by PC on 2017/10/25.
 * 作者：PC
 */

public class SDCardUtils {

    public static String SDCardRoot = Environment.getExternalStorageDirectory().getAbsolutePath()
            + File.separator;
    /**
     * 获得文章图片保存路径
     */
    public static String getPictureDir(){
        String imageCacheUrl = SDCardRoot + "XRichText" + File.separator ;
        File file = new File(imageCacheUrl);
        if(!file.exists()){
            file.mkdir();  //如果不存在则创建
        }
        return imageCacheUrl;
    }

    /**
     * 图片保存到SD卡
     */
    public static String saveToSdCard(Bitmap bitmap) {
        String imageUrl = getPictureDir() + System.currentTimeMillis() + "-";
        File file = new File(imageUrl);
        try {
            FileOutputStream out = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)) {
                out.flush();
                out.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }


}

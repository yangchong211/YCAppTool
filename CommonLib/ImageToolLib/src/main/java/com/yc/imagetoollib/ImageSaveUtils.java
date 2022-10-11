package com.yc.imagetoollib;

import android.graphics.Bitmap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time   : 2020/07/22
 *     desc   : 图片存储工具类
 *     revise :
 * </pre>
 */
public final class ImageSaveUtils {


    /**
     * 将图片保存为png格式图片
     * @param bmp           图片bitmap
     * @param path             png图片file
     */
    public static boolean saveBitmapAsPng(Bitmap bmp, String path) {
        File newFile = new File(path);
        if (!newFile.exists()) {
            //创建一个File对象所对应的目录，成功返回true，否则false。且File对象必须为路径而不是文件。
            //创建多级目录，创建路径中所有不存在的目录
            try {
                newFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return saveBitmapAsPng(bmp, newFile);
    }

    /**
     * 将图片保存为png格式图片
     * @param bmp           图片bitmap
     * @param f             png图片file
     */
    public static boolean saveBitmapAsPng(Bitmap bmp, File f) {
        boolean success;
        try {
            FileOutputStream out = new FileOutputStream(f);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            success = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            success = false;
        } catch (IOException e) {
            e.printStackTrace();
            success = false;
        }
        return success;
    }

    /**
     * 将图片保存为png格式图片
     * @param bmp           图片bitmap
     * @param path             png图片file
     */
    public static boolean saveBitmapAsJpg(Bitmap bmp, String path) {
        File newFile = new File(path);
        if (!newFile.exists()) {
            //创建一个File对象所对应的目录，成功返回true，否则false。且File对象必须为路径而不是文件。
            //创建多级目录，创建路径中所有不存在的目录
            try {
                newFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return saveBitmapAsJpg(bmp, newFile);
    }

    /**
     * 将图片保存为png格式图片
     * @param bmp           图片bitmap
     * @param f             png图片file
     */
    public static boolean saveBitmapAsJpg(Bitmap bmp, File f) {
        boolean success;
        try {
            FileOutputStream out = new FileOutputStream(f);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            success = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            success = false;
        } catch (IOException e) {
            e.printStackTrace();
            success = false;
        }
        return success;
    }


}

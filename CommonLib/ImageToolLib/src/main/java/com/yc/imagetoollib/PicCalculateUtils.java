package com.yc.imagetoollib;

import android.content.Context;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author yangchong
 * time  : 2018/11/9
 * GitHub : https://github.com/yangchong211/YCCommonLib
 * desc  : 图片计算工具类
 * revise:
 */
public final class PicCalculateUtils {

    /**
     * 计算图片合适压缩比较
     * @param srcWidth  资源宽度
     * @param srcHeight 资源高度
     * @return          压缩比例
     */
    public static int computeSize(int srcWidth, int srcHeight) {
        srcWidth = srcWidth % 2 == 1 ? srcWidth + 1 : srcWidth;
        srcHeight = srcHeight % 2 == 1 ? srcHeight + 1 : srcHeight;
        int longSide = Math.max(srcWidth, srcHeight);
        int shortSide = Math.min(srcWidth, srcHeight);
        float scale = ((float) shortSide / longSide);
        if (scale <= 1 && scale > 0.5625) {
            if (longSide < 1664) {
                return 1;
            } else if (longSide < 4990) {
                return 2;
            } else if (longSide > 4990 && longSide < 10240) {
                return 4;
            } else {
                return longSide / 1280;
            }
        } else if (scale <= 0.5625 && scale > 0.5) {
            return longSide / 1280 == 0 ? 1 : longSide / 1280;
        } else {
            return (int) Math.ceil(longSide / (1280.0 / scale));
        }
    }


    /**
     * 读取图片属性：旋转的角度
     *
     * @param context   上下文
     * @param filePath 图片绝对路径
     * @return degree旋转的角度
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static int readPictureDegree(Context context, String filePath) {
        ExifInterface exifInterface;
        InputStream inputStream = null;
        try {
            if (PicMimeTypeUtils.isContent(filePath)) {
                inputStream = context.getContentResolver().openInputStream(Uri.parse(filePath));
                exifInterface = new ExifInterface(inputStream);
            } else {
                exifInterface = new ExifInterface(filePath);
            }
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return 90;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    return 180;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    return 270;
                default:
                    return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            if (inputStream!=null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 读取图片属性：旋转的角度
     *
     * @param context   上下文
     * @param file      图片file
     * @return degree旋转的角度
     */
    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static int readPictureDegree(Context context, File file) {
        ExifInterface exifInterface;
        try {
            exifInterface = new ExifInterface(file);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return 90;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    return 180;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    return 270;
                default:
                    return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

}

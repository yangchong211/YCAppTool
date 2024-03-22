package com.yc.zxingserver.tools;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time  : 2020/6/6
 *     desc  : 二维码图片压缩工具类
 *     revise:
 * </pre>
 */
public class ZxingImageUtils {

    /**
     * 压缩图片
     */
    public static Bitmap compressBitmap(String path, int reqWidth, int reqHeight){
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        //获取原始图片大小
        newOpts.inJustDecodeBounds = true;
        // 此时返回bm为空
        BitmapFactory.decodeFile(path, newOpts);
        float width = newOpts.outWidth;
        float height = newOpts.outHeight;
        // 缩放比，由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        // wSize=1表示不缩放
        int wSize = 1;
        if (width > reqWidth) {
            // 如果宽度大的话根据宽度固定大小缩放
            wSize = (int) (width / reqWidth);
        }
        // wSize=1表示不缩放
        int hSize = 1;
        // 如果高度高的话根据宽度固定大小缩放
        if (height > reqHeight) {
            hSize = (int) (height / reqHeight);
        }
        int size = Math.max(wSize,hSize);
        if (size <= 0){
            size = 1;
        }
        // 设置缩放比例
        newOpts.inSampleSize = size;
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        newOpts.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, newOpts);
    }

}

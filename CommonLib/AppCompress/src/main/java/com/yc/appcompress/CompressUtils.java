package com.yc.appcompress;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.ByteArrayOutputStream;


/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time   : 2020/07/22
 *     desc   : 图片压缩工具类
 *     revise :
 * </pre>
 */
public final class CompressUtils {


    /**
     * 计算图片的缩放值
     * @param options                           属性
     * @param reqWidth                          宽
     * @param reqHeight                         高
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // 源图片的高度和宽度
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
            // 一定都会大于等于目标的宽和高。
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }


    /**
     * 质量压缩
     * @param image                             bitmap
     * @param maxSize                           最大值，这个是指字节大小
     * @return
     */
    public static Bitmap compressImage(Bitmap image, int maxSize){
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        // 缩放
        int options = 80;
        // 将位图存储到输出流中(不压缩)
        image.compress(Bitmap.CompressFormat.JPEG, options, os);
        // 循环压缩
        while ( os.toByteArray().length / 1024 > maxSize) {
            // 清除
            os.reset();
            // 每次减去10
            options -= 10;
            // 循环压缩到指定的大小
            image.compress(Bitmap.CompressFormat.JPEG, options, os);
        }
        Bitmap bitmap = null;
        byte[] b = os.toByteArray();
        if (b.length != 0) {
            //使用BitmapFactory工厂，加载byte得到bitmap对象
            bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
        }
        return bitmap;
    }

}

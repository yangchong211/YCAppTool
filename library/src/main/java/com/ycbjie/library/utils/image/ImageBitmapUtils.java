package com.ycbjie.library.utils.image;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.util.Log;

import java.io.ByteArrayOutputStream;

/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/11/26
 *     desc  : bitmap优化压缩工具类
 *     revise: 针对每个工具类，方法以及参数必须标注清晰
 * </pre>
 */
public class ImageBitmapUtils {


    /**
     * 将drawable转化成bitmap
     * @param drawable                  drawable
     * @return                          bitmap
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ?
                Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }

    /*
    质量压缩方法：在保持像素的前提下改变图片的位深及透明度等，来达到压缩图片的目的:
    1、bitmap图片的大小不会改变
    2、bytes.length是随着quality变小而变小的。
    这样适合去传递二进制的图片数据，比如分享图片，要传入二进制数据过去，限制500kb之内。
    */
    /**
     * 第一种：质量压缩法
     * @param image     目标原图
     * @param maxSize   最大的图片大小
     * @return          bitmap，注意可以测试以下压缩前后bitmap的大小值
     */
    public static Bitmap compressImage(Bitmap image , long maxSize) {
        int byteCount = image.getByteCount();
        Log.i("yc压缩图片","压缩前大小"+byteCount);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 把ByteArrayInputStream数据生成图片
        Bitmap bitmap = null;
        // 质量压缩方法，options的值是0-100，这里100表示原来图片的质量，不压缩，把压缩后的数据存放到baos中
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int options = 90;
        // 循环判断如果压缩后图片是否大于maxSize,大于继续压缩
        while (baos.toByteArray().length  > maxSize) {
            // 重置baos即清空baos
            baos.reset();
            // 这里压缩options%，把压缩后的数据存放到baos中
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);
            // 每次都减少10，当为1的时候停止，options<10的时候，递减1
            if(options == 1){
                break;
            }else if (options <= 10) {
                options -= 1;
            } else {
                options -= 10;
            }
        }
        byte[] bytes = baos.toByteArray();
        if (bytes.length != 0) {
            // 把压缩后的数据baos存放到bytes中
            bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            int byteCount1 = bitmap.getByteCount();
            Log.i("yc压缩图片","压缩后大小"+byteCount1);
        }
        return bitmap;
    }


    /**
     * 第一种：质量压缩法
     *
     * @param src           源图片
     * @param maxByteSize   允许最大值字节数
     * @param recycle       是否回收
     * @return              质量压缩压缩过的图片
     */
    public static Bitmap compressByQuality(final Bitmap src, final long maxByteSize, final boolean recycle) {
        if (src == null || src.getWidth() == 0 || src.getHeight() == 0 || maxByteSize <= 0) {
            return null;
        }
        Log.i("yc压缩图片","压缩前大小"+src.getByteCount());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        src.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] bytes;
        if (baos.size() <= maxByteSize) {// 最好质量的不大于最大字节，则返回最佳质量
            bytes = baos.toByteArray();
        } else {
            baos.reset();
            src.compress(Bitmap.CompressFormat.JPEG, 0, baos);
            if (baos.size() >= maxByteSize) { // 最差质量不小于最大字节，则返回最差质量
                bytes = baos.toByteArray();
            } else {
                // 二分法寻找最佳质量
                int st = 0;
                int end = 100;
                int mid = 0;
                while (st < end) {
                    mid = (st + end) / 2;
                    baos.reset();
                    src.compress(Bitmap.CompressFormat.JPEG, mid, baos);
                    int len = baos.size();
                    if (len == maxByteSize) {
                        break;
                    } else if (len > maxByteSize) {
                        end = mid - 1;
                    } else {
                        st = mid + 1;
                    }
                }
                if (end == mid - 1) {
                    baos.reset();
                    src.compress(Bitmap.CompressFormat.JPEG, st, baos);
                }
                bytes = baos.toByteArray();
            }
        }
        if (recycle && !src.isRecycled()){
            src.recycle();
        }
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        Log.i("yc压缩图片","压缩后大小"+bitmap.getByteCount());
        return bitmap;
    }


    /**
     * 第一种：质量压缩法
     *
     * @param src           源图片
     * @param quality       质量
     * @param recycle       是否回收
     * @return              质量压缩后的图片
     */
    public static Bitmap compressByQuality(final Bitmap src, @IntRange(from = 0, to = 100) final int quality, final boolean recycle) {
        if (src == null || src.getWidth() == 0 || src.getHeight() == 0) {
            return null;
        }
        Log.i("yc压缩图片","压缩前大小"+src.getByteCount());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        src.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        byte[] bytes = baos.toByteArray();
        if (recycle && !src.isRecycled()) {
            src.recycle();
        }
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        Log.i("yc压缩图片","压缩后大小"+bitmap.getByteCount());
        return bitmap;
    }



    /**
     * 第二种：按采样大小压缩
     *
     * @param src               源图片
     * @param sampleSize        采样率大小
     * @param recycle           是否回收
     * @return                  按采样率压缩后的图片
     */
    public static Bitmap compressBySampleSize(final Bitmap src, final int sampleSize, final boolean recycle) {
        if (src == null || src.getWidth() == 0 || src.getHeight() == 0) {
            return null;
        }
        Log.i("yc压缩图片","压缩前大小"+src.getByteCount());
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = sampleSize;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        src.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] bytes = baos.toByteArray();
        if (recycle && !src.isRecycled()) {
            src.recycle();
        }
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
        Log.i("yc压缩图片","压缩后大小"+bitmap.getByteCount());
        return bitmap;
    }


    /**
     * 第二种：按采样大小压缩
     *
     * @param src               源图片
     * @param maxWidth          最大宽度
     * @param maxHeight         最大高度
     * @param recycle           是否回收
     * @return                  按采样率压缩后的图片
     */
    public static Bitmap compressBySampleSize(final Bitmap src, final int maxWidth, final int maxHeight, final boolean recycle) {
        if (src == null || src.getWidth() == 0 || src.getHeight() == 0) {
            return null;
        }
        Log.i("yc压缩图片","压缩前大小"+src.getByteCount());
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        src.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] bytes = baos.toByteArray();
        BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
        options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight);
        options.inJustDecodeBounds = false;
        if (recycle && !src.isRecycled()) {
            src.recycle();
        }
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
        Log.i("yc压缩图片","压缩后大小"+bitmap.getByteCount());
        return bitmap;
    }

    /**
     * 计算获取缩放比例inSampleSize
     */
    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }
        return inSampleSize;
    }


    /**
     * 第三种：按缩放压缩
     *
     * @param src                   源图片
     * @param newWidth              新宽度
     * @param newHeight             新高度
     * @param recycle               是否回收
     * @return                      缩放压缩后的图片
     */
    public static Bitmap compressByScale(final Bitmap src, final int newWidth, final int newHeight, final boolean recycle) {
        Log.i("yc压缩图片","压缩前大小"+src.getByteCount());
        return scale(src, newWidth, newHeight, recycle);
    }

    /**
     * 第三种：按缩放压缩
     *
     * @param src                   源图片
     * @param scaleWidth            缩放宽度倍数
     * @param scaleHeight           缩放高度倍数
     * @param recycle               是否回收
     * @return                      缩放压缩后的图片
     */
    public static Bitmap compressByScale(final Bitmap src, final float scaleWidth, final float scaleHeight, final boolean recycle) {
        Log.i("yc压缩图片","压缩前大小"+src.getByteCount());
        return scale(src, scaleWidth, scaleHeight, recycle);
    }

    /**
     * 缩放图片
     *
     * @param src                   源图片
     * @param scaleWidth            缩放宽度倍数
     * @param scaleHeight           缩放高度倍数
     * @param recycle               是否回收
     * @return                      缩放后的图片
     */
    private static Bitmap scale(final Bitmap src, final float scaleWidth, final float scaleHeight, final boolean recycle) {
        if (src == null || src.getWidth() == 0 || src.getHeight() == 0) {
            return null;
        }
        Matrix matrix = new Matrix();
        matrix.setScale(scaleWidth, scaleHeight);
        Bitmap ret = Bitmap.createBitmap(src, 0, 0, src.getWidth(),
                src.getHeight(), matrix, true);
        if (recycle && !src.isRecycled()) {
            src.recycle();
        }
        Log.i("yc压缩图片","压缩后大小"+ret.getByteCount());
        return ret;
    }


}

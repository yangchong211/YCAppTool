package com.yc.compress.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import androidx.annotation.IntRange;

import java.io.ByteArrayOutputStream;
import java.io.File;


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
     * 图片缩放
     * @param bitmap 对象
     * @param w 要缩放的宽度
     * @param h 要缩放的高度
     * @return newBmp 新 Bitmap对象
     */
    public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h){
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) w / width);
        float scaleHeight = ((float) h / height);
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newBmp = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        return newBmp;
    }

    /**
     * 通过大小压缩，将修改图片宽高
     * @param file                                  图片file
     * @param pixelW                                宽
     * @param pixelH                                高
     * @return
     */
    public static Bitmap getBitmap(final File file, final int pixelW, int pixelH) {
        if (file == null) {
            return null;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getAbsolutePath(), options);
        options.inSampleSize = calculateInSampleSize(options, pixelW, pixelH);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(file.getAbsolutePath(), options);
    }


    /**
     * 按缩放压缩。双线性采样
     * @param src               目标bitmap
     * @param newWidth          宽
     * @param newHeight         高
     * @param recycle           是否回收
     * @return
     */
    protected static Bitmap compressByScale(final Bitmap src, final int newWidth, final int newHeight,
                                         final boolean recycle) {
        if (isEmptyBitmap(src)) {
            return null;
        }
        Bitmap ret = Bitmap.createScaledBitmap(src, newWidth, newHeight, true);
        if (recycle && !src.isRecycled() && ret != src) {
            src.recycle();
        }
        return ret;
    }

    /**
     * 按质量压缩
     * @param src               目标bitmap
     * @param quality           质量比
     * @return
     */
    protected static Bitmap compressByQuality(final Bitmap src, @IntRange(from = 0, to = 100) final int quality) {
        return compressByQuality(src, quality ,null);
    }

    /**
     * 按质量压缩
     * @param src               目标bitmap
     * @param quality           质量比
     * @return
     */
    protected static Bitmap compressByQuality(final Bitmap src, @IntRange(from = 0, to = 100) final int quality , BitmapFactory.Options options) {
        byte[] bytes = compressByQuality(src, quality, false);
        if (options == null){
            options = new BitmapFactory.Options();
        }
        if (bytes != null) {
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
        }
        return null;
    }

    /**
     * 按质量压缩
     * 质量压缩的特点是图片文件大小会减小，但是图片的像素数不会改变，加载压缩后的图片，占据的内存不会减少。
     * @param src               目标bitmap
     * @param quality           质量比
     * @return
     */
    private static byte[] compressByQuality(final Bitmap src, @IntRange(from = 0, to = 100) final int quality,
                                           final boolean recycle) {
        if (isEmptyBitmap(src)) {
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 质量压缩api
        // 第一个参数：format
        // format表示以某种格式压缩图片，支持三种格式：
        // Bitmap.CompressFormat.JPEG、Bitmap.CompressFormat.PNG、Bitmap.CompressFormat.WEBP。
        // 第二个参数：quality
        // quality代表压缩程度，取值0-100。
        // 100表示不压缩，压缩后图片和原图片文件大小一样；0表示压缩到最小的图片文件大小，质量压缩是保持像素不变的前提下改变图片的位深及透明度，来压缩图片大小。
        // 因为要保持像素不变，所以它就无法无限压缩，图片文件大小到达一个值之后就不会继续变小了。
        // 第三个参数：stream
        // 将压缩的图片写入输出流中，进而保存成文件。
        src.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        byte[] bytes = baos.toByteArray();
        if (recycle && !src.isRecycled()) {
            src.recycle();
        }
        return bytes;
    }

    /**
     * 按采样大小压缩
     * ImageView比图片明显偏小时，为了减少内存占用，防止oom，用采样率压缩压缩图片，降低内存占用。
     * @param src                   目标bitmap
     * @param sampleSize            采样率
     * @return
     */
    protected static Bitmap compressBySampleSize(final Bitmap src, final int sampleSize) {
        return compressBySampleSize(src, sampleSize, false);
    }

    /**
     * 按采样大小压缩
     * 当ImageView尺寸比图片尺寸过小时，加载原图的话，原图会占据较大的内存，容易出现oom，这时要使用采样率压缩将图片宽高压缩到ImageView的宽高。
     * @param src                   目标bitmap
     * @param sampleSize            采样率
     * @return
     */
    private static Bitmap compressBySampleSize(final Bitmap src, final int sampleSize,
                                              final boolean recycle) {
        if (isEmptyBitmap(src)) {
            return null;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inSampleSize = sampleSize;
        options.inJustDecodeBounds = false;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        src.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] bytes = baos.toByteArray();
        if (recycle && !src.isRecycled()) {
            src.recycle();
        }
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
    }


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
        if (inSampleSize <= 0) {
            // 1表示不缩放
            inSampleSize = 1;
        }
        return inSampleSize;
    }

    /**
     * 计算出所需要压缩的大小
     */
    protected static int calculateSampleSize(BitmapFactory.Options options) {
        CompressConfig compressConfig = AppCompress.getInstance().getCompressConfig();
        int sampleSize = 1;
        int picWidth = options.outWidth;
        int picHeight = options.outHeight;
        int maxPixel = compressConfig.getMaxPixel();
        // 缩放比,用高或者宽其中较大的一个数据进行计算
        if (picWidth >= picHeight && picWidth > maxPixel) {
            sampleSize = picWidth / maxPixel;
            sampleSize++;
        } else if (picWidth < picHeight && picHeight > maxPixel) {
            sampleSize = picHeight / maxPixel;
            sampleSize++;
        }
        return sampleSize;
    }


    /**
     * 质量压缩
     * @param image                             bitmap
     * @return
     */
    protected static Bitmap compressImage(Bitmap image){
        CompressConfig compressConfig = AppCompress.getInstance().getCompressConfig();
        if (isEmptyBitmap(image)) {
            return null;
        }
        // 创建字节流对象
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        // 缩放
        int options = compressConfig.getQuality();
        // 将位图存储到输出流中(不压缩)
        image.compress(compressConfig.getComPressFormat(), options, os);
        //如果压缩后图片还是>targetSize，则继续压缩
        if (compressConfig.getMaxSize() > compressConfig.getTargetSize()){
            // 循环压缩
            while ( os.toByteArray().length / 1024 > compressConfig.getMaxSize()) {
                // 判断如果图片大于maxSize,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
                // 清除
                os.reset();
                // 每次减去10
                options -= 10;
                // 循环压缩到指定的大小
                image.compress(compressConfig.getComPressFormat(), options, os);
            }
        }
        Bitmap bitmap = null;
        byte[] b = os.toByteArray();
        if (b.length != 0) {
            //使用BitmapFactory工厂，加载byte得到bitmap对象
            bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
        }
        return bitmap;
    }

    private static boolean isEmptyBitmap(final Bitmap src) {
        return src == null || src.getWidth() == 0 || src.getHeight() == 0;
    }

}

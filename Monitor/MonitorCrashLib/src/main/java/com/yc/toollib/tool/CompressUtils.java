package com.yc.toollib.tool;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

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
     * 获得屏幕宽度
     * @param context                               上下文
     * @return
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * 获得屏幕高度
     * @param context                               上下文
     * @return
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    /**
     * 将drawable转化成bitmap
     * @param drawable                              图片
     * @return
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        int intrinsicWidth = drawable.getIntrinsicWidth();
        int intrinsicHeight = drawable.getIntrinsicHeight();
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight,config);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, intrinsicWidth, intrinsicHeight);
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * 通过大小压缩，将修改图片宽高
     * 此处默认设置图片的压缩宽高是屏幕宽高
     * @param is                                    图片流
     * @param context                               上下文
     * @return
     */
    public static Bitmap compressDrawableByBmp(InputStream is , Context context){
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        return compressBitmapByBmp(bitmap,context);
    }


    /**
     * 通过大小压缩，将修改图片宽高
     * 此处默认设置图片的压缩宽高是屏幕宽高
     * @param drawable                              图片drawable
     * @param context                               上下文
     * @return
     */
    public static Bitmap compressDrawableByBmp(Drawable drawable , Context context){
        Bitmap bitmap = drawableToBitmap(drawable);
        return compressBitmapByBmp(bitmap,context);
    }

    /**
     * 通过大小压缩，将修改图片宽高
     * 此处默认设置图片的压缩宽高是屏幕宽高
     * @param image                                 图片Bitmap
     * @param context                               上下文
     * @return
     */
    public static Bitmap compressBitmapByBmp(Bitmap image, Context context){
        int screenHeight = getScreenHeight(context);
        int screenWidth = getScreenWidth(context);
        return compressBitmapByBmp(image,screenWidth,screenHeight);
    }

    /**
     * 通过大小压缩，将修改图片宽高
     * @param is                                    图片流
     * @param pixelW                                宽
     * @param pixelH                                高
     * @return
     */
    public static Bitmap compressBitmapByBmp(InputStream is, int pixelW, int pixelH){
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        return compressBitmapByBmp(bitmap,pixelW,pixelH);
    }

    /**
     * 通过大小压缩，将修改图片宽高
     * @param drawable                              图片drawable
     * @param pixelW                                宽
     * @param pixelH                                高
     * @return
     */
    public static Bitmap compressBitmapByBmp(Drawable drawable, int pixelW, int pixelH){
        Bitmap bitmap = drawableToBitmap(drawable);
        return compressBitmapByBmp(bitmap,pixelW,pixelH);
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
     * 通过大小压缩，将修改图片宽高
     * @param image                                 图片Bitmap
     * @param pixelW                                宽
     * @param pixelH                                高
     * @return
     */
    public static Bitmap compressBitmapByBmp(Bitmap image, int pixelW, int pixelH) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, os);
        if( os.toByteArray().length / 1024>1024) {
            //判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            os.reset();
            //重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 50, os);
            //这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        newOpts.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap bitmap = BitmapFactory.decodeStream(is, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        //be=1表示不缩放
        int be = calculateInSampleSize(newOpts, pixelW, pixelH);
        if (be <= 0) {
            be = 1;
        }
        //设置缩放比例
        newOpts.inSampleSize = be;
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        is = new ByteArrayInputStream(os.toByteArray());
        bitmap = BitmapFactory.decodeStream(is, null, newOpts);
        int desWidth = (int) (w / be);
        int desHeight = (int) (h / be);
        bitmap = Bitmap.createScaledBitmap(bitmap, desWidth, desHeight, true);
        //压缩好比例大小后再进行质量压缩
        //return compress(bitmap, maxSize); // 这里再进行质量压缩的意义不大，反而耗资源，删除
        return bitmap;
    }


    /**
     * 根据路径获得突破并压缩返回bitmap用于显示
     * @param filePath                              文件路径
     * @param newWidth                              宽
     * @param newHeight                             高
     * @return
     */
    public static Bitmap getSmallBitmap(String filePath, int newWidth, int newHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap decodeFile = BitmapFactory.decodeFile(filePath, options);
        ToolLogUtils.d("CompressUtils----getSmallBitmap---byteCount压缩前大小--"+decodeFile);
        // Calculate inSampleSize
        // 计算图片的缩放值
        options.inSampleSize = calculateInSampleSize(options, newWidth, newHeight);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
        int bitmapByteCount = bitmap.getByteCount();
        ToolLogUtils.d("CompressUtils----getSmallBitmap---byteCount压缩中大小--"+bitmapByteCount);
        // 质量压缩
        Bitmap newBitmap = compressImage(bitmap, 500);
        int byteCount = newBitmap.getByteCount();
        ToolLogUtils.d("CompressUtils----getSmallBitmap---byteCount压缩后大小--"+byteCount);
        if (bitmap != null){
            // 手动释放资源
            bitmap.recycle();
        }
        return newBitmap;
    }

    /**
     * 通过像素压缩图片，将修改图片宽高
     * @param srcPath                           图片路径
     * @param pixelW                            宽
     * @param pixelH                            高
     * @return
     */
    public static Bitmap compressBitmapByPath(String srcPath, float pixelW, float pixelH) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        newOpts.inPreferredConfig = Bitmap.Config.RGB_565;
        //此时返回bm为空
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath,newOpts);

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        //这里设置高度为800f
        float hh = pixelH;
        //这里设置宽度为480f
        float ww = pixelW;
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        //be=1表示不缩放
        int be = 1;
        //如果宽度大的话根据宽度固定大小缩放
        if (w > h && w > ww) {
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {
            //如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0) {
            be = 1;
        }
        //设置缩放比例
        newOpts.inSampleSize = be;
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        //return compress(bitmap, maxSize); // 这里再进行质量压缩的意义不大，反而耗资源，删除
        return bitmap;
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
        return inSampleSize;
    }


    /**
     * 质量压缩
     * @param image                             bitmap
     * @param maxSize                           最大值
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

package com.yc.compress.image;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time   : 2020/07/22
 *     desc   : bitmap图片工具类
 *     revise :
 * </pre>
 */
public final class AppBitmapUtils {

    @SuppressLint("UseCompatLoadingForDrawables")
    public static Drawable getDrawable(Context context,@DrawableRes int id){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return context.getResources().getDrawable(id,context.getTheme());
        } else {
            return context.getResources().getDrawable(id);
        }
    }

    public static Drawable getCompatDrawable(Context context,@DrawableRes int id){
        return ContextCompat.getDrawable(context, id);
    }

    /**
     * 通过资源id获取bitmap位图
     * @param context       上下文
     * @param id            资源id
     * @return
     */
    public static Bitmap getBitmap(Context context,@DrawableRes int id){
        Drawable compatDrawable = getDrawable(context,id);
        return drawableToBitmap(compatDrawable);
    }

    /**
     * 把Bitmap转Byte
     */
    public static byte[] bitmap2Bytes(Bitmap bm){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * 把Bitmap转Byte
     */
    public static InputStream bitmap2InputStream(Bitmap bm){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bytes = baos.toByteArray();
        InputStream isBm = new ByteArrayInputStream(bytes);
        return isBm;
    }

    /**
     * ColorMatrix类有一个内置的方法可用于改变饱和度。把图变成灰色
     * 传入一个大于1的数字将增加饱和度，而传入一个0～1之间的数字会减少饱和度。0值将产生一幅灰度图像。
     * @param bitmap        bitmap图片对象
     * @return              灰色图像
     */
    public static Bitmap greyBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        //创建一个指定宽度和高度的可变位图
        Bitmap faceIconGreyBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        //用要绘制的指定位图构造一个画布。位图必须是可变的。
        Canvas canvas = new Canvas(faceIconGreyBitmap);
        //然后创建画笔，在画板canvas上画出来
        Paint paint = new Paint();
        ColorMatrix colorMatrix = new ColorMatrix();
        //设置饱和度，起关键作用的是colorMatrix.setSaturation(0);  0会把图像变成灰度图。只有黑白。
        colorMatrix.setSaturation(0);
        ColorMatrixColorFilter colorMatrixFilter = new ColorMatrixColorFilter(colorMatrix);
        paint.setColorFilter(colorMatrixFilter);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return faceIconGreyBitmap;
    }


    /**
     * 旋转Bitmap的角度，获取一张新的图片
     *
     * @param bitmap            bitmap
     * @param angle             旋转角度
     * @return
     */
    public static Bitmap rotatingImage(Bitmap bitmap, int angle) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }

    /**
     * 将base64字符串转化为bitmap
     * @param base64Data                        图片base64
     * @return
     */
    public static Bitmap base64ToBitmap(String base64Data) {
        BitmapFactory.Options bitmapOption = new BitmapFactory.Options();
        bitmapOption.inPreferredConfig = Bitmap.Config.ARGB_4444;
        String[] split = base64Data.split(",");
        byte[] decodedString = Base64.decode(split[1], Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString,
                0, decodedString.length, bitmapOption);
        return decodedByte;
    }

    /**
     * 将drawable转化成bitmap
     * @param drawable                              图片
     * @return
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        int intrinsicWidth = drawable.getIntrinsicWidth();
        int intrinsicHeight = drawable.getIntrinsicHeight();
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ?
                Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight,config);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, intrinsicWidth, intrinsicHeight);
        drawable.draw(canvas);
        return bitmap;
    }


    /**
     * Bitmap转换成Drawable
     * @param resources                     resources
     * @param bm                            bm
     * @return
     */
    public static Drawable bitmapToDrawable(Resources resources, Bitmap bm) {
        Drawable drawable = new BitmapDrawable(resources, bm);
        return drawable;
    }

    /**
     * InputStream转换成Bitmap
     * @param is                            InputStream流
     * @return
     */
    public static Bitmap bitmapToDrawable(InputStream is) {
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        return bitmap;
    }

    /**
     *
     * 将bitmap根据scale生成一张图片
     *
     * @param bitmap                    bitmap
     * @param scale                     等比缩放值
     * @return                          bitmap
     */
    public static Bitmap bitmapScale(Bitmap bitmap, float scale) {
        Matrix matrix = new Matrix();
        // 长和宽放大缩小的比例
        matrix.postScale(scale, scale);
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizeBmp;
    }

}

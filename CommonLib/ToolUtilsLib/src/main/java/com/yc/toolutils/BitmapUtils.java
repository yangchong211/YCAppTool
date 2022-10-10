package com.yc.toolutils;

import android.annotation.SuppressLint;
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
public final class BitmapUtils {

    @SuppressLint("UseCompatLoadingForDrawables")
    public static Drawable getDrawable(@DrawableRes int id){
        Application context = AppToolUtils.getApp();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return context.getResources().getDrawable(id,context.getTheme());
        } else {
            return context.getResources().getDrawable(id);
        }
    }

    public static Drawable getCompatDrawable(@DrawableRes int id){
        return ContextCompat.getDrawable(AppToolUtils.getApp(), id);
    }

    public static Bitmap getBitmap(@DrawableRes int id){
        Drawable compatDrawable = getDrawable(id);
        return drawableToBitmap(compatDrawable);
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
        Bitmap faceIconGreyBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(faceIconGreyBitmap);
        Paint paint = new Paint();
        ColorMatrix colorMatrix = new ColorMatrix();
        //起关键作用的是colorMatrix.setSaturation(0);  0会把图像变成灰度图。只有黑白。
        colorMatrix.setSaturation(0);
        ColorMatrixColorFilter colorMatrixFilter = new ColorMatrixColorFilter(colorMatrix);
        paint.setColorFilter(colorMatrixFilter);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return faceIconGreyBitmap;
    }

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
     * 请求网络图片转化成bitmap
     * @param url                       url
     * @return                          将url图片转化成bitmap对象
     */
    public static Bitmap returnBitMap(String url) {
        long l1 = System.currentTimeMillis();
        URL myFileUrl = null;
        Bitmap bitmap = null;
        HttpURLConnection conn = null;
        InputStream is = null;
        try {
            myFileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        if (myFileUrl==null){
            return null;
        }
        try {
            conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(5000);
            conn.setDoInput(true);
            conn.connect();
            is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                    conn.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            long l2 = System.currentTimeMillis();
            long time = (l2-l1) ;
            Log.e("毫秒值",time+"");
        }
        return bitmap;
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

    public static Bitmap convertViewToBitMap(View view){
        // 打开图像缓存
        view.setDrawingCacheEnabled(true);
        // 必须调用measure和layout方法才能成功保存可视组件的截图到png图像文件
        // 测量View大小
        int i = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int n = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(i, n);
        // 发送位置和尺寸到View及其所有的子View
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        // 获得可视组件的截图
        Bitmap bitmap = view.getDrawingCache();
        return bitmap;
    }


    public static Bitmap loadBitmapFromView(View v) {
        v.measure(0, 0);
        int w = v.getMeasuredWidth();
        int h = v.getMeasuredHeight();
        if (w <= 0 || h <= 0) {
            DisplayMetrics metric = new DisplayMetrics();
            w = metric.widthPixels;// 屏幕宽度（像素）
            h = metric.heightPixels;// 屏幕高度（像素）
        }
        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);
        c.drawColor(Color.WHITE);
        //如果不设置canvas画布为白色，则生成透明
        v.layout(0, 0, w, h);
        v.draw(c);
        return bmp;
    }


    public static boolean saveBitmap(Context context, Bitmap bitmap, String filePath) {
        File file = new File(filePath);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
            //把文件插入到系统图库
            try {
                MediaStore.Images.Media.insertImage(context.getContentResolver(),
                        file.getAbsolutePath(), file.getName(), null);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            // 最后通知图库更新
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                    Uri.parse("file://" + file)));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}

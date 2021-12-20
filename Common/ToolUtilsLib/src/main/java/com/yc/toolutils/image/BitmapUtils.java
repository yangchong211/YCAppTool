package com.yc.toolutils.image;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

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

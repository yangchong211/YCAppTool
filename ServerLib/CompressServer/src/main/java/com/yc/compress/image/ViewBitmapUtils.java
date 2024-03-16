package com.yc.compress.image;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.SurfaceView;
import android.view.View;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time   : 2020/07/22
 *     desc   : 将view转化为bitmap图片工具类
 *     revise :
 * </pre>
 */
public final class ViewBitmapUtils {

    public static Bitmap loadBitmapFromView1(View v){
        // 打开图像缓存
        v.setDrawingCacheEnabled(true);
        // 必须调用measure和layout方法才能成功保存可视组件的截图到png图像文件
        // 测量View大小
        int i = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int n = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(i, n);
        // 发送位置和尺寸到View及其所有的子View
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        // 获得可视组件的截图
        Bitmap bitmap = v.getDrawingCache();
        return bitmap;
    }

    public static Bitmap loadBitmapFromView2(View v) {
        v.measure(0, 0);
        int w = v.getMeasuredWidth();
        int h = v.getMeasuredHeight();
        //如果view的宽高是小于或等于0，则是获取整个屏幕宽和高
        if (w <= 0 || h <= 0) {
            DisplayMetrics metric = new DisplayMetrics();
            // 屏幕宽度（像素）
            w = metric.widthPixels;
            // 屏幕高度（像素）
            h = metric.heightPixels;
        }
        //创建bitmap
        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);
        c.drawColor(Color.WHITE);
        //如果不设置canvas画布为白色，则生成透明
        v.layout(0, 0, w, h);
        v.draw(c);
        return bmp;
    }

    public static Bitmap loadBitmapFromView3(View view){
        if (view == null) {
            return null;
        }
        view.setDrawingCacheEnabled(true);
        //普通View生成图片
        Bitmap drawingCache = view.getDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(drawingCache);
        view.setDrawingCacheEnabled(false);
        view.destroyDrawingCache();
        return bitmap;
    }

    public static Bitmap loadBitmapFromView4(SurfaceView view){
        if (view == null) {
            return null;
        }
        view.setDrawingCacheEnabled(true);
        //普通View生成图片
        Bitmap drawingCache = view.getDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(drawingCache);
        view.setDrawingCacheEnabled(false);
        view.destroyDrawingCache();
        return bitmap;
    }

}

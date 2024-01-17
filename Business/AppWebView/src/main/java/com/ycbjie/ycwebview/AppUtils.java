package com.ycbjie.ycwebview;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

public class AppUtils {


    /**
     * 截屏，截取activity的可见区域的视图
     * @param activity
     * @return
     */
    public static Bitmap activityShot(Activity activity) {
        /*获取windows中最顶层的view*/
        View view = activity.getWindow().getDecorView();
        //允许当前窗口保存缓存信息
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        //获取状态栏高度
        Rect rect = new Rect();
        view.getWindowVisibleDisplayFrame(rect);
        int statusBarHeight = rect.top;
        WindowManager windowManager = activity.getWindowManager();
        //获取屏幕宽和高
        DisplayMetrics outMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        int height = outMetrics.heightPixels;
        //去掉状态栏
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache(), 0, statusBarHeight, width, height - statusBarHeight);
        //销毁缓存信息
        view.destroyDrawingCache();
        view.setDrawingCacheEnabled(false);
        return bitmap;
    }

    /**
     * 截取scrollview的屏幕
     * @param scrollView
     * @return
     */
    public static Bitmap getBitmapByView(ScrollView scrollView) {
        int h = 0;
        Bitmap bitmap = null;
        // 获取listView实际高度
        for (int i = 0; i < scrollView.getChildCount(); i++) {
            h += scrollView.getChildAt(i).getHeight();
            scrollView.getChildAt(i).setBackgroundColor(Color.WHITE);
        }
        // 创建对应大小的bitmap
        bitmap = Bitmap.createBitmap(scrollView.getWidth(), h, Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        scrollView.draw(canvas);
        return bitmap;
    }

    /**
     * 截取LinearLayout
     **/
    public static Bitmap getLinearLayoutBitmap(LinearLayout linearLayout) {
        int h = 0;
        Bitmap bitmap;
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            h += linearLayout.getChildAt(i).getHeight();
        }
        // 创建对应大小的bitmap
        bitmap = Bitmap.createBitmap(linearLayout.getWidth(), h,
                Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        linearLayout.draw(canvas);
        return bitmap;
    }

    /**
     * 截取RelativeLayout
     **/
    public static Bitmap getRelativeLayoutBitmap(RelativeLayout relativeLayout) {
        int h = 0;
        Bitmap bitmap;
        for (int i = 0; i < relativeLayout.getChildCount(); i++) {
            h += relativeLayout.getChildAt(i).getHeight();
        }
        // 创建对应大小的bitmap
        bitmap = Bitmap.createBitmap(relativeLayout.getWidth(), h, Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        relativeLayout.draw(canvas);
        return bitmap;
    }


    /**
     * 计算view的大小
     */
    public static Bitmap measureSize(Activity activity, View view) {
        //将布局转化成view对象
        View viewBitmap = view;
        WindowManager manager = activity.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        int height = outMetrics.heightPixels;
        //然后View和其内部的子View都具有了实际大小，也就是完成了布局，相当与添加到了界面上。
        //接着就可以创建位图并在上面绘制
        return layoutView(viewBitmap, width, height);
    }

    /**
     * 填充布局内容
     */
    private static  Bitmap layoutView(final View viewBitmap, int width, int height) {
        // 整个View的大小 参数是左上角 和右下角的坐标
        viewBitmap.layout(0, 0, width, height);
        int measuredWidth = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
        int measuredHeight = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.UNSPECIFIED);
        viewBitmap.measure(measuredWidth, measuredHeight);
        viewBitmap.layout(0, 0, viewBitmap.getMeasuredWidth(), viewBitmap.getMeasuredHeight());
        viewBitmap.setDrawingCacheEnabled(true);
        viewBitmap.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        viewBitmap.setDrawingCacheBackgroundColor(Color.WHITE);
        // 把一个View转换成图片
        Bitmap cachebmp = viewConversionBitmap(viewBitmap);
        viewBitmap.destroyDrawingCache();
        return cachebmp;
    }

    /**
     * view转bitmap
     */
    private static Bitmap viewConversionBitmap(View v) {
        int w = v.getWidth();
        int h = 0;
        if (v instanceof LinearLayout){
            LinearLayout linearLayout = (LinearLayout) v;
            for (int i = 0; i < linearLayout.getChildCount(); i++) {
                h += linearLayout.getChildAt(i).getHeight();
            }
        } else if (v instanceof RelativeLayout){
            RelativeLayout relativeLayout = (RelativeLayout) v;
            for (int i = 0; i < relativeLayout.getChildCount(); i++) {
                h += relativeLayout.getChildAt(i).getHeight();
            }
        } else {
            h = v.getHeight();
        }
        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);
        //如果不设置canvas画布为白色，则生成透明
        c.drawColor(Color.WHITE);
        v.layout(0, 0, w, h);
        v.draw(c);
        return bmp;
    }


}

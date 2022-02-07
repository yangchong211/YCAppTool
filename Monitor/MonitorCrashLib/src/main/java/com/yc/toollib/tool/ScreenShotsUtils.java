package com.yc.toollib.tool;

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

import androidx.coordinatorlayout.widget.CoordinatorLayout;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/9/10
 *     desc  : webView截图工具类
 *     revise: demo地址：https://github.com/yangchong211/YCWebView
 * </pre>
 */
public final class ScreenShotsUtils {

    /**
     * 截屏，截取activity的可见区域的视图[压缩过的]
     * @param activity              上下文
     * @return                      bitmap图片
     */
    public static Bitmap activityShotCompress(Activity activity){
        if (activity==null){
            return null;
        }
        Bitmap bitmap = activityShot(activity);
        Bitmap bmp = CompressUtils.compressBitmapByBmp(bitmap, activity);
        return bmp;
    }

    /**
     * 截屏，截取activity的可见区域的视图
     * @param activity              上下文
     * @return                      bitmap图片
     */
    public static Bitmap activityShot(Activity activity) {
        if (activity==null){
            return null;
        }
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
        //获取view的缓存位图
        Bitmap drawingCache = view.getDrawingCache();
        //去掉状态栏
        Bitmap bitmap = Bitmap.createBitmap(drawingCache, 0, statusBarHeight, width, height - statusBarHeight);
        //销毁缓存信息
        view.destroyDrawingCache();
        view.setDrawingCacheEnabled(false);
        return bitmap;
    }

    /**
     * 计算view的大小，测量宽高大小。万能工具方法，可以截屏长图
     * @param activity              上下文
     * @param view                  view
     * @return                      返回bitmap
     */
    public static Bitmap measureSize(Activity activity, View view) {
        //将布局转化成view对象
        WindowManager manager = activity.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        int height = outMetrics.heightPixels;
        //然后View和其内部的子View都具有了实际大小，也就是完成了布局，相当与添加到了界面上。
        //接着就可以创建位图并在上面绘制
        return layoutView(view, width, height);
    }

    /**
     * 填充布局内容
     * @param viewBitmap            view
     * @param width                 宽
     * @param height                高
     * @return
     */
    private static  Bitmap layoutView(final View viewBitmap, int width, int height) {
        // 整个View的大小 参数是左上角 和右下角的坐标
        viewBitmap.layout(0, 0, width, height);
        //宽，父容器已经检测出view所需的精确大小，这时候view的最终大小SpecSize所指定的值，相当于match_parent或指定具体数值。
        int measuredWidth = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
        //高，表示父容器不对View有任何限制，一般用于系统内部，表示一种测量状态；
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
     * view转bitmap，如果是截图控件中有scrollView，那么可以取它的子控件LinearLayout或者RelativeLayout
     * @param v                     view
     * @return                      bitmap对象
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
        } else if (v instanceof CoordinatorLayout){
            CoordinatorLayout coordinatorLayout = (CoordinatorLayout) v;
            for (int i = 0; i < coordinatorLayout.getChildCount(); i++) {
                h += coordinatorLayout.getChildAt(i).getHeight();
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

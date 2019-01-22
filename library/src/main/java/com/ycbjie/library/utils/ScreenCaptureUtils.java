package com.ycbjie.library.utils;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.util.LruCache;
import android.view.View;
import android.webkit.WebView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.List;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2018/1/9
 * 描    述：截屏工具类
 * 修订历史：
 * ================================================
 */
public class ScreenCaptureUtils {

    /**
     * 用状态是可转换的方式拍摄当前屏幕
     * @param activity              上下文
     * @return                      返回bitmap对象
     */
    public static Bitmap shotActivity(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bp = Bitmap.createBitmap(view.getDrawingCache(), 0, 0,
                view.getMeasuredWidth(), view.getMeasuredHeight());
        view.setDrawingCacheEnabled(false);
        view.destroyDrawingCache();
        return bp;
    }


    /**
     * 获取当前View的DrawingCache
     * @param v                     当前view对象
     * @return                      返回bitmap对象
     */
    @SuppressLint("ObsoleteSdkInt")
    public static Bitmap getViewBp(View v) {
        if (null == v) {
            return null;
        }
        v.setDrawingCacheEnabled(true);
        v.buildDrawingCache();
        if (Build.VERSION.SDK_INT >= 11) {
            v.measure(View.MeasureSpec.makeMeasureSpec(v.getWidth(),
                    View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(
                    v.getHeight(), View.MeasureSpec.EXACTLY));
            v.layout((int) v.getX(), (int) v.getY(),
                    (int) v.getX() + v.getMeasuredWidth(),
                    (int) v.getY() + v.getMeasuredHeight());
        } else {
            v.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        }
        Bitmap b = Bitmap.createBitmap(v.getDrawingCache(), 0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        v.setDrawingCacheEnabled(false);
        v.destroyDrawingCache();
        return b;
    }

    /**
     * 在滚动视图中，如果当前View并没有在视图中全部绘制出来，我们可以利用View的ScrollTo()和ScrollBy()方法
     * 来移动画布，同时获取当前View的可视部分的DrawingCache，最后进行拼接得到其Bitmap。
     * 参考demo：https://github.com/PGSSoft/scrollscreenshot
     */

    /**
     * ScrollView截屏
     * ScrollView最简单，因为ScrollView只有一个childView，虽然没有全部显示在界面上，但是已经全部渲染绘制，
     * 因此可以直接调用scrollView.draw(canvas)`来完成截图
     * @param scrollView            scrollView对象
     * @return                      返回bitmap对象
     */
    public static Bitmap shotScrollView(ScrollView scrollView) {
        int h = 0;
        Bitmap bitmap = null;
        for (int i = 0; i < scrollView.getChildCount(); i++) {
            h += scrollView.getChildAt(i).getHeight();
            scrollView.getChildAt(i).setBackgroundColor(Color.parseColor("#ffffff"));
        }
        bitmap = Bitmap.createBitmap(scrollView.getWidth(), h, Bitmap.Config.RGB_565);
        final Canvas canvas = new Canvas(bitmap);
        scrollView.draw(canvas);
        return bitmap;
    }


    /**
     * ListView截屏
     * 而ListView就是会回收与重用Item，并且只会绘制在屏幕上显示的ItemView，根据stackoverflow上大神的建议，
     * 采用一个List来存储Item的视图，这种方案依然不够好，当Item足够多的时候，可能会发生oom。
     * @param listView              ListView对象
     * @return                      返回bitmap对象
     */
    public static Bitmap shotListView(ListView listView) {
        ListAdapter adapter = listView.getAdapter();
        int itemsCount = adapter.getCount();
        int itemHeight = 0;
        List<Bitmap> bmp = new ArrayList<>();
        for (int i = 0; i < itemsCount; i++) {
            View childView = adapter.getView(i, null, listView);
            childView.measure(
                    View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            childView.layout(0, 0, childView.getMeasuredWidth(), childView.getMeasuredHeight());
            childView.setDrawingCacheEnabled(true);
            childView.buildDrawingCache();
            bmp.add(childView.getDrawingCache());
            itemHeight += childView.getMeasuredHeight();
        }
        Bitmap bigBitmap =
                Bitmap.createBitmap(listView.getMeasuredWidth(), itemHeight, Bitmap.Config.ARGB_8888);
        Canvas bigCanvas = new Canvas(bigBitmap);
        Paint paint = new Paint();
        int iHeight = 0;
        for (int i = 0; i < bmp.size(); i++) {
            Bitmap bp = bmp.get(i);
            bigCanvas.drawBitmap(bp, 0, iHeight, paint);
            iHeight += bp.getHeight();
            bp.recycle();
            bp = null;
        }
        return bigBitmap;
    }


    /**
     * RecyclerView截屏
     * 在新的Android版本中，已经可以用RecyclerView来代替使用ListView的场景，相比较ListView，
     * RecyclerView对Item View的缓存支持的更好。可以采用和ListView相同的方案，
     * 这里也是在stackoverflow上看到的方案。
     * @param view                  RecyclerView对象
     * @return                      返回bitmap对象
     */
    public static Bitmap shotRecyclerView(RecyclerView view) {
        RecyclerView.Adapter adapter = view.getAdapter();
        Bitmap bigBitmap = null;
        if (adapter != null) {
            int size = adapter.getItemCount();
            int height = 0;
            Paint paint = new Paint();
            int iHeight = 0;
            final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
            // Use 1/8th of the available memory for this memory cache.
            final int cacheSize = maxMemory / 8;
            LruCache<String, Bitmap> bitmaCache = new LruCache<>(cacheSize);
            for (int i = 0; i < size; i++) {
                RecyclerView.ViewHolder holder = adapter.createViewHolder(view, adapter.getItemViewType(i));
                //noinspection unchecked
                adapter.onBindViewHolder(holder, i);
                holder.itemView.measure(
                        View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.EXACTLY),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                holder.itemView.layout(0, 0, holder.itemView.getMeasuredWidth(),
                        holder.itemView.getMeasuredHeight());
                holder.itemView.setDrawingCacheEnabled(true);
                holder.itemView.buildDrawingCache();
                Bitmap drawingCache = holder.itemView.getDrawingCache();
                if (drawingCache != null) {
                    bitmaCache.put(String.valueOf(i), drawingCache);
                }
                height += holder.itemView.getMeasuredHeight();
            }
            bigBitmap = Bitmap.createBitmap(view.getMeasuredWidth(), height, Bitmap.Config.ARGB_8888);
            Canvas bigCanvas = new Canvas(bigBitmap);
            Drawable lBackground = view.getBackground();
            if (lBackground instanceof ColorDrawable) {
                ColorDrawable lColorDrawable = (ColorDrawable) lBackground;
                int lColor = lColorDrawable.getColor();
                bigCanvas.drawColor(lColor);
            }
            for (int i = 0; i < size; i++) {
                Bitmap bitmap = bitmaCache.get(String.valueOf(i));
                bigCanvas.drawBitmap(bitmap, 0f, iHeight, paint);
                iHeight += bitmap.getHeight();
                bitmap.recycle();
            }
        }
        return bigBitmap;
    }


    /**
     * Android5.0以下版本
     * 对WebView进行截屏，虽然使用过期方法，
     * 但在当前Android版本中测试可行
     * @param webView               WebView控件
     * @return                      返回bitmap对象
     */
    public static Bitmap captureWebViewKitKat(WebView webView) {
        Picture picture = webView.capturePicture();
        int width = picture.getWidth();
        int height = picture.getHeight();
        if (width > 0 && height > 0) {
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            picture.draw(canvas);
            return bitmap;
        }
        return null;
    }


    /**
     * X5内核截取长图
     * @param webView               x5WebView的控件
     * @return                      返回bitmap对象
     *
     *        方法
     *        1. 使用X5内核方法snapshotWholePage(Canvas, boolean, boolean)；在X5内核中提供了一个截取整个
     *        WebView界面的方法snapshotWholePage(Canvas, boolean, boolean)，但是这个方法有个缺点，
     *        就是不以屏幕上WebView的宽高截图，只是以WebView的contentWidth和contentHeight为宽高截图，
     *        所以截出来的图片会不怎么清晰，但作为缩略图效果还是不错了。
     *        2. 使用capturePicture()截取清晰长图；如果想要在X5内核下截到清晰的长图，不能使用
     *        snapshotWholePage()，依然可以采用capturePicture()。X5内核下使用capturePicture()进行截图，
     *        可以直接拿到WebView的清晰长图，但这是个Deprecated的方法，使用的时候要做好异常处理。
     */
    public static Bitmap captureX5WebViewUnsharp(com.tencent.smtt.sdk.WebView webView) {
        if (webView == null) {
            return null;
        }
        int width = webView.getContentWidth();
        int height = webView.getContentHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        webView.getX5WebViewExtension().snapshotWholePage(canvas, false, false);
        return bitmap;
    }

    /**
     * X5内核截取长图【暂时用不了】
     * @param webView               x5WebView的控件
     * @return                      返回bitmap对象
     *
     *        方法
     *        1. 使用X5内核方法snapshotWholePage(Canvas, boolean, boolean)；在X5内核中提供了一个截取整个
     *        WebView界面的方法snapshotWholePage(Canvas, boolean, boolean)，但是这个方法有个缺点，
     *        就是不以屏幕上WebView的宽高截图，只是以WebView的contentWidth和contentHeight为宽高截图，
     *        所以截出来的图片会不怎么清晰，但作为缩略图效果还是不错了。
     *        2. 使用capturePicture()截取清晰长图；如果想要在X5内核下截到清晰的长图，不能使用
     *        snapshotWholePage()，依然可以采用capturePicture()。X5内核下使用capturePicture()进行截图，
     *        可以直接拿到WebView的清晰长图，但这是个Deprecated的方法，使用的时候要做好异常处理。
     */
    public static Bitmap captureX5Picture(com.tencent.smtt.sdk.WebView webView) {
        if (webView == null) {
            return null;
        }
        Picture bitmap =  webView.capturePicture();
        return null;
    }

}

package com.yc.zoomimagelib;

import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewParent;
import android.widget.ImageView;

/**
 * <pre>
 *     @author yangchong
 *     GitHub : https://github.com/yangchong211/YCWidgetLib
 *     email  : yangchong211@163.com
 *     time  : 2018/7/10
 *     desc  : 缩放图片工具类
 *     revise:
 * </pre>
 */
public final class ZoomImageUtils {

    public static boolean hasSize(Drawable d) {
        if ((d.getIntrinsicHeight() <= 0 || d.getIntrinsicWidth() <= 0)
                && (d.getMinimumWidth() <= 0 || d.getMinimumHeight() <= 0)
                && (d.getBounds().width() <= 0 || d.getBounds().height() <= 0)) {
            return false;
        }
        return true;
    }

    public static int getDrawableWidth(Drawable d) {
        int width = d.getIntrinsicWidth();
        if (width <= 0) {
            width = d.getMinimumWidth();
        }
        if (width <= 0) {
            width = d.getBounds().width();
        }
        return width;
    }

    public static int getDrawableHeight(Drawable d) {
        int height = d.getIntrinsicHeight();
        if (height <= 0) {
            height = d.getMinimumHeight();
        }
        if (height <= 0) {
            height = d.getBounds().height();
        }
        return height;
    }


    public static ZoomImageInfo getImageViewInfo(ImageView imgView) {
        int[] p = new int[2];
        getLocation(imgView, p);

        Drawable drawable = imgView.getDrawable();

        Matrix matrix = imgView.getImageMatrix();

        int width = ZoomImageUtils.getDrawableWidth(drawable);
        int height = ZoomImageUtils.getDrawableHeight(drawable);

        RectF imgRect = new RectF(0, 0, width, height);
        matrix.mapRect(imgRect);

        RectF rect = new RectF(p[0] + imgRect.left, p[1] + imgRect.top, p[0] + imgRect.right, p[1] + imgRect.bottom);
        RectF widgetRect = new RectF(0, 0, imgView.getWidth(), imgView.getHeight());
        RectF baseRect = new RectF(widgetRect);
        PointF screenCenter = new PointF(widgetRect.width() / 2, widgetRect.height() / 2);

        return new ZoomImageInfo(rect, imgRect, widgetRect, baseRect, screenCenter, 1, 0, imgView.getScaleType());
    }

    public static void getLocation(View target, int[] position) {
        position[0] += target.getLeft();
        position[1] += target.getTop();
        ViewParent viewParent = target.getParent();
        while (viewParent instanceof View) {
            final View view = (View) viewParent;
            if (view.getId() == android.R.id.content) {
                return;
            }
            position[0] -= view.getScrollX();
            position[1] -= view.getScrollY();

            position[0] += view.getLeft();
            position[1] += view.getTop();

            viewParent = view.getParent();
        }
        position[0] = (int) (position[0] + 0.5f);
        position[1] = (int) (position[1] + 0.5f);
    }
}

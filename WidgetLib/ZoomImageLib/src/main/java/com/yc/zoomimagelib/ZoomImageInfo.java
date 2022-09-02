package com.yc.zoomimagelib;

import android.graphics.PointF;
import android.graphics.RectF;
import android.widget.ImageView;

/**
 * <pre>
 *     @author yangchong
 *     GitHub : https://github.com/yangchong211/YCWidgetLib
 *     email  : yangchong211@163.com
 *     time  : 2017/7/10
 *     desc  : 图片info
 *     revise:
 * </pre>
 */
public final class ZoomImageInfo {

    /**
     * 内部图片在整个手机界面的位置
     */
    RectF mRect = new RectF();
    /**
     * 控件在窗口的位置
     */
    RectF mImgRect = new RectF();

    RectF mWidgetRect = new RectF();

    RectF mBaseRect = new RectF();

    PointF mScreenCenter = new PointF();

    float mScale;

    float mDegrees;

    ImageView.ScaleType mScaleType;

    public ZoomImageInfo(RectF rect, RectF img, RectF widget, RectF base, PointF screenCenter,
                         float scale, float degrees, ImageView.ScaleType scaleType) {
        mRect.set(rect);
        mImgRect.set(img);
        mWidgetRect.set(widget);
        mScale = scale;
        mScaleType = scaleType;
        mDegrees = degrees;
        mBaseRect.set(base);
        mScreenCenter.set(screenCenter);
    }

}
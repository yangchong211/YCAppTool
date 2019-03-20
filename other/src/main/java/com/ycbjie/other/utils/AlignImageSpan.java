package com.ycbjie.other.utils;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.text.style.ImageSpan;

import com.blankj.utilcode.util.LogUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;


/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/11/2
 *     desc  : 自定义对齐模式的ImageSpan
 *     revise:
 * </pre>
 */
public class AlignImageSpan extends ImageSpan {

    /**
     * 顶部对齐
     */
    public static final int ALIGN_TOP = 3;
    /**
     * 垂直居中
     */
    public static final int ALIGN_CENTER = 4;
    /**
     * 使用弱引用
     */
    private WeakReference<Drawable> mDrawableRef;

    @IntDef({ALIGN_BOTTOM, ALIGN_BASELINE, ALIGN_TOP, ALIGN_CENTER})
    @Retention(RetentionPolicy.SOURCE)
    @interface Alignment { }

    public AlignImageSpan(Drawable d) {
        this(d, ALIGN_CENTER);
    }

    public AlignImageSpan(Drawable d, @Alignment int verticalAlignment) {
        super(d, verticalAlignment);
    }


    private Drawable getCachedDrawable() {
        WeakReference<Drawable> wr = mDrawableRef;
        Drawable d = null;
        if (wr != null) {
            d = wr.get();
        }
        if (d == null) {
            d = getDrawable();
            mDrawableRef = new WeakReference<>(d);
        }
        return d;
    }

    @Override
    public int getSize(@NonNull Paint paint, CharSequence text, int start,
                       int end, Paint.FontMetricsInt fm) {
        //获取图片资源
        //Drawable drawable = getCachedDrawable();
        Drawable drawable = getDrawable();
        Rect rect = drawable.getBounds();
        if (fm != null) {
            ////获取画笔的文字绘制时的具体测量数据
            Paint.FontMetrics fmPaint = paint.getFontMetrics();
            // 顶部 leading
            float topLeading = fmPaint.top - fmPaint.ascent;
            LogUtils.e("AlignImageSpan---------topLeading-----"+topLeading);
            // 底部 leading
            float bottomLeading = fmPaint.bottom - fmPaint.descent;
            LogUtils.e("AlignImageSpan---------bottomLeading-----"+bottomLeading);
            // drawable 的高度
            int drHeight = rect.height();
            LogUtils.e("AlignImageSpan---------drHeight-----"+drHeight);
            switch (mVerticalAlignment) {
                // drawable 的中间与 行中间对齐
                case ALIGN_CENTER: {
                    // 当前行 的高度
                    float fontHeight = fmPaint.descent - fmPaint.ascent;
                    LogUtils.e("AlignImageSpan---------fontHeight-----"+fontHeight);
                    // 整行的 y方向上的中间 y 坐标
                    float center = fmPaint.descent - fontHeight / 2;
                    LogUtils.e("AlignImageSpan---------center-----"+center);
                    // 算出 ascent 和 descent
                    float ascent = center - drHeight / 2;
                    LogUtils.e("AlignImageSpan---------ascent-----"+ascent);
                    float descent = center + drHeight / 2;
                    LogUtils.e("AlignImageSpan---------descent-----"+descent);
                    fm.ascent = (int) ascent;
                    fm.top = (int) (ascent + topLeading);
                    fm.descent = (int) descent;
                    fm.bottom = (int) (descent + bottomLeading);
                    break;
                }
                // drawable 的底部与 baseline 对齐
                case ALIGN_BASELINE: {
                    // 所以 ascent 的值就是 负的 drawable 的高度
                    float ascent = -drHeight;
                    fm.ascent = -drHeight;
                    fm.top = (int) (ascent + topLeading);
                    break;
                }
                // drawable 的顶部与 行的顶部 对齐
                case ALIGN_TOP: {
                    // 算出 descent
                    float descent = drHeight + fmPaint.ascent;
                    fm.descent = (int) descent;
                    fm.bottom = (int) (descent + bottomLeading);
                    break;
                }
                // drawable 的底部与 行的底部 对齐
                case ALIGN_BOTTOM:
                default: {
                    // 算出 ascent
                    float ascent = fmPaint.descent - drHeight;
                    fm.ascent = (int) ascent;
                    fm.top = (int) (ascent + topLeading);
                }
            }
        }
        return rect.right;
    }

    /**
     * 这里的 x, y, top 以及 bottom 都是基于整个 TextView 的坐标系的坐标
     *
     * @param x      drawable 绘制的起始 x 坐标
     * @param top    当前行最高处，在 TextView 中的 y 坐标
     * @param y      当前行的 BaseLine 在 TextView 中的 y 坐标
     * @param bottom 当前行最低处，在 TextView 中的 y 坐标
     */
    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end,
                     float x, int top, int y, int bottom, @NonNull Paint paint) {
        Drawable drawable = getDrawable();
        Rect rect = drawable.getBounds();
        float transY;
        int height = rect.height();
        LogUtils.e("AlignImageSpan---------height-----"+height);
        LogUtils.e("AlignImageSpan---------bottom-----"+bottom + "---top----"+top);
        switch (mVerticalAlignment) {
            case ALIGN_BASELINE:
                transY = y - rect.height();
                break;
            case ALIGN_CENTER:
                transY = ((bottom - top) - height) / 2 + top;
                LogUtils.e("AlignImageSpan---------transY-----"+transY);
                break;
            case ALIGN_TOP:
                transY = top;
                break;
            case ALIGN_BOTTOM:
            default:
                transY = bottom - height;
                break;
        }
        canvas.save();
        // 这里如果不移动画布，drawable 就会在 TextView 的左上角出现
        canvas.translate(x, transY);
        LogUtils.e("AlignImageSpan---------x-----"+x);
        drawable.draw(canvas);
        canvas.restore();
    }

}

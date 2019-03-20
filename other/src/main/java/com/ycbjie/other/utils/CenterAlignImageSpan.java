package com.ycbjie.other.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.text.style.ImageSpan;

public class CenterAlignImageSpan extends ImageSpan {

    public CenterAlignImageSpan(Context context, int resourceId) {
        super(context, resourceId);
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end,
                     float x, int top, int y, int bottom, @NonNull Paint paint) {
        //draw 方法是重写的ImageSpan父类 DynamicDrawableSpan中的方法，
        //在DynamicDrawableSpan类中，虽有getCachedDrawable()，
        //但是私有的，不能被调用，所以调用ImageSpan中的getrawable()方法，
        //该方法中 会根据传入的drawable ID ，获取该id对应的
        //drawable的流对象，并最终获取drawable对象
        //调用imageSpan中的方法获取drawable对象
        Drawable drawable = getDrawable();
        canvas.save();

        //获取画笔的文字绘制时的具体测量数据
        Paint.FontMetricsInt fm = paint.getFontMetricsInt();

        //系统原有方法，默认是Bottom模式)
        int transY = bottom - drawable.getBounds().bottom;
        //自定义对齐方式--与文字中间线对齐
        int alignFontCenter = 2;
        if (mVerticalAlignment == ALIGN_BASELINE) {
            transY -= fm.descent;
        } else if (mVerticalAlignment == alignFontCenter) {
            //此处加入判断， 如果是自定义的居中对齐
            //与文字的中间线对齐（这种方式不论是否设置行间距都能保障文字的中间线和图片的中间线是对齐的）
            // y+ascent得到文字内容的顶部坐标，y+descent得到文字的底部坐标，
            //（顶部坐标+底部坐标）/2=文字内容中间线坐标
            transY = ((y + fm.descent) + (y + fm.ascent)) / 2 - drawable.getBounds().bottom / 2;
        }
        canvas.translate(x, transY);
        drawable.draw(canvas);
        canvas.restore();
    }

    /**
     * 重写getSize方法，只有重写该方法后，才能保证不论是图片大于文字还是文字大于图片，都能实现中间对齐
     */
    @Override
    public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        Drawable d = getDrawable();
        Rect rect = d.getBounds();
        if (fm != null) {
            Paint.FontMetricsInt fmPaint = paint.getFontMetricsInt();
            int fontHeight = fmPaint.bottom - fmPaint.top;
            int drHeight = rect.bottom - rect.top;

            int top = drHeight / 2 - fontHeight / 4;
            int bottom = drHeight / 2 + fontHeight / 4;

            fm.ascent = -bottom;
            fm.top = -bottom;
            fm.bottom = top;
            fm.descent = top;
        }
        return rect.right;
    }

}


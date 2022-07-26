package com.yc.customwidget;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import android.text.style.ImageSpan;


/**
 * 解决原生 ImageSpan 无法设置垂直局中及左右边距问题
 * 参考：https://www.jianshu.com/p/79a14a3af54b
 */
public class VerticalImageSpan extends ImageSpan {

    private final int marginLeft;
    private final int marginRight;

    public VerticalImageSpan(Drawable drawable) {
        this(drawable, 0, 0);
    }

    public VerticalImageSpan(@NonNull Drawable drawable, int marginLeft, int marginRight) {
        super(drawable);
        this.marginLeft = marginLeft;
        this.marginRight = marginRight;
    }

    /**
     * 返回一个int的rect.right，也就是图片的宽度。
     */
    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end,
                       Paint.FontMetricsInt fontMetricsInt) {
        Drawable drawable = getDrawable();
        Rect rect = drawable.getBounds();
        if (null != fontMetricsInt) {
            Paint.FontMetricsInt fmPaint = paint.getFontMetricsInt();
            //获得文字高度
            int fontHeight = fmPaint.descent - fmPaint.ascent;
            //获取图片高度
            int drHeight = rect.bottom - rect.top;
            int centerY = fmPaint.ascent + fontHeight / 2;
            //重新设置文字的ascent和descent
            fontMetricsInt.ascent = centerY - drHeight / 2;
            fontMetricsInt.top = fontMetricsInt.ascent;
            fontMetricsInt.bottom = centerY + drHeight / 2;
            fontMetricsInt.descent = fontMetricsInt.bottom;
        }
        //图片宽度 = 左边margin + 图片宽度 + 右边margin
        return marginLeft + rect.right + marginRight;
    }

    /**
     * 在这个函数里面是根据不同的对齐参数进行图片的绘制。
     */
    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end,
                     float x, int top, int y, int bottom, Paint paint) {
        Drawable drawable = getDrawable();
        canvas.save();
        Paint.FontMetricsInt fmPaint = paint.getFontMetricsInt();
        //x = 左边间距 + x
        x = marginLeft + x;
        //获取
        int fontHeight = fmPaint.descent - fmPaint.ascent;
        int centerY = y + fmPaint.descent - fontHeight / 2;
        //获得将要显示y偏移 = 居中位置+top(换行情况)  - 图片高度除2
        int transY = centerY - (drawable.getBounds().bottom - drawable.getBounds().top) / 2;
        //移动canvas
        canvas.translate(x, transY);
        //绘制
        drawable.draw(canvas);
        canvas.restore();
    }

}


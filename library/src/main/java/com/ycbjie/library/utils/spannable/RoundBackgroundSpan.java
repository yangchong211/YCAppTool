package com.ycbjie.library.utils.spannable;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.text.style.ReplacementSpan;

public class RoundBackgroundSpan extends ReplacementSpan {

    private int mBgColor;
    private int mBorderColor;
    private int mTextColor;
    private int mRadius;
    private int mSize;
    private int padding;

    public RoundBackgroundSpan(int bgColor, int borderColor,
                               int textColor, int radius, int padding) {
        super();
        this.mBgColor = bgColor;
        this.mBorderColor = borderColor;
        this.mTextColor = textColor;
        this.mRadius = radius;
        this.padding = padding;
    }

    /**
     * @param paint                     paint
     * @param text                      text
     * @param start                     第一个字符的下标
     * @param end                       最后一个字符的下标
     * @param fm                        span 的宽度
     * @return                          size
     */
    @Override
    public int getSize(@NonNull Paint paint, CharSequence text, int start,
                       int end, Paint.FontMetricsInt fm) {
        mSize = (int) (paint.measureText(text, start, end) + 2 * mRadius);
        // 距离其他文字的空白
        return mSize + padding * 2 + 10;
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start,
                     int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
        // 保存文字颜色
        int defaultColor = paint.getColor();
        float defaultStrokeWidth = paint.getStrokeWidth();
        // 绘制圆角
        paint.setColor(mBorderColor);
        // 白色背景 时 style 设置 为 stroke
        if (mBgColor == 0xFFFFFFFF) {
            paint.setStyle(Paint.Style.STROKE);
        } else {
            paint.setStyle(Paint.Style.FILL);
        }

        paint.setStrokeWidth(1);
        paint.setAntiAlias(true);
        RectF rectF = new RectF(x + 2.5f, y + paint.ascent() + paint.ascent() / 2,
                x + mSize + padding * 2, y);
        canvas.drawRoundRect(rectF, mRadius, mRadius, paint);
        // 绘制文字
        paint.setColor(mTextColor);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(defaultStrokeWidth);
        canvas.drawText(text, start, end, x + mRadius + padding,
                y - mRadius - paint.descent(), paint);
        paint.setColor(defaultColor);
    }
}

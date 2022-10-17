package com.yc.shadow.drawable;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211/YCWidgetLib
 *     time  : 2019/3/20
 *     desc  : 自定义带阴影Drawable背景
 *     revise: 大搜车内部阴影方案
 * </pre>
 */
public class ShadowDrawable extends Drawable {

    private final int mColor;
    private final int mShadowColor;
    private final int[] mGradientColorArray;
    private final float[] mGradientPositions;
    private final LinearGradient mLinearGradient;
    private final int mRadius;
    private final int mShadowRadius;
    private final int mOffsetX;
    private final int mOffsetY;
    private RectF mRectF;
    private Paint mShadowPaint;


    protected ShadowDrawable(@ColorInt int color, @Nullable int[] colorArray,
                             @Nullable float[] gradientPositions, @ColorInt int shadowColor,
                             @Nullable LinearGradient linearGradient,
                             int radius, int shadowRadius, int offsetX, int offsetY) {
        this.mColor = color;
        this.mGradientColorArray = colorArray;
        this.mGradientPositions = gradientPositions;
        this.mShadowColor = shadowColor;
        this.mLinearGradient = linearGradient;

        this.mRadius = radius;
        this.mShadowRadius = shadowRadius;

        this.mOffsetX = offsetX;
        this.mOffsetY = offsetY;

    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        if (mRectF == null) {
            Rect bounds = getBounds();
            mRectF = new RectF(bounds.left + mShadowRadius - mOffsetX,
                    bounds.top + mShadowRadius - mOffsetY,
                    bounds.right - mShadowRadius - mOffsetX,
                    bounds.bottom - mShadowRadius - mOffsetY);
        }

        if (mShadowPaint == null) {
            initPaint();
        }
        //这个相当于绘制阴影区域
        canvas.drawRoundRect(mRectF, mRadius, mRadius, mShadowPaint);
    }

    @Override
    public void setAlpha(int alpha) {
        mShadowPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        mShadowPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    private void initPaint() {
        mShadowPaint = new Paint();
        mShadowPaint.setAntiAlias(true);
        //设置阴影效果很关键的api，给画笔设置该属性后
        mShadowPaint.setShadowLayer(mShadowRadius, mOffsetX, mOffsetY, mShadowColor);
        if (mGradientColorArray != null && mGradientColorArray.length > 1) {
            boolean isGradientPositions = mGradientPositions != null
                    && mGradientPositions.length > 0
                    && mGradientPositions.length == mGradientColorArray.length;
            LinearGradient gradient = new LinearGradient(mRectF.left,
                    0, mRectF.right, 0, mGradientColorArray,
                    isGradientPositions ? mGradientPositions : null, Shader.TileMode.CLAMP);
            mShadowPaint.setShader(mLinearGradient == null ? gradient : mLinearGradient);
        } else {
            mShadowPaint.setColor(mColor);
        }
    }

}

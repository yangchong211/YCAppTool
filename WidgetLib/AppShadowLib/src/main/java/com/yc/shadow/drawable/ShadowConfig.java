package com.yc.shadow.drawable;

import android.graphics.LinearGradient;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;

import com.yc.shadow.R;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211/YCWidgetLib
 *     time  : 2019/3/20
 *     desc  : 自定义带阴影Drawable背景
 *     revise: 阴影参数配置
 * </pre>
 */
public final class ShadowConfig {

    private int mColor;
    private int mShadowColor;
    private int[] mGradientColorArray;
    private float[] mGradientPositions;
    private LinearGradient mLinearGradient;
    private int mRadius;
    private int mShadowRadius;
    private int mOffsetX;
    private int mOffsetY;

    public ShadowConfig() {
        //初始化默认值
        mColor = R.color.primary_material_dark;
        mShadowColor = R.color.primary_text_disabled_material_dark;
        mRadius = 5;
        mShadowRadius = 10;
        mOffsetX = 0;
        mOffsetY = 0;
    }

    public ShadowConfig setColor(@ColorInt int color) {
        this.mColor = color;
        return this;
    }

    public ShadowConfig setShadowColor(@ColorInt int shadowColor) {
        this.mShadowColor = shadowColor;
        return this;
    }

    public ShadowConfig setGradientColorArray(@Nullable int[] colorArray) {
        this.mGradientColorArray = colorArray;
        return this;
    }

    public ShadowConfig setGradientPositions(@Nullable float[] positions) {
        this.mGradientPositions = positions;
        return this;
    }

    public ShadowConfig setLinearGradient(@Nullable LinearGradient linearGradient) {
        this.mLinearGradient = linearGradient;
        return this;
    }

    public ShadowConfig setRadius(int radius) {
        this.mRadius = radius;
        return this;
    }

    public ShadowConfig setShadowRadius(int shadowRadius) {
        this.mShadowRadius = shadowRadius;
        return this;
    }

    public ShadowConfig setOffsetX(int offsetX) {
        this.mOffsetX = offsetX;
        return this;
    }

    public ShadowConfig setOffsetY(int offsetY) {
        this.mOffsetY = offsetY;
        return this;
    }


    public ShadowDrawable builder() {
        return new ShadowDrawable(mColor, mGradientColorArray, mGradientPositions,
                mShadowColor, mLinearGradient, mRadius, mShadowRadius,
                mOffsetX, mOffsetY);
    }
}

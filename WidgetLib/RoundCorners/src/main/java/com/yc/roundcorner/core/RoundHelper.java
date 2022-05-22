package com.yc.roundcorner.core;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

public interface RoundHelper {
    void init(Context context, AttributeSet attrs, View view);

    void onSizeChanged(int width, int height);

    void preDraw(Canvas canvas);

    void drawPath(Canvas canvas, int[] drawableState);

    void setCircle(boolean isCircle);

    void setRadius(float radiusDp);

    void setRadius(float radiusTopLeftDp, float radiusTopRightDp, float radiusBottomLeftDp, float radiusBottomRightDp);

    void setRadiusLeft(float radiusDp);

    void setRadiusRight(float radiusDp);

    void setRadiusTop(float radiusDp);

    void setRadiusBottom(float radiusDp);

    void setRadiusTopLeft(float radiusDp);

    void setRadiusTopRight(float radiusDp);

    void setRadiusBottomLeft(float radiusDp);

    void setRadiusBottomRight(float radiusDp);

    void setStrokeWidth(float widthDp);

    void setStrokeColor(int color);

    void setStrokeWidthColor(float widthDp, int color);
}

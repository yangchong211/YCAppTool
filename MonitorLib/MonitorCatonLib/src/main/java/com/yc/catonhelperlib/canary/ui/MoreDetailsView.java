package com.yc.catonhelperlib.canary.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public final class MoreDetailsView extends View {

    private final Paint mIconPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private boolean mFolding = true;

    public MoreDetailsView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mIconPaint.setStrokeWidth(BlockCanaryUi.dpToPixel(2f, getResources()));
        mIconPaint.setColor(BlockCanaryUi.ROOT_COLOR);
    }

    @Override protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        int halfHeight = height / 2;
        int halfWidth = width / 2;

        canvas.drawLine(0, halfHeight, width, halfHeight, mIconPaint);
        if (mFolding) {
            canvas.drawLine(halfWidth, 0, halfWidth, height, mIconPaint);
        }
    }

    public void setFolding(boolean folding) {
        if (folding != this.mFolding) {
            this.mFolding = folding;
            invalidate();
        }
    }
}
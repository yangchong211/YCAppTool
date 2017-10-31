package com.ns.yc.lifehelper.ui.weight;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class DotView extends View {

    private boolean isInit = false;
    private boolean isSelected = false;
    private float mViewHeight;
    private float mViewWidth;
    private float mRadius;
    private Paint mPaintBg = new Paint();
    private int mBgUnselectedColor = Color.parseColor("#3c79a7");
    private int mBgSelectedColor = Color.parseColor("#d03e49");
    private float mArcWidth = 2.0f;

    public DotView(Context context) {
        super(context);
    }

    public DotView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DotView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isInit) {
            isInit = true;
            mViewHeight = getHeight();
            mViewWidth = getWidth();
            if (mViewHeight >= mViewWidth) {
                mRadius = mViewWidth / 2.f;
            } else {
                mRadius = mViewHeight / 2.f;
            }
        }
        if (isSelected){
            drawSelectedDot(canvas);
        } else{
            drawUnSelectedDot(canvas);
        }
    }

    private void drawSelectedDot(Canvas canvas) {
        mPaintBg.setAntiAlias(true);
        mPaintBg.setColor(mBgSelectedColor);
        mPaintBg.setStyle(Style.FILL);
        canvas.drawCircle(mViewWidth / 2.f, mViewHeight / 2.f, mRadius - 8.f, mPaintBg);
        mPaintBg.setStyle(Style.STROKE);
        float offset = 1.f + mArcWidth;
        RectF oval = new RectF(mViewWidth / 2.f - mRadius + offset, mViewHeight / 2.f - mRadius + offset, mViewWidth / 2.f + mRadius - offset, mViewHeight / 2.f + mRadius - offset);
        canvas.drawArc(oval, 0.f, 360.f, false, mPaintBg);
    }

    private void drawUnSelectedDot(Canvas canvas) {
        mPaintBg.setAntiAlias(true);
        mPaintBg.setColor(mBgUnselectedColor);
        mPaintBg.setStyle(Style.FILL);
        canvas.drawCircle(mViewWidth / 2.f, mViewHeight / 2.f, mRadius - 8.f, mPaintBg);
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
        this.invalidate();
    }

}

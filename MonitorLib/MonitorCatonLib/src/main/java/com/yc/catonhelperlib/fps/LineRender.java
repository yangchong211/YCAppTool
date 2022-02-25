package com.yc.catonhelperlib.fps;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Shader.TileMode;
import android.text.TextUtils;

import com.yc.catonhelperlib.R;


public class LineRender {

    private int mPaddingTop = 50;
    private int mPaddingBottom;
    private final int GRAPH_STROKE_WIDTH = 2;
    private final float SMALL_RADIUS = 10.0F;
    private final float CIRCLE_STROKE_WIDTH = 2.0F;
    private Context mContext;
    private float maxValue;
    private float minValue;
    private String label;
    private float nextValue;
    private Paint mLinePaint = new Paint(1);
    private Paint mLabelPaint = new Paint(1);
    private Paint mGradientPaint = new Paint();
    private Paint mPointPaint = new Paint(1);
    private float viewHeight;
    private float viewWidth;
    private float pointX;
    private float pointY;
    private float pointSize = 10.0F;
    private boolean drawRightLine = true;
    private boolean showLabel;
    private float labelAlpha;
    private float startPosition;
    private float baseLine = 20.0F;
    private Path mGradientPath = new Path();

    public LineRender(Context context) {
        this.mContext = context;
        this.mPaddingBottom = dp2px(context, 2.0F);
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = (float)maxValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = (float)minValue;
    }

    protected void measure(float width, float height) {
        this.viewHeight = height - (float)this.mPaddingBottom - (float)this.mPaddingTop;
        this.viewWidth = width;
        this.initPaint();
    }

    private void initPaint() {
        this.mGradientPaint.setShader(new LinearGradient(0.0F, 0.0F, this.viewWidth,
                this.viewHeight, this.mContext.getResources().getColor(R.color.fps_color_3300BFFF),
                this.mContext.getResources().getColor(R.color.fps_color_33434352), TileMode.CLAMP));
        this.mLabelPaint.setTextSize(24);
        this.mLabelPaint.setColor(-1);
        this.mLabelPaint.setTextAlign(Align.CENTER);
        this.mLinePaint.setPathEffect((PathEffect)null);
        this.mLinePaint.setStyle(Style.FILL);
        this.mLinePaint.setColor(this.mContext.getResources().getColor(R.color.fps_color_4c00C9F4));
        this.mLinePaint.setStrokeWidth(2.0F);
        this.mLinePaint.setAntiAlias(true);
        int color = this.mContext.getResources().getColor(R.color.fps_color_ff00C9F4);
        this.mPointPaint.setColor(color);
        this.mPointPaint.setStrokeWidth(2.0F);
    }

    public void draw(Canvas canvas) {
        this.drawGraph(canvas);
        this.drawGradient(canvas);
        this.drawPoint(canvas);
        this.drawLabel(canvas);
    }

    private void drawLabel(Canvas canvas) {
        if (this.showLabel && !TextUtils.isEmpty(this.label)) {
            this.mLabelPaint.setAlpha((int)(this.labelAlpha * 255.0F));
            canvas.drawText(this.label, this.startPosition, this.pointY - this.baseLine, this.mLabelPaint);
        }

    }

    public void setNextValue(float nextValue) {
        if (nextValue > this.maxValue) {
            nextValue = (float)((int)this.maxValue);
        }

        if (nextValue < this.minValue) {
            nextValue = (float)((int)this.minValue);
        }

        this.nextValue = (1.0F - nextValue / (this.maxValue - this.minValue)) * this.viewHeight + (float)this.mPaddingTop;
    }

    public void setCurrentValue(int index, float currentValue) {
        if (currentValue > this.maxValue) {
            currentValue = (float)((int)this.maxValue);
        }

        if (currentValue < this.minValue) {
            currentValue = (float)((int)this.minValue);
        }

        this.startPosition = (float)index * this.viewWidth;
        this.pointX = this.startPosition;
        this.pointY = (1.0F - currentValue / (this.maxValue - this.minValue)) * this.viewHeight + (float)this.mPaddingTop;
    }

    private void drawGraph(Canvas canvas) {
        if (this.drawRightLine) {
            float middleY = this.nextValue;
            canvas.drawLine(this.startPosition, this.pointY, this.viewWidth + this.startPosition, middleY, this.mLinePaint);
        }

    }

    private void drawGradient(Canvas canvas) {
        if (this.drawRightLine) {
            this.mGradientPath.rewind();
            this.mGradientPath.moveTo(this.pointX, this.pointY);
            this.mGradientPath.lineTo(this.pointX, this.viewHeight + (float)this.mPaddingTop);
            this.mGradientPath.lineTo(this.pointX + this.viewWidth, this.viewHeight + (float)this.mPaddingTop);
            this.mGradientPath.lineTo(this.pointX + this.viewWidth, this.nextValue);
            canvas.drawPath(this.mGradientPath, this.mGradientPaint);
        }

    }

    private void drawPoint(Canvas canvas) {
        canvas.drawCircle(this.pointX, this.pointY, this.pointSize, this.mPointPaint);
    }

    public void setDrawRightLine(boolean drawRightLine) {
        this.drawRightLine = drawRightLine;
    }

    public void setPointSize(float pointSize) {
        if (pointSize != 0.0F) {
            this.pointSize = pointSize;
        }

    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setShowLabel(boolean show) {
        this.showLabel = show;
    }

    public void setLabelAlpha(float alpha) {
        this.labelAlpha = alpha;
    }

    public static int dp2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dpValue * scale + 0.5F);
    }
}


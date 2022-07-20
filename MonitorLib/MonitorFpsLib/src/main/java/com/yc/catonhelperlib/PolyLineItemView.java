package com.yc.catonhelperlib;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Shader.TileMode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class PolyLineItemView extends View {
    private static float pointBottomY;
    private static float pointTopY = 50.0F;
    private static Paint mGradientPaint;
    private final int GRAPH_STROKE_WIDTH = 2;
    private final float SMALL_RADIUS = 10.0F;
    private final float BIG_RADIUS = 20.0F;
    private final float CIRCLE_STROKE_WIDTH = 2.0F;
    private float maxValue;
    private float minValue;
    private float currentValue;
    private String label;
    private float lastValue;
    private float nextValue;
    private Paint mPaint = new Paint();
    private float viewHeight;
    private float viewWidth;
    private float pointX;
    private float pointY;
    private float pointSize = 10.0F;
    private boolean drawLeftLine = true;
    private boolean drawRightLine = true;
    private boolean onTouch = false;
    private boolean touchable = true;
    private boolean showLabel;
    private boolean drawDiver;

    public PolyLineItemView(Context context) {
        super(context);
    }

    public PolyLineItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PolyLineItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setCurrentValue(float currentValue) {
        if (currentValue > this.maxValue) {
            currentValue = (float) ((int) this.maxValue);
        }

        if (currentValue < this.minValue) {
            currentValue = (float) ((int) this.minValue);
        }

        this.currentValue = currentValue;
        this.invalidate();
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = (float) maxValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = (float) minValue;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.viewHeight = (float) this.getMeasuredHeight();
        this.viewWidth = (float) this.getMeasuredWidth();
        this.pointX = this.viewWidth / 2.0F;
        if (pointBottomY == 0.0F) {
            pointBottomY = this.viewHeight - this.pointSize;
        }

        this.pointY = (1.0F - this.currentValue / (this.maxValue - this.minValue)) * (pointBottomY - pointTopY) + pointTopY;
        if (mGradientPaint == null) {
            mGradientPaint = new Paint();
            mGradientPaint.setShader(new LinearGradient(0.0F, 0.0F, this.viewWidth, this.viewHeight, this.getResources().getColor(R.color.fps_color_3300BFFF), this.getResources().getColor(R.color.fps_color_33434352), TileMode.CLAMP));
        }

    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        this.drawGraph(canvas);
        this.drawPoint(canvas);
        this.drawValue(canvas);
        this.drawLine(canvas);
    }

    private void drawValue(Canvas canvas) {
        if (this.onTouch || this.showLabel) {
            this.mPaint.setTextSize(20.0F);
            this.mPaint.setColor(-1);
            this.mPaint.setStrokeWidth(0.0F);
            this.mPaint.setStyle(Style.FILL);
            this.mPaint.setTextAlign(Align.CENTER);
            FontMetrics fontMetrics = this.mPaint.getFontMetrics();
            float baseLine = this.pointY - fontMetrics.bottom * 4.0F;
            canvas.drawText(this.label, this.viewWidth / 2.0F, baseLine, this.mPaint);
        }

    }

    public void setlastValue(float lastValue) {
        if (lastValue > this.maxValue) {
            lastValue = (float) ((int) this.maxValue);
        }

        if (lastValue < this.minValue) {
            lastValue = (float) ((int) this.minValue);
        }

        this.lastValue = lastValue;
    }

    public void setNextValue(float nextValue) {
        if (nextValue > this.maxValue) {
            nextValue = (float) ((int) this.maxValue);
        }

        if (nextValue < this.minValue) {
            nextValue = (float) ((int) this.minValue);
        }

        this.nextValue = nextValue;
    }

    private void drawGraph(Canvas canvas) {
        this.mPaint.setPathEffect((PathEffect) null);
        this.mPaint.setStyle(Style.FILL);
        this.mPaint.setColor(this.getResources().getColor(R.color.fps_color_4c00C9F4));
        this.mPaint.setStrokeWidth(2.0F);
        this.mPaint.setAntiAlias(true);
        float middleValue;
        float middleY;
        if (this.drawLeftLine) {
            middleValue = this.currentValue - (this.currentValue - this.lastValue) / 2.0F;
            middleY = (pointBottomY - pointTopY) * 1.0F / (this.maxValue - this.minValue) * (this.maxValue - middleValue + this.minValue) + pointTopY;
            canvas.drawLine(0.0F, middleY, this.pointX, this.pointY, this.mPaint);
            this.drawGradient(canvas, middleY, false);
        }

        if (this.drawRightLine) {
            middleValue = this.currentValue - (this.currentValue - this.nextValue) / 2.0F;
            middleY = (pointBottomY - pointTopY) * 1.0F / (this.maxValue - this.minValue) * (this.maxValue - middleValue + this.minValue) + pointTopY;
            canvas.drawLine(this.pointX, this.pointY, this.viewWidth, middleY, this.mPaint);
            this.drawGradient(canvas, middleY, true);
        }

    }

    private void drawGradient(Canvas canvas, float middleY, boolean isRight) {
        Path path = new Path();
        if (!isRight) {
            path.moveTo(0.0F, middleY);
            path.lineTo(this.pointX, this.pointY);
            path.lineTo(this.pointX, pointBottomY);
            path.lineTo(0.0F, pointBottomY);
        } else {
            path.moveTo(this.pointX, this.pointY);
            path.lineTo(this.pointX, pointBottomY);
            path.lineTo(this.pointX + this.viewWidth / 2.0F, pointBottomY);
            path.lineTo(this.pointX + this.viewWidth / 2.0F, middleY);
        }

        canvas.drawPath(path, mGradientPaint);
    }

    private void drawPoint(Canvas canvas) {
        int color;
        if (this.onTouch) {
            color = this.getResources().getColor(R.color.fps_color_4c00C9F4);
            this.mPaint.setColor(color);
            this.mPaint.setPathEffect((PathEffect) null);
            this.mPaint.setStrokeWidth(2.0F);
            this.mPaint.setStyle(Style.FILL);
            canvas.drawCircle(this.pointX, this.pointY, 20.0F, this.mPaint);
        }

        color = this.getResources().getColor(R.color.fps_color_ff00C9F4);
        this.mPaint.setColor(color);
        this.mPaint.setStrokeWidth(2.0F);
        canvas.drawCircle(this.pointX, this.pointY, this.pointSize, this.mPaint);
    }

    private void drawLine(Canvas canvas) {
        if (this.drawDiver) {
            this.mPaint.setColor(this.getResources().getColor(R.color.fps_color_999999));
            this.mPaint.setPathEffect((PathEffect) null);
            this.mPaint.setStrokeWidth(2.0F);
            this.mPaint.setStyle(Style.FILL);
            if (this.drawLeftLine) {
                canvas.drawLine(0.0F, pointBottomY, this.viewWidth / 2.0F, pointBottomY, this.mPaint);
            }

            if (this.drawRightLine) {
                canvas.drawLine(this.viewWidth / 2.0F, pointBottomY, this.viewWidth, pointBottomY, this.mPaint);
            }

        }
    }

    public void setDrawLeftLine(boolean drawLeftLine) {
        this.drawLeftLine = drawLeftLine;
    }

    public void setDrawRightLine(boolean drawRightLine) {
        this.drawRightLine = drawRightLine;
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (!this.touchable) {
            return super.onTouchEvent(event);
        } else {
            switch (event.getAction()) {
                case 0:
                    this.onTouch = true;
                    this.setBackgroundResource(R.drawable.line_chart_selected_bg);
                    break;
                case 1:
                case 3:
                    this.onTouch = false;
                    this.setBackgroundResource(0);
                case 2:
            }

            return super.onTouchEvent(event);
        }
    }

    public void setDrawDiver(boolean drawDiver) {
        this.drawDiver = drawDiver;
    }

    public void setPointSize(float pointSize) {
        if (pointSize != 0.0F) {
            this.pointSize = pointSize;
        }

    }

    public void setTouchable(boolean touchable) {
        this.touchable = touchable;
    }

    public void showLabel(boolean showLatestLabel) {
        this.showLabel = showLatestLabel;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}


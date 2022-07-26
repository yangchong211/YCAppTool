package com.yc.appchartview;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class PieChartView extends View {

    private Paint mPaint;

    private Path mPath, drawLinePath = new Path();

    private PathMeasure mPathMeasure = new PathMeasure();

    private Canvas mCanvas;

    private int width, height;

    private RectF pieRectF, tempRectF;

    private int radius;

    private List<PieChartView.ItemType> itemTypeList, leftTypeList, rightTypeList;

    private List<Point> itemPoints;

    private int cell = 0;

    private float innerRadius = 0.0f;

    private float offRadius = 0, offLine;

    private int textAlpha;

    private Point firstPoint;

    private int backGroundColor = 0xffffffff;

    private int itemTextSize = 30, textPadding = 8;

    private int defaultStartAngle = -90;

    private float pieCell;

    private ValueAnimator animator;

    private long animDuration = 1000;

    private Point startPoint = new Point();

    private Point centerPoint = new Point();

    private Point endPoint = new Point();

    private Point tempPoint = new Point();

    public PieChartView(Context context) {
        super(context);
        init();
    }

    public PieChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mPath = new Path();

        pieRectF = new RectF();
        tempRectF = new RectF();

        itemTypeList = new ArrayList<>();
        leftTypeList = new ArrayList<>();
        rightTypeList = new ArrayList<>();
        itemPoints = new ArrayList<>();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startAnim();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (animator != null) {
            animator.cancel();
        }
    }

    private void startAnim() {
        animator = ValueAnimator.ofFloat(0, 360f * 2);
        animator.setDuration(animDuration);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                if (value < 360f) {
                    offRadius = value;
                    offLine = 0;
                    textAlpha = 0;
                } else if (value >= 360f) {
                    offRadius = 360f;
                    offLine = (value - 360f) / 360f;
                    if (offLine > 0.5f) {
                        textAlpha = (int) (255 * ((offLine - 0.5f) / 0.5f));
                        if (textAlpha > 255) {
                            textAlpha = 255;
                        }
                    } else {
                        textAlpha = 0;
                    }
                } else if (value == 360f * 2) {
                    offRadius = 360f;
                    offLine = 1.0f;
                    textAlpha = 255;
                }
                postInvalidate();
            }
        });
        animator.start();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.width = w;
        this.height = h;
        radius = Math.min(width, height) / 4;
        pieRectF.set(width / 2 - radius, height / 2 - radius, width / 2 + radius, height / 2 + radius);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        try {
            mCanvas = canvas;
            drawPie();
            if (offRadius == 360f) {
                drawTitle();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void drawTitle() {
        resetPaint();
        float startRadius = defaultStartAngle;
        int count = rightTypeList.size();
        int h;
        if (count > 1) {
            h = (radius * 2) / (count - 1);
        } else {
            h = radius;
        }
        for (int i = 0; i < count; i++) {
            mPath.reset();
            PieChartView.ItemType itemType = rightTypeList.get(i);
            double angle = 2 * Math.PI * ((startRadius + itemType.radius / 2) / 360d);
            int x = (int) (width / 2 + radius * Math.cos(angle));
            int y = (int) (height / 2 + radius * Math.sin(angle));
            startPoint.set(x, y);
            centerPoint.set((int) (width / 2 + radius * 1.2f), height / 2 - radius + h * (i));
            endPoint.set((int) (width * 0.98f), centerPoint.y);
            mPath.moveTo(startPoint.x, startPoint.y);
            mPath.lineTo(centerPoint.x, centerPoint.y);
            mPath.lineTo(endPoint.x, endPoint.y);
            resetPaint();
            mPaint.setStrokeWidth(2);
            mPaint.setColor(itemType.color);
            mPaint.setStyle(Paint.Style.STROKE);
            mPathMeasure.setPath(mPath, false);
            drawLinePath.reset();
            mPathMeasure.getSegment(0, mPathMeasure.getLength() * offLine, drawLinePath, true);
            mCanvas.drawPath(drawLinePath, mPaint);
            startRadius += itemType.radius;

            if (textAlpha > 0) {
                mPaint.setTextSize(itemTextSize);
                mPaint.setStyle(Paint.Style.FILL);
                mPaint.setTextAlign(Paint.Align.CENTER);
                mPaint.setAlpha(textAlpha);
                mCanvas.drawText(itemType.type, centerPoint.x + (endPoint.x - centerPoint.x) / 2,
                        centerPoint.y - textPadding, mPaint);
                mPaint.setTextSize(itemTextSize * 4 / 5);
                mCanvas.drawText(itemType.getPercent(), centerPoint.x + (endPoint.x - centerPoint.x) / 2,
                        centerPoint.y + (itemTextSize + textPadding) * 4 / 5, mPaint);
            }
        }

        count = leftTypeList.size();
        if (count > 1) {
            h = (radius * 2) / (count - 1);
        } else {
            h = radius;
        }

        for (int i = 0; i < count; i++) {
            mPath.reset();
            PieChartView.ItemType itemType = leftTypeList.get(i);
            double angle = 2 * Math.PI * ((startRadius + itemType.radius / 2) / 360d);
            int x = (int) (width / 2 + radius * Math.cos(angle));
            int y = (int) (height / 2 + radius * Math.sin(angle));
            startPoint.set(x, y);
            centerPoint.set((int) (width / 2 - radius * 1.2f), height / 2 - radius + h * (count - 1 - i));
            endPoint.set((int) (width * 0.02f), centerPoint.y);
            mPath.moveTo(startPoint.x, startPoint.y);
            mPath.lineTo(centerPoint.x, centerPoint.y);
            mPath.lineTo(endPoint.x, endPoint.y);
            resetPaint();
            mPaint.setStrokeWidth(2);
            mPaint.setColor(itemType.color);
            mPaint.setAntiAlias(true);
            mPaint.setDither(true);
            mPaint.setStyle(Paint.Style.STROKE);
            mPathMeasure.setPath(mPath, false);
            drawLinePath.reset();
            mPathMeasure.getSegment(0, mPathMeasure.getLength() * offLine, drawLinePath, true);
            mCanvas.drawPath(drawLinePath, mPaint);
            startRadius += itemType.radius;

            if (textAlpha > 0) {
                mPaint.setTextSize(itemTextSize);
                mPaint.setStyle(Paint.Style.FILL);
                mPaint.setTextAlign(Paint.Align.CENTER);
                mPaint.setAlpha(textAlpha);
                mCanvas.drawText(itemType.type, centerPoint.x + (endPoint.x - centerPoint.x) / 2,
                        centerPoint.y - textPadding, mPaint);
                mPaint.setTextSize(itemTextSize * 4 / 5);
                mCanvas.drawText(itemType.getPercent(), centerPoint.x + (endPoint.x - centerPoint.x) / 2,
                        centerPoint.y + (itemTextSize + textPadding) * 4 / 5, mPaint);
            }
        }

        if (textAlpha == 1f) {
            itemTypeList.clear();
            leftTypeList.clear();
            rightTypeList.clear();
            itemPoints.clear();
        }
    }

    private void drawPie() {
        if (mCanvas == null) {
            return;
        }
        mCanvas.drawColor(backGroundColor);
        mPaint.setStyle(Paint.Style.FILL);
        int sum = 0;
        for (PieChartView.ItemType itemType : itemTypeList) {
            sum += itemType.widget;
        }
        float a = 360f / sum;
        float startRadius = defaultStartAngle;
        float sumRadius = 0;
        leftTypeList.clear();
        rightTypeList.clear();
        itemPoints.clear();
        for (PieChartView.ItemType itemType : itemTypeList) {
            itemType.radius = itemType.widget * a;
            double al = 2 * Math.PI * ((startRadius + 90) / 360d);
            tempPoint.set((int) (width / 2 + radius * Math.sin(al)),
                    (int) (height / 2 - radius * Math.cos(al)));
            if (cell > 0) {
                if (startRadius == defaultStartAngle) {
                    firstPoint = tempPoint;
                }
            }

            double angle = 2 * Math.PI * ((startRadius + itemType.radius / 2) / 360d);
            double sin = -Math.sin(angle);
            double cos = -Math.cos(angle);
            if (cos > 0) {
                leftTypeList.add(itemType);
            } else {
                rightTypeList.add(itemType);
            }
            sumRadius += Math.abs(itemType.radius);
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(itemType.color);
            if (pieCell > 0) {
                if (sumRadius <= offRadius) {
                    tempRectF.set(pieRectF.left - (float) (pieCell * cos), pieRectF.top - (float) (pieCell * sin),
                            pieRectF.right - (float) (pieCell * cos), pieRectF.bottom - (float) (pieCell * sin));
                    mCanvas.drawArc(tempRectF, startRadius, itemType.radius, true, mPaint);
                } else {
                    mCanvas.drawArc(tempRectF, startRadius, itemType.radius - (Math.abs(offRadius - sumRadius)), true, mPaint);
                    break;
                }
            } else {
                if (sumRadius <= offRadius) {
                    mCanvas.drawArc(pieRectF, startRadius, itemType.radius, true, mPaint);
                } else {
                    mCanvas.drawArc(pieRectF, startRadius, itemType.radius - (Math.abs(offRadius - sumRadius)), true, mPaint);
                    break;
                }

            }
            startRadius += itemType.radius;
            if (cell > 0 && pieCell == 0) {
                mPaint.setColor(backGroundColor);
                mPaint.setStrokeWidth(cell);
                mCanvas.drawLine(getWidth() / 2, getHeight() / 2, tempPoint.x, tempPoint.y, mPaint);
            }
        }
        if (cell > 0 && firstPoint != null && pieCell == 0) {
            mPaint.setColor(backGroundColor);
            mPaint.setStrokeWidth(cell);
            mCanvas.drawLine(getWidth() / 2, getHeight() / 2, firstPoint.x, firstPoint.y, mPaint);
        }
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(backGroundColor);
        if (innerRadius > 0 && pieCell == 0) {
            mCanvas.drawCircle(width / 2, height / 2, radius * innerRadius, mPaint);
        }
    }

    public void resetPaint() {
        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setAlpha(255);
    }

    /**
     * 添加一条分类数据
     *
     * @param itemType
     */
    public void addItemType(PieChartView.ItemType itemType) {
        if (itemTypeList != null) {
            itemTypeList.add(itemType);
        }
    }

    /**
     * 设置每条图之间的间歇大小
     *
     * @param cell
     */
    public void setCell(int cell) {
        this.cell = cell;
    }

    /**
     * 设置内部圆的半径比例，eg：0.5f
     *
     * @param innerRadius
     */
    public void setInnerRadius(float innerRadius) {
        if (innerRadius > 1.0f) {
            innerRadius = 1.0f;
        } else if (innerRadius < 0) {
            innerRadius = 0;
        }
        this.innerRadius = innerRadius;
    }

    /**
     * 设置背景颜色
     *
     * @param backGroundColor
     */
    public void setBackGroundColor(int backGroundColor) {
        this.backGroundColor = backGroundColor;
    }

    /**
     * 设置每条字体大小
     *
     * @param itemTextSize
     */
    public void setItemTextSize(int itemTextSize) {
        this.itemTextSize = itemTextSize;
    }

    /**
     * 设置字体距离横线的padding值
     *
     * @param textPadding
     */
    public void setTextPadding(int textPadding) {
        this.textPadding = textPadding;
    }

    /**
     * 设置动画时间
     *
     * @param animDuration
     */
    public void setAnimDuration(long animDuration) {
        this.animDuration = animDuration;
    }

    /**
     * 代替方法{@link #setCell(int)}
     *
     * @param pieCell
     */
    @Deprecated
    public void setPieCell(int pieCell) {
        this.cell = pieCell;
    }

    public static class ItemType {
        private static final DecimalFormat df = new DecimalFormat("0.0%");
        String type;
        int widget;
        int color;
        float radius;

        public ItemType(String type, int widget, int color) {
            this.type = type;
            this.widget = widget;
            this.color = color;
        }

        public String getPercent() {
            return df.format(radius / 360f);
        }
    }
}

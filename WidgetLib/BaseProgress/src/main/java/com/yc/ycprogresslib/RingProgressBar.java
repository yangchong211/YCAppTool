package com.yc.ycprogresslib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Typeface;
import android.graphics.Xfermode;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;


/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2016/2/10
 *     desc  : 自定义进度条，新芽，沙丘大学下载进度条
 *     revise: 参考案例：夏安明博客http://blog.csdn.net/xiaanming/article/details/10298163
 *             案例地址：https://github.com/yangchong211
 * </pre>
 */
public class RingProgressBar extends View {

    private int percent;                        //当前百分比
    private int progressMax;                    //最大进度
    private int dotColor;                       //颜色
    private int dotBgColor;                     //背景颜色
    private int showMode;                       //进度条的样式

    private float percentTextSize;              //百分比字体大小
    private int percentTextColor;               //百分比字体颜色
    private int percentThinPadding;

    private String unitText;
    private float unitTextSize;
    private int unitTextColor;
    private int unitTextAlignMode;
    public static final int UNIT_TEXT_ALIGN_MODE_DEFAULT = 0;
    public static final int UNIT_TEXT_ALIGN_MODE_CN = 1;
    public static final int UNIT_TEXT_ALIGN_MODE_EN      = 2;


    private Paint mPaint;
    private float mSin_1;                       // sin(1°)
    private Typeface mPercentTypeface;

    private float mCenterX;
    private float mCenterY;

    private Canvas mCanvas;
    private Bitmap mBitmap;
    private Xfermode mClearCanvasXfermode;
    private Xfermode mPercentThinXfermode;

    public RingProgressBar(Context context) {
        this(context, null);
    }

    public RingProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RingProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs);


        // 其他准备工作
        mSin_1 = (float) Math.sin(Math.toRadians(1)); // 求sin(1°)。角度需转换成弧度
        mPaint = new Paint();
        mPaint.setAntiAlias(true);      // 消除锯齿

        //百分比字体类型，设置成默认值
        mPercentTypeface = Typeface.DEFAULT;

        mClearCanvasXfermode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
        if (percentThinPadding != 0) {
            mPercentThinXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);
        }

    }

    private void initialize(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RingProgressBar);
        // 获取自定义属性值或默认值
        progressMax = ta.getInteger(R.styleable.RingProgressBar_progressMax, 100);
        dotColor = ta.getColor(R.styleable.RingProgressBar_dotColor, Color.WHITE);
        dotBgColor = ta.getColor(R.styleable.RingProgressBar_dotBgColor, Color.GRAY);
        showMode = ta.getInt(R.styleable.RingProgressBar_showMode, ProgressBarUtils.RingShowMode.SHOW_MODE_PERCENT);

        if (showMode != ProgressBarUtils.RingShowMode.SHOW_MODE_NULL) {
            percentTextSize = ta.getDimension(R.styleable.RingProgressBar_percentTextSize, ProgressBarUtils.dp2px(context,30));
            percentTextColor = ta.getInt(R.styleable.RingProgressBar_percentTextColor, Color.WHITE);
            percentThinPadding = ta.getInt(R.styleable.RingProgressBar_percentThinPadding, 0);

            unitText = ta.getString(R.styleable.RingProgressBar_unitText);
            unitTextSize = ta.getDimension(R.styleable.RingProgressBar_unitTextSize, percentTextSize);
            unitTextColor = ta.getInt(R.styleable.RingProgressBar_unitTextColor, Color.BLACK);
            unitTextAlignMode = ta.getInt(R.styleable.RingProgressBar_unitTextAlignMode, UNIT_TEXT_ALIGN_MODE_DEFAULT);

            if (unitText == null) {
                unitText = "%";
            }
        }
        ta.recycle();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 绘制外围圆点进度
        drawCircleDot(mCanvas);
        if (showMode != ProgressBarUtils.RingShowMode.SHOW_MODE_NULL) {
            // 绘制百分比+单位
            drawPercentUnit(mCanvas);
        }
        canvas.drawBitmap(mBitmap, 0, 0, null);
    }


    /**
     * 绘制圆点进度
     * @param canvas 画布
     */
    private void drawCircleDot(Canvas canvas) {
        // 先清除上次绘制的
        mPaint.setXfermode(mClearCanvasXfermode);
        mCanvas.drawPaint(mPaint);
        mPaint.setXfermode(null);

        // 计算圆点半径
        float outerRadius = (getWidth() < getHeight() ? getWidth() : getHeight()) / 2f;
        mCenterX = getWidth() / 2f;
        mCenterY = getHeight() / 2f;

        // outerRadius = innerRadius + dotRadius
        // sin((360°/200)/2) = sin(0.9°) = dotRadius / innerRadius;
        // 为了让圆点饱满一些，把角度0.9°增加0.1°到1°
        float dotRadius = mSin_1 * outerRadius / (1 + mSin_1);


        // 画进度
        mPaint.setColor(dotColor);
        mPaint.setStyle(Paint.Style.FILL);
        int count = 0;
        // 1.1 当前进度
        while (count++ < percent) {
            mCanvas.drawCircle(mCenterX, mCenterY - outerRadius + dotRadius, dotRadius, mPaint);
            mCanvas.rotate(3.6f, mCenterX, mCenterY);
        }
        // 1.2 未完成进度
        mPaint.setColor(dotBgColor);
        count--;
        while (count++ < 100) {
            mCanvas.drawCircle(mCenterX, mCenterY - outerRadius + dotRadius, dotRadius, mPaint);
            mCanvas.rotate(3.6f, mCenterX, mCenterY);
        }
    }

    /**
     * 绘制百分比和单位
     * @param canvas 画布
     */
    private void drawPercentUnit(Canvas canvas) {
        // 测量百分比和单位的宽度
        mPaint.setTypeface(mPercentTypeface);
        mPaint.setTextSize(percentTextSize);
        float percentTextWidth = mPaint.measureText(percent + "");

        mPaint.setTypeface(Typeface.DEFAULT);
        mPaint.setTextSize(unitTextSize);
        float unitTextWidth = mPaint.measureText(unitText);
        Paint.FontMetrics fm_unit = mPaint.getFontMetrics();

        float textWidth = percentTextWidth + unitTextWidth;

        float baseline = 0;
        if (showMode == ProgressBarUtils.RingShowMode.SHOW_MODE_PERCENT) {
            float textHeight = percentTextSize > unitTextSize ? percentTextSize : unitTextSize;
            // 计算Text垂直居中时的baseline
            mPaint.setTextSize(textHeight);
            Paint.FontMetrics fm = mPaint.getFontMetrics();
            // 字体在垂直居中时，字体中间就是centerY，加上字体实际高度的一半就是descent线，减去descent就是baseline线的位置（fm中以baseline为基准）
            baseline = mCenterY + (fm.descent - fm.ascent)/2 - fm.descent;
        }

        // 2.1 画百分比
        mPaint.setTypeface(mPercentTypeface);
        mPaint.setTextSize(percentTextSize);
        mPaint.setColor(percentTextColor);
        canvas.drawText(percent + "", mCenterX - textWidth / 2, baseline, mPaint);

        // 2.1.1 对百分比瘦身
        if (percentThinPadding != 0) {
            // 使用橡皮擦擦除
            mPaint.setXfermode(mPercentThinXfermode);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(percentThinPadding);
            canvas.drawText(percent + "", mCenterX - textWidth / 2, baseline, mPaint);
            mPaint.setXfermode(null);
            mPaint.setStyle(Paint.Style.FILL);
        }

        // 2.2 画单位
        mPaint.setTypeface(Typeface.DEFAULT);
        mPaint.setTextSize(unitTextSize);
        mPaint.setColor(unitTextColor);
        // 单位对齐方式
        switch (unitTextAlignMode) {
            case UNIT_TEXT_ALIGN_MODE_CN:
                baseline -= fm_unit.descent / 4;
                break;
            case UNIT_TEXT_ALIGN_MODE_EN:
                baseline -= fm_unit.descent * 2/3;
                break;
            default:
                break;
        }
        canvas.drawText(unitText, mCenterX - textWidth / 2 + percentTextWidth, baseline, mPaint);
    }


    public synchronized void setProgressMax(int progressMax) {
        if (progressMax < 0 || progressMax >100) {
            throw new IllegalArgumentException("progressMax mustn't smaller than 0 or 100");
        }
        this.progressMax = progressMax;
    }

    /**
     * 设置进度
     * 同步，允许多线程访问
     * @param progress 进度
     */
    public synchronized void setProgress(int progress) {
        if (progress < 0 || progress > progressMax) {
            throw new IllegalArgumentException(String.format(getResources().getString(R.string.progress_out_of_range), progressMax));
        }
        percent = progress * 100 / progressMax;
        postInvalidate();       // 可以直接在子线程中调用，而invalidate()必须在主线程（UI线程）中调用
    }

    /**
     * 设置更新的进度条颜色
     * @param dotColor                      int
     */
    public void setDotColor(@ColorInt int dotColor) {
        this.dotColor = dotColor;
        invalidate();
    }

    /**
     * 设置未更新部分的进度条颜色
     * @param dotBgColor                    int
     */
    public void setDotBgColor(@ColorInt int dotBgColor) {
        this.dotBgColor = dotBgColor;
        invalidate();
    }

    /**
     * 设置展示的类型
     * @param showMode                      @ProgressBarUtils.RingShowModeType
     */
    public void setShowMode(@ProgressBarUtils.RingShowModeType int showMode) {
        this.showMode = showMode;
    }

    /**
     * 设置百分比文字大小
     * @param percentTextSize               percentTextSize
     */
    public void setPercentTextSize(float percentTextSize) {
        this.percentTextSize = percentTextSize;
        invalidate();
    }

    /**
     * 设置百分比文字颜色
     * @param percentTextColor              percentTextColor
     */
    public void setPercentTextColor(@ColorInt int percentTextColor) {
        this.percentTextColor = percentTextColor;
        invalidate();
    }


    /**
     * 设置百分比间距
     * @param percentThinPadding            padding
     */
    public void setPercentThinPadding(int percentThinPadding) {
        this.percentThinPadding = percentThinPadding;
        invalidate();
    }

    /**
     * 设置单位的文字内容
     * @param unitText                      unitText
     */
    public void setUnitText(@NonNull String unitText) {
        this.unitText = unitText;
        invalidate();
    }

    /**
     * 设置单位的文字大小
     * @param unitTextSize                  size
     */
    public void setUnitTextSize(float unitTextSize) {
        this.unitTextSize = unitTextSize;
        invalidate();
    }

    /**
     * 设置单位的文字颜色
     * @param unitTextColor                 color
     */
    public void setUnitTextColor(@ColorInt int unitTextColor) {
        this.unitTextColor = unitTextColor;
        invalidate();
    }

}

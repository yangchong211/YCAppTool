package com.yc.appchartview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import java.text.DecimalFormat;
import java.util.List;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     GitHub : https://github.com/yangchong211/YCWidgetLib
 *     time  : 2018/11/9
 *     desc  : 圆形图表控件
 *     revise:
 * </pre>
 */
public class PieFlagView extends View {

    /**
     * 饼图半径
     */
    private final float mRadius;
    /**
     * 矩形边长
     */
    private final float mBorderLength;
    /**
     * 文字大小
     */
    private final float mTextSize;
    /**
     * 文字颜色
     */
    private final int mTextColor;
    /**
     * 中间文案
     */
    private final String centerTitle;
    /**
     * 中间二级文案
     */
    private final String centerHint;
    /**
     * 高
     */
    private int mHeight;
    private List<PieFlagData> mData;
    /**
     * 最大半径
     */
    private float radiusMax;
    /**
     * 间隔宽度
     */
    private final float intervalWidth;
    /**
     * 小方块左边距
     */
    private final float blockAgeMarginLeft;
    private final Context context;
    private Paint mPiePaint, mInnerPiePaint, mBlockAgePaint, mTextPaint;
    private RectF preRectF, preInnerRectF;
    private Rect rectBlockage;

    public PieFlagView(Context context) {
        this(context, null);
    }

    public PieFlagView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PieFlagView(Context c, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(c, attrs, defStyleAttr);
        this.context = c;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PieFlagView);
        mRadius = typedArray.getDimension(R.styleable.PieFlagView_radius, 120);
        mBorderLength = typedArray.getDimension(R.styleable.PieFlagView_border_length, 15);
        mTextColor = typedArray.getColor(R.styleable.PieFlagView_textColor, Color.BLACK);
        mTextSize = typedArray.getDimension(R.styleable.PieFlagView_textSize, 30);
        centerTitle = typedArray.getString(R.styleable.PieFlagView_centerTitle);
        centerHint = typedArray.getString(R.styleable.PieFlagView_centerHint);
        blockAgeMarginLeft = typedArray.getDimension(R.styleable.PieFlagView_blockAgeMarginLeft, 30);
        intervalWidth = typedArray.getDimension(R.styleable.PieFlagView_intervalWidth, 2);
        typedArray.recycle();
        init();
    }

    private void init() {
        //外圆
        mPiePaint = new Paint();
        mPiePaint.setAntiAlias(true);
        mPiePaint.setStyle(Paint.Style.STROKE);
        mPiePaint.setStrokeWidth(mBorderLength);
        //绘制大圆的矩形
        preRectF = new RectF();

        //内圆
        mInnerPiePaint = new Paint();
        mInnerPiePaint.setAntiAlias(true);
        mInnerPiePaint.setStyle(Paint.Style.FILL);
        //绘制内圆的矩形
        preInnerRectF = new RectF();

        //弧度小方格
        mBlockAgePaint = new Paint();
        mBlockAgePaint.setAntiAlias(true);
        mBlockAgePaint.setStyle(Paint.Style.FILL);
        mBlockAgePaint.setStrokeWidth(mTextSize);
        //右侧小方格 说明
        rectBlockage = new Rect();
        //文字画笔
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(mTextColor);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawPie(canvas, preRectF);
        drawCenter(canvas, preInnerRectF);
        drawBlockAge(canvas);
    }

    /**
     * 绘制圆中心文字
     *
     * @param rectF 基于圆环处理内圆
     */
    private void drawCenter(Canvas canvas, RectF rectF) {
        mInnerPiePaint.setColor(Color.WHITE);
        canvas.drawArc(rectF, 0, 360, true, mInnerPiePaint);
        mTextPaint.setColor(Color.parseColor("#333333"));
        mTextPaint.setFakeBoldText(true);
        mTextPaint.setTextSize(sp2px(context, 12));
        //中心title
        float textWidth = mTextPaint.measureText(centerTitle);
        float textHeight = measureTextHeight(mTextPaint);
        int textX = (int) (rectF.left + (rectF.right - rectF.left) / 2);
        canvas.drawText(centerTitle, textX - textWidth / 2, mHeight / 2, mTextPaint);
        //hint title
        mTextPaint.setFakeBoldText(false);
        mTextPaint.setTextSize(sp2px(context, 9));
        mTextPaint.setColor(Color.parseColor("#777777"));
        //hint
        float textHintWidth = mTextPaint.measureText(centerHint);
        canvas.drawText(centerHint, textX - textHintWidth / 2, mHeight / 2 + textHeight, mTextPaint);
    }

    /**
     * 测量文字的高度
     */
    public static float measureTextHeight(Paint paint) {
        float height = 0f;
        if (null == paint) {
            return height;
        }
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        height = fontMetrics.descent - fontMetrics.ascent;
        return height;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //宽
        int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        int mWidth = resolveSize(screenWidth, widthMeasureSpec);
        mHeight = resolveSize(screenWidth / 2, heightMeasureSpec);
        setMeasuredDimension(mWidth, mHeight);

        //最大半径
        int topAndBottom = getPaddingTop() + getPaddingBottom();
        int leftAndRight = getPaddingLeft() + getPaddingRight();

        radiusMax = (Math.min((mHeight - topAndBottom) / 2, (mWidth - leftAndRight) / 2)) - mBorderLength / 2;
        Log.d("onMeasure", "..mHeight..." + mHeight + "..mWidth..." + mWidth + "...." + radiusMax);
    }


    /**
     * 绘制饼图
     *
     * @param canvas   canvas
     * @param preRectF preRectF
     */
    private void drawPie(Canvas canvas, RectF preRectF) {
        //兼容 短边的一半 作半径
        //得到实际半径
        float mRadiusValue = Math.min(mRadius, radiusMax);
        //避免创建对象
        preRectF.left = mBorderLength / 2 + getPaddingLeft();
        float offsetTop = mBorderLength / 2 + getPaddingTop();
        preRectF.top = getRadiusOffset() + offsetTop;
        preRectF.right = preRectF.left + mRadiusValue * 2;
        preRectF.bottom = preRectF.top + mRadiusValue * 2;
        float currentAngle = 0;//当前角度
        for (int i = 0; i < mData.size(); i++) {
            PieFlagData pieData = mData.get(i);
            mPiePaint.setColor(Color.parseColor(mData.get(i).getGetColor()));
            //useCenter: 如果为True时，在绘制圆弧时将圆心包括在内，通常用来绘制扇形。
            canvas.drawArc(preRectF, currentAngle + intervalWidth,
                    (pieData.getPercentage()) * 360 - intervalWidth,
                    false, mPiePaint);
            currentAngle += (pieData.getPercentage()) * 360;
        }
        //圆内文本
        preInnerRectF.left = preRectF.left + mBorderLength / 3;
        preInnerRectF.top = preRectF.top + mBorderLength / 3;
        preInnerRectF.right = preRectF.right - mBorderLength / 3;
        preInnerRectF.bottom = preRectF.bottom - mBorderLength / 3;
    }

    private float getRadiusOffset() {
        float v = radiusMax - mRadius;
        return v > 0 ? v : 0f;
    }

    /**
     * 绘制小矩形和文字
     *
     * @param canvas canvas
     */
    private void drawBlockAge(Canvas canvas) {
        int areaHeight = dip2px(context, 8);
        int currentX = (int) (preRectF.right + getPaddingRight() + blockAgeMarginLeft);
        float textHeight = measureTextHeight(mTextPaint);
        //每一份
        int count = mData.size();
        //内容高度
        float contentHeight = count * (textHeight + areaHeight) - areaHeight;
        float centerYResult = (mHeight - contentHeight) / 2;
        float currentY = centerYResult + 2;
        //测量文本的高度  0,1,2
        for (int i = 0; i < count; i++) {
            PieFlagData pieData = mData.get(i);
            float diff = i > 0 ? (textHeight + areaHeight) : 0;
            currentY += diff;
            mBlockAgePaint.setColor(Color.parseColor(mData.get(i).getGetColor()));
            rectBlockage.left = currentX;
            rectBlockage.top = (int) (currentY + 2);
            rectBlockage.right = (int) (currentX + textHeight / 1.5);
            rectBlockage.bottom = (int) (currentY + areaHeight);
            canvas.drawRect(rectBlockage, mBlockAgePaint);

            DecimalFormat fNum = new DecimalFormat("#0.0");
            String percentage = fNum.format(pieData.getPercentage() * 100);
            String title = pieData.getTitle() + " (" + percentage + "%)";
            canvas.drawText(title, currentX + mBorderLength * 1.4f,
                    currentY + rectBlockage.bottom - rectBlockage.top, mTextPaint);
        }
    }


    public void setData(List<PieFlagData> data) {
        mData = data;
        postInvalidate();
    }

    public static int sp2px(Context context, final float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static int dip2px(Context ctx, float dp) {
        float density = ctx.getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5f);
    }


}

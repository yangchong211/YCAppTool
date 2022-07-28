package com.yc.appchartview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
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
public class PieChartView extends View {

    /**
     * 饼图半径
     */
    private final float mRadius;
    /**
     * 矩形边长
     */
    private final float mBorderLength;
    private List<PieFlagData> mData;
    /**
     * 最大半径
     */
    private float radiusMax;
    /**
     * 间隔宽度
     */
    private final float intervalWidth;
    private final Context context;
    private Paint mPiePaint;
    private RectF preRectF;

    public PieChartView(Context context) {
        this(context, null);
    }

    public PieChartView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PieChartView(Context c, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(c, attrs, defStyleAttr);
        this.context = c;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PieChartView);
        mRadius = typedArray.getDimension(R.styleable.PieChartView_pieRadius, 120);
        mBorderLength = typedArray.getDimension(R.styleable.PieChartView_borderLength, 15);
        intervalWidth = typedArray.getDimension(R.styleable.PieChartView_pieIntervalWidth, 2);
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
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawPie(canvas, preRectF);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //宽
        int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        int mWidth = resolveSize(screenWidth, widthMeasureSpec);
        int mHeight = resolveSize(screenWidth / 2, heightMeasureSpec);
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
        //避免创建对象。mBorderLength 矩形边长
        //从180度开始绘制
        preRectF.left = mBorderLength / 2 + getPaddingLeft();
        float offsetTop = mBorderLength / 2 + getPaddingTop();
        preRectF.top = getRadiusOffset() + offsetTop;
        preRectF.right = preRectF.left + mRadiusValue * 2;
        preRectF.bottom = preRectF.top + mRadiusValue * 2;
        //Log.i("drawPie",preRectF.left+" " + preRectF.top +" " + preRectF.right +" " + preRectF.bottom );

        float currentAngle = -90;
        if (mData!=null && mData.size()>0){
            //当前角度
            for (int i = 0; i < mData.size(); i++) {
                PieFlagData pieData = mData.get(i);
                mPiePaint.setColor(Color.parseColor(mData.get(i).getGetColor()));
                //useCenter: 如果为True时，在绘制圆弧时将圆心包括在内，通常用来绘制扇形。
                //preRectF 定义的圆弧的形状和大小的范围
                //第二个参数：float startAngle，这个参数的作用是设置圆弧是从哪个角度来顺时针绘画的
                //第三个参数：float sweepAngle，这个参数的作用是设置圆弧扫过的角度
                //第四个参数：boolean useCenter，这个参数的作用是设置我们的圆弧在绘画的时候，是否经过圆形
                canvas.drawArc(preRectF, currentAngle + intervalWidth,
                        (pieData.getPercentage()) * 360 - intervalWidth,
                        false, mPiePaint);
                currentAngle += (pieData.getPercentage()) * 360;
            }
        }
    }

    private float getRadiusOffset() {
        float v = radiusMax - mRadius;
        return v > 0 ? v : 0f;
    }

    public void setData(List<PieFlagData> data) {
        mData = data;
        postInvalidate();
    }

}

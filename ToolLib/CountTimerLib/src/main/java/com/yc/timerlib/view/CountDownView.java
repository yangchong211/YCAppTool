/*
Copyright 2017 yangchong211（github.com/yangchong211）

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package com.yc.timerlib.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.os.Parcelable;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.yc.timerlib.R;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211/YCStatusBar
 *     time  : 2017/5/18
 *     desc  : 倒计时自定义控件
 *     revise:
 * </pre>
 */
public class CountDownView extends View {

    /**
     * 上下文
     */
    private final Context mContext;
    /**
     * 背景画笔
     */
    private Paint mPaintBackGround;
    /**
     * 圆弧画笔
     */
    private Paint mPaintArc;
    /**
     * 文字画笔
     */
    private Paint mPaintText;
    /**
     * 圆弧绘制方式（增加和减少）
     */
    private final int mRetreatType;
    /**
     * 最外层圆弧的宽度
     */
    private final float mPaintArcWidth;
    /**
     * 圆圈的半径
     */
    private final int mCircleRadius;
    /**
     * 初始值
     */
    private int mPaintArcColor = Color.parseColor("#3C3F41");
    /**
     * 初始值
     */
    private int mPaintBackGroundColor = Color.parseColor("#55B2E5");
    /**
     * 时间，单位秒
     */
    private int mLoadingTime;
    /**
     * 时间单位
     */
    private String mLoadingTimeUnit = "";
    /**
     * 字体颜色
     */
    private int mTextColor = Color.BLACK;
    /**
     * 字体大小
     */
    private final int mTextSize;
    /**
     * 从哪个位置开始
     */
    private int location;
    /**
     * 开始角度
     */
    private float startAngle;
    /**
     * 起点
     */
    private float mmSweepAngleStart;
    /**
     * 终点
     */
    private float mmSweepAngleEnd;
    /**
     * 扫过的角度
     */
    private float mSweepAngle;
    /**
     * 要绘制的文字
     */
    private String mText = "";
    /**
     * 是否在运行中
     */
    private boolean isRunning = false;
    private int mWidth;
    private int mHeight;
    private AnimatorSet set;

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stop();
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        CountDownState viewState = new CountDownState(superState);
        viewState.mLoadingTime = mLoadingTime;
        viewState.mLocation = location;
        viewState.isRunning = isRunning;
        return viewState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        CountDownState viewState = (CountDownState) state;
        super.onRestoreInstanceState(viewState.getSuperState());
        mLoadingTime = viewState.mLoadingTime;
        location = viewState.mLocation;
        if (viewState.isRunning) {
            start();
        }
    }

    public CountDownView(Context context) {
        this(context, null);
    }

    public CountDownView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CountDownView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CountDownView);
        mRetreatType = array.getInt(R.styleable.CountDownView_cd_retreat_type, 1);
        location = array.getInt(R.styleable.CountDownView_cd_location, 1);
        //默认25dp
        mCircleRadius = (int) array.getDimension(R.styleable.CountDownView_cd_circle_radius, dip2px(context, 25));
        //默认3dp
        mPaintArcWidth = array.getDimension(R.styleable.CountDownView_cd_arc_width, dip2px(context, 3));
        mPaintArcColor = array.getColor(R.styleable.CountDownView_cd_arc_color, mPaintArcColor);
        //默认14sp
        mTextSize = (int) array.getDimension(R.styleable.CountDownView_cd_text_size, dip2px(context, 14));
        mTextColor = array.getColor(R.styleable.CountDownView_cd_text_color, mTextColor);
        mPaintBackGroundColor = array.getColor(R.styleable.CountDownView_cd_bg_color, mPaintBackGroundColor);
        //默认3秒
        mLoadingTime = array.getInteger(R.styleable.CountDownView_cd_animator_time, 3);
        //时间单位
        mLoadingTimeUnit = array.getString(R.styleable.CountDownView_cd_animator_time_unit);
        if (TextUtils.isEmpty(mLoadingTimeUnit)) {
            mLoadingTimeUnit = "";
        }
        array.recycle();
        init();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void init() {
        //背景设为透明，然后造成圆形View的视觉错觉
        this.setBackground(ContextCompat.getDrawable(mContext, android.R.color.transparent));
        //创建背景画笔
        mPaintBackGround = new Paint();
        mPaintBackGround.setStyle(Paint.Style.FILL);
        mPaintBackGround.setAntiAlias(true);
        mPaintBackGround.setColor(mPaintBackGroundColor);

        //创建圆弧画笔
        mPaintArc = new Paint();
        mPaintArc.setStyle(Paint.Style.STROKE);
        mPaintArc.setAntiAlias(true);
        mPaintArc.setColor(mPaintArcColor);
        mPaintArc.setStrokeWidth(mPaintArcWidth);

        //创建文字画笔
        mPaintText = new Paint();
        mPaintText.setStyle(Paint.Style.STROKE);
        mPaintText.setAntiAlias(true);
        mPaintText.setColor(mTextColor);
        mPaintText.setTextSize(mTextSize);
        //如果时间为小于0，则默认倒计时时间为3秒
        if (mLoadingTime < 0) {
            mLoadingTime = 3;
        }
        if (location == 1) {
            //默认从左侧开始
            startAngle = -180;
        } else if (location == 2) {
            startAngle = -90;
        } else if (location == 3) {
            startAngle = 0;
        } else if (location == 4) {
            startAngle = 90;
        }

        if (mRetreatType == 1) {
            mmSweepAngleStart = 0f;
            mmSweepAngleEnd = 360f;
        } else {
            mmSweepAngleStart = 360f;
            mmSweepAngleEnd = 0f;
        }
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //获取view宽高
        mWidth = w;
        mHeight = h;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //因为必须是圆形的view，所以在这里重新赋值
        setMeasuredDimension(mCircleRadius * 2, mCircleRadius * 2);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画北景园
        canvas.drawCircle(mWidth / 2, mHeight / 2, mWidth / 2 - mPaintArcWidth, mPaintBackGround);
        //画圆弧
        RectF rectF = new RectF(0 + mPaintArcWidth / 2, 0 + mPaintArcWidth / 2, mWidth - mPaintArcWidth / 2, mHeight - mPaintArcWidth / 2);
        canvas.drawArc(rectF, startAngle, mSweepAngle, false, mPaintArc);
        //画文字
        float mTextWidth = mPaintText.measureText(mText, 0, mText.length());
        float dx = (mWidth / 2) - mTextWidth / 2;
        Paint.FontMetricsInt fontMetricsInt = mPaintText.getFontMetricsInt();
        float dy = (fontMetricsInt.bottom - fontMetricsInt.top) / 2 - fontMetricsInt.bottom;
        float baseLine = mHeight / 2 + dy;
        canvas.drawText(mText, dx, baseLine, mPaintText);
    }

    /**
     * 开始动态倒计时
     */
    public void start() {
        isRunning = true;
        ValueAnimator animator1 = ValueAnimator.ofFloat(mmSweepAngleStart, mmSweepAngleEnd);
        animator1.setInterpolator(new LinearInterpolator());
        animator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mSweepAngle = (float) valueAnimator.getAnimatedValue();
                //获取到需要绘制的角度，重新绘制
                //在UI线程自身中使用
                invalidate();
            }
        });
        //这里是时间获取和赋值
        ValueAnimator animator2 = ValueAnimator.ofInt(mLoadingTime, 0);
        animator2.setInterpolator(new LinearInterpolator());
        animator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int time = (int) valueAnimator.getAnimatedValue();
                mText = time + mLoadingTimeUnit;
            }
        });
        set = new AnimatorSet();
        set.playTogether(animator1, animator2);
        set.setDuration(mLoadingTime * 1000);
        set.setInterpolator(new LinearInterpolator());
        set.start();
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                clearAnimation();
                if (loadingFinishListener != null) {
                    loadingFinishListener.finish();
                }
                isRunning = false;
            }
        });
    }

    /**
     * 停止动画
     */
    public void stop(){
        if(set!=null && set.isRunning()){
            set.cancel();
            isRunning = false;
        }
    }

    /**
     * 设置倒计时时间
     * @param time      时间，秒
     */
    public void setTime(int time){
        if (time<0){
            time = 3;
        }
        mLoadingTime = time;
    }

    private OnFinishListener loadingFinishListener;
    public void setFinishListener(OnFinishListener listener) {
        this.loadingFinishListener = listener;
    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}

package com.yc.ycprogresslib;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.ColorInt;

/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2016/2/10
 *     desc  : 自定义百分比进度条
 *     revise: 参考案例：夏安明博客http://blog.csdn.net/xiaanming/article/details/10298163
 *             案例地址：https://github.com/yangchong211
 * </pre>
 */
public class NumberProgressbar extends View {

    private int mMaxProgress = 100;
    private int mCurrentProgress = 0;
    private int mReachedBarColor;
    private int mUnreachedBarColor;
    private int mTextColor;
    private float mTextSize;
    private float mReachedBarHeight;
    private float mUnreachedBarHeight;
    private String mSuffix = "%";
    private String mPrefix = "";
    private float mDrawTextStart;
    private float mDrawTextEnd;
    private String mCurrentDrawText;
    private Paint mReachedBarPaint;
    private Paint mUnreachedBarPaint;
    private Paint mTextPaint;
    private RectF mUnreachedRectF = new RectF(0, 0, 0, 0);
    private RectF mReachedRectF = new RectF(0, 0, 0, 0);
    private float mOffset;
    private boolean mDrawUnreachedBar = true;
    private boolean mDrawReachedBar = true;
    private boolean mTextIsVisible = true;
    private long timeMillis = 0;

    public NumberProgressbar(Context context) {
        this(context, null);
    }

    public NumberProgressbar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NumberProgressbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeAttrs(context,attrs,defStyleAttr);
        initializePainters();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //开始绘制
        drawCanvas(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measureWidth = measure(widthMeasureSpec, true);
        int measureHeight = measure(heightMeasureSpec, false);
        setMeasuredDimension(measureWidth, measureHeight);
    }

    /**
     * 初始化自定义属性
     * @param context                       上下文
     * @param attrs                         attrs
     * @param defStyleAttr                  defStyleAttr
     */
    private void initializeAttrs(Context context, AttributeSet attrs, int defStyleAttr) {
        final TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.NumberProgressbar, defStyleAttr, 0);
        mReachedBarColor = attributes.getColor(R.styleable.NumberProgressbar_progress_reached_color, Color.GRAY);
        mUnreachedBarColor = attributes.getColor(R.styleable.NumberProgressbar_progress_unreached_color, Color.DKGRAY);
        mTextColor = attributes.getColor(R.styleable.NumberProgressbar_progress_text_color, Color.BLACK);
        mTextSize = attributes.getDimension(R.styleable.NumberProgressbar_progress_text_size, ProgressBarUtils.sp2px(context,12));
        mReachedBarHeight = attributes.getDimension(R.styleable.NumberProgressbar_progress_reached_bar_height, ProgressBarUtils.dp2px(context,1.5f));
        mUnreachedBarHeight = attributes.getDimension(R.styleable.NumberProgressbar_progress_unreached_bar_height, ProgressBarUtils.dp2px(context,1.0f));
        mOffset = attributes.getDimension(R.styleable.NumberProgressbar_progress_text_offset, ProgressBarUtils.dp2px(context,2.0f));
        int textVisible = attributes.getInt(R.styleable.NumberProgressbar_progress_text_visibility, 0);
        if (textVisible != 0) {
            mTextIsVisible = false;
        }
        setProgress(attributes.getInt(R.styleable.NumberProgressbar_progress_current, 0));
        setMax(attributes.getInt(R.styleable.NumberProgressbar_progress_max, 100));
        attributes.recycle();
    }


    /**
     * 初始化画笔
     */
    private void initializePainters() {
        //创建到达进度条画笔
        mReachedBarPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mReachedBarPaint.setColor(mReachedBarColor);

        //创建未到达进度条画笔
        mUnreachedBarPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mUnreachedBarPaint.setColor(mUnreachedBarColor);

        //创建百分比文本进度条画笔
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);
    }


    @Override
    protected int getSuggestedMinimumWidth() {
        return (int) mTextSize;
    }

    @Override
    protected int getSuggestedMinimumHeight() {
        return Math.max((int) mTextSize, Math.max((int) mReachedBarHeight, (int) mUnreachedBarHeight));
    }

    private int measure(int measureSpec, boolean isWidth) {
        int result;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        int padding = isWidth ? getPaddingLeft() + getPaddingRight() : getPaddingTop() + getPaddingBottom();
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = isWidth ? getSuggestedMinimumWidth() : getSuggestedMinimumHeight();
            result += padding;
            if (mode == MeasureSpec.AT_MOST) {
                if (isWidth) {
                    result = Math.max(result, size);
                } else {
                    result = Math.min(result, size);
                }
            }
        }
        return result;
    }


    private void drawCanvas(Canvas canvas) {
        if (mTextIsVisible) {
            calculateDrawRectF();
        } else {
            calculateDrawRectFWithoutProgressText();
        }
        if (mDrawReachedBar) {
            canvas.drawRect(mReachedRectF, mReachedBarPaint);
        }
        if (mDrawUnreachedBar) {
            canvas.drawRect(mUnreachedRectF, mUnreachedBarPaint);
        }
        if (mTextIsVisible){
            canvas.drawText(mCurrentDrawText, mDrawTextStart, mDrawTextEnd, mTextPaint);
        }
    }


    private void calculateDrawRectFWithoutProgressText() {
        mReachedRectF.left = getPaddingLeft();
        mReachedRectF.top = getHeight() / 2.0f - mReachedBarHeight / 2.0f;
        mReachedRectF.right = (getWidth() - getPaddingLeft() - getPaddingRight()) / (mMaxProgress * 1.0f) * mCurrentProgress + getPaddingLeft();
        mReachedRectF.bottom = getHeight() / 2.0f + mReachedBarHeight / 2.0f;

        mUnreachedRectF.left = mReachedRectF.right;
        mUnreachedRectF.right = getWidth() - getPaddingRight();
        mUnreachedRectF.top = getHeight() / 2.0f + -mUnreachedBarHeight / 2.0f;
        mUnreachedRectF.bottom = getHeight() / 2.0f + mUnreachedBarHeight / 2.0f;
    }


    @SuppressLint("DefaultLocale")
    private void calculateDrawRectF() {
        mCurrentDrawText = String.format("%d", mCurrentProgress * 100 / mMaxProgress);
        mCurrentDrawText = mPrefix + mCurrentDrawText + mSuffix;
        float mDrawTextWidth = mTextPaint.measureText(mCurrentDrawText);

        if (mCurrentProgress == 0) {
            mDrawReachedBar = false;
            mDrawTextStart = getPaddingLeft();
        } else {
            mDrawReachedBar = true;
            mReachedRectF.left = getPaddingLeft();
            mReachedRectF.top = getHeight() / 2.0f - mReachedBarHeight / 2.0f;
            mReachedRectF.right = (getWidth() - getPaddingLeft() - getPaddingRight()) / (mMaxProgress * 1.0f) * mCurrentProgress - mOffset + getPaddingLeft();
            mReachedRectF.bottom = getHeight() / 2.0f + mReachedBarHeight / 2.0f;
            mDrawTextStart = (mReachedRectF.right + mOffset);
        }

        mDrawTextEnd = (int) ((getHeight() / 2.0f) - ((mTextPaint.descent() + mTextPaint.ascent()) / 2.0f));

        if ((mDrawTextStart + mDrawTextWidth) >= getWidth() - getPaddingRight()) {
            mDrawTextStart = getWidth() - getPaddingRight() - mDrawTextWidth;
            mReachedRectF.right = mDrawTextStart - mOffset;
        }

        float unreachedBarStart = mDrawTextStart + mDrawTextWidth + mOffset;
        if (unreachedBarStart >= getWidth() - getPaddingRight()) {
            mDrawUnreachedBar = false;
        } else {
            mDrawUnreachedBar = true;
            mUnreachedRectF.left = unreachedBarStart;
            mUnreachedRectF.right = getWidth() - getPaddingRight();
            mUnreachedRectF.top = getHeight() / 2.0f + -mUnreachedBarHeight / 2.0f;
            mUnreachedRectF.bottom = getHeight() / 2.0f + mUnreachedBarHeight / 2.0f;
        }
    }


    /**
     * 进度更新task。
     */
    private Runnable progressChangeTask = new Runnable() {
        @Override
        public void run() {
            removeCallbacks(this);
            mCurrentProgress += 1;
            if (mCurrentProgress >= 0 && mCurrentProgress <= 100) {
                if (mListener != null){
                    mListener.onProgressChange(mCurrentProgress,mMaxProgress);
                }
                invalidate();
                if (timeMillis == 0){
                    postDelayed(progressChangeTask, 500);
                }else {
                    postDelayed(progressChangeTask, timeMillis / 100);
                }
            } else{
                mCurrentProgress = validateProgress(mCurrentProgress);
            }
        }
    };


    /**
     * 当自定义控件销毁时，则调用该方法
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stop();
    }

    /**
     * 开始。
     */
    public void start() {
        stop();
        post(progressChangeTask);
    }

    /**
     * 重新开始。
     */
    public void reStart() {
        mCurrentProgress = 0;
        start();
    }

    /**
     * 停止。
     */
    public void stop() {
        if(progressChangeTask!=null){
            removeCallbacks(progressChangeTask);
        }
    }

    /**
     * 验证进度。
     *
     * @param progress                  你要验证的进度值。
     * @return                          返回真正的进度值。
     */
    private int validateProgress(int progress) {
        if (progress > 100){
            progress = 100;
        } else if (progress < 0){
            progress = 0;
        }
        return progress;
    }

    /**
     * 设置进度条文本的大小
     * @param textSize                  textSize
     */
    public void setProgressTextSize(float textSize) {
        this.mTextSize = textSize;
        mTextPaint.setTextSize(mTextSize);
        invalidate();
    }

    /**
     * 设置进度条文本的颜色
     * @param textColor                 textColor
     */
    public void setProgressTextColor(@ColorInt int textColor) {
        this.mTextColor = textColor;
        mTextPaint.setColor(mTextColor);
        invalidate();
    }

    /**
     * 设置未更新百分比进度条的颜色
     * @param barColor                      barColor
     */
    public void setUnreachedBarColor(@ColorInt int barColor) {
        this.mUnreachedBarColor = barColor;
        mUnreachedBarPaint.setColor(mUnreachedBarColor);
        invalidate();
    }

    /**
     * 设置百分比进度条的颜色
     * @param progressColor                 progressColor
     */
    public void setReachedBarColor(@ColorInt int progressColor) {
        this.mReachedBarColor = progressColor;
        mReachedBarPaint.setColor(mReachedBarColor);
        invalidate();
    }

    /**
     * 设置百分比进度条的高度
     * @param height                        height
     */
    public void setReachedBarHeight(float height) {
        mReachedBarHeight = height;
    }

    /**
     * 设置未更新百分比进度条的高度
     * @param height                        height
     */
    public void setUnreachedBarHeight(float height) {
        mUnreachedBarHeight = height;
    }

    /**
     * 设置最大进度条的值
     * @param maxProgress                   maxProgress
     */
    public void setMax(int maxProgress) {
        if (maxProgress > 0) {
            this.mMaxProgress = validateProgress(maxProgress);
            invalidate();
        }
    }

    public void setSuffix(String suffix) {
        if (suffix == null) {
            mSuffix = "";
        } else {
            mSuffix = suffix;
        }
    }


    public void setPrefix(String prefix) {
        if (prefix == null)
            mPrefix = "";
        else {
            mPrefix = prefix;
        }
    }

    public void incrementProgressBy(int by) {
        if (by > 0) {
            setProgress(mCurrentProgress + by);
        }
        if(mListener != null){
            mListener.onProgressChange(mCurrentProgress, mMaxProgress);
        }
    }

    public void setProgress(int progress) {
        if (progress <= mMaxProgress && progress >= 0) {
            this.mCurrentProgress = validateProgress(progress);
            invalidate();
        }
    }

    /**
     * 设置百分比文字内容是否可见
     * @param visibility                    是否可见
     */
    public void setNumberTextVisibility(@ProgressBarUtils.NumberVisibility int visibility) {
        mTextIsVisible = (visibility == ProgressBarUtils.NumberTextVisibility.Visible);
        invalidate();
    }

    /**
     * 设置倒计时总时间。
     *
     * @param timeMillis 毫秒。
     */
    public void setTimeMillis(long timeMillis) {
        this.timeMillis = timeMillis;
        invalidate();
    }


    private OnNumberProgressListener mListener;

    /**
     * 设置百分比进度条的监听
     * @param listener                  listener
     */
    public void setOnProgressBarListener(OnNumberProgressListener listener){
        mListener = listener;
    }



}

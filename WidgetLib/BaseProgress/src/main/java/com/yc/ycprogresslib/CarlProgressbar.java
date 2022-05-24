package com.yc.ycprogresslib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.ColorInt;

/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2016/2/10
 *     desc  : 自定义出游进度条
 *     revise: 可以动态addView
 * </pre>
 */
public class CarlProgressbar extends View {

    private int mMaxProgress = 100;
    private int mCurrentProgress = 0;
    private int mReachedBarColor;
    private int mUnreachedBarColor;
    private float mReachedBarHeight;
    private float mUnreachedBarHeight;
    private int mTotalWidth;
    /**
     * 到达进度条画笔
     */
    private Paint mReachedBarPaint;
    /**
     * 未到达进度条画笔
     */
    private Paint mUnreachedBarPaint;
    /**
     * 未到达进度条RectF
     */
    private RectF mUnreachedRectF = new RectF(0, 0, 0, 0);
    /**
     * 到达进度条RectF
     */
    private RectF mReachedRectF = new RectF(0, 0, 0, 0);
    private boolean mDrawUnreachedBar = true;
    private boolean mDrawReachedBar = true;
    private View customView;
    //弹出控件的x,y坐标
    int customViewX = 0;
    int customViewY = 0;
    int[] point = new int[2];
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mLayoutParams;

    public CarlProgressbar(Context context) {
        this(context, null);
    }

    public CarlProgressbar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CarlProgressbar(Context context, AttributeSet attrs, int defStyleAttr) {
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
        int defaultSize = 200;
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int specValue = MeasureSpec.getSize(widthMeasureSpec);
        mTotalWidth = specValue;
        switch (mode) {
            //指定一个默认值
            case MeasureSpec.UNSPECIFIED:
                mTotalWidth = defaultSize;
                break;
            //取测量值
            case MeasureSpec.EXACTLY:
                mTotalWidth = specValue;
                break;
            //取测量值和默认值中的最小值
            case MeasureSpec.AT_MOST:
                mTotalWidth = Math.min(defaultSize, specValue);
                break;
            default:
                break;
        }
        Log.i("测量--onMeasure--",mTotalWidth+"----"+mReachedBarHeight);
        setMeasuredDimension(mTotalWidth, (int) (mReachedBarHeight * 1.5f));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        getLocationOnScreen(point);
    }

    /**
     * 初始化自定义属性
     * @param context                       上下文
     * @param attrs                         attrs
     * @param defStyleAttr                  defStyleAttr
     */
    private void initializeAttrs(Context context, AttributeSet attrs, int defStyleAttr) {
        final TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.CarlProgressbar, defStyleAttr, 0);
        mReachedBarColor = attributes.getColor(R.styleable.CarlProgressbar_carl_progress_reached_color, Color.GRAY);
        mUnreachedBarColor = attributes.getColor(R.styleable.CarlProgressbar_carl_progress_unreached_color, Color.DKGRAY);
        mReachedBarHeight = attributes.getDimension(R.styleable.CarlProgressbar_carl_progress_reached_bar_height, ProgressBarUtils.dp2px(context,1.5f));
        mUnreachedBarHeight = attributes.getDimension(R.styleable.CarlProgressbar_carl_progress_unreached_bar_height, ProgressBarUtils.dp2px(context,1.0f));
        setProgress(attributes.getInt(R.styleable.CarlProgressbar_carl_progress_current, 0));
        setMax(attributes.getInt(R.styleable.CarlProgressbar_carl_progress_max, 100));
        attributes.recycle();
        initView(context);
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
    }


    private void initView(Context context) {
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mLayoutParams = new WindowManager.LayoutParams();
        mLayoutParams.gravity = Gravity.START | Gravity.TOP;
        mLayoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        mLayoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        mLayoutParams.format = PixelFormat.TRANSLUCENT;
        mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            mLayoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION;
        } else {
            mLayoutParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        }
    }

    private void drawCanvas(Canvas canvas) {
        calculateDrawRectFWithoutProgressText();
        //绘制到达进度条
        if (mDrawReachedBar) {
            canvas.drawRect(mReachedRectF, mReachedBarPaint);
        }
        //绘制未到达进度条
        if (mDrawUnreachedBar) {
            canvas.drawRect(mUnreachedRectF, mUnreachedBarPaint);
        }
        calculationToastIndex();
        drawCustom();
        showCustomView();
    }

    private void calculateDrawRectFWithoutProgressText() {
        mReachedRectF.left = getPaddingLeft();
        mReachedRectF.top = getHeight() / 2.0f - mReachedBarHeight / 2.0f;
        mReachedRectF.right = (getWidth() - getPaddingLeft() - getPaddingRight()) /
                (mMaxProgress * 1.0f) * mCurrentProgress + getPaddingLeft();
        mReachedRectF.bottom = getHeight() / 2.0f + mReachedBarHeight / 2.0f;

        mUnreachedRectF.left = mReachedRectF.right;
        mUnreachedRectF.right = getWidth() - getPaddingRight();
        mUnreachedRectF.top = getHeight() / 2.0f + -mUnreachedBarHeight / 2.0f;
        mUnreachedRectF.bottom = getHeight() / 2.0f + mUnreachedBarHeight / 2.0f;
    }


    private void calculationToastIndex() {
        int width = View.MeasureSpec.makeMeasureSpec((1 << 30) - 1, View.MeasureSpec.AT_MOST);
        int height = View.MeasureSpec.makeMeasureSpec((1 << 30) - 1, View.MeasureSpec.AT_MOST);
        if (customView != null) {
            customView.measure(width, height);
            customViewY = (int) (point[1] - getStatusBarHeight() - (customView.getMeasuredHeight() - mReachedBarHeight/2));
            //toastViewX = (int) ((mCurrentProgress/mMaxProgress)*mTotalWidth + point[0] - toastView.getMeasuredWidth()/2);
            customViewX = (int) (mTotalWidth * mCurrentProgress/mMaxProgress + point[0] - customView.getMeasuredWidth()/2);
        }
    }

    private void drawCustom() {
        if (customView == null) {
            return;
        }
        if (customView.getVisibility() == GONE) {
            customView.setVisibility(VISIBLE);
        }
        mLayoutParams.x = customViewX;
        mLayoutParams.y = customViewY;
        if (customView.getParent() == null) {
            mWindowManager.addView(customView, mLayoutParams);
        } else {
            mWindowManager.updateViewLayout(customView, mLayoutParams);
        }
    }

    private void showCustomView() {
        if (customView != null && customView.getParent() != null) {
            customView.setVisibility(VISIBLE);
        }
    }

    private void hideCustomView(View view) {
        if (view == null){
            return;
        }
        // 防闪烁
        view.setVisibility(GONE);
        if (view.getParent() != null) {
            mWindowManager.removeViewImmediate(view);
        }
    }

    public void setCustomView(View view) {
        customView = view;
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getContext().getResources().getIdentifier(
                "status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getContext().getResources().getDimensionPixelSize(resourceId);
        }
        return result;
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
     * 设置未绘制区域是否显示，不显示就不会draw
     * @param drawUnreachedBar              是否显示
     */
    public void setReachedBarDraw(boolean drawUnreachedBar){
        this.mDrawUnreachedBar = drawUnreachedBar;
    }

    /**
     * 设置最大进度条的值
     * @param maxProgress                   maxProgress
     */
    public void setMax(int maxProgress) {
        if (maxProgress > 0) {
            this.mMaxProgress = maxProgress;
            invalidate();
        }
    }

    public void setProgress(int progress) {
        if (progress <= mMaxProgress && progress >= 0) {
            this.mCurrentProgress = progress;
            //invalidate();
            requestLayout();
        }
    }

}

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

package com.yc.webviewlib.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/9/10
 *     desc  : WebView进度条
 *     revise: 1.progress同时返回两次100时进度条出现两次
 *             2.当一条进度没跑完，又点击其他链接开始第二次进度时，第二次进度不出现
 *             3.修改消失动画时长，使其消失时看到可以进度跑完
 *             4.修复当第一次进度返回 0 或超过 10，出现不显示进度条的问题
 *             5.能显示渐变色
 *             参考案例：Link to https://github.com/youlookwhat/WebProgress
 * </pre>
 */
public class WebProgress extends FrameLayout {

    /**
     * 默认匀速动画最大的时长
     */
    public static final int MAX_UNIFORM_SPEED_DURATION = 8 * 1000;
    /**
     * 默认加速后减速动画最大时长
     */
    public static final int MAX_DECELERATE_SPEED_DURATION = 450;
    /**
     * 95f-100f时，透明度1f-0f时长
     */
    public static final int DO_END_ALPHA_DURATION = 630;
    /**
     * 95f - 100f动画时长
     */
    public static final int DO_END_PROGRESS_DURATION = 500;
    /**
     * 当前匀速动画最大的时长
     */
    private static int CURRENT_MAX_UNIFORM_SPEED_DURATION = MAX_UNIFORM_SPEED_DURATION;
    /**
     * 当前加速后减速动画最大时长
     */
    private static int CURRENT_MAX_DECELERATE_SPEED_DURATION = MAX_DECELERATE_SPEED_DURATION;
    /**
     * 默认的高度(dp)
     */
    public static int WEB_PROGRESS_DEFAULT_HEIGHT = 3;
    /**
     * 进度条颜色默认
     */
    public static String WEB_PROGRESS_COLOR = "#2483D9";
    /**
     * 进度条颜色
     */
    private int mColor;
    /**
     * 进度条的画笔
     */
    private Paint mPaint;
    /**
     * 进度条动画
     */
    private Animator mAnimator;
    /**
     * 控件的宽度
     */
    private int mTargetWidth = 0;
    /**
     * 控件的高度
     */
    private int mTargetHeight;
    /**
     * 标志当前进度条的状态
     */
    private int TAG = 0;
    /**
     * 第一次过来进度show，后面就是setProgress
     */
    private boolean isShow = false;
    /**
     * 用来记录不能继续开始
     */
    public static final int UN_START = 0;
    /**
     * 用来记录已经开始，当开始执行加载动画后就切换到该状态
     */
    public static final int STARTED = 1;
    /**
     * 用来记录已经结束
     */
    public static final int FINISH = 2;
    /**
     * 百分比进度值
     */
    private float mCurrentProgress = 0F;

    public WebProgress(Context context) {
        this(context, null);
    }

    public WebProgress(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WebProgress(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    /**
     * 创建的时候
     */
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    /**
     * 销毁的时候，注意记得清楚动画资源
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        /**
         * animator cause leak , if not cancel;
         */
        if (mAnimator != null && mAnimator.isStarted()) {
            mAnimator.cancel();
            mAnimator = null;
        }
    }


    /**
     * 初始化操作
     * @param context                           上下文
     * @param attrs                             attrs属性
     * @param defStyleAttr                      defStyleAttr
     */
    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        //创建画笔，设置属性
        mPaint = new Paint();
        mColor = Color.parseColor(WEB_PROGRESS_COLOR);
        mPaint.setAntiAlias(true);
        //设置颜色
        mPaint.setColor(mColor);
        mPaint.setDither(true);
        mPaint.setStrokeCap(Paint.Cap.SQUARE);

        mTargetWidth = context.getResources().getDisplayMetrics().widthPixels;
        mTargetHeight = dip2px(WEB_PROGRESS_DEFAULT_HEIGHT);
    }

    /**
     * 设置单色进度条
     */
    public void setColor(int color) {
        this.mColor = color;
        mPaint.setColor(color);
    }

    /**
     * 设置单色进度条
     * @param color                     颜色
     */
    public void setColor(String color) {
        try {
            int parseColor = Color.parseColor(color);
            this.setColor(parseColor);
        } catch (IllegalArgumentException exception){
            exception.printStackTrace();
        }
    }

    /**
     * 设置渐变色进度条
     *
     * @param startColor                        开始颜色
     * @param endColor                          结束颜色
     */
    public void setColor(int startColor, int endColor) {
        try {
            LinearGradient linearGradient = new LinearGradient(0, 0, mTargetWidth,
                    mTargetHeight, startColor, endColor, Shader.TileMode.CLAMP);
            mPaint.setShader(linearGradient);
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * 设置渐变色进度条
     *
     * @param startColor                        开始颜色
     * @param endColor                          结束颜色
     */
    public void setColor(String startColor, String endColor) {
        this.setColor(Color.parseColor(startColor), Color.parseColor(endColor));
    }

    /**
     * 测量方法
     * MeasureSpec由两部分组成，一部分是测量模式，另一部分是测量的尺寸大小
     * @param widthMeasureSpec                  widthMeasureSpec
     * @param heightMeasureSpec                 heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //获取宽高的测量模式
        int wMode = MeasureSpec.getMode(widthMeasureSpec);
        int hMode = MeasureSpec.getMode(heightMeasureSpec);
        //获取测量到的尺寸大小，获取View的宽/高
        int w = MeasureSpec.getSize(widthMeasureSpec);
        int h = MeasureSpec.getSize(heightMeasureSpec);

        //UNSPECIFIED ：不对View进行任何限制，要多大给多大，一般用于系统内部
        //EXACTLY：对应LayoutParams中的match_parent和具体数值这两种模式。
        //AT_MOST ：对应LayoutParams中的wrap_content
        if (wMode == MeasureSpec.AT_MOST) {
            DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
            int metrics = displayMetrics.widthPixels;
            w = (w <= metrics ? w : displayMetrics.widthPixels);
        }
        if (hMode == MeasureSpec.AT_MOST) {
            h = mTargetHeight;
        }
        //指定View的宽高，完成测量工作
        this.setMeasuredDimension(w, h);
    }

    /**
     * ①绘制背景 background.draw(canvas)
     * ②绘制自己（onDraw）
     * ③绘制Children(dispatchDraw)
     * ④绘制装饰（onDrawScrollBars）
     * @param canvas        canvas
     */
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }

    /**
     * ②绘制自己（onDraw）
     * @param canvas        canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {

    }

    /**
     * ③绘制Children(dispatchDraw)
     * @param canvas        canvas
     */
    @Override
    protected void dispatchDraw(Canvas canvas) {
        //绘制线
        int width = this.getWidth();
        canvas.drawRect(0, 0,
                mCurrentProgress / 100 * Float.valueOf(width), this.getHeight(), mPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mTargetWidth = getMeasuredWidth();
        int screenWidth = getContext().getResources().getDisplayMetrics().widthPixels;
        if (mTargetWidth >= screenWidth) {
            CURRENT_MAX_DECELERATE_SPEED_DURATION = MAX_DECELERATE_SPEED_DURATION;
            CURRENT_MAX_UNIFORM_SPEED_DURATION = MAX_UNIFORM_SPEED_DURATION;
        } else {
            //取比值
            float rate = (float) (this.mTargetWidth / (screenWidth*1.0));
            CURRENT_MAX_UNIFORM_SPEED_DURATION = (int) (MAX_UNIFORM_SPEED_DURATION * rate);
            CURRENT_MAX_DECELERATE_SPEED_DURATION = (int) (MAX_DECELERATE_SPEED_DURATION * rate);
        }
    }

    private void setFinish() {
        isShow = false;
        TAG = FINISH;
    }

    /**
     * 开始动画
     */
    private void startAnim(boolean isFinished) {
        float v = isFinished ? 100 : 95;
        //先清除动画资源
        if (mAnimator != null && mAnimator.isStarted()) {
            mAnimator.cancel();
        }
        mCurrentProgress = ((int)mCurrentProgress) == 0f ? 0.00000001f : mCurrentProgress;

        if (!isFinished) {
            //如果还没有完成
            ValueAnimator mAnimator = ValueAnimator.ofFloat(mCurrentProgress, v);
            float residue = 1f - mCurrentProgress / 100 - 0.05f;
            mAnimator.setInterpolator(new LinearInterpolator());
            mAnimator.setDuration((long) (residue * CURRENT_MAX_UNIFORM_SPEED_DURATION));
            mAnimator.addUpdateListener(mAnimatorUpdateListener);
            mAnimator.start();
            this.mAnimator = mAnimator;
        } else {
            //如果还没有完成
            ValueAnimator segment95Animator = null;
            if (mCurrentProgress < 95f) {
                segment95Animator = ValueAnimator.ofFloat(mCurrentProgress, 95);
                float residue = 1f - mCurrentProgress / 100f - 0.05f;
                segment95Animator.setInterpolator(new LinearInterpolator());
                segment95Animator.setDuration((long) (residue * CURRENT_MAX_DECELERATE_SPEED_DURATION));
                segment95Animator.setInterpolator(new DecelerateInterpolator());
                segment95Animator.addUpdateListener(mAnimatorUpdateListener);
            }

            ObjectAnimator mObjectAnimator = ObjectAnimator.ofFloat(
                    this, "alpha", 1f, 0f);
            mObjectAnimator.setDuration(DO_END_ALPHA_DURATION);
            ValueAnimator mValueAnimatorEnd = ValueAnimator.ofFloat(95f, 100f);
            mValueAnimatorEnd.setDuration(DO_END_PROGRESS_DURATION);
            mValueAnimatorEnd.addUpdateListener(mAnimatorUpdateListener);

            AnimatorSet mAnimatorSet = new AnimatorSet();
            mAnimatorSet.playTogether(mObjectAnimator, mValueAnimatorEnd);

            if (segment95Animator != null) {
                AnimatorSet mAnimatorSet1 = new AnimatorSet();
                mAnimatorSet1.play(mAnimatorSet).after(segment95Animator);
                mAnimatorSet = mAnimatorSet1;
            }
            mAnimatorSet.addListener(mAnimatorListener);
            mAnimatorSet.start();
            mAnimator = mAnimatorSet;
        }

        TAG = STARTED;
    }

    /**
     * 创建属性动画监听进度变化的对象
     */
    private ValueAnimator.AnimatorUpdateListener mAnimatorUpdateListener =
            new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            float t = (float) animation.getAnimatedValue();
            //更改进度
            WebProgress.this.mCurrentProgress = t;
            //调用invalidate方法刷新UI
            WebProgress.this.invalidate();
        }
    };

    /**
     * 创建属性动画监听的对象
     */
    private AnimatorListenerAdapter mAnimatorListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            doEnd();
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            super.onAnimationCancel(animation);
        }

        @Override
        public void onAnimationStart(Animator animation) {
            super.onAnimationStart(animation);
        }
    };

    private void doEnd() {
        if (TAG == FINISH && ((int)mCurrentProgress) == 100f) {
            setVisibility(GONE);
            mCurrentProgress = 0f;
            this.setAlpha(1f);
        }
        TAG = UN_START;
    }

    public void reset() {
        mCurrentProgress = 0;
        if (mAnimator != null && mAnimator.isStarted()) {
            mAnimator.cancel();
        }
    }

    /**
     * 设置进度
     * @param progress                          进度值
     */
    public void setProgress(int progress) {
        // fix 同时返回两个 100，产生两次进度条的问题；
        if (TAG == UN_START && progress == 100f) {
            setVisibility(View.GONE);
            return;
        }
        if (getVisibility() == View.GONE) {
            setVisibility(View.VISIBLE);
        }
        if (progress < 95f) {
            return;
        }
        if (TAG != FINISH) {
            startAnim(true);
        }
    }

    public LayoutParams offerLayoutParams() {
        return new LayoutParams(mTargetWidth, mTargetHeight);
    }

    private int dip2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 显示进度条
     */
    public void show() {
        isShow = true;
        setVisibility(View.VISIBLE);
        mCurrentProgress = 0f;
        startAnim(false);
    }

    /**
     * 进度完成后消失
     */
    public void hide() {
        setWebProgress(100);
    }

    /**
     * 为单独处理WebView进度条
     */
    public void setWebProgress(int newProgress) {
        if (newProgress >= 0 && newProgress < 95) {
            if (!isShow) {
                show();
            } else {
                setProgress(newProgress);
            }
        } else {
            setProgress(newProgress);
            setFinish();
        }
    }
}


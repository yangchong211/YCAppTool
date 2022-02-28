package com.yc.photo.internal.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import androidx.core.content.res.ResourcesCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.yc.photo.R;


public class CheckView extends View {

    public static final int UNCHECKED = Integer.MIN_VALUE;
    /**
     * 笔划宽度
     */
    private static final float STROKE_WIDTH = 1.0f; // dp
    /**
     * 影子宽度
     */
    private static final float SHADOW_WIDTH = 6.0f; // dp
    /**
     * 大小
     */
    private static final int SIZE = 20; // dp
    /**
     * 半径
     */
    private static final float STROKE_RADIUS = 9.5f; // dp
    /**
     * 齿轮半径
     */
    private static final float BG_RADIUS = 9.0f; // dp
    /**
     * 内容大小
     */
    private static final int CONTENT_SIZE = 12; // dp
    private boolean mCountable;
    private boolean mChecked;
    private int mCheckedNum;
    private Paint mStrokePaint;
    private Paint mBackgroundPaint;
    private TextPaint mTextPaint;
    private Paint mShadowPaint;
    private Drawable mCheckDrawable;
    private float mDensity;
    private Rect mCheckRect;
    private boolean mEnabled = true;

    public CheckView(Context context) {
        super(context);
        init(context);
    }

    public CheckView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CheckView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // fixed size 48dp x 48dp
        int sizeSpec = MeasureSpec.makeMeasureSpec((int) (SIZE * mDensity), MeasureSpec.EXACTLY);
        super.onMeasure(sizeSpec, sizeSpec);
    }

    private void init(Context context) {
        mDensity = context.getResources().getDisplayMetrics().density;

        mStrokePaint = new Paint();
        mStrokePaint.setAntiAlias(true);
        mStrokePaint.setStyle(Paint.Style.STROKE);
        mStrokePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        mStrokePaint.setStrokeWidth(STROKE_WIDTH * mDensity);
        TypedArray ta = getContext().getTheme().obtainStyledAttributes(new int[]{R.attr.item_checkCircle_borderColor});
        int defaultColor = ResourcesCompat.getColor(
                getResources(), R.color.zhihu_item_checkCircle_borderColor,
                getContext().getTheme());
        int color = ta.getColor(0, defaultColor);
        ta.recycle();
        mStrokePaint.setColor(color);

        mCheckDrawable = ResourcesCompat.getDrawable(context.getResources(),
                R.drawable.ic_check_white_18dp, context.getTheme());
    }

    public void setChecked(boolean checked) {
        if (mCountable) {
            throw new IllegalStateException("CheckView is countable, call setCheckedNum() instead.");
        }
        mChecked = checked;
        invalidate();
    }

    public void setCountable(boolean countable) {
        mCountable = countable;
    }

    public void setCheckedNum(int checkedNum) {
        if (!mCountable) {
            throw new IllegalStateException("CheckView is not countable, call setChecked() instead.");
        }
        if (checkedNum != UNCHECKED && checkedNum <= 0) {
            throw new IllegalArgumentException("checked num can't be negative.");
        }
        mCheckedNum = checkedNum;
        invalidate();
    }

    public void setEnabled(boolean enabled) {
        if (mEnabled != enabled) {
            mEnabled = enabled;
            invalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // draw outer and inner shadow
        initShadowPaint();
        canvas.drawCircle((float) SIZE * mDensity / 2, (float) SIZE * mDensity / 2,
                (STROKE_RADIUS + STROKE_WIDTH / 2 + SHADOW_WIDTH) * mDensity, mShadowPaint);

        // draw white stroke
        canvas.drawCircle((float) SIZE * mDensity / 2, (float) SIZE * mDensity / 2,
                STROKE_RADIUS * mDensity, mStrokePaint);

        // draw content
        if (mCountable) {
            if (mCheckedNum != UNCHECKED) {
                initBackgroundPaint();
                canvas.drawCircle((float) SIZE * mDensity / 2, (float) SIZE * mDensity / 2,
                        BG_RADIUS * mDensity, mBackgroundPaint);
                initTextPaint();
                String text = String.valueOf(mCheckedNum);
                int baseX = (int) (canvas.getWidth() - mTextPaint.measureText(text)) / 2;
                int baseY = (int) (canvas.getHeight() - mTextPaint.descent() - mTextPaint.ascent()) / 2;
                canvas.drawText(text, baseX, baseY, mTextPaint);
            }
        } else {
            if (mChecked) {
                initBackgroundPaint();
                canvas.drawCircle((float) SIZE * mDensity / 2, (float) SIZE * mDensity / 2,
                        BG_RADIUS * mDensity, mBackgroundPaint);

                mCheckDrawable.setBounds(getCheckRect());
                mCheckDrawable.draw(canvas);
            }
        }

        // enable hint
        setAlpha(mEnabled ? 1.0f : 0.5f);
    }

    private void initShadowPaint() {
        if (mShadowPaint == null) {
            mShadowPaint = new Paint();
            mShadowPaint.setAntiAlias(true);
            // all in dp
            float outerRadius = STROKE_RADIUS + STROKE_WIDTH / 2;
            float innerRadius = outerRadius - STROKE_WIDTH;
            float gradientRadius = outerRadius + SHADOW_WIDTH;
            float stop0 = (innerRadius - SHADOW_WIDTH) / gradientRadius;
            float stop1 = innerRadius / gradientRadius;
            float stop2 = outerRadius / gradientRadius;
            float stop3 = 1.0f;
            mShadowPaint.setShader(new RadialGradient((float) SIZE * mDensity / 2,
                            (float) SIZE * mDensity / 2,
                            gradientRadius * mDensity,
                            new int[]{Color.parseColor("#00000000"), Color.parseColor("#0D000000"),
                                    Color.parseColor("#0D000000"), Color.parseColor("#00000000")},
                            new float[]{stop0, stop1, stop2, stop3},
                            Shader.TileMode.CLAMP));
        }
    }

    private void initBackgroundPaint() {
        if (mBackgroundPaint == null) {
            mBackgroundPaint = new Paint();
            mBackgroundPaint.setAntiAlias(true);
            mBackgroundPaint.setStyle(Paint.Style.FILL);
            mBackgroundPaint.setColor(getContext().getResources().getColor(R.color.select_primary));
        }
    }

    private void initTextPaint() {
        if (mTextPaint == null) {
            mTextPaint = new TextPaint();
            mTextPaint.setAntiAlias(true);
            mTextPaint.setColor(Color.WHITE);
            mTextPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            mTextPaint.setTextSize(12.0f * mDensity);
        }
    }

    // rect for drawing checked number or mark
    private Rect getCheckRect() {
        if (mCheckRect == null) {
            int rectPadding = (int) (SIZE * mDensity / 2 - CONTENT_SIZE * mDensity / 2);
            mCheckRect = new Rect(rectPadding, rectPadding,
                    (int) (SIZE * mDensity - rectPadding), (int) (SIZE * mDensity - rectPadding));
        }

        return mCheckRect;
    }
}

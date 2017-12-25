
package com.ns.yc.lifehelper.ui.other.weather.weight;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ns.yc.lifehelper.R;


public class IndicatorView extends LinearLayout {

    private Context context;
    private Paint paint;
    private int markerId;
    private Bitmap marker = null;

    private int indicatorValue = 0;// 默认AQI值
    private int textSize = 6;// 默认文字大小
    private int intervalValue = 1;// TextView之间的间隔大小，单位dp
    private int textColorId = R.color.colorWhite;// 默认文字颜色
    private int textColor;
    private int indicatorStringsResourceId = R.array.indicator_strings;
    private int indicatorColorsResourceId = R.array.indicator_colors;

    private int indicatorViewWidth;// IndicatorView宽度

    private int paddingTopInXML;

    private String[] indicatorStrings;
    int[] indicatorColorIds;

    public IndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    /**
     * 控件初始化，构造函数调用
     */
    private void init(Context context, AttributeSet attrs) {
        this.context = context;
        this.setOrientation(LinearLayout.HORIZONTAL);
        //开启绘图缓存，提高绘图效率
        this.setDrawingCacheEnabled(true);

        initPaint();
        initAttrs(attrs);
        fillViewToParent(context);

        this.setWillNotDraw(false);// 确保onDraw()被调用

        this.paddingTopInXML = this.getPaddingTop();
        this.setPadding(this.getPaddingLeft() + this.marker.getWidth() / 2,
                this.getPaddingTop() + this.marker.getHeight(),
                this.getPaddingRight() + this.marker.getWidth() / 2,
                this.getPaddingBottom());
    }

    /**
     * 初始化paint
     */
    private void initPaint() {
        this.paint = new Paint();
        // 设置是否使用抗锯齿功能，会消耗较大资源，绘制图形速度会变慢。
        this.paint.setAntiAlias(true);
        // 设定是否使用图像抖动处理，会使绘制出来的图片颜色更加平滑和饱满，图像更加清晰
        this.paint.setDither(true);
    }

    /**
     * 获取自定义attrs
     */
    private void initAttrs(AttributeSet attrs) {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        this.textSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, textSize, dm);
        this.intervalValue = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, intervalValue, dm);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.IndicatorView);
        this.markerId = typedArray.getResourceId(R.styleable.IndicatorView_marker, R.drawable.ic_vector_indicator_down);
        this.marker = drawableToBitmap(createVectorDrawable(markerId, R.color.indicator_color_1));
        this.indicatorValue = typedArray.getInt(R.styleable.IndicatorView_indicatorValue, indicatorValue);
        this.textSize = typedArray.getDimensionPixelSize(R.styleable.IndicatorView_inTextSize, textSize);
        this.intervalValue = typedArray.getDimensionPixelSize(R.styleable.IndicatorView_intervalSize, intervalValue);
        this.textColor = typedArray.getColor(R.styleable.IndicatorView_inTextColor, getResources().getColor(textColorId));
        this.indicatorStringsResourceId = typedArray.getInt(R.styleable.IndicatorView_indicatorStrings, indicatorStringsResourceId);
        this.indicatorColorsResourceId = typedArray.getInt(R.styleable.IndicatorView_indicatorColors, indicatorColorsResourceId);
        typedArray.recycle();
    }

    /**
     * 向父容器中填充View
     */
    private void fillViewToParent(Context context) {
        indicatorStrings = context.getResources().getStringArray(indicatorStringsResourceId);
        indicatorColorIds = context.getResources().getIntArray(indicatorColorsResourceId);
        if (indicatorStrings.length != indicatorColorIds.length) {
            throw new IllegalArgumentException("qualities和aqiColors的数组长度不一致！");
        }
        for (int i = 0; i < indicatorStrings.length; i++) {
            addTextView(context, indicatorStrings[i], indicatorColorIds[i]);
            if (i != (indicatorStrings.length - 1)) {
                addBlankView(context);
            }
        }
    }

    /**
     * 向父容器中添加TextView
     *
     * @param text  TextView显示文字
     * @param color TextView的背景颜色，如："#FADBCC"
     */
    private void addTextView(Context context, String text, int color) {
        TextView textView = new TextView(context);
        textView.setBackgroundColor(color);
        textView.setText(text);
        textView.setTextColor(textColor);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        textView.setSingleLine();
        textView.setGravity(Gravity.CENTER);
        textView.setLayoutParams(new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1.0F));
        this.addView(textView);
    }

    /**
     * 向父容器中添加空白View
     */
    private void addBlankView(Context context) {
        View transparentView = new View(context);
        transparentView.setBackgroundColor(Color.TRANSPARENT);
        transparentView.setLayoutParams(new LayoutParams(intervalValue, LayoutParams.WRAP_CONTENT));
        this.addView(transparentView);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        this.indicatorViewWidth = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int indicatorViewHeight = MeasureSpec.getSize(heightMeasureSpec);

        int desiredWidth = indicatorViewWidth + getPaddingLeft() + getPaddingRight();
        int desiredHeight = this.getChildAt(0).getMeasuredHeight() + getPaddingTop() + getPaddingBottom();

        //测量宽度
        switch (widthMode) {
            case MeasureSpec.EXACTLY:
                break;
            case MeasureSpec.AT_MOST:
                indicatorViewWidth = Math.min(desiredWidth, indicatorViewWidth);
                break;
            case MeasureSpec.UNSPECIFIED:
                indicatorViewWidth = desiredWidth;
                break;
        }

        //测量高度
        switch (heightMode) {
            case MeasureSpec.EXACTLY:
                break;
            case MeasureSpec.AT_MOST:
                indicatorViewHeight = Math.min(desiredHeight, indicatorViewHeight);
                break;
            case MeasureSpec.UNSPECIFIED:
                indicatorViewHeight = desiredHeight;
                break;
        }
        setMeasuredDimension(indicatorViewWidth, indicatorViewHeight);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        drawMarkView(canvas);
    }

    /**
     * 用于绘制指示器图标
     */
    private void drawMarkView(Canvas canvas) {

        int width = this.indicatorViewWidth - this.getPaddingLeft() - this.getPaddingRight() - intervalValue * 5;

        int left = this.getPaddingLeft();
        if (indicatorValue <= 50) {
            left += indicatorValue * (width * 4 / 6 / 200);
        } else if (indicatorValue > 50 && indicatorValue <= 100) {
            left += indicatorValue * (width * 4 / 6 / 200) + intervalValue;
        } else if (indicatorValue > 100 && indicatorValue <= 150) {
            left += indicatorValue * (width * 4 / 6 / 200) + intervalValue * 2;
        } else if (indicatorValue > 150 && indicatorValue <= 200) {
            left += indicatorValue * (width * 4 / 6 / 200) + intervalValue * 3;
        } else if (indicatorValue > 200 && indicatorValue <= 300) {
            left += (width * 4 / 6) + (indicatorValue - 200) * width / 6 / 100 + intervalValue * 4;
        } else {
            left += (width * 5 / 6) + (indicatorValue - 300) * width / 6 / 200 + intervalValue * 5;
        }
        canvas.drawBitmap(marker, left - marker.getWidth() / 2 - 2, this.paddingTopInXML, paint);
    }


    private IndicatorValueChangeListener indicatorValueChangeListener;
    public void setIndicatorValueChangeListener(IndicatorValueChangeListener indicatorValueChangeListener) {
        this.indicatorValueChangeListener = indicatorValueChangeListener;
    }

    public void setIndicatorValue(int indicatorValue) {
        if (indicatorValue < 0)
            throw new IllegalStateException("参数indicatorValue必须大于0");
        this.indicatorValue = indicatorValue;
        if (indicatorValueChangeListener != null) {
            String stateDescription;
            int indicatorTextColor;
            if (indicatorValue <= 50) {
                stateDescription = indicatorStrings[0];
                indicatorTextColor = indicatorColorIds[0];
            } else if (indicatorValue > 50 && indicatorValue <= 100) {
                stateDescription = indicatorStrings[1];
                indicatorTextColor = indicatorColorIds[1];
            } else if (indicatorValue > 100 && indicatorValue <= 150) {
                stateDescription = indicatorStrings[2];
                indicatorTextColor = indicatorColorIds[2];
            } else if (indicatorValue > 150 && indicatorValue <= 200) {
                stateDescription = indicatorStrings[3];
                indicatorTextColor = indicatorColorIds[3];
            } else if (indicatorValue > 200 && indicatorValue <= 300) {
                stateDescription = indicatorStrings[4];
                indicatorTextColor = indicatorColorIds[4];
            } else {
                stateDescription = indicatorStrings[5];
                indicatorTextColor = indicatorColorIds[5];
            }
            marker.recycle();
            marker = drawableToBitmap(createVectorDrawable(markerId, indicatorTextColor));
            indicatorValueChangeListener.onChange(this.indicatorValue,
                    stateDescription, indicatorTextColor);
        }
        invalidate();
    }


    private Drawable createVectorDrawable(int drawableId, int color) {
        VectorDrawableCompat vectorDrawableCompat = VectorDrawableCompat.create(
                context.getResources(), drawableId, context.getTheme());
        assert vectorDrawableCompat != null;
        DrawableCompat.setTint(vectorDrawableCompat, color);
        DrawableCompat.setTintMode(vectorDrawableCompat, PorterDuff.Mode.SRC_IN);
        return vectorDrawableCompat;
    }


    private Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap;
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }
        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
            // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

}
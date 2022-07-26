package com.yc.shadow;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;

import java.util.Arrays;
import java.util.List;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/7/20
 *     desc  : 可以自定义背景阴影效果的控件
 *     revise:
 *     GitHub: https://github.com/yangchong211/YCWidgetLib
 * </pre>
 */
public class ShadowView extends ViewGroup {

    private static final int DEFAULT_CHILD_GRAVITY = Gravity.TOP | Gravity.START;
    private static final int SIZE_UNSET = -1;
    private static final int SIZE_DEFAULT = 0;

    private Drawable foregroundDrawable;
    private Rect selfBounds = new Rect();
    private Rect overlayBounds = new Rect();
    private int foregroundDrawGravity = Gravity.FILL;
    private boolean foregroundDrawBoundsChanged = false;

    private Paint bgPaint = new Paint();
    private int shadowColor;
    private int foregroundColor;
    private int backgroundColor;
    private float shadowRadius;
    private float shadowDx;
    private float shadowDy;
    private float cornerRadiusTL;
    private float cornerRadiusTR;
    private float cornerRadiusBL;
    private float cornerRadiusBR;
    private int shadowMarginTop;
    private int shadowMarginLeft;
    private int shadowMarginRight;
    private int shadowMarginBottom;

    public ShadowView(Context context) {
        this(context, null);
    }

    public ShadowView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShadowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
        initPaint();
    }

    /**
     * 初始化自定义的attr属性
     *
     * @param context      context
     * @param attrs        attrs
     * @param defStyleAttr defStyleAttr
     */
    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ShadowView,
                defStyleAttr, 0);
        shadowColor = a.getColor(R.styleable.ShadowView_shadowColor
                , ContextCompat.getColor(context, R.color.shadow_view_default_shadow_color));
        foregroundColor = a.getColor(R.styleable.ShadowView_foregroundColor
                , ContextCompat.getColor(context, R.color.shadow_view_foreground_color_dark));
        backgroundColor = a.getColor(R.styleable.ShadowView_backgroundColor, Color.WHITE);
        shadowDx = a.getFloat(R.styleable.ShadowView_shadowDx, 0f);
        shadowDy = a.getFloat(R.styleable.ShadowView_shadowDy, 1f);
        shadowRadius = a.getDimensionPixelSize(R.styleable.ShadowView_shadowRadius, SIZE_DEFAULT);
        Drawable drawable = a.getDrawable(R.styleable.ShadowView_android_foreground);
        if (drawable != null) {
            setForeground(drawable);
        }
        int shadowMargin = a.getDimensionPixelSize(R.styleable.ShadowView_shadowMargin, SIZE_UNSET);
        if (shadowMargin >= 0) {
            shadowMarginTop = shadowMargin;
            shadowMarginLeft = shadowMargin;
            shadowMarginRight = shadowMargin;
            shadowMarginBottom = shadowMargin;
        } else {
            shadowMarginTop = a.getDimensionPixelSize(R.styleable.ShadowView_shadowMarginTop, SIZE_DEFAULT);
            shadowMarginLeft = a.getDimensionPixelSize(R.styleable.ShadowView_shadowMarginLeft, SIZE_DEFAULT);
            shadowMarginRight = a.getDimensionPixelSize(R.styleable.ShadowView_shadowMarginRight, SIZE_DEFAULT);
            shadowMarginBottom = a.getDimensionPixelSize(R.styleable.ShadowView_shadowMarginBottom, SIZE_DEFAULT);
        }

        float cornerRadius = a.getDimensionPixelSize(R.styleable.ShadowView_cornerRadius, SIZE_UNSET);
        if (cornerRadius >= 0) {
            cornerRadiusTL = cornerRadius;
            cornerRadiusTR = cornerRadius;
            cornerRadiusBL = cornerRadius;
            cornerRadiusBR = cornerRadius;
        } else {
            cornerRadiusTL = a.getDimensionPixelSize(R.styleable.ShadowView_cornerRadiusTL, SIZE_DEFAULT);
            cornerRadiusTR = a.getDimensionPixelSize(R.styleable.ShadowView_cornerRadiusTR, SIZE_DEFAULT);
            cornerRadiusBL = a.getDimensionPixelSize(R.styleable.ShadowView_cornerRadiusBL, SIZE_DEFAULT);
            cornerRadiusBR = a.getDimensionPixelSize(R.styleable.ShadowView_cornerRadiusBR, SIZE_DEFAULT);
        }
        a.recycle();
    }


    /**
     * 初始化画笔操作
     */
    private void initPaint() {
        bgPaint.setColor(backgroundColor);
        bgPaint.setAntiAlias(true);
        bgPaint.setStyle(Paint.Style.FILL);
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        setWillNotDraw(false);
        ViewCompat.setBackground(this, null);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int maxHeight = 0;
        int maxWidth = 0;
        int childState = 0;

        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec),
                getDefaultSize(0, heightMeasureSpec));
        boolean shadowMeasureWidthMatchParent = layoutParams.width ==
                ViewGroup.LayoutParams.MATCH_PARENT;
        boolean shadowMeasureHeightMatchParent = layoutParams.height ==
                ViewGroup.LayoutParams.MATCH_PARENT;
        int widthSpec = widthMeasureSpec;
        if (shadowMeasureWidthMatchParent) {
            int childWidthSize = getMeasuredWidth() - shadowMarginRight - shadowMarginLeft;
            widthSpec = MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY);
        }
        int heightSpec = heightMeasureSpec;
        if (shadowMeasureHeightMatchParent) {
            int childHeightSize = getMeasuredHeight() - shadowMarginTop - shadowMarginBottom;
            heightSpec = MeasureSpec.makeMeasureSpec(childHeightSize, MeasureSpec.EXACTLY);
        }
        View child = getChildAt(0);
        if (child.getVisibility() != View.GONE) {
            measureChildWithMargins(child, widthSpec, 0, heightSpec, 0);
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            if (shadowMeasureWidthMatchParent) {
                maxWidth = Math.max(maxWidth,
                        child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin);
            } else {
                maxWidth = Math.max(maxWidth, child.getMeasuredWidth() +
                        shadowMarginLeft + shadowMarginRight + lp.leftMargin + lp.rightMargin);
            }
            if (shadowMeasureHeightMatchParent) {
                maxHeight = Math.max(maxHeight, child.getMeasuredHeight() +
                        lp.topMargin + lp.bottomMargin);
            } else {
                maxHeight = Math.max(maxHeight, child.getMeasuredHeight() +
                        shadowMarginTop + shadowMarginBottom + lp.topMargin + lp.bottomMargin);
            }
            childState = View.combineMeasuredStates(childState, child.getMeasuredState());
        }
        maxWidth += getPaddingLeft() + getPaddingRight();
        maxHeight += getPaddingTop() + getPaddingBottom();
        maxHeight = Math.max(maxHeight, getSuggestedMinimumHeight());
        maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth());
        Drawable drawable = getForeground();
        if (drawable != null) {
            maxHeight = Math.max(maxHeight, drawable.getMinimumHeight());
            maxWidth = Math.max(maxWidth, drawable.getMinimumWidth());
        }
        setMeasuredDimension(View.resolveSizeAndState(maxWidth, shadowMeasureWidthMatchParent ?
                        widthMeasureSpec : widthSpec, childState),
                View.resolveSizeAndState(maxHeight, shadowMeasureHeightMatchParent ?
                                heightMeasureSpec : heightSpec,
                        childState << View.MEASURED_HEIGHT_STATE_SHIFT));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        layoutChildren(left, top, right, bottom);
        if (changed) {
            foregroundDrawBoundsChanged = changed;
        }
    }

    @SuppressLint("RtlHardcoded")
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void layoutChildren(int left, int top, int right, int bottom) {
        int count = getChildCount();

        int parentLeft = getPaddingLeftWithForeground();
        int parentRight = right - left - getPaddingRightWithForeground();

        int parentTop = getPaddingTopWithForeground();
        int parentBottom = bottom - top - getPaddingBottomWithForeground();

        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != View.GONE) {
                LayoutParams lp = (LayoutParams) child.getLayoutParams();

                int width = child.getMeasuredWidth();
                int height = child.getMeasuredHeight();

                int childLeft = 0;
                int childTop;

                int gravity = lp.gravity;
                if (gravity == -1) {
                    gravity = DEFAULT_CHILD_GRAVITY;
                }

                int layoutDirection = getLayoutDirection();
                int absoluteGravity = Gravity.getAbsoluteGravity(gravity, layoutDirection);
                int verticalGravity = gravity & Gravity.VERTICAL_GRAVITY_MASK;

                switch (absoluteGravity & Gravity.HORIZONTAL_GRAVITY_MASK) {
                    case Gravity.CENTER_HORIZONTAL:
                        childLeft = parentLeft + (parentRight - parentLeft - width) / 2 +
                                lp.leftMargin - lp.rightMargin + shadowMarginLeft - shadowMarginRight;
                        break;
                    case Gravity.RIGHT:
                        childLeft = parentRight - width - lp.rightMargin - shadowMarginRight;
                        break;
                    case Gravity.LEFT:
                        childLeft = parentLeft + lp.leftMargin + shadowMarginLeft;
                        break;
                    default:
                        childLeft = parentLeft + lp.leftMargin + shadowMarginLeft;
                        break;
                }
                switch (verticalGravity) {
                    case Gravity.TOP:
                        childTop = parentTop + lp.topMargin + shadowMarginTop;
                        break;
                    case Gravity.CENTER_VERTICAL:
                        childTop = parentTop + (parentBottom - parentTop - height) / 2 +
                                lp.topMargin - lp.bottomMargin + shadowMarginTop - shadowMarginBottom;
                        break;
                    case Gravity.BOTTOM:
                        childTop = parentBottom - height - lp.bottomMargin - shadowMarginBottom;
                        break;
                    default:
                        childTop = parentTop + lp.topMargin + shadowMarginTop;
                        break;
                }
                child.layout(childLeft, childTop, childLeft + width, childTop + height);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (canvas != null) {
            int w = getMeasuredWidth();
            int h = getMeasuredHeight();
            Path path = ShapeUtils.roundedRect(shadowMarginLeft, shadowMarginTop,
                    (w - shadowMarginRight), (h - shadowMarginBottom)
                    , cornerRadiusTL
                    , cornerRadiusTR
                    , cornerRadiusBR
                    , cornerRadiusBL);
            canvas.drawPath(path, bgPaint);
            canvas.clipPath(path);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (canvas != null) {
            canvas.save();
            int w = getMeasuredWidth();
            int h = getMeasuredHeight();
            Path path = ShapeUtils.roundedRect(shadowMarginLeft, shadowMarginTop,
                    (w - shadowMarginRight)
                    , (h - shadowMarginBottom)
                    , cornerRadiusTL
                    , cornerRadiusTR
                    , cornerRadiusBR
                    , cornerRadiusBL);
            canvas.clipPath(path);
            drawForeground(canvas);
            canvas.restore();
        }
    }

    private void updatePaintShadow() {
        updatePaintShadow(shadowRadius, shadowDx, shadowDy, shadowColor);
    }

    private void updatePaintShadow(float radius, float dx, float dy, int color) {
        bgPaint.setShadowLayer(radius, dx, dy, color);
        invalidate();
    }

    private float getShadowMarginMax() {
        float max = 0f;
        List<Integer> margins = Arrays.asList(shadowMarginLeft, shadowMarginTop,
                shadowMarginRight, shadowMarginBottom);
        for (Integer value : margins) {
            max = Math.max(max, value);
        }
        return max;
    }

    private void drawForeground(Canvas canvas) {
        if (foregroundDrawable != null) {
            if (foregroundDrawBoundsChanged) {
                foregroundDrawBoundsChanged = false;
                int w = getRight() - getLeft();
                int h = getBottom() - getTop();
                boolean foregroundDrawInPadding = true;
                if (foregroundDrawInPadding) {
                    selfBounds.set(0, 0, w, h);
                } else {
                    selfBounds.set(getPaddingLeft(), getPaddingTop(),
                            w - getPaddingRight(), h - getPaddingBottom());
                }
                Gravity.apply(foregroundDrawGravity, foregroundDrawable.getIntrinsicWidth(),
                        foregroundDrawable.getIntrinsicHeight(), selfBounds, overlayBounds);
                foregroundDrawable.setBounds(overlayBounds);
            }
            foregroundDrawable.draw(canvas);
        }

    }

    @Override
    public Drawable getForeground() {
        return foregroundDrawable;
    }

    @Override
    public void setForeground(Drawable foreground) {
        if (foregroundDrawable != null) {
            foregroundDrawable.setCallback(null);
            unscheduleDrawable(foregroundDrawable);
        }
        foregroundDrawable = foreground;

        updateForegroundColor();

        if (foreground != null) {
            setWillNotDraw(false);
            foreground.setCallback(this);
            if (foreground.isStateful()) {
                foreground.setState(getDrawableState());
            }
            if (foregroundDrawGravity == Gravity.FILL) {
                Rect padding = new Rect();
                foreground.getPadding(padding);
            }
        }
        requestLayout();
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        foregroundDrawBoundsChanged = true;
    }

    @Override
    public int getForegroundGravity() {
        return foregroundDrawGravity;
    }

    @Override
    public void setForegroundGravity(int foregroundGravity) {
        if (foregroundDrawGravity != foregroundGravity) {
            if ((foregroundGravity & Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK) == 0) {
                foregroundGravity = foregroundGravity | Gravity.START;
            }
            if ((foregroundGravity & Gravity.VERTICAL_GRAVITY_MASK) == 0) {
                foregroundGravity = foregroundGravity | Gravity.TOP;
            }
            foregroundDrawGravity = foregroundGravity;
            if (foregroundDrawGravity == Gravity.FILL && foregroundDrawable != null) {
                Rect padding = new Rect();
                foregroundDrawable.getPadding(padding);
            }
            requestLayout();
        }
    }

    @Override
    protected boolean verifyDrawable(@NonNull Drawable who) {
        return super.verifyDrawable(who) || who == foregroundDrawable;
    }

    @Override
    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        if (foregroundDrawable != null) {
            foregroundDrawable.jumpToCurrentState();
        }
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (foregroundDrawable != null) {
            if (foregroundDrawable.isStateful()) {
                foregroundDrawable.setState(getDrawableState());
            }
        }
    }

    private void updateForegroundColor() {
        if (foregroundDrawable != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (foregroundDrawable instanceof RippleDrawable) {
                    ((RippleDrawable) foregroundDrawable).setColor(ColorStateList.valueOf(foregroundColor));
                }
            } else {
                foregroundDrawable.setColorFilter(foregroundColor, PorterDuff.Mode.SRC_ATOP);
            }
        }
    }

    @Override
    public void drawableHotspotChanged(float x, float y) {
        super.drawableHotspotChanged(x, y);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (foregroundDrawable != null) {
                foregroundDrawable.setHotspot(x, y);
            }
        }
    }

    /**
     * 获取阴影的颜色
     *
     * @return int
     */
    public int getShadowColor() {
        return shadowColor;
    }

    /**
     * 设置阴影的颜色
     *
     * @param shadowColor 颜色，这里需要用注解限定一下
     */
    public void setShadowColor(@ColorInt int shadowColor) {
        this.shadowColor = shadowColor;
        updatePaintShadow(shadowRadius, shadowDx, shadowDy, shadowColor);
    }

    /**
     * 获取foreground的颜色
     *
     * @return
     */
    public int getForegroundColor() {
        return foregroundColor;
    }

    /**
     * 设置foreground颜色
     *
     * @param foregroundColor 颜色
     */
    public void setForegroundColor(@ColorInt int foregroundColor) {
        this.foregroundColor = foregroundColor;
        updateForegroundColor();
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }


    @Override
    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        invalidate();
    }

    public float getShadowRadius() {
        if (shadowRadius > getShadowMarginMax() && getShadowMarginMax() != 0f) {
            return getShadowMarginMax();
        } else {
            return shadowRadius;
        }
    }

    public void setShadowRadius(float shadowRadius) {
        if (shadowRadius > getShadowMarginMax() && getShadowMarginMax() != 0f) {
            shadowRadius = getShadowMarginMax();
        }
        this.shadowRadius = shadowRadius;
        updatePaintShadow(shadowRadius, shadowDx, shadowDy, shadowColor);
    }

    public float getShadowDx() {
        return shadowDx;
    }

    public void setShadowDx(float shadowDx) {
        this.shadowDx = shadowDx;
        updatePaintShadow(shadowRadius, shadowDx, shadowDy, shadowColor);
    }

    public float getShadowDy() {
        return shadowDy;
    }

    public void setShadowDy(float shadowDy) {
        this.shadowDy = shadowDy;
        updatePaintShadow(shadowRadius, shadowDx, shadowDy, shadowColor);
    }

    public int getShadowMarginTop() {
        return shadowMarginTop;
    }

    public void setShadowMarginTop(int shadowMarginTop) {
        this.shadowMarginTop = shadowMarginTop;
        updatePaintShadow();
    }

    public int getShadowMarginLeft() {
        return shadowMarginLeft;
    }

    public void setShadowMarginLeft(int shadowMarginLeft) {
        this.shadowMarginLeft = shadowMarginLeft;
        updatePaintShadow();
    }

    public int getShadowMarginRight() {
        return shadowMarginRight;
    }


    public void setShadowMarginRight(int shadowMarginRight) {
        this.shadowMarginRight = shadowMarginRight;
        updatePaintShadow();
    }


    public int getShadowMarginBottom() {
        return shadowMarginBottom;
    }


    public void setShadowMarginBottom(int shadowMarginBottom) {
        this.shadowMarginBottom = shadowMarginBottom;
        updatePaintShadow();
    }


    public void setShadowMargin(int left, int top, int right, int bottom) {
        this.shadowMarginLeft = left;
        this.shadowMarginTop = top;
        this.shadowMarginRight = right;
        this.shadowMarginBottom = bottom;
        requestLayout();
        invalidate();
    }


    public float getCornerRadiusTL() {
        return cornerRadiusTL;
    }

    public void setCornerRadiusTL(float cornerRadiusTL) {
        this.cornerRadiusTL = cornerRadiusTL;
        invalidate();
    }


    public float getCornerRadiusTR() {
        return cornerRadiusTR;
    }

    public void setCornerRadiusTR(float cornerRadiusTR) {
        this.cornerRadiusTR = cornerRadiusTR;
        invalidate();
    }


    public float getCornerRadiusBL() {
        return cornerRadiusBL;
    }

    public void setCornerRadiusBL(float cornerRadiusBL) {
        this.cornerRadiusBL = cornerRadiusBL;
        invalidate();
    }

    public float getCornerRadiusBR() {
        return cornerRadiusBR;
    }

    public void setCornerRadiusBR(float cornerRadiusBR) {
        this.cornerRadiusBR = cornerRadiusBR;
        invalidate();
    }


    public void setCornerRadius(float tl, float tr, float br, float bl) {
        this.cornerRadiusTL = tl;
        this.cornerRadiusTR = tr;
        this.cornerRadiusBR = br;
        this.cornerRadiusBL = bl;
        invalidate();
    }

    private int getPaddingLeftWithForeground() {
        return getPaddingLeft();
    }

    private int getPaddingRightWithForeground() {
        return getPaddingRight();
    }

    private int getPaddingTopWithForeground() {
        return getPaddingTop();
    }

    private int getPaddingBottomWithForeground() {
        return getPaddingBottom();
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    @Override
    public CharSequence getAccessibilityClassName() {
        return ShadowView.class.getName();
    }

    @Override
    public boolean shouldDelayChildPressedState() {
        return false;
    }

    public static class LayoutParams extends MarginLayoutParams {

        public static final int UNSPECIFIED_GRAVITY = -1;
        public int gravity = UNSPECIFIED_GRAVITY;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.ShadowView_Layout);
            gravity = a.getInt(R.styleable.ShadowView_Layout_layout_gravity, UNSPECIFIED_GRAVITY);
            a.recycle();
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(int width, int height, int gravity) {
            super(width, height);
            this.gravity = gravity;
        }

        public LayoutParams(@NonNull ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(@NonNull ViewGroup.MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(@NonNull LayoutParams source) {
            super(source);
            this.gravity = source.gravity;
        }

    }

}

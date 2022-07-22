package com.yc.appimageview;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.os.Build;
import android.util.AttributeSet;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     GitHub : https://github.com/yangchong211/YCCommonLib
 *     time  : 2018/11/9
 *     desc  : Paint.setXfermode 实现圆角
 *     revise:
 * </pre>
 */
public class RoundImageView extends AppCompatImageView {
    
    private final Context context;

    private boolean isCircle; // 是否显示为圆形，如果为圆形则设置的corner无效
    private boolean isCoverSrc; // border、inner_border是否覆盖图片
    private int borderWidth; // 边框宽度
    private int borderColor = Color.WHITE; // 边框颜色
    private int innerBorderWidth; // 内层边框宽度
    private int innerBorderColor = Color.WHITE; // 内层边框充色

    private int cornerRadius; // 统一设置圆角半径，优先级高于单独设置每个角的半径
    private int cornerTopLeftRadius; // 左上角圆角半径
    private int cornerTopRightRadius; // 右上角圆角半径
    private int cornerBottomLeftRadius; // 左下角圆角半径
    private int cornerBottomRightRadius; // 右下角圆角半径
    private int maskColor; // 遮罩颜色
    private final Xfermode xfermode;

    private int width;
    private int height;
    private float radius;

    private final float[] borderRadii;
    private final float[] srcRadii;

    private RectF srcRectF; // 图片占的矩形区域
    private final RectF borderRectF; // 边框的矩形区域

    private final Paint paint;
    private final Path path; // 用来裁剪图片的ptah
    private Path srcPath; // 图片区域大小的path

    public RoundImageView(Context context) {
        this(context, null);
    }

    public RoundImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initAttr(context,attrs);

        borderRadii = new float[8];
        srcRadii = new float[8];
        borderRectF = new RectF();
        srcRectF = new RectF();
        paint = new Paint();
        path = new Path();

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O_MR1) {
            xfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
        } else {
            xfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);
            srcPath = new Path();
        }

        //计算RectF的圆角半径
        calculateRadii();
        //目前圆角矩形情况下不支持inner_border，需要将其置0
        clearInnerBorderWidth();
    }

    private void initAttr(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs,
                R.styleable.RoundImageView, 0, 0);
        for (int i = 0; i < ta.getIndexCount(); i++) {
            int attr = ta.getIndex(i);
            if (attr == R.styleable.RoundImageView_is_cover_src) {
                isCoverSrc = ta.getBoolean(attr, isCoverSrc);
            } else if (attr == R.styleable.RoundImageView_is_circle) {
                isCircle = ta.getBoolean(attr, isCircle);
            } else if (attr == R.styleable.RoundImageView_border_width) {
                borderWidth = ta.getDimensionPixelSize(attr, borderWidth);
            } else if (attr == R.styleable.RoundImageView_border_color) {
                borderColor = ta.getColor(attr, borderColor);
            } else if (attr == R.styleable.RoundImageView_inner_border_width) {
                innerBorderWidth = ta.getDimensionPixelSize(attr, innerBorderWidth);
            } else if (attr == R.styleable.RoundImageView_inner_border_color) {
                innerBorderColor = ta.getColor(attr, innerBorderColor);
            } else if (attr == R.styleable.RoundImageView_corner_radius) {
                cornerRadius = ta.getDimensionPixelSize(attr, cornerRadius);
            } else if (attr == R.styleable.RoundImageView_corner_top_left_radius) {
                cornerTopLeftRadius = ta.getDimensionPixelSize(attr, cornerTopLeftRadius);
            } else if (attr == R.styleable.RoundImageView_corner_top_right_radius) {
                cornerTopRightRadius = ta.getDimensionPixelSize(attr, cornerTopRightRadius);
            } else if (attr == R.styleable.RoundImageView_corner_bottom_left_radius) {
                cornerBottomLeftRadius = ta.getDimensionPixelSize(attr, cornerBottomLeftRadius);
            } else if (attr == R.styleable.RoundImageView_corner_bottom_right_radius) {
                cornerBottomRightRadius = ta.getDimensionPixelSize(attr, cornerBottomRightRadius);
            } else if (attr == R.styleable.RoundImageView_mask_color) {
                maskColor = ta.getColor(attr, maskColor);
            }
        }
        ta.recycle();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;

        initBorderRectF();
        initSrcRectF();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 使用图形混合模式来显示指定区域的图片
        // 使用离屏缓存，新建一个srcRectF区域大小的图层
        canvas.saveLayer(srcRectF, null, Canvas.ALL_SAVE_FLAG);
        if (!isCoverSrc) {
            //绘制的边框会覆盖在图片上，如果边框太宽会导致图片的可见区域变小。
            //如何让边框不覆盖在图片上呢？可以在 Alpha 合成绘制前先将画布缩小一定比例，最后再绘制边框，这样问题就解决。
            float sx = 1.0f * (width - 2 * borderWidth - 2 * innerBorderWidth) / width;
            float sy = 1.0f * (height - 2 * borderWidth - 2 * innerBorderWidth) / height;
            // 缩小画布，使图片内容不被borders覆盖
            canvas.scale(sx, sy, width / 2.0f, height / 2.0f);
        }
        //绘制自己
        super.onDraw(canvas);

        paint.reset();
        path.reset();
        //绘制，给path添加一个圆角矩形或者圆形
        if (isCircle) {
            path.addCircle(width / 2.0f, height / 2.0f, radius, Path.Direction.CCW);
        } else {
            path.addRoundRect(srcRectF, srcRadii, Path.Direction.CCW);
        }

        paint.setAntiAlias(true);
        // 画笔为填充模式
        paint.setStyle(Paint.Style.FILL);
        // 设置混合模式
        paint.setXfermode(xfermode);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O_MR1) {
            canvas.drawPath(path, paint);
        } else {
            srcPath.addRect(srcRectF, Path.Direction.CCW);
            // 计算tempPath和path的差集
            srcPath.op(path, Path.Op.DIFFERENCE);
            canvas.drawPath(srcPath, paint);
        }
        // 清除Xfermode
        paint.setXfermode(null);

        // 绘制遮罩
        if (maskColor != 0) {
            //遮罩可以理解为一层带透明度的颜色，遮罩默认不绘制，当制定了遮罩颜色时才会绘制
            paint.setColor(maskColor);
            canvas.drawPath(path, paint);
        }
        // 恢复画布
        canvas.restore();
        // 绘制边框
        drawBorders(canvas);
    }

    private void drawBorders(Canvas canvas) {
        if (isCircle) {
            if (borderWidth > 0) {
                drawCircleBorder(canvas, borderWidth, borderColor, radius - borderWidth / 2.0f);
            }
            if (innerBorderWidth > 0) {
                drawCircleBorder(canvas, innerBorderWidth, innerBorderColor, radius - borderWidth - innerBorderWidth / 2.0f);
            }
        } else {
            if (borderWidth > 0) {
                drawRectFBorder(canvas, borderWidth, borderColor, borderRectF, borderRadii);
            }
        }
    }

    private void drawCircleBorder(Canvas canvas, int borderWidth, int borderColor, float radius) {
        initBorderPaint(borderWidth, borderColor);
        path.addCircle(width / 2.0f, height / 2.0f, radius, Path.Direction.CCW);
        canvas.drawPath(path, paint);
    }

    private void drawRectFBorder(Canvas canvas, int borderWidth, int borderColor, RectF rectF, float[] radii) {
        initBorderPaint(borderWidth, borderColor);
        path.addRoundRect(rectF, radii, Path.Direction.CCW);
        canvas.drawPath(path, paint);
    }

    private void initBorderPaint(int borderWidth, int borderColor) {
        path.reset();
        paint.setStrokeWidth(borderWidth);
        paint.setColor(borderColor);
        paint.setStyle(Paint.Style.STROKE);
    }

    /**
     * 计算外边框的RectF
     */
    private void initBorderRectF() {
        if (!isCircle) {
            borderRectF.set(borderWidth / 2.0f, borderWidth / 2.0f, width - borderWidth / 2.0f, height - borderWidth / 2.0f);
        }
    }

    /**
     * 计算图片原始区域的RectF
     */
    private void initSrcRectF() {
        if (isCircle) {
            radius = Math.min(width, height) / 2.0f;
            srcRectF.set(width / 2.0f - radius, height / 2.0f - radius, width / 2.0f + radius, height / 2.0f + radius);
        } else {
            srcRectF.set(0, 0, width, height);
            if (isCoverSrc) {
                srcRectF = borderRectF;
            }
        }
    }

    /**
     * 计算RectF的圆角半径
     */
    private void calculateRadii() {
        if (isCircle) {
            return;
        }
        if (cornerRadius > 0) {
            for (int i = 0; i < borderRadii.length; i++) {
                borderRadii[i] = cornerRadius;
                srcRadii[i] = cornerRadius - borderWidth / 2.0f;
            }
        } else {
            borderRadii[0] = borderRadii[1] = cornerTopLeftRadius;
            borderRadii[2] = borderRadii[3] = cornerTopRightRadius;
            borderRadii[4] = borderRadii[5] = cornerBottomRightRadius;
            borderRadii[6] = borderRadii[7] = cornerBottomLeftRadius;

            srcRadii[0] = srcRadii[1] = cornerTopLeftRadius - borderWidth / 2.0f;
            srcRadii[2] = srcRadii[3] = cornerTopRightRadius - borderWidth / 2.0f;
            srcRadii[4] = srcRadii[5] = cornerBottomRightRadius - borderWidth / 2.0f;
            srcRadii[6] = srcRadii[7] = cornerBottomLeftRadius - borderWidth / 2.0f;
        }
    }

    private void calculateRadiiAndRectF(boolean reset) {
        if (reset) {
            cornerRadius = 0;
        }
        calculateRadii();
        initBorderRectF();
        invalidate();
    }

    /**
     * 目前圆角矩形情况下不支持inner_border，需要将其置0
     */
    private void clearInnerBorderWidth() {
        if (!isCircle) {
            this.innerBorderWidth = 0;
        }
    }

    public void isCoverSrc(boolean isCoverSrc) {
        this.isCoverSrc = isCoverSrc;
        initSrcRectF();
        invalidate();
    }

    public void isCircle(boolean isCircle) {
        this.isCircle = isCircle;
        clearInnerBorderWidth();
        initSrcRectF();
        invalidate();
    }

    public void setBorderWidth(int borderWidth) {
        this.borderWidth = dp2px(context, borderWidth);
        calculateRadiiAndRectF(false);
    }

    public void setBorderColor(@ColorInt int borderColor) {
        this.borderColor = borderColor;
        invalidate();
    }

    public void setInnerBorderWidth(int innerBorderWidth) {
        this.innerBorderWidth = dp2px(context, innerBorderWidth);
        clearInnerBorderWidth();
        invalidate();
    }

    public void setInnerBorderColor(@ColorInt int innerBorderColor) {
        this.innerBorderColor = innerBorderColor;
        invalidate();
    }

    public void setCornerRadius(int cornerRadius) {
        this.cornerRadius = dp2px(context, cornerRadius);
        calculateRadiiAndRectF(false);
    }

    public void setCornerTopLeftRadius(int cornerTopLeftRadius) {
        this.cornerTopLeftRadius = dp2px(context, cornerTopLeftRadius);
        calculateRadiiAndRectF(true);
    }

    public void setCornerTopRightRadius(int cornerTopRightRadius) {
        this.cornerTopRightRadius = dp2px(context, cornerTopRightRadius);
        calculateRadiiAndRectF(true);
    }

    public void setCornerBottomLeftRadius(int cornerBottomLeftRadius) {
        this.cornerBottomLeftRadius = dp2px(context, cornerBottomLeftRadius);
        calculateRadiiAndRectF(true);
    }

    public void setCornerBottomRightRadius(int cornerBottomRightRadius) {
        this.cornerBottomRightRadius = dp2px(context, cornerBottomRightRadius);
        calculateRadiiAndRectF(true);
    }

    public void setMaskColor(@ColorInt int maskColor) {
        this.maskColor = maskColor;
        invalidate();
    }

    protected int dp2px(Context context,float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

}

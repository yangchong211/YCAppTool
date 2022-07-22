package com.yc.appimageview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     GitHub : https://github.com/yangchong211/YCCommonLib
 *     time  : 2018/11/9
 *     desc  : canvas.clipPath 实现圆角
 *     revise:
 * </pre>
 */
@Deprecated
public class RoundCornerImageView extends AppCompatImageView {

    private float width, height;
    private int leftTopRadius;
    private int rightTopRadius;
    private int rightBottomRadius;
    private int leftBottomRadius;
    private final Path path = new Path();

    public RoundCornerImageView(Context context) {
        this(context, null);
        init(context, null);
    }

    public RoundCornerImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init(context, attrs);
    }

    public RoundCornerImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        // 读取配置
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RoundCornerImageView);
        int defaultRadius = 0;
        int radius = array.getDimensionPixelOffset(R.styleable.RoundCornerImageView_radius, defaultRadius);
        leftTopRadius = array.getDimensionPixelOffset(R.styleable.RoundCornerImageView_left_top_radius, defaultRadius);
        rightTopRadius = array.getDimensionPixelOffset(R.styleable.RoundCornerImageView_right_top_radius, defaultRadius);
        rightBottomRadius = array.getDimensionPixelOffset(R.styleable.RoundCornerImageView_right_bottom_radius, defaultRadius);
        leftBottomRadius = array.getDimensionPixelOffset(R.styleable.RoundCornerImageView_left_bottom_radius, defaultRadius);

        //如果四个角的值没有设置，那么就使用通用的radius的值。
        if (defaultRadius == leftTopRadius) {
            leftTopRadius = radius;
        }
        if (defaultRadius == rightTopRadius) {
            rightTopRadius = radius;
        }
        if (defaultRadius == rightBottomRadius) {
            rightBottomRadius = radius;
        }
        if (defaultRadius == leftBottomRadius) {
            leftBottomRadius = radius;
        }
        array.recycle();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getWidth();
        height = getHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        clipPath(canvas,path);
        super.onDraw(canvas);
        // 恢复画布状态
        canvas.restore();
    }

    private void clipPath(Canvas canvas, Path path) {
        //这里做下判断，只有图片的宽高大于设置的圆角距离的时候才进行裁剪
        int maxLeft = Math.max(leftTopRadius, leftBottomRadius);
        int maxRight = Math.max(rightTopRadius, rightBottomRadius);
        int minWidth = maxLeft + maxRight;
        int maxTop = Math.max(leftTopRadius, rightTopRadius);
        int maxBottom = Math.max(leftBottomRadius, rightBottomRadius);
        int minHeight = maxTop + maxBottom;
        if (width >= minWidth && height > minHeight) {
            //四个角：右上，右下，左下，左上
            path.moveTo(leftTopRadius, 0);
            path.lineTo(width - rightTopRadius, 0);
            path.quadTo(width, 0, width, rightTopRadius);

            path.lineTo(width, height - rightBottomRadius);
            path.quadTo(width, height, width - rightBottomRadius, height);

            path.lineTo(leftBottomRadius, height);
            path.quadTo(0, height, 0, height - leftBottomRadius);

            path.lineTo(0, leftTopRadius);
            path.quadTo(0, 0, leftTopRadius, 0);

            //将当前剪辑与指定路径相交
            canvas.clipPath(path);
        }
    }

}

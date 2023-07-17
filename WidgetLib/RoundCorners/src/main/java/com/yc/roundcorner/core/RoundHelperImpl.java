package com.yc.roundcorner.core;

import android.content.Context;
import android.content.res.ColorStateList;
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
import android.view.View;
import android.view.ViewGroup;

import com.yc.roundcorner.R;
import com.yc.toolutils.AppSizeUtils;


public class RoundHelperImpl implements RoundHelper {

    private Context mContext;
    private View mView;

    private Paint mPaint;
    private RectF mRectF;
    private RectF mStrokeRectF;
    private RectF mOriginRectF;

    private Path mPath;
    private Path mTempPath;

    private Xfermode mXfermode;

    private boolean isCircle;

    private float[] mRadii;
    private float[] mStrokeRadii;

    private int mWidth;
    private int mHeight;
    private int mStrokeColor;
    private float mStrokeWidth;
    private ColorStateList mStrokeColorStateList;

    private float mRadiusTopLeft;
    private float mRadiusTopRight;
    private float mRadiusBottomLeft;
    private float mRadiusBottomRight;

    private boolean isNewLayer;

    @Override
    public void init(Context context, AttributeSet attrs, View view) {
        if (view instanceof ViewGroup && view.getBackground() == null) {
            view.setBackgroundColor(Color.parseColor("#00000000"));
        }

        mContext = context;
        mView = view;
        mRadii = new float[8];
        mStrokeRadii = new float[8];
        mPaint = new Paint();

        mRectF = new RectF();
        mStrokeRectF = new RectF();
        mOriginRectF = new RectF();
        mPath = new Path();
        mTempPath = new Path();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            mXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);
        } else {
            mXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
        }
        mStrokeColor = Color.WHITE;

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RoundCorner);
        if (array == null) {
            return;
        }
        float radius = array.getDimension(R.styleable.RoundCorner_rRadius, 0);
        float radiusLeft = array.getDimension(R.styleable.RoundCorner_rLeftRadius, radius);
        float radiusRight = array.getDimension(R.styleable.RoundCorner_rRightRadius, radius);
        float radiusTop = array.getDimension(R.styleable.RoundCorner_rTopRadius, radius);
        float radiusBottom = array.getDimension(R.styleable.RoundCorner_rBottomRadius, radius);

        mRadiusTopLeft = array.getDimension(R.styleable.RoundCorner_rTopLeftRadius, radiusTop > 0 ? radiusTop : radiusLeft);
        mRadiusTopRight = array.getDimension(R.styleable.RoundCorner_rTopRightRadius, radiusTop > 0 ? radiusTop : radiusRight);
        mRadiusBottomLeft = array.getDimension(R.styleable.RoundCorner_rBottomLeftRadius, radiusBottom > 0 ? radiusBottom : radiusLeft);
        mRadiusBottomRight = array.getDimension(R.styleable.RoundCorner_rBottomRightRadius, radiusBottom > 0 ? radiusBottom : radiusRight);

        mStrokeWidth = array.getDimension(R.styleable.RoundCorner_rStrokeWidth, 0);
        mStrokeColor = array.getColor(R.styleable.RoundCorner_rStrokeColor, mStrokeColor);
        mStrokeColorStateList = array.getColorStateList(R.styleable.RoundCorner_rStrokeColor);

        isNewLayer = array.getBoolean(R.styleable.RoundCorner_rNewLayer, false);
        if (isNewLayer && Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && view instanceof ViewGroup) {
            view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        array.recycle();
    }

    @Override
    public void onSizeChanged(int width, int height) {
        mWidth = width;
        mHeight = height;

        if (isCircle) {
            float radius = Math.min(height, width) * 1f / 2;
            mRadiusTopLeft = radius;
            mRadiusTopRight = radius;
            mRadiusBottomRight = radius;
            mRadiusBottomLeft = radius;
        }
        setRadius();

        if (mRectF != null) {
            mRectF.set(mStrokeWidth, mStrokeWidth, width - mStrokeWidth, height - mStrokeWidth);
        }
        if (mStrokeRectF != null) {
            mStrokeRectF.set((mStrokeWidth / 2), (mStrokeWidth / 2), width - mStrokeWidth / 2, height - mStrokeWidth / 2);
        }
        if (mOriginRectF != null) {
            mOriginRectF.set(0, 0, width, height);
        }
    }

    @Override
    public void preDraw(Canvas canvas) {
        // 使用图形混合模式来显示指定区域的图片
        // 使用离屏缓存，新建一个srcRectF区域大小的图层
        canvas.saveLayer(isNewLayer && Build.VERSION.SDK_INT > Build.VERSION_CODES.P
                ? mOriginRectF : mRectF, null, Canvas.ALL_SAVE_FLAG);
    }

    @Override
    public void drawPath(Canvas canvas, int[] drawableState) {
        mPaint.reset();
        mPath.reset();

        mPaint.setAntiAlias(true);
        // 画笔为填充模式
        mPaint.setStyle(Paint.Style.FILL);
        // 设置混合模式
        mPaint.setXfermode(mXfermode);

        mPath.addRoundRect(mRectF, mRadii, Path.Direction.CCW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mTempPath.reset();
            mTempPath.addRect(mOriginRectF, Path.Direction.CCW);
            mTempPath.op(mPath, Path.Op.DIFFERENCE);
            canvas.drawPath(mTempPath, mPaint);
        } else {
            canvas.drawPath(mPath, mPaint);
        }
        // 清除Xfermode
        mPaint.setXfermode(null);
        // 恢复画布状态
        canvas.restore();

        // draw stroke
        if (mStrokeWidth > 0) {
            if (mStrokeColorStateList != null && mStrokeColorStateList.isStateful()) {
                mStrokeColor = mStrokeColorStateList.getColorForState(drawableState, mStrokeColorStateList.getDefaultColor());
            }

            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(mStrokeWidth);
            mPaint.setColor(mStrokeColor);

            mPath.reset();
            mPath.addRoundRect(mStrokeRectF, mStrokeRadii, Path.Direction.CCW);
            canvas.drawPath(mPath, mPaint);
        }
    }

    @Override
    public void setCircle(boolean isCircle) {
        this.isCircle = isCircle;
    }

    @Override
    public void setRadius(float radiusDp) {
        if (mContext == null) {
            return;
        }
        float radiusPx = AppSizeUtils.dp2px(mContext, radiusDp);
        mRadiusTopLeft = radiusPx;
        mRadiusTopRight = radiusPx;
        mRadiusBottomLeft = radiusPx;
        mRadiusBottomRight = radiusPx;
        if (mView != null) {
            onSizeChanged(mWidth, mHeight);
            mView.invalidate();
        }
    }

    @Override
    public void setRadius(float radiusTopLeftDp, float radiusTopRightDp, float radiusBottomLeftDp, float radiusBottomRightDp) {
        if (mContext == null) {
            return;
        }
        mRadiusTopLeft = AppSizeUtils.dp2px(mContext, radiusTopLeftDp);
        mRadiusTopRight = AppSizeUtils.dp2px(mContext, radiusTopRightDp);
        mRadiusBottomLeft = AppSizeUtils.dp2px(mContext, radiusBottomLeftDp);
        mRadiusBottomRight = AppSizeUtils.dp2px(mContext, radiusBottomRightDp);
        if (mView != null) {
            onSizeChanged(mWidth, mHeight);
            mView.invalidate();
        }
    }

    @Override
    public void setRadiusLeft(float radiusDp) {
        if (mContext == null) {
            return;
        }
        float radiusPx = AppSizeUtils.dp2px(mContext, radiusDp);
        mRadiusTopLeft = radiusPx;
        mRadiusBottomLeft = radiusPx;
        if (mView != null) {
            onSizeChanged(mWidth, mHeight);
            mView.invalidate();
        }
    }

    @Override
    public void setRadiusRight(float radiusDp) {
        if (mContext == null) {
            return;
        }
        float radiusPx = AppSizeUtils.dp2px(mContext, radiusDp);
        mRadiusTopRight = radiusPx;
        mRadiusBottomRight = radiusPx;
        if (mView != null) {
            onSizeChanged(mWidth, mHeight);
            mView.invalidate();
        }
    }

    @Override
    public void setRadiusTop(float radiusDp) {
        if (mContext == null) {
            return;
        }
        float radiusPx = AppSizeUtils.dp2px(mContext, radiusDp);
        mRadiusTopLeft = radiusPx;
        mRadiusTopRight = radiusPx;
        if (mView != null) {
            onSizeChanged(mWidth, mHeight);
            mView.invalidate();
        }
    }

    @Override
    public void setRadiusBottom(float radiusDp) {
        if (mContext == null) {
            return;
        }
        float radiusPx = AppSizeUtils.dp2px(mContext, radiusDp);
        mRadiusBottomLeft = radiusPx;
        mRadiusBottomRight = radiusPx;
        if (mView != null) {
            onSizeChanged(mWidth, mHeight);
            mView.invalidate();
        }
    }

    @Override
    public void setRadiusTopLeft(float radiusDp) {
        if (mContext == null) {
            return;
        }
        mRadiusTopLeft = AppSizeUtils.dp2px(mContext, radiusDp);
        if (mView != null) {
            onSizeChanged(mWidth, mHeight);
            mView.invalidate();
        }
    }

    @Override
    public void setRadiusTopRight(float radiusDp) {
        if (mContext == null) {
            return;
        }
        mRadiusTopRight = AppSizeUtils.dp2px(mContext, radiusDp);
        if (mView != null) {
            onSizeChanged(mWidth, mHeight);
            mView.invalidate();
        }
    }

    @Override
    public void setRadiusBottomLeft(float radiusDp) {
        if (mContext == null) {
            return;
        }
        mRadiusBottomLeft = AppSizeUtils.dp2px(mContext, radiusDp);
        if (mView != null) {
            onSizeChanged(mWidth, mHeight);
            mView.invalidate();
        }
    }

    @Override
    public void setRadiusBottomRight(float radiusDp) {
        if (mContext == null) {
            return;
        }
        mRadiusBottomRight = AppSizeUtils.dp2px(mContext, radiusDp);
        if (mView != null) {
            onSizeChanged(mWidth, mHeight);
            mView.invalidate();
        }
    }

    @Override
    public void setStrokeWidth(float widthDp) {
        if (mContext == null) {
            return;
        }
        mStrokeWidth = AppSizeUtils.dp2px(mContext, widthDp);
        if (mView != null) {
            onSizeChanged(mWidth, mHeight);
            mView.invalidate();
        }
    }

    @Override
    public void setStrokeColor(int color) {
        mStrokeColor = color;
        if (mView != null) {
            onSizeChanged(mWidth, mHeight);
            mView.invalidate();
        }
    }

    @Override
    public void setStrokeWidthColor(float widthDp, int color) {
        if (mContext == null) {
            return;
        }
        mStrokeWidth = AppSizeUtils.dp2px(mContext, widthDp);
        mStrokeColor = color;
        if (mView != null) {
            onSizeChanged(mWidth, mHeight);
            mView.invalidate();
        }
    }

    private void setRadius() {
        mRadii[0] = mRadii[1] = mRadiusTopLeft - mStrokeWidth;
        mRadii[2] = mRadii[3] = mRadiusTopRight - mStrokeWidth;
        mRadii[4] = mRadii[5] = mRadiusBottomRight - mStrokeWidth;
        mRadii[6] = mRadii[7] = mRadiusBottomLeft - mStrokeWidth;

        mStrokeRadii[0] = mStrokeRadii[1] = mRadiusTopLeft - mStrokeWidth / 2;
        mStrokeRadii[2] = mStrokeRadii[3] = mRadiusTopRight - mStrokeWidth / 2;
        mStrokeRadii[4] = mStrokeRadii[5] = mRadiusBottomRight - mStrokeWidth / 2;
        mStrokeRadii[6] = mStrokeRadii[7] = mRadiusBottomLeft - mStrokeWidth / 2;
    }
}

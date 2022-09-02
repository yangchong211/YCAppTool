package com.yc.zoomimagelib;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.OverScroller;
import android.widget.Scroller;
import androidx.appcompat.widget.AppCompatImageView;


/**
 * <pre>
 *     @author yangchong
 *     GitHub : https://github.com/yangchong211/YCWidgetLib
 *     email  : yangchong211@163.com
 *     time  : 2018/7/10
 *     desc  : 缩放图片
 *     revise:
 * </pre>
 */
public class ZoomImageView extends AppCompatImageView {

    private final static int MIN_ROTATE = 35;
    private final static int ANIMA_DURING = 340;
    private final static float MAX_SCALE = 2.5f;

    private int mMinRotate;
    private int mAnimaDuring;
    private float mMaxScale;

    private int MAX_FLING_OVER_SCROLL = 0;
    private int MAX_OVER_RESISTANCE = 0;
    private int MAX_ANIM_FROM_WAITE = 500;

    private final Matrix mBaseMatrix = new Matrix();
    private final Matrix mAnimaMatrix = new Matrix();
    private final Matrix mSynthesisMatrix = new Matrix();
    private final Matrix mTmpMatrix = new Matrix();

    private RotateGestureDetector mRotateDetector;
    private GestureDetector mDetector;
    private ScaleGestureDetector mScaleDetector;
    private OnClickListener mClickListener;

    private ScaleType mScaleType;

    private boolean hasMultiTouch;
    private boolean hasDrawable;
    private boolean isKnowSize;
    private boolean hasOverTranslate;
    private boolean isEnable = false;
    private boolean isRotateEnable = false;
    private boolean isInit;
    private boolean mAdjustViewBounds;
    // 当前是否处于放大状态
    private boolean isZoonUp;
    private boolean canRotate;

    private boolean imgLargeWidth;
    private boolean imgLargeHeight;

    private float mRotateFlag;
    private float mDegrees;
    private float mScale = 1.0f;
    private int mTranslateX;
    private int mTranslateY;

    private float mHalfBaseRectWidth;
    private float mHalfBaseRectHeight;

    private final RectF mWidgetRect = new RectF();
    private final RectF mBaseRect = new RectF();
    private final RectF mImgRect = new RectF();
    private final RectF mTmpRect = new RectF();
    private final RectF mCommonRect = new RectF();

    private final PointF mScreenCenter = new PointF();
    private final PointF mScaleCenter = new PointF();
    private final PointF mRotateCenter = new PointF();

    private final Transform mTranslate = new Transform();

    private RectF mClip;
    private ZoomImageInfo mFromInfo;
    private long mInfoTime;
    private Runnable mCompleteCallBack;

    private OnLongClickListener mLongClick;

    public ZoomImageView(Context context) {
        super(context);
        init();
    }

    public ZoomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ZoomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        super.setScaleType(ScaleType.MATRIX);
        if (mScaleType == null) {
            mScaleType = ScaleType.CENTER_INSIDE;
        }
        mRotateDetector = new RotateGestureDetector(mRotateListener);
        mDetector = new GestureDetector(getContext(), mGestureListener);
        mScaleDetector = new ScaleGestureDetector(getContext(), mScaleListener);
        float density = getResources().getDisplayMetrics().density;
        MAX_FLING_OVER_SCROLL = (int) (density * 30);
        MAX_OVER_RESISTANCE = (int) (density * 140);

        mMinRotate = MIN_ROTATE;
        mAnimaDuring = ANIMA_DURING;
        mMaxScale = MAX_SCALE;
    }

    /**
     * 获取默认的动画持续时间
     */
    public int getDefaultAnimaDuring() {
        return ANIMA_DURING;
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        super.setOnClickListener(l);
        mClickListener = l;
    }

    @Override
    public void setScaleType(ScaleType scaleType) {
        if (scaleType == ScaleType.MATRIX) {
            return;
        }
        if (scaleType != mScaleType) {
            mScaleType = scaleType;

            if (isInit) {
                initBase();
            }
        }
    }

    @Override
    public void setOnLongClickListener(OnLongClickListener l) {
        super.setOnLongClickListener(l);
        mLongClick = l;
    }

    /**
     * 设置动画的插入器
     */
    public void setInterpolator(Interpolator interpolator) {
        mTranslate.setInterpolator(interpolator);
    }

    /**
     * 获取动画持续时间
     */
    public int getAnimaDuring() {
        return mAnimaDuring;
    }

    /**
     * 设置动画的持续时间
     */
    public void setAnimaDuring(int during) {
        mAnimaDuring = during;
    }

    /**
     * 设置最大可以缩放的倍数
     */
    public void setMaxScale(float maxScale) {
        mMaxScale = maxScale;
    }

    /**
     * 获取最大可以缩放的倍数
     */
    public float getMaxScale() {
        return mMaxScale;
    }

    /**
     * 启用缩放功能
     */
    public void enable() {
        isEnable = true;
    }

    /**
     * 禁用缩放功能
     */
    public void disenable() {
        isEnable = false;
    }

    /**
     * 启用旋转功能
     */
    public void enableRotate() {
        isRotateEnable = true;
    }

    /**
     * 禁用旋转功能
     */
    public void disableRotate() {
        isRotateEnable = false;
    }

    /**
     */
    public void setMaxAnimFromWaiteTime(int wait) {
        MAX_ANIM_FROM_WAITE = wait;
    }

    @Override
    public void setImageResource(int resId) {
        Drawable drawable = null;
        try {
            drawable = getResources().getDrawable(resId);
        } catch (Exception e) {
        }

        setImageDrawable(drawable);
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);

        if (drawable == null) {
            hasDrawable = false;
            return;
        }

        if (!ZoomImageUtils.hasSize(drawable)) {
            return;
        }

        if (!hasDrawable) {
            hasDrawable = true;
        }

        initBase();
    }


    private void initBase() {
        if (!hasDrawable) {
            return;
        }
        if (!isKnowSize) {
            return;
        }

        mBaseMatrix.reset();
        mAnimaMatrix.reset();

        isZoonUp = false;

        Drawable img = getDrawable();

        int w = getWidth();
        int h = getHeight();
        int imgw = ZoomImageUtils.getDrawableWidth(img);
        int imgh = ZoomImageUtils.getDrawableHeight(img);

        mBaseRect.set(0, 0, imgw, imgh);

        // 以图片中心点居中位移
        int tx = (w - imgw) / 2;
        int ty = (h - imgh) / 2;

        float sx = 1;
        float sy = 1;

        // 缩放，默认不超过屏幕大小
        if (imgw > w) {
            sx = (float) w / imgw;
        }

        if (imgh > h) {
            sy = (float) h / imgh;
        }

        float scale = sx < sy ? sx : sy;

        mBaseMatrix.reset();
        mBaseMatrix.postTranslate(tx, ty);
        mBaseMatrix.postScale(scale, scale, mScreenCenter.x, mScreenCenter.y);
        mBaseMatrix.mapRect(mBaseRect);

        mHalfBaseRectWidth = mBaseRect.width() / 2;
        mHalfBaseRectHeight = mBaseRect.height() / 2;

        mScaleCenter.set(mScreenCenter);
        mRotateCenter.set(mScaleCenter);

        executeTranslate();

        switch (mScaleType) {
            case CENTER:
                initCenter();
                break;
            case CENTER_CROP:
                initCenterCrop();
                break;
            case CENTER_INSIDE:
                initCenterInside();
                break;
            case FIT_CENTER:
                initFitCenter();
                break;
            case FIT_START:
                initFitStart();
                break;
            case FIT_END:
                initFitEnd();
                break;
            case FIT_XY:
                initFitXY();
                break;
        }

        isInit = true;

        if (mFromInfo != null && System.currentTimeMillis() - mInfoTime < MAX_ANIM_FROM_WAITE) {
            animaFrom(mFromInfo);
        }

        mFromInfo = null;
    }

    private void initCenter() {
        if (!hasDrawable) {
            return;
        }
        if (!isKnowSize) {
            return;
        }

        Drawable img = getDrawable();

        int imgw = ZoomImageUtils.getDrawableWidth(img);
        int imgh = ZoomImageUtils.getDrawableHeight(img);

        if (imgw > mWidgetRect.width() || imgh > mWidgetRect.height()) {
            float scaleX = imgw / mImgRect.width();
            float scaleY = imgh / mImgRect.height();

            mScale = scaleX > scaleY ? scaleX : scaleY;

            mAnimaMatrix.postScale(mScale, mScale, mScreenCenter.x, mScreenCenter.y);

            executeTranslate();

            resetBase();
        }
    }

    private void initCenterCrop() {
        if (mImgRect.width() < mWidgetRect.width() || mImgRect.height() < mWidgetRect.height()) {
            float scaleX = mWidgetRect.width() / mImgRect.width();
            float scaleY = mWidgetRect.height() / mImgRect.height();

            mScale = scaleX > scaleY ? scaleX : scaleY;

            mAnimaMatrix.postScale(mScale, mScale, mScreenCenter.x, mScreenCenter.y);

            executeTranslate();
            resetBase();
        }
    }

    private void initCenterInside() {
        if (mImgRect.width() > mWidgetRect.width() || mImgRect.height() > mWidgetRect.height()) {
            float scaleX = mWidgetRect.width() / mImgRect.width();
            float scaleY = mWidgetRect.height() / mImgRect.height();

            mScale = scaleX < scaleY ? scaleX : scaleY;

            mAnimaMatrix.postScale(mScale, mScale, mScreenCenter.x, mScreenCenter.y);

            executeTranslate();
            resetBase();
        }
    }

    private void initFitCenter() {
        if (mImgRect.width() < mWidgetRect.width()) {
            mScale = mWidgetRect.width() / mImgRect.width();

            mAnimaMatrix.postScale(mScale, mScale, mScreenCenter.x, mScreenCenter.y);

            executeTranslate();
            resetBase();
        }
    }

    private void initFitStart() {
        initFitCenter();

        float ty = -mImgRect.top;
        mAnimaMatrix.postTranslate(0, ty);
        executeTranslate();
        resetBase();
        mTranslateY += ty;
    }

    private void initFitEnd() {
        initFitCenter();

        float ty = (mWidgetRect.bottom - mImgRect.bottom);
        mTranslateY += ty;
        mAnimaMatrix.postTranslate(0, ty);
        executeTranslate();
        resetBase();
    }

    private void initFitXY() {
        float scaleX = mWidgetRect.width() / mImgRect.width();
        float scaleY = mWidgetRect.height() / mImgRect.height();

        mAnimaMatrix.postScale(scaleX, scaleY, mScreenCenter.x, mScreenCenter.y);

        executeTranslate();
        resetBase();
    }

    private void resetBase() {
        Drawable img = getDrawable();
        int imgw = ZoomImageUtils.getDrawableWidth(img);
        int imgh = ZoomImageUtils.getDrawableHeight(img);
        mBaseRect.set(0, 0, imgw, imgh);
        mBaseMatrix.set(mSynthesisMatrix);
        mBaseMatrix.mapRect(mBaseRect);
        mHalfBaseRectWidth = mBaseRect.width() / 2;
        mHalfBaseRectHeight = mBaseRect.height() / 2;
        mScale = 1;
        mTranslateX = 0;
        mTranslateY = 0;
        mAnimaMatrix.reset();
    }

    private void executeTranslate() {
        mSynthesisMatrix.set(mBaseMatrix);
        mSynthesisMatrix.postConcat(mAnimaMatrix);
        setImageMatrix(mSynthesisMatrix);

        mAnimaMatrix.mapRect(mImgRect, mBaseRect);

        imgLargeWidth = mImgRect.width() > mWidgetRect.width();
        imgLargeHeight = mImgRect.height() > mWidgetRect.height();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (!hasDrawable) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }

        Drawable d = getDrawable();
        int drawableW = ZoomImageUtils.getDrawableWidth(d);
        int drawableH = ZoomImageUtils.getDrawableHeight(d);

        int pWidth = MeasureSpec.getSize(widthMeasureSpec);
        int pHeight = MeasureSpec.getSize(heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width = 0;
        int height = 0;

        ViewGroup.LayoutParams p = getLayoutParams();

        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        if (p.width == ViewGroup.LayoutParams.MATCH_PARENT) {
            if (widthMode == MeasureSpec.UNSPECIFIED) {
                width = drawableW;
            } else {
                width = pWidth;
            }
        } else {
            if (widthMode == MeasureSpec.EXACTLY) {
                width = pWidth;
            } else if (widthMode == MeasureSpec.AT_MOST) {
                width = drawableW > pWidth ? pWidth : drawableW;
            } else {
                width = drawableW;
            }
        }

        if (p.height == ViewGroup.LayoutParams.MATCH_PARENT) {
            if (heightMode == MeasureSpec.UNSPECIFIED) {
                height = drawableH;
            } else {
                height = pHeight;
            }
        } else {
            if (heightMode == MeasureSpec.EXACTLY) {
                height = pHeight;
            } else if (heightMode == MeasureSpec.AT_MOST) {
                height = drawableH > pHeight ? pHeight : drawableH;
            } else {
                height = drawableH;
            }
        }

        if (mAdjustViewBounds && (float) drawableW / drawableH != (float) width / height) {

            float hScale = (float) height / drawableH;
            float wScale = (float) width / drawableW;

            float scale = hScale < wScale ? hScale : wScale;
            width = p.width == ViewGroup.LayoutParams.MATCH_PARENT ? width : (int) (drawableW * scale);
            height = p.height == ViewGroup.LayoutParams.MATCH_PARENT ? height : (int) (drawableH * scale);
        }

        setMeasuredDimension(width, height);
    }

    @Override
    public void setAdjustViewBounds(boolean adjustViewBounds) {
        super.setAdjustViewBounds(adjustViewBounds);
        mAdjustViewBounds = adjustViewBounds;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mWidgetRect.set(0, 0, w, h);
        mScreenCenter.set(w / 2, h / 2);

        if (!isKnowSize) {
            isKnowSize = true;
            initBase();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if (mClip != null) {
            canvas.clipRect(mClip);
            mClip = null;
        }
        super.draw(canvas);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (isEnable) {
            final int Action = event.getActionMasked();
            if (event.getPointerCount() >= 2) {
                hasMultiTouch = true;
            }

            mDetector.onTouchEvent(event);
            if (isRotateEnable) {
                mRotateDetector.onTouchEvent(event);
            }
            mScaleDetector.onTouchEvent(event);

            if (Action == MotionEvent.ACTION_UP || Action == MotionEvent.ACTION_CANCEL) {
                onUp();
            }

            return true;
        } else {
            return super.dispatchTouchEvent(event);
        }
    }

    private void onUp() {
        if (mTranslate.isRuning) {
            return;
        }

        if (canRotate || mDegrees % 90 != 0) {
            float toDegrees = (int) (mDegrees / 90) * 90;
            float remainder = mDegrees % 90;

            if (remainder > 45) {
                toDegrees += 90;
            } else if (remainder < -45) {
                toDegrees -= 90;
            }

            mTranslate.withRotate((int) mDegrees, (int) toDegrees);

            mDegrees = toDegrees;
        }

        float scale = mScale;

        if (mScale < 1) {
            scale = 1;
            mTranslate.withScale(mScale, 1);
        } else if (mScale > mMaxScale) {
            scale = mMaxScale;
            mTranslate.withScale(mScale, mMaxScale);
        }

        float cx = mImgRect.left + mImgRect.width() / 2;
        float cy = mImgRect.top + mImgRect.height() / 2;

        mScaleCenter.set(cx, cy);
        mRotateCenter.set(cx, cy);

        mTranslateX = 0;
        mTranslateY = 0;

        mTmpMatrix.reset();
        mTmpMatrix.postTranslate(-mBaseRect.left, -mBaseRect.top);
        mTmpMatrix.postTranslate(cx - mHalfBaseRectWidth, cy - mHalfBaseRectHeight);
        mTmpMatrix.postScale(scale, scale, cx, cy);
        mTmpMatrix.postRotate(mDegrees, cx, cy);
        mTmpMatrix.mapRect(mTmpRect, mBaseRect);

        doTranslateReset(mTmpRect);
        mTranslate.start();
    }

    private void doTranslateReset(RectF imgRect) {
        int tx = 0;
        int ty = 0;

        if (imgRect.width() <= mWidgetRect.width()) {
            if (!isImageCenterWidth(imgRect)) {
                tx = -(int) ((mWidgetRect.width() - imgRect.width()) / 2 - imgRect.left);
            }
        } else {
            if (imgRect.left > mWidgetRect.left) {
                tx = (int) (imgRect.left - mWidgetRect.left);
            } else if (imgRect.right < mWidgetRect.right) {
                tx = (int) (imgRect.right - mWidgetRect.right);
            }
        }

        if (imgRect.height() <= mWidgetRect.height()) {
            if (!isImageCenterHeight(imgRect)) {
                ty = -(int) ((mWidgetRect.height() - imgRect.height()) / 2 - imgRect.top);
            }
        } else {
            if (imgRect.top > mWidgetRect.top) {
                ty = (int) (imgRect.top - mWidgetRect.top);
            } else if (imgRect.bottom < mWidgetRect.bottom) {
                ty = (int) (imgRect.bottom - mWidgetRect.bottom);
            }
        }

        if (tx != 0 || ty != 0) {
            if (!mTranslate.mFlingScroller.isFinished()) {
                mTranslate.mFlingScroller.abortAnimation();
            }
            mTranslate.withTranslate(mTranslateX, mTranslateY, -tx, -ty);
        }
    }

    private boolean isImageCenterHeight(RectF rect) {
        return Math.abs(Math.round(rect.top) - (mWidgetRect.height() - rect.height()) / 2) < 1;
    }

    private boolean isImageCenterWidth(RectF rect) {
        return Math.abs(Math.round(rect.left) - (mWidgetRect.width() - rect.width()) / 2) < 1;
    }

    private final OnRotateListener mRotateListener = new OnRotateListener() {

        @Override
        public void onRotate(float degrees, float focusX, float focusY) {
            mRotateFlag += degrees;
            if (canRotate) {
                mDegrees += degrees;
                mAnimaMatrix.postRotate(degrees, focusX, focusY);
            } else {
                if (Math.abs(mRotateFlag) >= mMinRotate) {
                    canRotate = true;
                    mRotateFlag = 0;
                }
            }
        }
    };

    private final ScaleGestureDetector.OnScaleGestureListener mScaleListener = new ScaleGestureDetector.OnScaleGestureListener() {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float scaleFactor = detector.getScaleFactor();

            if (Float.isNaN(scaleFactor) || Float.isInfinite(scaleFactor)) {
                return false;
            }

            mScale *= scaleFactor;
//            mScaleCenter.set(detector.getFocusX(), detector.getFocusY());
            mAnimaMatrix.postScale(scaleFactor, scaleFactor, detector.getFocusX(), detector.getFocusY());
            executeTranslate();
            return true;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {

        }
    };

    private float resistanceScrollByX(float overScroll, float detalX) {
        float s = detalX * (Math.abs(Math.abs(overScroll) - MAX_OVER_RESISTANCE) / (float) MAX_OVER_RESISTANCE);
        return s;
    }

    private float resistanceScrollByY(float overScroll, float detalY) {
        float s = detalY * (Math.abs(Math.abs(overScroll) - MAX_OVER_RESISTANCE) / (float) MAX_OVER_RESISTANCE);
        return s;
    }

    /**
     * 匹配两个Rect的共同部分输出到out，若无共同部分则输出0，0，0，0
     */
    private void mapRect(RectF r1, RectF r2, RectF out) {

        float l, r, t, b;

        l = r1.left > r2.left ? r1.left : r2.left;
        r = r1.right < r2.right ? r1.right : r2.right;

        if (l > r) {
            out.set(0, 0, 0, 0);
            return;
        }

        t = r1.top > r2.top ? r1.top : r2.top;
        b = r1.bottom < r2.bottom ? r1.bottom : r2.bottom;

        if (t > b) {
            out.set(0, 0, 0, 0);
            return;
        }

        out.set(l, t, r, b);
    }

    private void checkRect() {
        if (!hasOverTranslate) {
            mapRect(mWidgetRect, mImgRect, mCommonRect);
        }
    }

    private final Runnable mClickRunnable = new Runnable() {
        @Override
        public void run() {
            if (mClickListener != null) {
                mClickListener.onClick(ZoomImageView.this);
            }
        }
    };

    private final GestureDetector.OnGestureListener mGestureListener = new GestureDetector.SimpleOnGestureListener() {

        @Override
        public void onLongPress(MotionEvent e) {
            if (mLongClick != null) {
                mLongClick.onLongClick(ZoomImageView.this);
            }
        }

        @Override
        public boolean onDown(MotionEvent e) {
            hasOverTranslate = false;
            hasMultiTouch = false;
            canRotate = false;
            removeCallbacks(mClickRunnable);
            return false;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (hasMultiTouch) {
                return false;
            }
            if (!imgLargeWidth && !imgLargeHeight) {
                return false;
            }
            if (mTranslate.isRuning) {
                return false;
            }

            float vx = velocityX;
            float vy = velocityY;

            if (Math.round(mImgRect.left) >= mWidgetRect.left || Math.round(mImgRect.right) <= mWidgetRect.right) {
                vx = 0;
            }

            if (Math.round(mImgRect.top) >= mWidgetRect.top || Math.round(mImgRect.bottom) <= mWidgetRect.bottom) {
                vy = 0;
            }

            if (canRotate || mDegrees % 90 != 0) {
                float toDegrees = (int) (mDegrees / 90) * 90;
                float remainder = mDegrees % 90;

                if (remainder > 45) {
                    toDegrees += 90;
                } else if (remainder < -45) {
                    toDegrees -= 90;
                }

                mTranslate.withRotate((int) mDegrees, (int) toDegrees);

                mDegrees = toDegrees;
            }

            doTranslateReset(mImgRect);

            mTranslate.withFling(vx, vy);

            mTranslate.start();
            // onUp(e2);
            return super.onFling(e1, e2, velocityX, velocityY);
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (mTranslate.isRuning) {
                mTranslate.stop();
            }

            if (canScrollHorizontallySelf(distanceX)) {
                if (distanceX < 0 && mImgRect.left - distanceX > mWidgetRect.left) {
                    distanceX = mImgRect.left;
                }
                if (distanceX > 0 && mImgRect.right - distanceX < mWidgetRect.right) {
                    distanceX = mImgRect.right - mWidgetRect.right;
                }

                mAnimaMatrix.postTranslate(-distanceX, 0);
                mTranslateX -= distanceX;
            } else if (imgLargeWidth || hasMultiTouch || hasOverTranslate) {
                checkRect();
                if (!hasMultiTouch) {
                    if (distanceX < 0 && mImgRect.left - distanceX > mCommonRect.left) {
                        distanceX = resistanceScrollByX(mImgRect.left - mCommonRect.left, distanceX);
                    }
                    if (distanceX > 0 && mImgRect.right - distanceX < mCommonRect.right) {
                        distanceX = resistanceScrollByX(mImgRect.right - mCommonRect.right, distanceX);
                    }
                }

                mTranslateX -= distanceX;
                mAnimaMatrix.postTranslate(-distanceX, 0);
                hasOverTranslate = true;
            }

            if (canScrollVerticallySelf(distanceY)) {
                if (distanceY < 0 && mImgRect.top - distanceY > mWidgetRect.top) {
                    distanceY = mImgRect.top;
                }
                if (distanceY > 0 && mImgRect.bottom - distanceY < mWidgetRect.bottom) {
                    distanceY = mImgRect.bottom - mWidgetRect.bottom;
                }

                mAnimaMatrix.postTranslate(0, -distanceY);
                mTranslateY -= distanceY;
            } else if (imgLargeHeight || hasOverTranslate || hasMultiTouch) {
                checkRect();
                if (!hasMultiTouch) {
                    if (distanceY < 0 && mImgRect.top - distanceY > mCommonRect.top) {
                        distanceY = resistanceScrollByY(mImgRect.top - mCommonRect.top, distanceY);
                    }
                    if (distanceY > 0 && mImgRect.bottom - distanceY < mCommonRect.bottom) {
                        distanceY = resistanceScrollByY(mImgRect.bottom - mCommonRect.bottom, distanceY);
                    }
                }

                mAnimaMatrix.postTranslate(0, -distanceY);
                mTranslateY -= distanceY;
                hasOverTranslate = true;
            }

            executeTranslate();
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            postDelayed(mClickRunnable, 250);
            return false;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {

            mTranslate.stop();

            float from = 1;
            float to = 1;

            float imgcx = mImgRect.left + mImgRect.width() / 2;
            float imgcy = mImgRect.top + mImgRect.height() / 2;

            mScaleCenter.set(imgcx, imgcy);
            mRotateCenter.set(imgcx, imgcy);
            mTranslateX = 0;
            mTranslateY = 0;

            if (isZoonUp) {
                from = mScale;
                to = 1;
            } else {
                from = mScale;
                to = mMaxScale;

                mScaleCenter.set(e.getX(), e.getY());
            }

            mTmpMatrix.reset();
            mTmpMatrix.postTranslate(-mBaseRect.left, -mBaseRect.top);
            mTmpMatrix.postTranslate(mRotateCenter.x, mRotateCenter.y);
            mTmpMatrix.postTranslate(-mHalfBaseRectWidth, -mHalfBaseRectHeight);
            mTmpMatrix.postRotate(mDegrees, mRotateCenter.x, mRotateCenter.y);
            mTmpMatrix.postScale(to, to, mScaleCenter.x, mScaleCenter.y);
            mTmpMatrix.postTranslate(mTranslateX, mTranslateY);
            mTmpMatrix.mapRect(mTmpRect, mBaseRect);
            doTranslateReset(mTmpRect);

            isZoonUp = !isZoonUp;
            mTranslate.withScale(from, to);
            mTranslate.start();

            return false;
        }
    };

    public boolean canScrollHorizontallySelf(float direction) {
        if (mImgRect.width() <= mWidgetRect.width()) {
            return false;
        }
        if (direction < 0 && Math.round(mImgRect.left) - direction >= mWidgetRect.left) {
            return false;
        }
        if (direction > 0 && Math.round(mImgRect.right) - direction <= mWidgetRect.right){
            return false;
        }
        return true;
    }

    public boolean canScrollVerticallySelf(float direction) {
        if (mImgRect.height() <= mWidgetRect.height()) {
            return false;
        }
        if (direction < 0 && Math.round(mImgRect.top) - direction >= mWidgetRect.top) {
            return false;
        }
        if (direction > 0 && Math.round(mImgRect.bottom) - direction <= mWidgetRect.bottom){
            return false;
        }
        return true;
    }

    @Override
    public boolean canScrollHorizontally(int direction) {
        if (hasMultiTouch) {
            return true;
        }
        return canScrollHorizontallySelf(direction);
    }

    @Override
    public boolean canScrollVertically(int direction) {
        if (hasMultiTouch) {
            return true;
        }
        return canScrollVerticallySelf(direction);
    }

    private class Transform implements Runnable {

        boolean isRuning;

        OverScroller mTranslateScroller;
        OverScroller mFlingScroller;
        Scroller mScaleScroller;
        Scroller mClipScroller;
        Scroller mRotateScroller;

        ClipCalculate C;

        int mLastFlingX;
        int mLastFlingY;

        int mLastTranslateX;
        int mLastTranslateY;

        RectF mClipRect = new RectF();

        InterpolatorProxy mInterpolatorProxy = new InterpolatorProxy();

        Transform() {
            Context ctx = getContext();
            mTranslateScroller = new OverScroller(ctx, mInterpolatorProxy);
            mScaleScroller = new Scroller(ctx, mInterpolatorProxy);
            mFlingScroller = new OverScroller(ctx, mInterpolatorProxy);
            mClipScroller = new Scroller(ctx, mInterpolatorProxy);
            mRotateScroller = new Scroller(ctx, mInterpolatorProxy);
        }

        public void setInterpolator(Interpolator interpolator) {
            mInterpolatorProxy.setTargetInterpolator(interpolator);
        }

        void withTranslate(int startX, int startY, int deltaX, int deltaY) {
            mLastTranslateX = 0;
            mLastTranslateY = 0;
            mTranslateScroller.startScroll(0, 0, deltaX, deltaY, mAnimaDuring);
        }

        void withScale(float form, float to) {
            mScaleScroller.startScroll((int) (form * 10000), 0, (int) ((to - form) * 10000), 0, mAnimaDuring);
        }

        void withClip(float fromX, float fromY, float deltaX, float deltaY, int d, ClipCalculate c) {
            mClipScroller.startScroll((int) (fromX * 10000), (int) (fromY * 10000), (int) (deltaX * 10000), (int) (deltaY * 10000), d);
            C = c;
        }

        void withRotate(int fromDegrees, int toDegrees) {
            mRotateScroller.startScroll(fromDegrees, 0, toDegrees - fromDegrees, 0, mAnimaDuring);
        }

        void withRotate(int fromDegrees, int toDegrees, int during) {
            mRotateScroller.startScroll(fromDegrees, 0, toDegrees - fromDegrees, 0, during);
        }

        void withFling(float velocityX, float velocityY) {
            mLastFlingX = velocityX < 0 ? Integer.MAX_VALUE : 0;
            int distanceX = (int) (velocityX > 0 ? Math.abs(mImgRect.left) : mImgRect.right - mWidgetRect.right);
            distanceX = velocityX < 0 ? Integer.MAX_VALUE - distanceX : distanceX;
            int minX = velocityX < 0 ? distanceX : 0;
            int maxX = velocityX < 0 ? Integer.MAX_VALUE : distanceX;
            int overX = velocityX < 0 ? Integer.MAX_VALUE - minX : distanceX;

            mLastFlingY = velocityY < 0 ? Integer.MAX_VALUE : 0;
            int distanceY = (int) (velocityY > 0 ? Math.abs(mImgRect.top) : mImgRect.bottom - mWidgetRect.bottom);
            distanceY = velocityY < 0 ? Integer.MAX_VALUE - distanceY : distanceY;
            int minY = velocityY < 0 ? distanceY : 0;
            int maxY = velocityY < 0 ? Integer.MAX_VALUE : distanceY;
            int overY = velocityY < 0 ? Integer.MAX_VALUE - minY : distanceY;

            if (velocityX == 0) {
                maxX = 0;
                minX = 0;
            }

            if (velocityY == 0) {
                maxY = 0;
                minY = 0;
            }

            mFlingScroller.fling(mLastFlingX, mLastFlingY, (int) velocityX, (int) velocityY, minX, maxX, minY, maxY, Math.abs(overX) < MAX_FLING_OVER_SCROLL * 2 ? 0 : MAX_FLING_OVER_SCROLL, Math.abs(overY) < MAX_FLING_OVER_SCROLL * 2 ? 0 : MAX_FLING_OVER_SCROLL);
        }

        void start() {
            isRuning = true;
            postExecute();
        }

        void stop() {
            removeCallbacks(this);
            mTranslateScroller.abortAnimation();
            mScaleScroller.abortAnimation();
            mFlingScroller.abortAnimation();
            mRotateScroller.abortAnimation();
            isRuning = false;
        }

        @Override
        public void run() {

            // if (!isRuning) return;

            boolean endAnima = true;

            if (mScaleScroller.computeScrollOffset()) {
                mScale = mScaleScroller.getCurrX() / 10000f;
                endAnima = false;
            }

            if (mTranslateScroller.computeScrollOffset()) {
                int tx = mTranslateScroller.getCurrX() - mLastTranslateX;
                int ty = mTranslateScroller.getCurrY() - mLastTranslateY;
                mTranslateX += tx;
                mTranslateY += ty;
                mLastTranslateX = mTranslateScroller.getCurrX();
                mLastTranslateY = mTranslateScroller.getCurrY();
                endAnima = false;
            }

            if (mFlingScroller.computeScrollOffset()) {
                int x = mFlingScroller.getCurrX() - mLastFlingX;
                int y = mFlingScroller.getCurrY() - mLastFlingY;

                mLastFlingX = mFlingScroller.getCurrX();
                mLastFlingY = mFlingScroller.getCurrY();

                mTranslateX += x;
                mTranslateY += y;
                endAnima = false;
            }

            if (mRotateScroller.computeScrollOffset()) {
                mDegrees = mRotateScroller.getCurrX();
                endAnima = false;
            }

            if (mClipScroller.computeScrollOffset() || mClip != null) {
                float sx = mClipScroller.getCurrX() / 10000f;
                float sy = mClipScroller.getCurrY() / 10000f;
                mTmpMatrix.setScale(sx, sy, (mImgRect.left + mImgRect.right) / 2, C.calculateTop());
                mTmpMatrix.mapRect(mClipRect, mImgRect);

                if (sx == 1) {
                    mClipRect.left = mWidgetRect.left;
                    mClipRect.right = mWidgetRect.right;
                }

                if (sy == 1) {
                    mClipRect.top = mWidgetRect.top;
                    mClipRect.bottom = mWidgetRect.bottom;
                }

                mClip = mClipRect;
            }

            if (!endAnima) {
                applyAnima();
                postExecute();
            } else {
                isRuning = false;

                // 修复动画结束后边距有些空隙，
                boolean needFix = false;

                if (imgLargeWidth) {
                    if (mImgRect.left > 0) {
                        mTranslateX -= mImgRect.left;
                    } else if (mImgRect.right < mWidgetRect.width()) {
                        mTranslateX -= (int) (mWidgetRect.width() - mImgRect.right);
                    }
                    needFix = true;
                }

                if (imgLargeHeight) {
                    if (mImgRect.top > 0) {
                        mTranslateY -= mImgRect.top;
                    } else if (mImgRect.bottom < mWidgetRect.height()) {
                        mTranslateY -= (int) (mWidgetRect.height() - mImgRect.bottom);
                    }
                    needFix = true;
                }

                if (needFix) {
                    applyAnima();
                }

                invalidate();

                if (mCompleteCallBack != null) {
                    mCompleteCallBack.run();
                    mCompleteCallBack = null;
                }
            }
        }

        private void applyAnima() {
            mAnimaMatrix.reset();
            mAnimaMatrix.postTranslate(-mBaseRect.left, -mBaseRect.top);
            mAnimaMatrix.postTranslate(mRotateCenter.x, mRotateCenter.y);
            mAnimaMatrix.postTranslate(-mHalfBaseRectWidth, -mHalfBaseRectHeight);
            mAnimaMatrix.postRotate(mDegrees, mRotateCenter.x, mRotateCenter.y);
            mAnimaMatrix.postScale(mScale, mScale, mScaleCenter.x, mScaleCenter.y);
            mAnimaMatrix.postTranslate(mTranslateX, mTranslateY);
            executeTranslate();
        }


        private void postExecute() {
            if (isRuning) {
                post(this);
            }
        }
    }

    public ZoomImageInfo getInfo() {
        RectF rect = new RectF();
        int[] p = new int[2];
        ZoomImageUtils.getLocation(this, p);
        rect.set(p[0] + mImgRect.left, p[1] + mImgRect.top, p[0] + mImgRect.right, p[1] + mImgRect.bottom);
        return new ZoomImageInfo(rect, mImgRect, mWidgetRect, mBaseRect, mScreenCenter, mScale, mDegrees, mScaleType);
    }

    private void reset() {
        mAnimaMatrix.reset();
        executeTranslate();
        mScale = 1;
        mTranslateX = 0;
        mTranslateY = 0;
    }

    public interface ClipCalculate {
        float calculateTop();
    }

    public class START implements ClipCalculate {
        @Override
        public float calculateTop() {
            return mImgRect.top;
        }
    }

    public class END implements ClipCalculate {
        @Override
        public float calculateTop() {
            return mImgRect.bottom;
        }
    }

    public class OTHER implements ClipCalculate {
        @Override
        public float calculateTop() {
            return (mImgRect.top + mImgRect.bottom) / 2;
        }
    }

    /**
     * 在PhotoView内部还没有图片的时候同样可以调用该方法
     * <p></p>
     * 此时并不会播放动画，当给PhotoView设置图片后会自动播放动画。
     * <p></p>
     * 若等待时间过长也没有给控件设置图片，则会忽略该动画，若要再次播放动画则需要重新调用该方法
     * (等待的时间默认500毫秒，可以通过setMaxAnimFromWaiteTime(int)设置最大等待时间)
     */
    public void animaFrom(ZoomImageInfo info) {
        if (isInit) {
            reset();

            ZoomImageInfo mine = getInfo();

            float scaleX = info.mImgRect.width() / mine.mImgRect.width();
            float scaleY = info.mImgRect.height() / mine.mImgRect.height();
            float scale = scaleX < scaleY ? scaleX : scaleY;

            float ocx = info.mRect.left + info.mRect.width() / 2;
            float ocy = info.mRect.top + info.mRect.height() / 2;

            float mcx = mine.mRect.left + mine.mRect.width() / 2;
            float mcy = mine.mRect.top + mine.mRect.height() / 2;

            mAnimaMatrix.reset();
            // mAnimaMatrix.postTranslate(-mBaseRect.left, -mBaseRect.top);
            mAnimaMatrix.postTranslate(ocx - mcx, ocy - mcy);
            mAnimaMatrix.postScale(scale, scale, ocx, ocy);
            mAnimaMatrix.postRotate(info.mDegrees, ocx, ocy);
            executeTranslate();

            mScaleCenter.set(ocx, ocy);
            mRotateCenter.set(ocx, ocy);

            mTranslate.withTranslate(0, 0, (int) -(ocx - mcx), (int) -(ocy - mcy));
            mTranslate.withScale(scale, 1);
            mTranslate.withRotate((int) info.mDegrees, 0);

            if (info.mWidgetRect.width() < info.mImgRect.width() || info.mWidgetRect.height() < info.mImgRect.height()) {
                float clipX = info.mWidgetRect.width() / info.mImgRect.width();
                float clipY = info.mWidgetRect.height() / info.mImgRect.height();
                clipX = clipX > 1 ? 1 : clipX;
                clipY = clipY > 1 ? 1 : clipY;

                ClipCalculate c = info.mScaleType == ScaleType.FIT_START ? new START() : info.mScaleType == ScaleType.FIT_END ? new END() : new OTHER();

                mTranslate.withClip(clipX, clipY, 1 - clipX, 1 - clipY, mAnimaDuring / 3, c);

                mTmpMatrix.setScale(clipX, clipY, (mImgRect.left + mImgRect.right) / 2, c.calculateTop());
                mTmpMatrix.mapRect(mTranslate.mClipRect, mImgRect);
                mClip = mTranslate.mClipRect;
            }

            mTranslate.start();
        } else {
            mFromInfo = info;
            mInfoTime = System.currentTimeMillis();
        }
    }

    public void animaTo(ZoomImageInfo info, Runnable completeCallBack) {
        if (isInit) {
            mTranslate.stop();

            mTranslateX = 0;
            mTranslateY = 0;

            float tcx = info.mRect.left + info.mRect.width() / 2;
            float tcy = info.mRect.top + info.mRect.height() / 2;

            mScaleCenter.set(mImgRect.left + mImgRect.width() / 2, mImgRect.top + mImgRect.height() / 2);
            mRotateCenter.set(mScaleCenter);

            // 将图片旋转回正常位置，用以计算
            mAnimaMatrix.postRotate(-mDegrees, mScaleCenter.x, mScaleCenter.y);
            mAnimaMatrix.mapRect(mImgRect, mBaseRect);

            // 缩放
            float scaleX = info.mImgRect.width() / mBaseRect.width();
            float scaleY = info.mImgRect.height() / mBaseRect.height();
            float scale = scaleX > scaleY ? scaleX : scaleY;

            mAnimaMatrix.postRotate(mDegrees, mScaleCenter.x, mScaleCenter.y);
            mAnimaMatrix.mapRect(mImgRect, mBaseRect);

            mDegrees = mDegrees % 360;

            mTranslate.withTranslate(0, 0, (int) (tcx - mScaleCenter.x), (int) (tcy - mScaleCenter.y));
            mTranslate.withScale(mScale, scale);
            mTranslate.withRotate((int) mDegrees, (int) info.mDegrees, mAnimaDuring * 2 / 3);

            if (info.mWidgetRect.width() < info.mRect.width() || info.mWidgetRect.height() < info.mRect.height()) {
                float clipX = info.mWidgetRect.width() / info.mRect.width();
                float clipY = info.mWidgetRect.height() / info.mRect.height();
                clipX = clipX > 1 ? 1 : clipX;
                clipY = clipY > 1 ? 1 : clipY;

                final float cx = clipX;
                final float cy = clipY;
                final ClipCalculate c = info.mScaleType == ScaleType.FIT_START ? new START() : info.mScaleType == ScaleType.FIT_END ? new END() : new OTHER();

                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mTranslate.withClip(1, 1, -1 + cx, -1 + cy, mAnimaDuring / 2, c);
                    }
                }, mAnimaDuring / 2);
            }

            mCompleteCallBack = completeCallBack;
            mTranslate.start();
        }
    }

    public void rotate(float degrees) {
        mDegrees += degrees;
        int centerX = (int) (mWidgetRect.left + mWidgetRect.width() / 2);
        int centerY = (int) (mWidgetRect.top + mWidgetRect.height() /2);

        mAnimaMatrix.postRotate(degrees, centerX, centerY);
        executeTranslate();
    }
}

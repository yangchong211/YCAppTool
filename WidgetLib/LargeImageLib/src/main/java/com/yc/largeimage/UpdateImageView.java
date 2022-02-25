
package com.yc.largeimage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.ViewCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

import com.yc.largeimage.BlockImageLoader.DrawData;
import com.yc.largeimage.BlockImageLoader.OnImageLoadListener;
import com.yc.largeimage.factory.BitmapDecoderFactory;

import java.util.ArrayList;
import java.util.List;

public class UpdateImageView extends UpdateView implements OnImageLoadListener, ILargeImageView {

    private OnImageLoadListener onImageLoadListener;
    private int mDrawableWidth;
    private int mDrawableHeight;
    private int mLevel;

    public UpdateImageView(Context context) {
        this(context, null);
    }

    public UpdateImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UpdateImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        imageBlockLoader = new BlockImageLoader(context);
        imageBlockLoader.setOnImageLoadListener(this);
    }


    private float mScale;
    private float mOffsetX;
    private float mOffsetY;

    public void setScale(float scale) {
        this.mScale = scale;
        notifyInvalidate();
    }

    @Override
    public float getScale() {
        return mScale;
    }

    @Override
    public OnImageLoadListener getOnImageLoadListener() {
        return onImageLoadListener;
    }

    public void setScale(float scale, float offsetX, float offsetY) {
        this.mScale = scale;
        this.mOffsetX = offsetX;
        this.mOffsetY = offsetY;
        notifyInvalidate();
    }

    @Override
    public int getImageWidth() {
        if (mDrawable != null) {
            return mDrawable.getIntrinsicWidth();
        }
        return imageBlockLoader.getWidth();
    }

    @Override
    public int getImageHeight() {
        if (mDrawable != null) {
            return mDrawable.getIntrinsicHeight();
        }
        return imageBlockLoader.getHeight();
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        resizeFromDrawable();
    }

    private void resizeFromDrawable() {
        Drawable d = mDrawable;
        if (d != null) {
            int w = d.getIntrinsicWidth();
            if (w < 0) w = mDrawableWidth;
            int h = d.getIntrinsicHeight();
            if (h < 0) h = mDrawableHeight;
            if (w != mDrawableWidth || h != mDrawableHeight) {
                mDrawableWidth = w;
                mDrawableHeight = h;
                requestLayout();
            }
        }
    }

    private BlockImageLoader imageBlockLoader;

    private boolean isAttachedWindow = false;

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        isAttachedWindow = true;
        if (mDrawable != null) {
            mDrawable.setVisible(getVisibility() == VISIBLE, false);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isAttachedWindow = false;
        imageBlockLoader.stopLoad();
        if (mDrawable != null) {
            mDrawable.setVisible(false, false);
        }
    }

    @Override
    public boolean hasLoad() {
        if (mDrawable != null) {
            return true;
        } else if (mFactory != null) {
            if (defaultDrawable != null) {
                return true;
            }
            return imageBlockLoader.hasLoad();
        }
        return false;
    }

    @Override
    public void setOnImageLoadListener(OnImageLoadListener onImageLoadListener) {
        this.onImageLoadListener = onImageLoadListener;
    }

    public void setOnLoadStateChangeListener(BlockImageLoader.OnLoadStateChangeListener onLoadStateChangeListener) {
        if (imageBlockLoader != null) {
            imageBlockLoader.setOnLoadStateChangeListener(onLoadStateChangeListener);
        }
    }

    @Override
    public void setImage(BitmapDecoderFactory factory) {
        setImage(factory, null);
    }

    @Override
    public void setImage(BitmapDecoderFactory factory, Drawable defaultDrawable) {
        mScale = 1;
        mOffsetX = 0;
        mOffsetY = 0;
        mDrawable = null;
        this.mFactory = factory;
        this.defaultDrawable = defaultDrawable;
        if (defaultDrawable != null) {
            onLoadImageSize(defaultDrawable.getIntrinsicWidth(), defaultDrawable.getIntrinsicHeight());
        }
        imageBlockLoader.setBitmapDecoderFactory(factory);
        invalidate();
    }

    private Drawable defaultDrawable;

    private Drawable mDrawable;
    private BitmapDecoderFactory mFactory;

    /**
     * Sets a Bitmap as the content of this ImageView.
     *
     * @param bm The bitmap to set
     */
    @Override
    public void setImage(Bitmap bm) {
        setImageDrawable(new BitmapDrawable(getResources(), bm));
    }

    @Override
    public void setImage(Drawable drawable) {
        setImageDrawable(drawable);
    }

    @Override
    public void setImage(@DrawableRes int resId) {
        setImageDrawable(ContextCompat.getDrawable(getContext(), resId));
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        mFactory = null;
        mScale = 1;
        mOffsetX = 0;
        mOffsetY = 0;
        if (mDrawable != drawable) {
            final int oldWidth = mDrawableWidth;
            final int oldHeight = mDrawableHeight;
            updateDrawable(drawable);
            onLoadImageSize(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            if (oldWidth != mDrawableWidth || oldHeight != mDrawableHeight) {
                requestLayout();
            }
            invalidate();
        }
    }


    private void updateDrawable(Drawable d) {
        boolean sameDrawable = false;
        boolean sCompatDrawableVisibilityDispatch = false;

        if (mDrawable != null) {
            sameDrawable = mDrawable == d;
            mDrawable.setCallback(null);
            unscheduleDrawable(mDrawable);
            if (!sCompatDrawableVisibilityDispatch && !sameDrawable && isAttachedWindow) {
                mDrawable.setVisible(false, false);
            }
        }

        mDrawable = d;

        if (d != null) {
            d.setCallback(this);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                d.setLayoutDirection(getLayoutDirection());
            }
            if (d.isStateful()) {
                d.setState(getDrawableState());
            }
            if (!sameDrawable || sCompatDrawableVisibilityDispatch) {
                final boolean visible = sCompatDrawableVisibilityDispatch
                        ? getVisibility() == VISIBLE
                        : isAttachedWindow && getWindowVisibility() == VISIBLE && isShown();
                d.setVisible(visible, true);
            }
            d.setLevel(mLevel);
            mDrawableWidth = d.getIntrinsicWidth();
            mDrawableHeight = d.getIntrinsicHeight();
//            applyImageTint();
//            applyColorMod();
//
//            configureBounds();
        } else {
            mDrawableWidth = mDrawableHeight = -1;
        }
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        Drawable d = mDrawable;
        if (d != null && d.isStateful()) {
            d.setState(getDrawableState());
        }
    }

    @Override
    public void drawableHotspotChanged(float x, float y) {
        super.drawableHotspotChanged(x, y);
        if (mDrawable != null) {
            DrawableCompat.setHotspot(mDrawable, x, y);
        }
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (mDrawable != null) {
            mDrawable.setVisible(visibility == VISIBLE, false);
        }
    }

    @Override
    protected void onUpdateWindow(Rect visibilityRect) {
        //只有大图滑动的时候才需要重绘
        if (mFactory != null && hasLoad()) {
            notifyInvalidate();
        }
    }

    private void notifyInvalidate() {
        ViewCompat.postInvalidateOnAnimation(this);
    }

    private Rect tempVisibilityRect = new Rect();
    private Rect tempImageRect = new Rect();

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int viewWidth = getWidth();
        int viewHeight = getHeight();
        if (viewWidth == 0) {
            return;
        }
        if (mDrawable != null) {
            mDrawable.setBounds((int) mOffsetX, (int) mOffsetY, (int) (viewWidth * mScale), (int) (viewHeight * mScale));
            mDrawable.draw(canvas);
        } else if (mFactory != null) {
            Rect visibilityRect = tempVisibilityRect;
            getVisibilityRect(visibilityRect);
            float width = mScale * viewWidth;
            int imgWidth = imageBlockLoader.getWidth();

            float imageScale = imgWidth / width;

            // 需要显示的图片的实际宽度。
            Rect imageRect = tempImageRect;
            imageRect.left = (int) Math.ceil((visibilityRect.left - mOffsetX) * imageScale);
            imageRect.top = (int) Math.ceil((visibilityRect.top - mOffsetY) * imageScale);
            imageRect.right = (int) Math.ceil((visibilityRect.right - mOffsetX) * imageScale);
            imageRect.bottom = (int) Math.ceil((visibilityRect.bottom - mOffsetY) * imageScale);

            //如果是大图就需要继续加载图片块，如果不是大图直接用默认的
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            if (defaultDrawable == null || (imageBlockLoader.hasLoad() && (imageBlockLoader.getWidth() * imageBlockLoader.getHeight() > (displayMetrics.widthPixels * displayMetrics.heightPixels)))) {
                imageBlockLoader.loadImageBlocks(drawDataList, imageScale, imageRect, viewWidth, viewHeight);
            }

            if (drawDataList.isEmpty()) {
                if (defaultDrawable != null) {
                    int height = (int) (1.0f * defaultDrawable.getIntrinsicHeight() / defaultDrawable.getIntrinsicWidth() * viewWidth);
                    int offset = (viewHeight - height) / 2;
                    defaultDrawable.setBounds((int) mOffsetX, (int) mOffsetY + offset, (int) (viewWidth * mScale), (int) (height * mScale));
                    defaultDrawable.draw(canvas);
                }
            } else {
                int saveCount = canvas.save();
                for (DrawData data : drawDataList) {
                    Rect drawRect = data.imageRect;
                    drawRect.left = (int) (Math.ceil(drawRect.left / imageScale) + mOffsetX);
                    drawRect.top = (int) (Math.ceil(drawRect.top / imageScale) + mOffsetY);
                    drawRect.right = (int) (Math.ceil(drawRect.right / imageScale) + mOffsetX);
                    drawRect.bottom = (int) (Math.ceil(drawRect.bottom / imageScale) + mOffsetY);
                    canvas.drawBitmap(data.bitmap, data.srcRect, drawRect, null);
                }
                canvas.restoreToCount(saveCount);
            }
        }
    }

    private List<DrawData> drawDataList = new ArrayList<>();

    @Override
    public void onBlockImageLoadFinished() {
        notifyInvalidate();
        if (onImageLoadListener != null) {
            onImageLoadListener.onBlockImageLoadFinished();
        }
    }

    @Override
    public void onLoadImageSize(final int imageWidth, final int imageHeight) {
        mDrawableWidth = imageWidth;
        mDrawableHeight = imageHeight;
        notifyInvalidate();
        if (onImageLoadListener != null) {
            onImageLoadListener.onLoadImageSize(imageWidth, imageHeight);
        }
    }

    @Override
    public void onLoadFail(Exception e) {
        if (onImageLoadListener != null) {
            onImageLoadListener.onLoadFail(e);
        }
    }


}

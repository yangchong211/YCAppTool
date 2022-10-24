package com.yc.blurview.view;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.ApplicationInfo;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RSRuntimeException;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import android.util.AttributeSet;
import android.util.StateSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;


import com.com.yc.blurview.R;
import com.yc.blurview.view.enu.BlurCorner;
import com.yc.blurview.view.enu.BlurMode;
import com.yc.blurview.view.impl.AndroidStockBlurImpl;
import com.yc.blurview.view.impl.AndroidXBlurImpl;
import com.yc.blurview.view.impl.BlurImpl;
import com.yc.blurview.view.impl.EmptyBlurImpl;
import com.yc.blurview.view.impl.SupportLibraryBlurImpl;


/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/8/22
 * 描    述：模糊视图，毛玻璃自定义控件
 * 修订历史：
 * ================================================
 */
public class RealTimeBlurView extends View {

	/**
	 * 模糊视图
	 * 1.使用高斯模糊，太复杂了，网上绝大部分都是这样，这种做法依赖硬件绘制
	 * 2.自定义控件取得背景图片自己通过canvas画出来，简单
	 * 实现了 ViewTreeObserver.OnPreDrawListener View的这个接口：此接口是系统要绘制视图树的时候调用的方法。
	 * 当系统要开始绘制视图树，说明视图已经改变，这样就做到了实时更新，
	 * 接下来要做的就是虚化，然后drawCanvas（bitmap），更新invalidate();
	 */
	private Context mContext;

	/**
	 * default 4
	 */
	private float mDownSampleFactor;
	/**
	 * default #000000
	 */
	private int mOverlayColor;
	/**
	 * default 10dp (0 < r <= 25)
	 */
	private float mBlurRadius;
	public static final int DEFAULT_BORDER_COLOR = Color.WHITE;
	private final BlurImpl mBlurImpl;
	private boolean mDirty;
	private Bitmap mBitmapToBlur, mBlurredBitmap;
	private Canvas mBlurringCanvas;
	private boolean mIsRendering;


	private final Rect mRectSrc = new Rect();
	private final RectF mRectFDst = new RectF();
	/**
	 * mDecorView should be the root view of the activity (even if you are on a different window like a dialog)
	 */
	private View mDecorView;
	/**
	 * If the view is on different root view (usually means we are on a PopupWindow),
	 * we need to manually call invalidate() in onPreDraw(), otherwise we will not be able to see the changes
	 */
	private boolean mDifferentRoot;
	private static int RENDERING_COUNT;
	private static int BLUR_IMPL;

	private int blurMode = BlurMode.MODE_RECTANGLE;
	private final Paint mBitmapPaint;
	//圆形 相关
	private float cx = 0, cy = 0, cRadius = 0;

	//圆角相关
	private static final float DEFAULT_RADIUS = 0f;
	private final float[] mCornerRadii = new float[]{DEFAULT_RADIUS, DEFAULT_RADIUS, DEFAULT_RADIUS, DEFAULT_RADIUS};
	private final Path cornerPath = new Path();
	private float[] cornerRids;

	//边框相关
	private static final float DEFAULT_BORDER_WIDTH = 0f;

	private final RectF mBorderRect = new RectF();
	private final Paint mBorderPaint;
	private float mBorderWidth = 0;
	private ColorStateList mBorderColor = ColorStateList.valueOf(DEFAULT_BORDER_COLOR);
	private Matrix matrix = new Matrix();
	private BitmapShader shader;

	public RealTimeBlurView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		// provide your own by override getBlurImpl()
		mBlurImpl = getBlurImpl();
		try {
			TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ShapeBlurView);
			mBlurRadius = a.getDimension(R.styleable.ShapeBlurView_blur_radius,
					TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, context.getResources().getDisplayMetrics()));
			mDownSampleFactor = a.getFloat(R.styleable.ShapeBlurView_blur_down_sample, 4);
			mOverlayColor = a.getColor(R.styleable.ShapeBlurView_blur_overlay_color, 0x000000);

			float cornerRadiusOverride =
					a.getDimensionPixelSize(R.styleable.ShapeBlurView_blur_corner_radius, -1);
			mCornerRadii[BlurCorner.TOP_LEFT] =
					a.getDimensionPixelSize(R.styleable.ShapeBlurView_blur_corner_radius_top_left, -1);
			mCornerRadii[BlurCorner.TOP_RIGHT] =
					a.getDimensionPixelSize(R.styleable.ShapeBlurView_blur_corner_radius_top_right, -1);
			mCornerRadii[BlurCorner.BOTTOM_RIGHT] =
					a.getDimensionPixelSize(R.styleable.ShapeBlurView_blur_corner_radius_bottom_right, -1);
			mCornerRadii[BlurCorner.BOTTOM_LEFT] =
					a.getDimensionPixelSize(R.styleable.ShapeBlurView_blur_corner_radius_bottom_left, -1);
			initCornerData(cornerRadiusOverride);
			blurMode = a.getInt(R.styleable.ShapeBlurView_blur_mode, BlurMode.MODE_RECTANGLE);

			mBorderWidth = a.getDimensionPixelSize(R.styleable.ShapeBlurView_blur_border_width, -1);
			if (mBorderWidth < 0) {
				mBorderWidth = DEFAULT_BORDER_WIDTH;
			}
			mBorderColor = a.getColorStateList(R.styleable.ShapeBlurView_blur_border_color);
			if (mBorderColor == null) {
				mBorderColor = ColorStateList.valueOf(DEFAULT_BORDER_COLOR);
			}


			a.recycle();
		} catch (Exception e) {
			e.printStackTrace();
		}
		mBitmapPaint = new Paint();
//        mBitmapPaint.setStyle(Paint.Style.FILL);
		mBitmapPaint.setAntiAlias(true);

		mBorderPaint = new Paint();
		mBorderPaint.setStyle(Paint.Style.STROKE);
		mBorderPaint.setAntiAlias(true);
		mBorderPaint.setColor(mBorderColor.getColorForState(getState(), DEFAULT_BORDER_COLOR));
		mBorderPaint.setStrokeWidth(mBorderWidth);

//        matrix = new Matrix();
	}

	private void initCornerData(float cornerRadiusOverride) {
		boolean any = false;
		for (int i = 0, len = mCornerRadii.length; i < len; i++) {
			if (mCornerRadii[i] < 0) {
				mCornerRadii[i] = 0f;
			} else {
				any = true;
			}
		}
		if (!any) {
			if (cornerRadiusOverride < 0) {
				cornerRadiusOverride = DEFAULT_RADIUS;
			}
			for (int i = 0, len = mCornerRadii.length; i < len; i++) {
				mCornerRadii[i] = cornerRadiusOverride;
			}
		}
		initCornerRids();
	}

	private void initCornerRids() {
		if (cornerRids == null) {
			cornerRids = new float[]{mCornerRadii[BlurCorner.TOP_LEFT], mCornerRadii[BlurCorner.TOP_LEFT],
					mCornerRadii[BlurCorner.TOP_RIGHT], mCornerRadii[BlurCorner.TOP_RIGHT],
					mCornerRadii[BlurCorner.BOTTOM_RIGHT], mCornerRadii[BlurCorner.BOTTOM_RIGHT],
					mCornerRadii[BlurCorner.BOTTOM_LEFT], mCornerRadii[BlurCorner.BOTTOM_LEFT]};
		} else {
			cornerRids[0] = mCornerRadii[BlurCorner.TOP_LEFT];
			cornerRids[1] = mCornerRadii[BlurCorner.TOP_LEFT];
			cornerRids[2] = mCornerRadii[BlurCorner.TOP_RIGHT];
			cornerRids[3] = mCornerRadii[BlurCorner.TOP_RIGHT];
			cornerRids[4] = mCornerRadii[BlurCorner.BOTTOM_RIGHT];
			cornerRids[5] = mCornerRadii[BlurCorner.BOTTOM_RIGHT];
			cornerRids[6] = mCornerRadii[BlurCorner.BOTTOM_LEFT];
			cornerRids[7] = mCornerRadii[BlurCorner.BOTTOM_LEFT];
		}
	}

	protected BlurImpl getBlurImpl() {
		if (BLUR_IMPL == 0) {
			// try to use stock impl first
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
				try {
					AndroidStockBlurImpl impl = new AndroidStockBlurImpl();
					Bitmap bmp = Bitmap.createBitmap(4, 4, Bitmap.Config.ARGB_8888);
					impl.prepare(getContext(), bmp, 4);
					impl.release();
					bmp.recycle();
					BLUR_IMPL = 3;
				} catch (Throwable e) {
				}
			}
		}
		if (BLUR_IMPL == 0) {
			try {
				getClass().getClassLoader().loadClass("androidx.renderscript.RenderScript");
				// initialize RenderScript to load jni impl
				// may throw unsatisfied link error
				AndroidXBlurImpl impl = new AndroidXBlurImpl();
				Bitmap bmp = Bitmap.createBitmap(4, 4, Bitmap.Config.ARGB_8888);
				impl.prepare(getContext(), bmp, 4);
				impl.release();
				bmp.recycle();
				BLUR_IMPL = 1;
			} catch (Throwable e) {
				// class not found or unsatisfied link
			}
		}
		if (BLUR_IMPL == 0) {
			try {
				getClass().getClassLoader().loadClass("android.support.v8.renderscript.RenderScript");
				// initialize RenderScript to load jni impl
				// may throw unsatisfied link error
				SupportLibraryBlurImpl impl = new SupportLibraryBlurImpl();
				Bitmap bmp = Bitmap.createBitmap(4, 4, Bitmap.Config.ARGB_8888);
				impl.prepare(getContext(), bmp, 4);
				impl.release();
				bmp.recycle();
				BLUR_IMPL = 2;
			} catch (Throwable e) {
				// class not found or unsatisfied link
			}
		}
		if (BLUR_IMPL == 0) {
			// fallback to empty impl, which doesn't have blur effect
			BLUR_IMPL = -1;
		}
		switch (BLUR_IMPL) {
			case 1:
				return new AndroidXBlurImpl();
			case 2:
				return new SupportLibraryBlurImpl();
			case 3:
				return new AndroidStockBlurImpl();
			default:
				return new EmptyBlurImpl();
		}
	}

	public void setBlurRadius(@FloatRange(from = 0, to = 25) float radius) {
		if (mBlurRadius != radius) {
			mBlurRadius = radius;
			mDirty = true;
			invalidate();
		}
	}

	public void setDownSampleFactor(float factor) {
		if (factor <= 0) {
			throw new IllegalArgumentException("DownSample factor must be greater than 0.");
		}
		if (mDownSampleFactor != factor) {
			mDownSampleFactor = factor;
			// may also change blur radius
			mDirty = true;
			releaseBitmap();
			invalidate();
		}
	}

	public void setOverlayColor(int color) {
		if (mOverlayColor != color) {
			mOverlayColor = color;
			invalidate();
		}
	}

	/**
	 * Set all the corner radii from a dimension resource id.
	 *
	 * @param resId dimension resource id of radii.
	 */
	public void setCornerRadiusDimen(@DimenRes int resId) {
		float radius = getResources().getDimension(resId);
		setCornerRadius(radius, radius, radius, radius);
	}

	/**
	 * Set the corner radius of a specific corner in px.
	 *
	 * @param radius
	 */
	public void setCornerRadius(float radius) {
		setCornerRadius(radius, radius, radius, radius);
	}

	/**
	 * Set the corner radius of a specific corner in px.
	 */
	public void setCornerRadius(@BlurCorner int corner, float radius) {
		if (mCornerRadii[corner] == radius) {
			return;
		}
		mCornerRadii[corner] = radius;
		initCornerRids();
		invalidate();
	}

	/**
	 * Set the corner radius of a specific corner in px.
	 */
	public void setCornerRadius(float topLeft, float topRight, float bottomLeft, float bottomRight) {
		if (mCornerRadii[BlurCorner.TOP_LEFT] == topLeft
				&& mCornerRadii[BlurCorner.TOP_RIGHT] == topRight
				&& mCornerRadii[BlurCorner.BOTTOM_RIGHT] == bottomRight
				&& mCornerRadii[BlurCorner.BOTTOM_LEFT] == bottomLeft) {
			return;
		}
		mCornerRadii[BlurCorner.TOP_LEFT] = topLeft;
		mCornerRadii[BlurCorner.TOP_RIGHT] = topRight;
		mCornerRadii[BlurCorner.BOTTOM_LEFT] = bottomLeft;
		mCornerRadii[BlurCorner.BOTTOM_RIGHT] = bottomRight;
		initCornerRids();
		invalidate();
	}

	/**
	 * @return the largest corner radius.
	 */
	public float getCornerRadius() {
		return getMaxCornerRadius();
	}

	/**
	 * @return the largest corner radius.
	 */
	public float getMaxCornerRadius() {
		float maxRadius = 0;
		for (float r : mCornerRadii) {
			maxRadius = Math.max(r, maxRadius);
		}
		return maxRadius;
	}


	public float getBorderWidth() {
		return mBorderWidth;
	}

	public void setBorderWidth(@DimenRes int resId) {
		setBorderWidth(getResources().getDimension(resId));
	}

	public void setBorderWidth(float width) {
		if (mBorderWidth == width) {
			return;
		}
		mBorderWidth = width;
		invalidate();
	}

	@ColorInt
	public int getBorderColor() {
		return mBorderColor.getDefaultColor();
	}

    public void setBorderColor(@ColorInt int color) {
        setBorderColor(ColorStateList.valueOf(color));
    }

    public void setBorderColor(ColorStateList colors) {
        if (mBorderColor.equals(colors)) {
            return;
        }
        mBorderColor = (colors != null) ? colors : ColorStateList.valueOf(DEFAULT_BORDER_COLOR);
        mBorderPaint.setColor(mBorderColor.getColorForState(getState(), DEFAULT_BORDER_COLOR));
        if (mBorderWidth > 0) {
            invalidate();
        }
    }

	@BlurMode
	public int getBlurMode() {
		return this.blurMode;
	}

    public void setBlurMode(@BlurMode int blurMode) {
        if (this.blurMode == blurMode) {
            return;
        }
        this.blurMode = blurMode;
        invalidate();
    }

	private void releaseBitmap() {
		if (mBitmapToBlur != null) {
			mBitmapToBlur.recycle();
			mBitmapToBlur = null;
		}
		if (mBlurredBitmap != null) {
			mBlurredBitmap.recycle();
			mBlurredBitmap = null;
		}
		if (matrix != null) {
			matrix = null;
		}
		if (shader != null) {
			shader = null;
		}
		mContext = null;
	}

	protected void release() {
		releaseBitmap();
		mBlurImpl.release();
	}

	protected boolean prepare() {
		if (mBlurRadius == 0) {
			release();
			return false;
		}
		float downSampleFactor = mDownSampleFactor;
		float radius = mBlurRadius / downSampleFactor;
		if (radius > 25) {
			downSampleFactor = downSampleFactor * radius / 25;
			radius = 25;
		}
		final int width = getWidth();
		final int height = getHeight();
		int scaledWidth = Math.max(1, (int) (width / downSampleFactor));
		int scaledHeight = Math.max(1, (int) (height / downSampleFactor));
		boolean dirty = mDirty;
		if (mBlurringCanvas == null || mBlurredBitmap == null
				|| mBlurredBitmap.getWidth() != scaledWidth
				|| mBlurredBitmap.getHeight() != scaledHeight) {
			dirty = true;
			releaseBitmap();
			boolean r = false;
			try {
				mBitmapToBlur = Bitmap.createBitmap(scaledWidth, scaledHeight, Bitmap.Config.ARGB_8888);
				if (mBitmapToBlur == null) {
					return false;
				}
				mBlurringCanvas = new Canvas(mBitmapToBlur);
				mBlurredBitmap = Bitmap.createBitmap(scaledWidth, scaledHeight, Bitmap.Config.ARGB_8888);
				if (mBlurredBitmap == null) {
					return false;
				}
				r = true;
			} catch (OutOfMemoryError e) {
				// Bitmap.createBitmap() may cause OOM error
				// Simply ignore and fallback
			} finally {
				if (!r) {
					release();
					return false;
				}
			}
		}
		if (dirty) {
			if (mBlurImpl.prepare(getContext(), mBitmapToBlur, radius)) {
				mDirty = false;
			} else {
				return false;
			}
		}
		return true;
	}

	protected void blur(Bitmap bitmapToBlur, Bitmap blurredBitmap) {
		shader = new BitmapShader(blurredBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
		mBlurImpl.blur(bitmapToBlur, blurredBitmap);
	}

	private final ViewTreeObserver.OnPreDrawListener preDrawListener = new ViewTreeObserver.OnPreDrawListener() {
		@Override
		public boolean onPreDraw() {
			final int[] locations = new int[2];
			Bitmap oldBmp = mBlurredBitmap;
			View decor = mDecorView;
			if (decor != null && isShown() && prepare()) {
				boolean redrawBitmap = mBlurredBitmap != oldBmp;
				oldBmp = null;
				decor.getLocationOnScreen(locations);
				int x = -locations[0];
				int y = -locations[1];
				getLocationOnScreen(locations);
				x += locations[0];
				y += locations[1];
				// just erase transparent
				mBitmapToBlur.eraseColor(mOverlayColor & 0xffffff);
				int rc = mBlurringCanvas.save();
				mIsRendering = true;
				RENDERING_COUNT++;
				try {
					mBlurringCanvas.scale(1.f * mBitmapToBlur.getWidth() / getWidth(), 1.f * mBitmapToBlur.getHeight() / getHeight());
					mBlurringCanvas.translate(-x, -y);
					if (decor.getBackground() != null) {
						decor.getBackground().draw(mBlurringCanvas);
					}
					decor.draw(mBlurringCanvas);
				} catch (RealTimeBlurView.StopException e) {
				} finally {
					mIsRendering = false;
					RENDERING_COUNT--;
					mBlurringCanvas.restoreToCount(rc);
				}
				blur(mBitmapToBlur, mBlurredBitmap);
				if (redrawBitmap || mDifferentRoot) {
					invalidate();
				}
			}

			return true;
		}
	};

	protected View getActivityDecorView() {
		Context ctx = getContext();
		for (int i = 0; i < 4 && !(ctx instanceof Activity) && ctx instanceof ContextWrapper; i++) {
			ctx = ((ContextWrapper) ctx).getBaseContext();
		}
		if (ctx instanceof Activity) {
			return ((Activity) ctx).getWindow().getDecorView();
		} else {
			return null;
		}
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		mDecorView = getActivityDecorView();
		if (mDecorView != null) {
			mDecorView.getViewTreeObserver().addOnPreDrawListener(preDrawListener);
			mDifferentRoot = mDecorView.getRootView() != getRootView();
			if (mDifferentRoot) {
				mDecorView.postInvalidate();
			}
		} else {
			mDifferentRoot = false;
		}
	}

	@Override
	protected void onDetachedFromWindow() {
		if (mDecorView != null) {
			mDecorView.getViewTreeObserver().removeOnPreDrawListener(preDrawListener);
		}
		release();
		super.onDetachedFromWindow();
	}

	@Override
	public void draw(Canvas canvas) {
		if (mIsRendering) {
			// Quit here, don't draw views above me
			throw STOP_EXCEPTION;
		} else if (RENDERING_COUNT > 0) {
			// Doesn't support blurview overlap on another blurview
		} else {
			super.draw(canvas);
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		drawBlurredBitmap(canvas, mBlurredBitmap, mOverlayColor);
	}

	/**
	 * Custom draw the blurred bitmap and color to define your own shape
	 *
	 * @param canvas
	 * @param blurBitmap
	 * @param overlayColor
	 */
	protected void drawBlurredBitmap(Canvas canvas, Bitmap blurBitmap, int overlayColor) {
		if (blurBitmap != null) {
			if (blurMode == BlurMode.MODE_CIRCLE) {
				drawCircleRectBitmap(canvas, blurBitmap, overlayColor);
			} else if (blurMode == BlurMode.MODE_OVAL) {
				drawOvalRectBitmap(canvas, blurBitmap, overlayColor);
			} else {
				drawRoundRectBitmap(canvas, blurBitmap, overlayColor);
			}
		}
	}

	/**
	 * 默认或者画矩形可带圆角
	 *
	 * @param canvas
	 * @param blurBitmap
	 * @param overlayColor
	 */
	private void drawRoundRectBitmap(Canvas canvas, Bitmap blurBitmap, int overlayColor) {
		try {
			//圆角的半径，依次为左上角xy半径，右上角，右下角，左下角
			mRectFDst.right = getWidth();
			mRectFDst.bottom = getHeight();
			/*向路径中添加圆角矩形。radii数组定义圆角矩形的四个圆角的x,y半径。radii长度必须为8*/
			//Path.Direction.CW：clockwise ，沿顺时针方向绘制,Path.Direction.CCW：counter-clockwise ，沿逆时针方向绘制
			cornerPath.addRoundRect(mRectFDst, cornerRids, Path.Direction.CW);
			cornerPath.close();
			canvas.clipPath(cornerPath);

			mRectSrc.right = blurBitmap.getWidth();
			mRectSrc.bottom = blurBitmap.getHeight();
			canvas.drawBitmap(blurBitmap, mRectSrc, mRectFDst, null);
			mBitmapPaint.setColor(overlayColor);
			canvas.drawRect(mRectFDst, mBitmapPaint);
			if (mBorderWidth > 0) {
				//目前没找到合适方式
				mBorderPaint.setStrokeWidth(mBorderWidth * 2);
				canvas.drawPath(cornerPath, mBorderPaint);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 画椭圆，如果宽高一样则为圆形
	 *
	 * @param canvas
	 * @param blurBitmap
	 * @param overlayColor
	 */
	private void drawOvalRectBitmap(Canvas canvas, Bitmap blurBitmap, int overlayColor) {
		try {
			mRectFDst.right = getWidth();
			mRectFDst.bottom = getHeight();
			mBitmapPaint.reset();
			mBitmapPaint.setAntiAlias(true);
			if (shader == null) {
				shader = new BitmapShader(blurBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
			}
			if (matrix == null) {
				matrix = new Matrix();
				matrix.postScale(mRectFDst.width() / blurBitmap.getWidth(), mRectFDst.height() / blurBitmap.getHeight());
			}
			shader.setLocalMatrix(matrix);
			mBitmapPaint.setShader(shader);
			canvas.drawOval(mRectFDst, mBitmapPaint);
			mBitmapPaint.reset();
			mBitmapPaint.setAntiAlias(true);
			mBitmapPaint.setColor(overlayColor);
			canvas.drawOval(mRectFDst, mBitmapPaint);
			if (mBorderWidth > 0) {
				mBorderRect.set(mRectFDst);
				mBorderRect.inset(mBorderWidth / 2, mBorderWidth / 2);
				canvas.drawOval(mBorderRect, mBorderPaint);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 画圆形，以宽高最小的为半径
	 *
	 * @param canvas
	 * @param blurBitmap
	 * @param overlayColor
	 */
	private void drawCircleRectBitmap(Canvas canvas, Bitmap blurBitmap, int overlayColor) {
		try {
			mRectFDst.right = getMeasuredWidth();
			mRectFDst.bottom = getMeasuredHeight();
			mRectSrc.right = blurBitmap.getWidth();
			mRectSrc.bottom = blurBitmap.getHeight();
			mBitmapPaint.reset();
			mBitmapPaint.setAntiAlias(true);
			if (shader == null) {
				shader = new BitmapShader(blurBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
			}
			if (matrix == null) {
				matrix = new Matrix();
				matrix.postScale(mRectFDst.width() / mRectSrc.width(), mRectFDst.height() / mRectSrc.height());
			}
			shader.setLocalMatrix(matrix);
			mBitmapPaint.setShader(shader);
			//前面Scale，故判断以哪一个来取中心点和半径
			if (mRectFDst.width() >= mRectSrc.width()) {
				cx = mRectFDst.width() / 2;
				cy = mRectFDst.height() / 2;
				//取宽高最小的为半径
				cRadius = Math.min(mRectFDst.width(), mRectFDst.height()) / 2;
				mBorderRect.set(mRectFDst);
			} else {
				cx = mRectSrc.width() / 2f;
				cy = mRectSrc.height() / 2f;
				cRadius = Math.min(mRectSrc.width(), mRectSrc.height()) / 2f;
				mBorderRect.set(mRectSrc);
			}
			canvas.drawCircle(cx, cy, cRadius, mBitmapPaint);
			mBitmapPaint.reset();
			mBitmapPaint.setAntiAlias(true);
			mBitmapPaint.setColor(overlayColor);
			canvas.drawCircle(cx, cy, cRadius, mBitmapPaint);
			//使用宽高相等的椭圆为圆形来画边框
			if (mBorderWidth > 0) {
				if (mBorderRect.width() > mBorderRect.height()) {
					//原本宽大于高，圆是以中心点为圆心和高的一半为半径，椭圆区域是以初始00为开始，故整体向右移动差值
					float dif = Math.abs(mBorderRect.height() - mBorderRect.width()) / 2;
					mBorderRect.left = dif;
					mBorderRect.right = Math.min(mBorderRect.width(), mBorderRect.height()) + dif;
					mBorderRect.bottom = Math.min(mBorderRect.width(), mBorderRect.height());
				} else if (mBorderRect.width() < mBorderRect.height()) {
					//原本高大于宽，圆是以中心点为圆心和宽的一半为半径，椭圆区域是以初始00为开始，故整体向下移动差值
					float dif = Math.abs(mBorderRect.height() - mBorderRect.width()) / 2;
					mBorderRect.top = dif;
					mBorderRect.right = Math.min(mBorderRect.width(), mBorderRect.height());
					mBorderRect.bottom = Math.min(mBorderRect.width(), mBorderRect.height()) + dif;
				} else {
					//如果快高相同，则不需要偏移，椭圆画出来就是圆
					mBorderRect.right = Math.min(mBorderRect.width(), mBorderRect.height());
					mBorderRect.bottom = Math.min(mBorderRect.width(), mBorderRect.height());
				}
				mBorderRect.inset(mBorderWidth / 2, mBorderWidth / 2);
				canvas.drawOval(mBorderRect, mBorderPaint);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	/**
	 * dp转px
	 *
	 * @param dpValue dp值
	 * @return px值
	 */
	public int dp2px(final float dpValue) {
		final float scale = mContext.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	public @NonNull int[] getState() {
		return StateSet.WILD_CARD;
	}

	private static class StopException extends RuntimeException {
	}

	private static RealTimeBlurView.StopException STOP_EXCEPTION = new RealTimeBlurView.StopException();

	/**
	 * 传入构造器，避免传统的设置一个参数调用一次invalidate()重新绘制
	 *
	 * @return
	 */
	public void refreshView(RealTimeBlurView.Builder builder) {
		boolean isInvalidate = false;
		if (builder == null) {
			return;
		}
		if (builder.blurMode != -1 && this.blurMode != builder.blurMode) {
			this.blurMode = builder.blurMode;
			isInvalidate = true;
		}
		if (builder.mBorderColor != null && !mBorderColor.equals(builder.mBorderColor)) {
			this.mBorderColor = builder.mBorderColor;
			mBorderPaint.setColor(mBorderColor.getColorForState(getState(), DEFAULT_BORDER_COLOR));
			if (mBorderWidth > 0) {
				isInvalidate = true;
			}
		}
		if (builder.mBorderWidth > 0) {
			mBorderWidth = builder.mBorderWidth;
			mBorderPaint.setStrokeWidth(mBorderWidth);
			isInvalidate = true;
		}
		if (mCornerRadii[BlurCorner.TOP_LEFT] != builder.mCornerRadii[BlurCorner.TOP_LEFT]
				|| mCornerRadii[BlurCorner.TOP_RIGHT] != builder.mCornerRadii[BlurCorner.TOP_RIGHT]
				|| mCornerRadii[BlurCorner.BOTTOM_RIGHT] != builder.mCornerRadii[BlurCorner.BOTTOM_RIGHT]
				|| mCornerRadii[BlurCorner.BOTTOM_LEFT] != builder.mCornerRadii[BlurCorner.BOTTOM_LEFT]) {
			mCornerRadii[BlurCorner.TOP_LEFT] = builder.mCornerRadii[BlurCorner.TOP_LEFT];
			mCornerRadii[BlurCorner.TOP_RIGHT] = builder.mCornerRadii[BlurCorner.TOP_RIGHT];
			mCornerRadii[BlurCorner.BOTTOM_LEFT] = builder.mCornerRadii[BlurCorner.BOTTOM_LEFT];
			mCornerRadii[BlurCorner.BOTTOM_RIGHT] = builder.mCornerRadii[BlurCorner.BOTTOM_RIGHT];
			isInvalidate = true;
			initCornerRids();
		}
		if (builder.mOverlayColor != -1 && mOverlayColor != builder.mOverlayColor) {
			mOverlayColor = builder.mOverlayColor;
			isInvalidate = true;
		}
		if (builder.mBlurRadius > 0 && mBlurRadius != builder.mBlurRadius) {
			mBlurRadius = builder.mBlurRadius;
			mDirty = true;
			isInvalidate = true;
		}
		if (builder.mDownSampleFactor > 0 && mDownSampleFactor != builder.mDownSampleFactor) {
			mDownSampleFactor = builder.mDownSampleFactor;
			mDirty = true;
			isInvalidate = true;
			releaseBitmap();
		}
		if (isInvalidate) {
			invalidate();
		}
	}

	public static class Builder {

		// default 4
		private float mDownSampleFactor = -1;
		// default #aaffffff
		private int mOverlayColor = -1;
		// default 10dp (0 < r <= 25)
		private float mBlurRadius = -1;
		private float mBorderWidth = -1;
		private ColorStateList mBorderColor = null;
		private int blurMode = -1;
		private final float[] mCornerRadii = new float[]{0f, 0f, 0f, 0f};
		private Context mContext;

		private Builder(Context context) {
			mContext = context.getApplicationContext();
		}

		/**
		 * 模糊半径
		 *
		 * @param radius 0~25
		 * @return
		 */
		public RealTimeBlurView.Builder setBlurRadius(@FloatRange(from = 0, to = 25) float radius) {
			mBlurRadius = radius;
			return this;
		}

		/**
		 * 采样率
		 *
		 * @param factor
		 * @return
		 */
		public RealTimeBlurView.Builder setDownSampleFactor(float factor) {
			if (factor <= 0) {
				throw new IllegalArgumentException("DownSample factor must be greater than 0.");
			}
			mDownSampleFactor = factor;
			return this;
		}

		/**
		 * 蒙层颜色
		 *
		 * @param color
		 * @return
		 */
		public RealTimeBlurView.Builder setOverlayColor(int color) {
			mOverlayColor = color;
			return this;
		}

		/**
		 * Set the corner radius of a specific corner in px.
		 * 设置圆角 圆形、椭圆无效
		 *
		 * @param corner 枚举类型 对应4个角
		 * @param radius 角半径幅度
		 * @return
		 */
		public RealTimeBlurView.Builder setCornerRadius(@BlurCorner int corner, float radius) {
			mCornerRadii[corner] = radius;
			return this;
		}

		/**
		 * Set all the corner radii from a dimension resource id.
		 * 设置圆角 圆形、椭圆无效
		 *
		 * @param resId dimension resource id of radii.
		 */
		public RealTimeBlurView.Builder setCornerRadiusDimen(@DimenRes int resId) {
			float radius = mContext.getResources().getDimension(resId);
			return setCornerRadius(radius, radius, radius, radius);
		}

		/**
		 * Set the corner radius of a specific corner in px.
		 * 设置圆角 圆形、椭圆无效
		 *
		 * @param radius 4个角同值
		 */
		public RealTimeBlurView.Builder setCornerRadius(float radius) {
			return setCornerRadius(radius, radius, radius, radius);
		}

		/**
		 * Set the corner radius of a specific corner in px.
		 * 设置圆角 圆形、椭圆无效
		 */
		public RealTimeBlurView.Builder setCornerRadius(float topLeft, float topRight, float bottomLeft, float bottomRight) {
			mCornerRadii[BlurCorner.TOP_LEFT] = topLeft;
			mCornerRadii[BlurCorner.TOP_RIGHT] = topRight;
			mCornerRadii[BlurCorner.BOTTOM_LEFT] = bottomLeft;
			mCornerRadii[BlurCorner.BOTTOM_RIGHT] = bottomRight;
			return this;
		}

		/**
		 * 设置边框的宽度
		 *
		 * @param resId
		 * @return
		 */
		public RealTimeBlurView.Builder setBorderWidth(@DimenRes int resId) {
			return setBorderWidth(mContext.getResources().getDimension(resId));
		}

		/**
		 * 设置边框的宽度
		 *
		 * @param width 转px值
		 * @return
		 */
		public RealTimeBlurView.Builder setBorderWidth(float width) {
			mBorderWidth = width;
			return this;
		}

		/**
		 * 设置边框颜色
		 *
		 * @param color R.color.xxxx
		 * @return
		 */
		public RealTimeBlurView.Builder setBorderColor(@ColorRes int color) {
			return setBorderColor(ColorStateList.valueOf(ContextCompat.getColor(mContext, color)));
		}

		public RealTimeBlurView.Builder setBorderColor(ColorStateList colors) {
			mBorderColor = (colors != null) ? colors : ColorStateList.valueOf(DEFAULT_BORDER_COLOR);
			return this;
		}

		/**
		 * 设置高斯模糊的类型
		 *
		 * @param blurMode BlurMode枚举值，支持圆、方形、椭圆（宽高相等椭圆为圆）
		 * @return
		 */
		public RealTimeBlurView.Builder setBlurMode(@BlurMode int blurMode) {
			this.blurMode = blurMode;
			return this;
		}

	}

	/**
	 * 建造者模式，避免设置一个参数调用一次重新绘制
	 *
	 * @return
	 */
	public static RealTimeBlurView.Builder build(Context context) {
		return new RealTimeBlurView.Builder(context);
	}
}

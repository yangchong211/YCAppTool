package com.yc.widget.blurview;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.ApplicationInfo;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RSRuntimeException;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;

import com.yc.widget.R;


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
	 * 默认4
	 */
	private float mDownsampleFactor;
	/**
	 * 默认 #aaffffff
	 */
	private int mOverlayColor;
	/**
	 * 默认 10dp (0 < r <= 25)
	 */
	private float mBlurRadius;

	private boolean mDirty;
	private Bitmap mBitmapToBlur, mBlurredBitmap;
	private Canvas mBlurringCanvas;
	private RenderScript mRenderScript;
	private ScriptIntrinsicBlur mBlurScript;
	private Allocation mBlurInput, mBlurOutput;
	private boolean mIsRendering;
	private final Rect mRectSrc = new Rect(), mRectDst = new Rect();
	private View mDecorView;
	private boolean mDifferentRoot;
	private static int RENDERING_COUNT;

	public RealTimeBlurView(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RealTimeBlurView);
		mBlurRadius = a.getDimension(R.styleable.RealTimeBlurView_realtimeBlurRadius,
				TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
						10, context.getResources().getDisplayMetrics()));
		mDownsampleFactor = a.getFloat(R.styleable.RealTimeBlurView_realtimeDownsampleFactor, 4);
		mOverlayColor = a.getColor(R.styleable.RealTimeBlurView_realtimeOverlayColor, 0xAAFFFFFF);
		a.recycle();
	}


	public void setBlurRadius(float radius) {
		if (mBlurRadius != radius) {
			mBlurRadius = radius;
			mDirty = true;
			invalidate();
		}
	}


	public void setDownsampleFactor(float factor) {
		if (factor <= 0) {
			throw new IllegalArgumentException("Downsample factor must be greater than 0.");
		}
		if (mDownsampleFactor != factor) {
			mDownsampleFactor = factor;
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


	private void releaseBitmap() {
		if (mBlurInput != null) {
			mBlurInput.destroy();
			mBlurInput = null;
		}
		if (mBlurOutput != null) {
			mBlurOutput.destroy();
			mBlurOutput = null;
		}
		if (mBitmapToBlur != null) {
			mBitmapToBlur.recycle();
			mBitmapToBlur = null;
		}
		if (mBlurredBitmap != null) {
			mBlurredBitmap.recycle();
			mBlurredBitmap = null;
		}
	}


	private void releaseScript() {
		if (mRenderScript != null) {
			mRenderScript.destroy();
			mRenderScript = null;
		}
		if (mBlurScript != null) {
			mBlurScript.destroy();
			mBlurScript = null;
		}
	}

	protected void release() {
		releaseBitmap();
		releaseScript();
	}

	@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    protected boolean prepare() {
		if (mBlurRadius == 0) {
			release();
			return false;
		}

		float downsampleFactor = mDownsampleFactor;

		if (mDirty || mRenderScript == null) {
			if (mRenderScript == null) {
				try {
					mRenderScript = RenderScript.create(getContext());
					mBlurScript = ScriptIntrinsicBlur.create(mRenderScript, Element.U8_4(mRenderScript));
				} catch (RSRuntimeException e) {
					if (isDebug(getContext())) {
						if (e.getMessage() != null && e.getMessage().startsWith("Error loading RS jni library: java.lang.UnsatisfiedLinkError:")) {
							throw new RuntimeException("Error loading RS jni library, Upgrade buildToolsVersion=\"24.0.2\" or higher may solve this issue");
						} else {
							throw e;
						}
					} else {
						// In release mode, just ignore
						releaseScript();
						return false;
					}
				}
			}

			mDirty = false;
			float radius = mBlurRadius / downsampleFactor;
			if (radius > 25) {
				downsampleFactor = downsampleFactor * radius / 25;
				radius = 25;
			}
			mBlurScript.setRadius(radius);
		}

		final int width = getWidth();
		final int height = getHeight();

		int scaledWidth = Math.max(1, (int) (width / downsampleFactor));
		int scaledHeight = Math.max(1, (int) (height / downsampleFactor));

		if (mBlurringCanvas == null || mBlurredBitmap == null
				|| mBlurredBitmap.getWidth() != scaledWidth
				|| mBlurredBitmap.getHeight() != scaledHeight) {
			releaseBitmap();

			boolean r = false;
			try {
				mBitmapToBlur = Bitmap.createBitmap(scaledWidth, scaledHeight, Bitmap.Config.ARGB_8888);
				if (mBitmapToBlur == null) {
					return false;
				}
				mBlurringCanvas = new Canvas(mBitmapToBlur);

				mBlurInput = Allocation.createFromBitmap(mRenderScript, mBitmapToBlur,
						Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);
				mBlurOutput = Allocation.createTyped(mRenderScript, mBlurInput.getType());

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
					releaseBitmap();
					return false;
				}
			}
		}
		return true;
	}

	@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    protected void blur(Bitmap bitmapToBlur, Bitmap blurredBitmap) {
		mBlurInput.copyFrom(bitmapToBlur);
		mBlurScript.setInput(mBlurInput);
		mBlurScript.forEach(mBlurOutput);
		mBlurOutput.copyTo(blurredBitmap);
	}

	private final ViewTreeObserver.OnPreDrawListener preDrawListener = new ViewTreeObserver.OnPreDrawListener() {
		@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
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
				} catch (StopException e) {
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
		for (int i = 0; i < 4 && ctx != null && !(ctx instanceof Activity) && ctx instanceof ContextWrapper; i++) {
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
	 */
	protected void drawBlurredBitmap(Canvas canvas, Bitmap blurredBitmap, int overlayColor) {
		if (blurredBitmap != null) {
			mRectSrc.right = blurredBitmap.getWidth();
			mRectSrc.bottom = blurredBitmap.getHeight();
			mRectDst.right = getWidth();
			mRectDst.bottom = getHeight();
			canvas.drawBitmap(blurredBitmap, mRectSrc, mRectDst, null);
		}
		canvas.drawColor(overlayColor);
	}

	private static class StopException extends RuntimeException {}

	private static StopException STOP_EXCEPTION = new StopException();

	static {
		try {
			RealTimeBlurView.class.getClassLoader().loadClass("android.support.v8.renderscript.RenderScript");
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("RenderScript support not enabled. Add \"android { defaultConfig { renderscriptSupportModeEnabled true }}\" in your build.gradle");
		}
	}

	// android:debuggable="true" in AndroidManifest.xml (auto set by build tool)
	static Boolean DEBUG = null;

	static boolean isDebug(Context ctx) {
		if (DEBUG == null && ctx != null) {
			DEBUG = (ctx.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
		}
		return DEBUG == Boolean.TRUE;
	}
}

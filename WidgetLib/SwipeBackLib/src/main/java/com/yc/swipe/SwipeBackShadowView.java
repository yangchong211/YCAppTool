package com.yc.swipe;

import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import androidx.annotation.DrawableRes;
import androidx.core.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.yc.toolutils.AppActivityUtils;
import com.yc.toolutils.AppWindowUtils;
import com.yc.toolutils.StatusBarUtils;

import java.lang.ref.WeakReference;


public class SwipeBackShadowView extends FrameLayout {

    private static final String TAG = SwipeBackShadowView.class.getSimpleName();
    private static final float WE_CHAT_STYLE_MAX_OFFSET = 0.75f;
    private Activity mActivity;
    private WeakReference<Activity> mPreActivity;
    private ViewGroup mPreDecorView;
    private View mPreContentView;
    private ImageView mPreImageView;
    private View mShadowView;

    /**
     * 是否显示滑动返回的阴影效果
     */
    private boolean mIsNeedShowShadow = true;
    /**
     * 阴影资源 id
     */
    private int mShadowResId = R.drawable.swipe_shadow;
    /**
     * 阴影区域的透明度是否根据滑动的距离渐变
     */
    private boolean mIsShadowAlphaGradient = true;
    /**
     * 是否是微信滑动返回样式
     */
    private boolean mIsWeChatStyle = true;

    private boolean mIsCurrentActivityTranslucent;

    SwipeBackShadowView(Activity activity) {
        super(activity);
        mActivity = activity;

        TypedArray typedArray = mActivity.getTheme().obtainStyledAttributes(new int[]{
                android.R.attr.windowIsTranslucent
        });
        mIsCurrentActivityTranslucent = typedArray.getBoolean(0, false);
        typedArray.recycle();
    }

    /**
     * 设置是否显示滑动返回的阴影效果。默认值为 true
     */
    void setIsNeedShowShadow(boolean isNeedShowShadow) {
        mIsNeedShowShadow = isNeedShowShadow;
        updateShadow();
    }

    /**
     * 设置阴影资源 id。默认值为 R.drawable.bga_sbl_shadow
     */
    void setShadowResId(@DrawableRes int shadowResId) {
        mShadowResId = shadowResId;
        updateShadow();
    }

    /**
     * 设置阴影区域的透明度是否根据滑动的距离渐变。默认值为 true
     */
    void setIsShadowAlphaGradient(boolean isShadowAlphaGradient) {
        mIsShadowAlphaGradient = isShadowAlphaGradient;
    }

    /**
     * 设置是否是微信滑动返回样式。默认值为 true
     */
    void setIsWeChatStyle(boolean isWeChatStyle) {
        mIsWeChatStyle = isWeChatStyle;
    }

    private void updateShadow() {
        if (mIsCurrentActivityTranslucent) {
            if (mIsNeedShowShadow) {
                setBackgroundResource(mShadowResId);
            } else {
                setBackgroundResource(android.R.color.transparent);
            }
        } else {
            if (mIsNeedShowShadow) {
                if (mShadowView == null) {
                    mShadowView = new View(getContext());
                    addView(mShadowView, getChildCount(), new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                }
                mShadowView.setBackgroundResource(mShadowResId);
            } else {
                if (mShadowView != null) {
                    removeView(mShadowView);
                }
            }
        }
    }

    void bindPreActivity() {
        if (mIsCurrentActivityTranslucent) {
            return;
        }

        if (mPreActivity == null) {
            Activity preActivity = SwipeBackManager.getInstance().getPenultimateActivity(mActivity);
            if (preActivity != null) {
                mPreActivity = new WeakReference<>(preActivity);
                mPreDecorView = (ViewGroup) preActivity.getWindow().getDecorView();
                mPreContentView = mPreDecorView.getChildAt(0);
                mPreDecorView.removeView(mPreContentView);
                addView(mPreContentView, 0, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            }
        }
    }

    void unBindPreActivity(boolean isNeedAddImageView) {
        if (mIsCurrentActivityTranslucent) {
            return;
        }

        if (mPreActivity == null) {
            return;
        }

        Activity activity = mPreActivity.get();
        if (activity == null || activity.isFinishing()) {
            return;
        }

        if (mPreDecorView != null && mPreContentView != null) {
            if (isNeedAddImageView && mPreImageView == null && containsProblemView((ViewGroup) mPreContentView)) {
                mPreImageView = new ImageView(mActivity);
                mPreImageView.setScaleType(ImageView.ScaleType.FIT_XY);
                mPreImageView.setImageBitmap(getBitmap(mPreContentView));
                addView(mPreImageView, 0, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            }

            removeView(mPreContentView);

            ViewGroup.LayoutParams lp = null;
            if (!(mPreContentView instanceof SwipeBackLayout)) {
                int width = mPreDecorView.getWidth();
                int height = mPreDecorView.getHeight() - StatusBarUtils.getNavigationBarHeight(activity);
                if (!AppActivityUtils.isPortrait(activity)) {
                    width = mPreDecorView.getWidth() - StatusBarUtils.getNavigationBarHeight(activity);
                    height = mPreDecorView.getHeight();
                }
                if (mPreDecorView instanceof FrameLayout) {
                    lp = new FrameLayout.LayoutParams(width, height);
                } else if (mPreDecorView instanceof LinearLayout) {
                    lp = new LinearLayout.LayoutParams(width, height);
                } else if (mPreDecorView instanceof RelativeLayout) {
                    lp = new RelativeLayout.LayoutParams(width, height);
                }
            }
            if (lp == null) {
                mPreDecorView.addView(mPreContentView, 0);
            } else {
                mPreDecorView.addView(mPreContentView, 0, lp);
            }

            mPreContentView = null;
            mPreActivity.clear();
            mPreActivity = null;
        }
    }

    @Override
    protected void dispatchDraw(final Canvas canvas) {
        super.dispatchDraw(canvas);
        if (mIsCurrentActivityTranslucent) {
            return;
        }

        if (mPreImageView != null) {
            return;
        }

        // add 和 remove 方式时，滑动返回结束后的最后一帧通过 draw 来实现，避免抖动
        if (mPreContentView == null && mPreDecorView != null) {
            mPreDecorView.draw(canvas);
        }
    }

    private Bitmap getBitmap(View view) {
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache(), 0, 0,
                AppWindowUtils.getRealScreenWidth(mActivity),
                AppWindowUtils.getRealScreenHeight(mActivity) - StatusBarUtils.getNavigationBarHeight(mActivity));
        view.destroyDrawingCache();
        return bitmap;
    }

    /**
     * 该 ViewGroup 中是否包含导致多次 draw 后应用崩溃的 View
     */
    private boolean containsProblemView(ViewGroup viewGroup) {
        int childCount = viewGroup.getChildCount();
        View childView;
        for (int i = 0; i < childCount; i++) {
            childView = viewGroup.getChildAt(i);
            if (SwipeBackManager.getInstance().isProblemView(childView)) {
                return true;
            } else if (childView instanceof ViewGroup) {
                if (containsProblemView((ViewGroup) childView)) {
                    return true;
                }
            }
        }
        return false;
    }

    void setShadowAlpha(float alpha) {
        if (mIsNeedShowShadow && mIsShadowAlphaGradient) {
            if (mIsCurrentActivityTranslucent) {
                ViewCompat.setAlpha(this, alpha);
            } else if (mShadowView != null) {
                ViewCompat.setAlpha(mShadowView, alpha);
            }
        }
    }

    void onPanelSlide(float slideOffset) {
        if (mIsWeChatStyle) { // 微信滑动返回样式的情况
            if (mIsCurrentActivityTranslucent) { // 透明主题
                onPanelSlide(mActivity, slideOffset);
            } else if (mPreContentView != null) { // 非透明主题
                ViewCompat.setTranslationX(mPreContentView, (mPreContentView.getMeasuredWidth() * WE_CHAT_STYLE_MAX_OFFSET) * (1 - slideOffset));
            }
        } else { // 非微信滑动返回样式的情况
            if (!mIsCurrentActivityTranslucent && mPreContentView != null) { // 只有非透明主题时才移动
                ViewCompat.setTranslationX(mPreContentView, mPreContentView.getMeasuredWidth() * (1 - slideOffset));
            }
        }
    }

    private void onPanelSlide(Activity currentActivity, float slideOffset) {
        try {
            Activity preActivity = SwipeBackManager.getInstance().getPenultimateActivity(currentActivity);
            if (preActivity != null) {
                View decorView = preActivity.getWindow().getDecorView();
                ViewCompat.setTranslationX(decorView, -decorView.getMeasuredWidth() * (1 - WE_CHAT_STYLE_MAX_OFFSET) * (1 - slideOffset));
            }
        } catch (Exception e) {
        }
    }

    void onPanelClosed() {
        if (mIsWeChatStyle) {
            // 微信滑动返回样式的情况
            if (mIsCurrentActivityTranslucent) {
                // 透明主题
                onPanelClosed(mActivity);
            } else if (mPreContentView != null) {
                // 非透明主题
                ViewCompat.setTranslationX(mPreContentView, 0);
            }
        } else {
            // 非微信滑动返回样式的情况
            if (!mIsCurrentActivityTranslucent && mPreContentView != null) {
                // 只有非透明主题时才移动
                ViewCompat.setTranslationX(mPreContentView, 0);
            }
        }
        unBindPreActivity(false);
    }

    private void onPanelClosed(Activity currentActivity) {
        try {
            Activity preActivity = SwipeBackManager.getInstance().getPenultimateActivity(currentActivity);
            if (preActivity != null) {
                View decorView = preActivity.getWindow().getDecorView();
                ViewCompat.setTranslationX(decorView, 0);
            }
        } catch (Exception e) {
        }
    }
}

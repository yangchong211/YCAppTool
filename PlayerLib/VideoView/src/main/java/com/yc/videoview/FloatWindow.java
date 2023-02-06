package com.yc.videoview;

import android.animation.TimeInterpolator;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.yc.videoview.impl.FloatWindowImpl;
import com.yc.videoview.inter.IFloatWindow;
import com.yc.videoview.tool.FloatMoveType;
import com.yc.videoview.tool.FloatScreenType;
import com.yc.videoview.tool.FloatWindowUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 记得添加下面这个权限
 * uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW"
 */
public class FloatWindow {

    /**
     * 如果想要 Window 位于所有 Window 的最顶层，那么采用较大的层级即可，很显然系统 Window 的层级是最大的。
     * 当我们采用系统层级时，一般选用TYPE_SYSTEM_ERROR或者TYPE_SYSTEM_OVERLAY，还需要声明权限。
     * <uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />
     */
    private FloatWindow() {

    }

    private static final String mDefaultTag = "default_float_window_tag";
    private static Map<String, IFloatWindow> mFloatWindowMap;

    public static IFloatWindow get() {
        return get(mDefaultTag);
    }

    public static IFloatWindow get(@NonNull String tag) {
        return mFloatWindowMap == null ? null : mFloatWindowMap.get(tag);
    }


    @MainThread
    public static Builder with(@NonNull Context applicationContext) {
        return new Builder(applicationContext);
    }

    public static void destroy() {
        destroy(mDefaultTag);
    }

    public static void destroy(String tag) {
        if (mFloatWindowMap == null || !mFloatWindowMap.containsKey(tag)) {
            return;
        }
        IFloatWindow iFloatWindow = mFloatWindowMap.get(tag);
        if (iFloatWindow != null) {
            iFloatWindow.dismiss();
        }
        mFloatWindowMap.remove(tag);
    }

    public static class Builder {
        public Context mApplicationContext;
        public View mView;
        private int mLayoutId;
        public int mWidth = ViewGroup.LayoutParams.WRAP_CONTENT;
        public int mHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
        public int gravity = Gravity.TOP | Gravity.START;
        public int xOffset;
        public int yOffset;
        public boolean mShow = true;
        public Class[] mActivities;
        public int mMoveType = FloatMoveType.FIXED;
        public long mDuration = 300;
        public TimeInterpolator mInterpolator;
        private String mTag = mDefaultTag;

        private Builder() {

        }

        Builder(Context applicationContext) {
            mApplicationContext = applicationContext;
        }

        public Builder setView(@NonNull View view) {
            mView = view;
            return this;
        }

        public Builder setView(@LayoutRes int layoutId) {
            mLayoutId = layoutId;
            return this;
        }

        public Builder setWidth(int width) {
            mWidth = width;
            return this;
        }

        public Builder setHeight(int height) {
            mHeight = height;
            return this;
        }

        public Builder setWidth(@FloatScreenType.screenType int screenType, float ratio) {
            mWidth = (int) ((screenType == FloatScreenType.WIDTH ?
                    FloatWindowUtils.getScreenWidth(mApplicationContext) :
                    FloatWindowUtils.getScreenHeight(mApplicationContext)) * ratio);
            return this;
        }


        public Builder setHeight(@FloatScreenType.screenType int screenType, float ratio) {
            mHeight = (int) ((screenType == FloatScreenType.WIDTH ?
                    FloatWindowUtils.getScreenWidth(mApplicationContext) :
                    FloatWindowUtils.getScreenHeight(mApplicationContext)) * ratio);
            return this;
        }


        public Builder setX(int x) {
            xOffset = x;
            return this;
        }

        public Builder setY(int y) {
            yOffset = y;
            return this;
        }

        public Builder setX(@FloatScreenType.screenType int screenType, float ratio) {
            xOffset = (int) ((screenType == FloatScreenType.WIDTH ?
                    FloatWindowUtils.getScreenWidth(mApplicationContext) :
                    FloatWindowUtils.getScreenHeight(mApplicationContext)) * ratio);
            return this;
        }

        public Builder setY(@FloatScreenType.screenType int screenType, float ratio) {
            yOffset = (int) ((screenType == FloatScreenType.WIDTH ?
                    FloatWindowUtils.getScreenWidth(mApplicationContext) :
                    FloatWindowUtils.getScreenHeight(mApplicationContext)) * ratio);
            return this;
        }


        /**
         * 设置 Activity 过滤器，用于指定在哪些界面显示悬浮窗，默认全部界面都显示
         *
         * @param show       　过滤类型,子类类型也会生效
         * @param activities 　过滤界面
         */
        public Builder setFilter(boolean show, @NonNull Class... activities) {
            mShow = show;
            mActivities = activities;
            return this;
        }

        public Builder setMoveType(@FloatMoveType.MOVE_TYPE int moveType) {
            mMoveType = moveType;
            return this;
        }

        public Builder setMoveStyle(long duration, @Nullable TimeInterpolator interpolator) {
            mDuration = duration;
            mInterpolator = interpolator;
            return this;
        }

        public Builder setTag(@NonNull String tag) {
            mTag = tag;
            return this;
        }

        public void build() {
            if (mFloatWindowMap == null) {
                mFloatWindowMap = new HashMap<>();
            }
            if (mFloatWindowMap.containsKey(mTag)) {
                throw new IllegalArgumentException("FloatWindow of this tag has been added," +
                        " Please set a new tag for the new FloatWindow");
            }
            if (mView == null && mLayoutId == 0) {
                throw new IllegalArgumentException("View has not been set!");
            }
            if (mView == null) {
                LayoutInflater inflate = (LayoutInflater)
                        mApplicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                if (inflate != null) {
                    mView = inflate.inflate(mLayoutId, null);
                }
            }
            IFloatWindow floatWindowImpl = new FloatWindowImpl(this);
            mFloatWindowMap.put(mTag, floatWindowImpl);
        }
    }
}

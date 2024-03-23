/*
Copyright 2017 yangchong211（github.com/yangchong211）

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package com.yc.statusbar.bar;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import java.lang.reflect.Field;

import com.yc.statusbar.R;
import com.yc.statusbar.utils.StatusBarUtils;
import com.yc.statusbar.view.StatusBarView;

import static com.yc.toolutils.StatusBarUtils.getStatusBarHeight;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211/YCStatusBar
 *     time  : 2018/06/4
 *     desc  : 状态栏工具类
 *     revise: 使用方法请看GitHub说明文档
 * </pre>
 */
@TargetApi(Build.VERSION_CODES.KITKAT)
final class BarStatusKitKat {

    /**
     * 添加margin
     */
    private static final String TAG_MARGIN_ADDED = "marginAdded";

    /**
     * 设置状态栏的颜色，4.4以上，5.0以下，可以直接通过添加状态view来修改颜色
     *
     * @param activity                          activity
     * @param statusColor                       颜色
     */
    static void setStatusBarColor(Activity activity, int statusColor) {
        Window window = activity.getWindow();
        //设置Window为全透明
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        ViewGroup mContentView = (ViewGroup) window.findViewById(Window.ID_ANDROID_CONTENT);
        //获取父布局
        View mContentChild = mContentView.getChildAt(0);
        //获取状态栏高度
        int statusBarHeight = getStatusBarHeight(activity);
        //如果已经存在假状态栏则移除，防止重复添加
        StatusBarUtils.removeFakeStatusBarViewIfExist(activity);
        //添加一个View来作为状态栏的填充
        addFakeStatusBarView(activity, statusColor);
        //设置子控件到状态栏的间距
        addMarginTopToContentChild(mContentChild, statusBarHeight);
        //不预留系统栏位置
        if (mContentChild != null) {
            mContentChild.setFitsSystemWindows(false);
        }
        //如果在Activity中使用了ActionBar则需要再将布局与状态栏的高度跳高一个ActionBar的高度，否则内容会被ActionBar遮挡
        int actionBarId = activity.getResources().getIdentifier("action_bar", "id",
                activity.getPackageName());
        View view = activity.findViewById(actionBarId);
        if (view != null) {
            TypedValue typedValue = new TypedValue();
            if (activity.getTheme().resolveAttribute(R.attr.actionBarSize, typedValue, true)) {
                int actionBarHeight = TypedValue.complexToDimensionPixelSize(
                        typedValue.data, activity.getResources().getDisplayMetrics());
                StatusBarUtils.setContentTopPadding(activity, actionBarHeight);
            }
        }
    }


    /**
     * 设置状态栏透明效果
     * @param activity                          activity
     */
    static void translucentStatusBar(Activity activity) {
        Window window = activity.getWindow();
        //设置Window为透明
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //获取content布局
        ViewGroup mContentView = (ViewGroup) activity.findViewById(Window.ID_ANDROID_CONTENT);
        View mContentChild = mContentView.getChildAt(0);
        //移除已经存在假状态栏则，并且取消它的Margin间距
        StatusBarUtils.removeFakeStatusBarViewIfExist(activity);
        //获取状态栏的高度
        int statusBarHeight = getStatusBarHeight(activity);
        removeMarginTopOfContentChild(mContentChild, statusBarHeight);
        if (mContentChild != null) {
            //fitsSystemWindow 为 false, 不预留系统栏位置.
            mContentChild.setFitsSystemWindows(false);
        }
    }

    static void setStatusBarColorForCollapsingToolbar(final Activity activity, final AppBarLayout appBarLayout, final CollapsingToolbarLayout collapsingToolbarLayout,
                                                      Toolbar toolbar, int statusColor) {
        Window window = activity.getWindow();
        //设置Window为全透明
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        ViewGroup mContentView = (ViewGroup) window.findViewById(Window.ID_ANDROID_CONTENT);

        //AppBarLayout,CollapsingToolbarLayout,ToolBar,ImageView的fitsSystemWindow统一改为false, 不预留系统栏位置.
        View mContentChild = mContentView.getChildAt(0);
        mContentChild.setFitsSystemWindows(false);
        ((View) appBarLayout.getParent()).setFitsSystemWindows(false);
        appBarLayout.setFitsSystemWindows(false);
        collapsingToolbarLayout.setFitsSystemWindows(false);
        collapsingToolbarLayout.getChildAt(0).setFitsSystemWindows(false);

        toolbar.setFitsSystemWindows(false);
        //为Toolbar添加一个状态栏的高度, 同时为Toolbar添加paddingTop,使Toolbar覆盖状态栏，ToolBar的title可以正常显示.
        if (toolbar.getTag() == null) {
            CollapsingToolbarLayout.LayoutParams lp = (CollapsingToolbarLayout.LayoutParams) toolbar.getLayoutParams();
            int statusBarHeight = getStatusBarHeight(activity);
            lp.height += statusBarHeight;
            toolbar.setLayoutParams(lp);
            toolbar.setPadding(toolbar.getPaddingLeft(), toolbar.getPaddingTop() + statusBarHeight, toolbar.getPaddingRight(), toolbar.getPaddingBottom());
            toolbar.setTag(true);
        }
        //移除已经存在假状态栏则,并且取消它的Margin间距
        int statusBarHeight = getStatusBarHeight(activity);
        StatusBarUtils.removeFakeStatusBarViewIfExist(activity);
        removeMarginTopOfContentChild(mContentChild, statusBarHeight);
        //添加一个View来作为状态栏的填充
        final View statusView = addFakeStatusBarView(activity, statusColor);

        CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams()).getBehavior();
        if (behavior != null && behavior instanceof AppBarLayout.Behavior) {
            int verticalOffset = ((AppBarLayout.Behavior) behavior).getTopAndBottomOffset();
            if (Math.abs(verticalOffset) > appBarLayout.getHeight() - collapsingToolbarLayout.getScrimVisibleHeightTrigger()) {
                statusView.setAlpha(1f);
            } else {
                statusView.setAlpha(0f);
            }
        } else {
            statusView.setAlpha(0f);
        }
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset) > appBarLayout.getHeight() - collapsingToolbarLayout.getScrimVisibleHeightTrigger()) {
                    //toolbar被折叠时显示状态栏
                    if (statusView.getAlpha() == 0) {
                        statusView.animate().cancel();
                        statusView.animate().alpha(1f).setDuration(collapsingToolbarLayout.getScrimAnimationDuration()).start();
                    }
                } else {
                    //toolbar展开时显示状态栏
                    if (statusView.getAlpha() == 1) {
                        statusView.animate().cancel();
                        statusView.animate().alpha(0f).setDuration(collapsingToolbarLayout.getScrimAnimationDuration()).start();
                    }
                }
            }
        });
    }

    static void setStatusBarWhiteForCollapsingToolbar(final Activity activity, AppBarLayout appBarLayout, final CollapsingToolbarLayout collapsingToolbarLayout, Toolbar toolbar, final int statusBarColor) {
        final Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        ViewGroup mContentView = (ViewGroup) window.findViewById(Window.ID_ANDROID_CONTENT);
        View mContentChild = mContentView.getChildAt(0);
        mContentChild.setFitsSystemWindows(false);
        ((View) appBarLayout.getParent()).setFitsSystemWindows(false);
        appBarLayout.setFitsSystemWindows(false);
        collapsingToolbarLayout.setFitsSystemWindows(false);
        collapsingToolbarLayout.getChildAt(0).setFitsSystemWindows(false);
        toolbar.setFitsSystemWindows(false);

        if (toolbar.getTag() == null) {
            CollapsingToolbarLayout.LayoutParams lp = (CollapsingToolbarLayout.LayoutParams) toolbar.getLayoutParams();
            int statusBarHeight = getStatusBarHeight(activity);
            lp.height += statusBarHeight;
            toolbar.setLayoutParams(lp);
            toolbar.setPadding(toolbar.getPaddingLeft(), toolbar.getPaddingTop() + statusBarHeight, toolbar.getPaddingRight(), toolbar.getPaddingBottom());
            toolbar.setTag(true);
        }

        int statusBarHeight = getStatusBarHeight(activity);
        int color = Color.BLACK;
        try {
            @SuppressLint("PrivateApi")
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            color = statusBarColor;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
            color = statusBarColor;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        final View statusView = addFakeStatusBarView(activity, color);
        CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams()).getBehavior();
        if (behavior != null && behavior instanceof AppBarLayout.Behavior) {
            int verticalOffset = ((AppBarLayout.Behavior) behavior).getTopAndBottomOffset();
            if (Math.abs(verticalOffset) > appBarLayout.getHeight() - collapsingToolbarLayout.getScrimVisibleHeightTrigger()) {
                statusView.setAlpha(1f);
            } else {
                statusView.setAlpha(0f);
            }
        } else {
            statusView.setAlpha(0f);
        }

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            private final static int EXPANDED = 0;
            private final static int COLLAPSED = 1;
            private int appBarLayoutState;

            @SuppressLint("NewApi")
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset) >= (appBarLayout.getTotalScrollRange() - StateAppBar.getPxFromDp(activity, 56))) {
                    if (appBarLayoutState != COLLAPSED) {
                        appBarLayoutState = COLLAPSED;

                        if (StateAppBar.setStatusBarLightMode(activity, true) ||
                                StateAppBar.FlymeSetStatusBarLightMode(activity, true)) {
                        }
                        if (statusView.getAlpha() == 0) {
                            statusView.animate().cancel();
                            statusView.animate().alpha(1f).setDuration(collapsingToolbarLayout.getScrimAnimationDuration()).start();
                        }
                    }
                } else {
                    if (appBarLayoutState != EXPANDED) {
                        appBarLayoutState = EXPANDED;

                        if (StateAppBar.setStatusBarLightMode(activity, false) ||
                                StateAppBar.FlymeSetStatusBarLightMode(activity, false)) {
                        }
                        if (statusView.getAlpha() == 1) {
                            statusView.animate().cancel();
                            statusView.animate().alpha(0f).setDuration(collapsingToolbarLayout.getScrimAnimationDuration()).start();
                        }
                        translucentStatusBar(activity);
                    }
                }
            }
        });
    }


    /**
     * 添加一个假的状态栏
     * 4.4以上，5.0以下
     * 原理：只要在根布局去设置一个与状态栏等高的View，设置背景色为我们期望的颜色就可以
     * @param activity                      activity
     * @param statusBarColor                状态栏颜色
     * @return
     */
    private static View addFakeStatusBarView(Activity activity, int statusBarColor) {
        Window window = activity.getWindow();
        ViewGroup mDecorView = (ViewGroup) window.getDecorView();
        //创建一个假的状态栏，添加到DecorView上
        StatusBarView statusBarView = StatusBarUtils.createStatusBarView(activity, statusBarColor);
        //添加view
        mDecorView.addView(statusBarView);
        return statusBarView;
    }

    /**
     * 添加到顶部margin间距
     * @param mContentChild                 子child
     * @param statusBarHeight               状态栏高度
     */
    private static void addMarginTopToContentChild(View mContentChild, int statusBarHeight) {
        if (mContentChild == null) {
            return;
        }
        if (!TAG_MARGIN_ADDED.equals(mContentChild.getTag())) {
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mContentChild.getLayoutParams();
            //设置到顶部的间距
            lp.topMargin += statusBarHeight;
            mContentChild.setLayoutParams(lp);
            mContentChild.setTag(TAG_MARGIN_ADDED);
        }
    }

    /**
     * 移除到顶部margin间距
     * @param mContentChild                 子child
     * @param statusBarHeight               状态栏高度
     */
    private static void removeMarginTopOfContentChild(View mContentChild, int statusBarHeight) {
        if (mContentChild == null) {
            return;
        }
        if (TAG_MARGIN_ADDED.equals(mContentChild.getTag())) {
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mContentChild.getLayoutParams();
            //移除到顶部的间距
            lp.topMargin -= statusBarHeight;
            mContentChild.setLayoutParams(lp);
            mContentChild.setTag(null);
        }
    }


}

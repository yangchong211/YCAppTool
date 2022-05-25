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
import android.content.Context;
import android.os.Build;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.yc.statusbar.utils.StatusBarColorUtils;
import com.yc.statusbar.utils.StatusBarUtils;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211/YCStatusBar
 *     time  : 2017/06/4
 *     desc  : 状态栏工具类
 *     revise: 使用方法请看GitHub说明文档
 * </pre>
 */
public final class StateAppBar {

    /**
     * 设置状态栏颜色，使用该方法也可以，如果是设置状态栏为白色或者黑色，请使用setStatusBarLightMode方法
     * @param activity                      activity
     * @param statusColor                   颜色
     */
    public static void setStatusBarColor(Activity activity,@ColorInt int statusColor) {
        StatusBarUtils.checkNull(activity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //21，大于21设置状态栏颜色，也就是5.0
            BarStatusLollipop.setStatusBarColor(activity, statusColor);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //19，也就是4.4
            BarStatusKitKat.setStatusBarColor(activity, statusColor);
        }
    }

    /**
     * 设置透明状态栏
     * @param activity                      activity
     */
    public static void translucentStatusBar(Activity activity) {
        StatusBarUtils.checkNull(activity);
        //设置透明状态栏，不隐藏状态栏
        translucentStatusBar(activity, false);
    }

    /**
     * 隐藏状态栏
     * 第一种，在onCreate设置，网络上大部分方案是这样的。但是隐藏状态栏后，用户手指滑动让状态栏出现，则可能会有视觉上bug
     * 第二种，监听页面状态栏变化，实现状态之间的无缝切换，界面控件的可见性与系统栏保持同步。当应用进入沉浸模式后，任何界面控件也应随系统栏一起隐藏，然后在系统界面重新出现时也再次显示出来。为此，可以在 View.OnSystemUiVisibilityChangeListener 以接收回调
     * 第三种，实现 onWindowFocusChanged()。 如果您获得窗口焦点，则可能需要再次隐藏系统栏。
     * @param activity      activity
     */
    public static void hideStatusBar(Activity activity , int type){
        if (activity!=null){
            View decorView = activity.getWindow().getDecorView();
            int uiOptions = decorView.getSystemUiVisibility();
            if (type == StatusBarUtils.HideBarStatus.LEAN_BACK){
                //全屏幕模式，允许交互
                uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN
                        //视图要求暂时隐藏系统导航，隐藏导航栏
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            } else if (type == StatusBarUtils.HideBarStatus.IMMERSION_MODE){
                //全屏幕模式，允许交互
                uiOptions |= View.SYSTEM_UI_FLAG_FULLSCREEN
                        //视图要求暂时隐藏系统导航，隐藏导航栏
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    uiOptions |= View.SYSTEM_UI_FLAG_IMMERSIVE;
                }
            } else if (type == StatusBarUtils.HideBarStatus.VISCOUS_IMMERSION){
                //全屏幕模式，允许交互
                uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN
                        //视图要求暂时隐藏系统导航，隐藏导航栏
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    //视图希望在隐藏状态栏时保持交互式
                    uiOptions |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
                }
            } else {
                uiOptions |= View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        //视图要求暂时隐藏系统导航，隐藏导航栏
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        //全屏幕模式，允许交互
                        | View.SYSTEM_UI_FLAG_FULLSCREEN;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    uiOptions |= View.SYSTEM_UI_FLAG_IMMERSIVE;
                }
            }
            //给根布局设置属性
            decorView.setSystemUiVisibility(uiOptions);
            //设置全屏幕
            //FLAG_FULLSCREEN 窗口标志:显示此窗口时，隐藏所有屏幕装饰(如状态栏)。
            activity.getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    /**
     * 设置透明状态栏
     * @param activity                      activity
     * @param hideStatusBarBackground       是否隐藏状态栏
     */
    public static void translucentStatusBar(Activity activity, boolean hideStatusBarBackground) {
        StatusBarUtils.checkNull(activity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //大于21
            BarStatusLollipop.translucentStatusBar(activity, hideStatusBarBackground);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //大于19
            BarStatusKitKat.translucentStatusBar(activity);
        }
    }

    /**
     * 设置AppBarLayout折叠布局的状态栏颜色
     * @param activity                      activity
     * @param appBarLayout                  appBar
     * @param collapsingToolbarLayout       collapsingToolbarLayout
     * @param toolbar                       toolbar
     * @param statusColor                   颜色
     */
    public static void setStatusBarColorForCollapsingToolbar(
            @NonNull Activity activity, AppBarLayout appBarLayout,
            CollapsingToolbarLayout collapsingToolbarLayout,
            Toolbar toolbar, @ColorInt int statusColor) {
        StatusBarUtils.checkNull(activity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            BarStatusLollipop.setStatusBarColorForCollapsingToolbar(activity,
                    appBarLayout, collapsingToolbarLayout, toolbar, statusColor);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            BarStatusKitKat.setStatusBarColorForCollapsingToolbar(activity,
                    appBarLayout, collapsingToolbarLayout, toolbar, statusColor);
        }
    }

    /**
     * 设置状态栏颜色，设置状态栏白色，或者灰色，请用这个方法。
     * @param activity                      activity
     * @param color                         颜色
     */
    @SuppressLint("NewApi")
    public static void setStatusBarLightMode(Activity activity, @ColorInt int color) {
        StatusBarUtils.checkNull(activity);
        //大于19，也就是4.4，现在几乎都是4.4以上的手机呢
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //判断是否为小米或魅族手机，如果是则将状态栏文字改为黑色
            if (setStatusBarLightMode(activity, true) || FlymeSetStatusBarLightMode(activity, true)) {
                //设置状态栏为指定颜色
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    //5.0
                    //给状态栏设置颜色
                    //FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS 表示此窗口负责绘制系统栏的背景
                    activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    //请求一个半透明的状态栏，系统提供最小的状态栏背景的保护。
                    activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    activity.getWindow().setStatusBarColor(color);
                    //BarStatusLollipop.setStatusBarColor(activity, color);
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    //4.4  到   5.0之间
                    //调用修改状态栏颜色的方法
                    setStatusBarColor(activity, color);
                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //如果是6.0以上将状态栏文字改为黑色，并设置状态栏颜色
                activity.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                //SYSTEM_UI_FLAG_LIGHT_STATUS_BAR属性介绍
                //相当于在布局中设置android:fitsSystemWindows="true"，让contentView顶上去
                //SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN属性介绍
                //状态栏一直存在并且不会挤压activity高度，状态栏会覆盖在activity之上
                activity.getWindow().getDecorView().
                        setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                activity.getWindow().setStatusBarColor(color);
                StatusBarUtils.setFitsSystemWindows(activity.getWindow(),true);
            }
        }
    }


    public static void setStatusBarLightForCollapsingToolbar(
            Activity activity, AppBarLayout appBarLayout,
            CollapsingToolbarLayout collapsingToolbarLayout,
            Toolbar toolbar,@ColorInt int statusBarColor) {
        StatusBarUtils.checkNull(activity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            BarStatusLollipop.setStatusBarWhiteForCollapsingToolbar(activity,
                    appBarLayout, collapsingToolbarLayout, toolbar, statusBarColor);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            BarStatusKitKat.setStatusBarWhiteForCollapsingToolbar(activity,
                    appBarLayout, collapsingToolbarLayout, toolbar, statusBarColor);
        }
    }


    /**
     * MIUI的沉浸支持透明白色字体和透明黑色字体
     * https://dev.mi.com/console/doc/detail?pId=1159
     */
    public static boolean setStatusBarLightMode(Activity activity, boolean darkmode) {
        StatusBarUtils.checkNull(activity);
        try {
            @SuppressLint("PrivateApi")
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Window window = activity.getWindow();
            //具体参考小米的解决方案
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //先清除
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //相当于在布局中设置android:fitsSystemWindows="true"，让contentView顶上去
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }

            Class<? extends Window> clazz = activity.getWindow().getClass();
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            int darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(activity.getWindow(), darkmode ? darkModeFlag : 0, darkModeFlag);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 设置状态栏图标为深色和魅族特定的文字风格，Flyme4.0以上
     */
    public static boolean FlymeSetStatusBarLightMode(Activity activity, boolean darkmode) {
        StatusBarUtils.checkNull(activity);
        /*try {
            WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
            Field darkFlag = WindowManager.LayoutParams.class
                    .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
            Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
            darkFlag.setAccessible(true);
            meizuFlags.setAccessible(true);
            int bit = darkFlag.getInt(null);
            int value = meizuFlags.getInt(lp);
            if (darkmode) {
                value |= bit;
            } else {
                value &= ~bit;
            }
            meizuFlags.setInt(lp, value);
            activity.getWindow().setAttributes(lp);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;*/
        return StatusBarColorUtils.setStatusBarDarkIcon(activity,darkmode);
    }


    static int getPxFromDp(Context context, float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dp, context.getResources().getDisplayMetrics());
    }
}

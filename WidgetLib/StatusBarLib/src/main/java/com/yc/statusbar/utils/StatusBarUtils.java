package com.yc.statusbar.utils;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.core.view.ViewCompat;

import com.yc.statusbar.bar.StateAppBar;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211/YCStatusBar
 *     time  : 2018/06/4
 *     desc  : 状态栏工具类
 *     revise: 使用方法请看GitHub说明文档
 *             状态栏文字，图标，大部分手机默认是白色，这个时候如果是设置白色状态栏，那么就会看不见状态栏文字和图标
 *             则可以设置状态栏文字和图标为黑色
 * </pre>
 */
public final class StatusBarUtils {

    private static final String TAG = "StatusBarUtils";

    /**
     * 设置 fitSystemWindows 属性
     * @param window            window窗口
     * @param fitSystemWindows  是否设置fitSystemWindows
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static void setFitsSystemWindows(Window window, boolean fitSystemWindows) {
        ViewGroup mContentView = (ViewGroup) window.findViewById(Window.ID_ANDROID_CONTENT);
        //获取第一个孩子
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            //setFitsSystemWindows(boolean):设置系统是否需要考虑System Bar占据的区域来显示。
            //如果需要的话就会执行 fitSystemWindows(Rect)方法。
            //即设置为true的是时候系统会适应System Bar的区域，让内容不被遮住。
            //注意不是设置 ContentView 的 FitsSystemWindows, 而是设置 ContentView 的第一个子View，预留出系统View的空间.
            mChildView.setFitsSystemWindows(fitSystemWindows);
            ViewCompat.requestApplyInsets(mChildView);
        }
    }

    /**
     * 设置content布局的top的padding间距值
     * @param activity                      activity
     * @param padding                       padding值
     */
    public static void setContentTopPadding(Activity activity, int padding) {
        StatusBarUtils.checkNull(activity);
        ViewGroup mContentView = (ViewGroup) activity.getWindow()
                .findViewById(Window.ID_ANDROID_CONTENT);
        mContentView.setPadding(0, padding, 0, 0);
    }

    /**
     * 设置状态栏字体图标颜色
     *
     * @param activity 当前activity
     * @param dark     是否深色 true为深色 false 为白色
     */
    public static void setMeiZu(Activity activity , boolean dark){
        if (RomUtils.isFlyme()){
            StatusBarColorUtils.setStatusBarDarkIcon(activity , dark);
        }
    }

    /**
     * 状态栏亮色模式，设置状态栏黑色文字、图标，
     * 适配4.4以上版本MIUIV、Flyme和6.0以上版本其他Android
     * @param activity      activity
     * @return              1:MIUUI 2:Flyme 3:android6.0
     * */
    public static int StatusBarLightMode(Activity activity){
        int result=0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if(StateAppBar.setStatusBarLightMode(activity, true)){
                //是否是MIUI
                result=1;
            }else if(StateAppBar.FlymeSetStatusBarLightMode(activity, true)){
                //是否是Flyme
                result=2;
            }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //其他
                activity.getWindow().getDecorView().
                        setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|
                                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                result=3;
            }
        } return result;
    }


    /**
     * 已知系统类型时，设置状态栏黑色文字、图标。
     * 适配4.4以上版本MIUIV、Flyme和6.0以上版本其他Android
     * @param activity      activity
     * @param type          1:MIUUI 2:Flyme 3:android6.0
     */
    public static void StatusBarLightMode(Activity activity,int type){
        if(type==1){
            StateAppBar.setStatusBarLightMode(activity, true);
        }else if(type==2){
            StateAppBar.FlymeSetStatusBarLightMode(activity, true);
        }else if(type==3){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    activity.getWindow().getDecorView().setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                    |View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                }
            }
        }
    }


    /**
     * 状态栏暗色模式，清除MIUI、flyme或6.0以上版本状态栏黑色文字、图标
     */
    public static void StatusBarDarkMode(Activity activity,int type){
        if(type==1){
            StateAppBar.setStatusBarLightMode(activity, false);
        }else if(type==2){
            StateAppBar.FlymeSetStatusBarLightMode(activity, false);
        }else if(type==3){
            activity.getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_VISIBLE);
        }
    }

    public static void checkNull(Object object){
        if (object == null){
            throw new NullPointerException("object is not null");
        }
    }

}

package com.yc.statusbar.utils;


import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

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
public class StatusBarUtils {

    private static final String TAG = "StatusBarHeightUtils";

    /**
     * 获取状态栏高度
     * @param context       context
     * @return              状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        checkNull(context);
        int statusBarHeight = 0;
        Resources res = context.getResources();
        int resourceId = res.getIdentifier("status_bar_height",
                "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = res.getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

    /**
     * 判断状态栏是否存在
     * @param activity                  activity
     * @return
     */
    public static boolean isStatusBarVisible(Activity activity){
        if ((activity.getWindow().getAttributes().flags &
                WindowManager.LayoutParams.FLAG_FULLSCREEN) == 0){
            Log.d(TAG,"status bar is visible");
            return true;
        } else {
            Log.d(TAG,"status bar is not visible");
            return false;
        }
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

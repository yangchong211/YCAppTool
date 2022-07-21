package com.yc.statusbar.utils;


import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.IntDef;
import androidx.core.view.ViewCompat;

import com.yc.statusbar.bar.StateAppBar;
import com.yc.statusbar.view.StatusBarView;
import com.yc.toolutils.AppRomUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.yc.toolutils.StatusBarUtils.getStatusBarHeight;


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
     * 添加一个状态栏高度的自定义view
     */
    private static final String TAG_FAKE_STATUS_BAR_VIEW = "statusBarView";


    /**
     * 设置 fitSystemWindows 属性
     *
     * @param window           window窗口
     * @param fitSystemWindows 是否设置fitSystemWindows
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
     *
     * @param activity activity
     * @param padding  padding值
     */
    public static void setContentTopPadding(Activity activity, int padding) {
        StatusBarUtils.checkNull(activity);
        ViewGroup mContentView = (ViewGroup) activity.getWindow()
                .findViewById(Window.ID_ANDROID_CONTENT);
        mContentView.setPadding(0, padding, 0, 0);
    }


    /**
     * 生成一个和状态栏大小相同的彩色矩形条
     *
     * @param activity 需要设置的 activity
     * @param color    状态栏颜色值
     * @return 状态栏矩形条
     */
    public static StatusBarView createStatusBarView(Activity activity, @ColorInt int color) {
        StatusBarUtils.checkNull(activity);
        // 绘制一个和状态栏一样高的矩形
        StatusBarView statusBarView = new StatusBarView(activity);
        // 获取状态栏的高度
        int statusBarHeight = getStatusBarHeight(activity);
        // 设置view的属性，包括高度和背景颜色
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight);
        params.gravity = Gravity.TOP;
        //设置属性
        statusBarView.setLayoutParams(params);
        //设置颜色
        statusBarView.setBackgroundColor(color);
        //设置标签
        statusBarView.setTag(TAG_FAKE_STATUS_BAR_VIEW);
        return statusBarView;
    }

    /**
     * 如果已经存在假状态栏则移除，假状态栏是指StatusBarView，添加之前可以先移除操作，避免重复添加
     * TAG_FAKE_STATUS_BAR_VIEW    给假的状态栏StatusBarView打的tag标签
     *
     * @param activity activity
     */
    public static void removeFakeStatusBarViewIfExist(Activity activity) {
        Window window = activity.getWindow();
        ViewGroup mDecorView = (ViewGroup) window.getDecorView();
        //通过DecorView找到状态栏view，然后移除
        View fakeView = mDecorView.findViewWithTag(TAG_FAKE_STATUS_BAR_VIEW);
        if (fakeView != null) {
            mDecorView.removeView(fakeView);
        }
    }

    /**
     * 设置状态栏字体图标颜色
     *
     * @param activity 当前activity
     * @param dark     是否深色 true为深色 false 为白色
     */
    public static void setMeiZu(Activity activity, boolean dark) {
        if (AppRomUtils.isFlyme()) {
            StatusBarColorUtils.setStatusBarDarkIcon(activity, dark);
        }
    }

    /**
     * 状态栏亮色模式，设置状态栏黑色文字、图标，
     * 适配4.4以上版本MIUIV、Flyme和6.0以上版本其他Android
     *
     * @param activity activity
     * @return 1:MIUUI 2:Flyme 3:android6.0
     */
    public static int StatusBarLightMode(Activity activity) {
        int result = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (StateAppBar.setStatusBarLightMode(activity, true)) {
                //是否是MIUI
                result = 1;
            } else if (StateAppBar.FlymeSetStatusBarLightMode(activity, true)) {
                //是否是Flyme
                result = 2;
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //其他
                activity.getWindow().getDecorView().
                        setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                result = 3;
            }
        }
        return result;
    }


    /**
     * 已知系统类型时，设置状态栏黑色文字、图标。
     * 适配4.4以上版本MIUIV、Flyme和6.0以上版本其他Android
     *
     * @param activity activity
     * @param type     1:MIUUI 2:Flyme 3:android6.0
     */
    public static void StatusBarLightMode(Activity activity, int type) {
        if (type == 1) {
            StateAppBar.setStatusBarLightMode(activity, true);
        } else if (type == 2) {
            StateAppBar.FlymeSetStatusBarLightMode(activity, true);
        } else if (type == 3) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    activity.getWindow().getDecorView().setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                    | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                }
            }
        }
    }


    /**
     * 状态栏暗色模式，清除MIUI、flyme或6.0以上版本状态栏黑色文字、图标
     */
    public static void StatusBarDarkMode(Activity activity, int type) {
        if (type == 1) {
            StateAppBar.setStatusBarLightMode(activity, false);
        } else if (type == 2) {
            StateAppBar.FlymeSetStatusBarLightMode(activity, false);
        } else if (type == 3) {
            activity.getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_VISIBLE);
        }
    }

    public static void checkNull(Object object) {
        if (object == null) {
            throw new NullPointerException("object is not null");
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface HideBarStatus {
        /**
         * 向后倾斜
         * 向后倾斜模式适用于用户不会与屏幕进行大量互动的全屏体验，例如在观看视频时。
         * 当用户希望调出系统栏时，只需点按屏幕上的任意位置即可。
         */
        int LEAN_BACK = 0;
        /**
         * 沉浸模式
         * 沉浸模式适用于用户将与屏幕进行大量互动的应用。
         * 当用户需要调出系统栏时，他们可从隐藏系统栏的任一边滑动。
         * 要求使用这种这种意图更强的手势是为了确保用户与您应用的互动不会因意外轻触和滑动而中断。
         */
        int IMMERSION_MODE = 1;
        /**
         * 粘性沉浸模式
         * 在普通的沉浸模式中，只要用户从边缘滑动，系统就会负责显示系统栏
         * 在粘性沉浸模式下，如果用户从隐藏了系统栏的边缘滑动，系统栏会显示出来，是半透明的，并且轻触手势会传递给应用，因此应用也会响应该手势。
         * 无互动几秒钟后，或者用户在系统栏之外的任何位置轻触或做手势时，系统栏会自动消失。
         */
        int VISCOUS_IMMERSION = 2;
        /**
         * 全屏幕模式
         * 上面三种模式的综合体，上面三种搞不定，就用这种，把上面所有标签都设置
         */
        int FULL_ALL = 3;
    }

    @IntDef({HideBarStatus.LEAN_BACK,HideBarStatus.IMMERSION_MODE,
            HideBarStatus.FULL_ALL, HideBarStatus.VISCOUS_IMMERSION})
    @Retention(RetentionPolicy.SOURCE)
    public @interface HideBarType{}

}

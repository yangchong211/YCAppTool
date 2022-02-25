package com.yc.toolutils.window;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/11/21
 *     desc  : window相关工具类
 *     revise: v1.4 17年6月8日
 * </pre>
 */
public final class AppWindowUtils {


    /**
     * 获得屏幕宽度
     * @param context                               上下文
     * @return
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * 获得屏幕高度
     * @param context                               上下文
     * @return
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    public static boolean copyToClipBoard(Context context , String content){
        if (!TextUtils.isEmpty(content)){
            //获取剪贴版
            ClipboardManager clipboard = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
            //创建ClipData对象
            //第一个参数只是一个标记，随便传入。
            //第二个参数是要复制到剪贴版的内容
            ClipData clip = ClipData.newPlainText("", content);
            //传入clipdata对象.
            if (clipboard != null) {
                clipboard.setPrimaryClip(clip);
                return true;
            }
        }
        return false;
    }

    /**
     * 设置页面的透明度
     * 主要作用于：弹窗时设置宿主Activity的背景色
     * @param bgAlpha 1表示不透明
     */
    public static void setBackgroundAlpha(Activity activity, float bgAlpha) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        Window window = activity.getWindow();
        if(window!=null){
            if (bgAlpha == 1) {
                //不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
                window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            } else {
                //此行代码主要是解决在华为手机上半透明效果无效的bug
                window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            }
            window.setAttributes(lp);
        }
    }

    /**
     * 设置页面的昏暗度
     * 主要作用于：弹窗时设置宿主Activity的背景色
     * @param bgDimAmount
     */
    public static void setBackgroundDimAmount(Activity activity, float bgDimAmount){
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.dimAmount = bgDimAmount;
        Window window = activity.getWindow();
        if(window!=null){
            if(bgDimAmount == 1){
                window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            }else {
                window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            }
            window.setAttributes(lp);
        }
    }


}

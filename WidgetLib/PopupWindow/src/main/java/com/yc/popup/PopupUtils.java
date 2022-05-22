package com.yc.popup;

import android.app.Activity;
import android.os.Looper;
import android.view.Window;
import android.view.WindowManager;
import java.lang.ref.SoftReference;
/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/06/4
 *     desc  : 弹窗工具类
 *     revise:
 *     GitHub: https://github.com/yangchong211/YCDialog
 * </pre>
 */
public final class PopupUtils {

    public static void checkMainThread(){
        if (!isMainThread()){
            throw new IllegalStateException("请不要在子线程中做弹窗操作");
        }
    }

    private static boolean isMainThread(){
        return Looper.getMainLooper() == Looper.myLooper();
    }

    /**
     * 设置页面的透明度
     * 主要作用于：弹窗时设置宿主Activity的背景色
     * @param bgAlpha 1表示不透明
     */
    public static void setBackgroundAlpha(Activity activity , float bgAlpha) {
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


    public static boolean checkNull(SoftReference softReference) {
        if (softReference == null || softReference.get() == null) {
            return true;
        }
        return false;
    }

}

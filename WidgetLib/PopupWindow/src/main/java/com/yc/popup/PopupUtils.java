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

}

package com.ycbjie.library.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Color;
import android.view.View;

import com.ns.yc.ycutilslib.rippleLayout.MaterialRippleLayout;

import java.util.List;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/11/21
 *     desc  : 工具类
 *     revise: v1.4 17年6月8日
 *             v1.5 17年10月3日修改
 * </pre>
 */
public class AppToolUtils {



    /**
     * 设置水波纹效果
     * @param view          view视图
     */
    public static void setRipper(View view){
        MaterialRippleLayout.on(view)
                .rippleColor(Color.parseColor("#999999"))
                .rippleAlpha(0.2f)
                .rippleHover(true)
                .create();
    }


    /**
     * 判断app是否正在运行
     * @param context                       上下文
     * @param packageName                   应用的包名
     * @return true 表示正在运行，false 表示没有运行
     */
    public static boolean isAppRunning(Context context, String packageName) {
        ActivityManager am = (ActivityManager) context.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list ;
        if (am != null) {
            list = am.getRunningTasks(100);
            if (list.size() <= 0) {
                return false;
            }
            for (ActivityManager.RunningTaskInfo info : list) {
                if (info.baseActivity.getPackageName().equals(packageName)) {
                    return true;
                }
            }
        }
        return false;
    }


}

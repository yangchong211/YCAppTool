package com.yc.toolutils.component;

import android.app.Activity;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;

/**
 * <pre>
 *     @author yangchong
 *     GitHub : https://github.com/yangchong211/YCAppTool
 *     email  : yangchong211@163.com
 *     time  : 2018/09/23
 *     desc  : View 相关工具类
 *     revise:
 * </pre>
 */
public final class AppViewUtils {


    /**
     * 判断屏幕上是否可见
     *
     * @param activity activity
     * @param view     view
     * @return true表示可见
     */
    public static boolean checkIfVisibility(Activity activity, View view) {
        if (activity == null || view == null) {
            return false;
        }
        Point p = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(p);
        int screenWidth = p.x;
        int screenHeight = p.y;
        Rect rect = new Rect(0, 0, screenWidth, screenHeight);
        int[] location = new int[2];
        view.getLocationInWindow(location);
        if (view.getLocalVisibleRect(rect)) {
            return true;
        } else {
            return false;
        }
    }

}

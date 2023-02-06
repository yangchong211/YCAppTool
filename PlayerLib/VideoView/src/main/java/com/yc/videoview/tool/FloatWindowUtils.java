package com.yc.videoview.tool;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.provider.Settings;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;


public final class FloatWindowUtils {


    @RequiresApi(api = Build.VERSION_CODES.M)
    public static boolean hasPermission(Context context) {
        return Settings.canDrawOverlays(context);
    }


    private static Point sPoint;

    public static int getScreenWidth(Context context) {
        if (sPoint == null) {
            sPoint = new Point();
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            if (wm != null) {
                wm.getDefaultDisplay().getSize(sPoint);
            }
        }
        return sPoint.x;
    }

    public static int getScreenHeight(Context context) {
        if (sPoint == null) {
            sPoint = new Point();
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            if (wm != null) {
                wm.getDefaultDisplay().getSize(sPoint);
            }
        }
        return sPoint.y;
    }
}

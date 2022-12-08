package com.yc.appgraylib;

import android.app.Application;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.view.View;
import android.view.Window;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time   : 2018/5/11
 *     desc   : App切换灰色设置
 *     revise :
 * </pre>
 */
public class AppGrayHelper {

    private static volatile AppGrayHelper appGrayHelper;
    private boolean isGray = false;
    private int type = 1;

    public static AppGrayHelper getInstance() {
        if (appGrayHelper == null) {
            synchronized (AppGrayHelper.class) {
                if (appGrayHelper == null) {
                    appGrayHelper = new AppGrayHelper();
                }
            }
        }
        return appGrayHelper;
    }


    public AppGrayHelper setGray(boolean gray) {
        isGray = gray;
        return this;
    }

    public boolean isGray() {
        return isGray;
    }

    public int getType() {
        return type;
    }

    public AppGrayHelper setType(int type) {
        this.type = type;
        return this;
    }

    public void setGrayApp(Application app) {
        GrayManager.getInstance().init();
        ActivityCallback.inject(app);
    }

    /**
     * 方案一
     * 给Activity的顶层View设置置灰，实现全局置灰效果
     * 可以在BaseActivity的onCreate方法中，使用ColorMatrix设置灰度
     *
     * @param window window
     */
    public void setGray1(Window window) {
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        //灰度效果
        if (isGray) {
            cm.setSaturation(0);
        } else {
            cm.setSaturation(1);
        }
        paint.setColorFilter(new ColorMatrixColorFilter(cm));
        window.getDecorView().setLayerType(View.LAYER_TYPE_HARDWARE, paint);
    }

    public void setGray2(View view) {
        GrayManager.getInstance().setLayerGrayType(view);
    }
}

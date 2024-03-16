package com.yc.appgraylib;

import android.app.Application;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.view.View;
import android.view.Window;

import com.yc.appgraylib.hook.GlobalGrayHelper;
import com.yc.appgraylib.lifecycle.ActivityCallback;
import com.yc.appgraylib.manager.GrayManager;


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
    private boolean isGlobalGray = false;

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

    /**
     * 初始化
     * @param app           app
     */
    public void initGrayApp(Application app , boolean globalGray) {
        GrayManager.getInstance().init();
        ActivityCallback.inject(app);
        this.isGlobalGray = globalGray;
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

    public boolean isGlobalGray() {
        return isGlobalGray;
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
        //1、LAYER_TYPE_NONE：视图正常渲染，不受屏幕外缓冲区支持。这是默认行为。
        //2、LAYER_TYPE_HARDWARE：如果应用经过硬件加速，视图在硬件中渲染为硬件纹理。
        //   如果应用未经过硬件加速，此层类型的行为方式与 LAYER_TYPE_SOFTWARE 相同。
        //3、LAYER_TYPE_SOFTWARE：使用软件来渲染视图，绘制到 Bitmap，并顺便关闭硬件加速 。
        window.getDecorView().setLayerType(View.LAYER_TYPE_HARDWARE, paint);
    }

    /**
     * 方案二：设置任意View灰色方案
     * @param view                  view
     */
    public void setGray2(View view) {
        GrayManager.getInstance().setLayerGrayType(view);
    }

    /**
     * 方案三：使用hook设置全局灰色方案
     */
    public void setGray3(){
        GlobalGrayHelper.enable(isGray);
    }
}

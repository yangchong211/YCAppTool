package com.yc.appgraylib.manager;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.view.View;

import com.yc.appgraylib.AppGrayHelper;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time   : 2018/5/11
 *     desc   : 灰色设置View工具
 *     revise :
 * </pre>
 */
public final class GrayManager {

    private static volatile GrayManager mInstance;
    private Paint mGrayPaint;
    private ColorMatrix mGrayMatrix;
    private boolean isGray;

    public static GrayManager getInstance() {
        if (mInstance == null) {
            synchronized (GrayManager.class) {
                if (mInstance == null) {
                    mInstance = new GrayManager();
                }
            }
        }
        return mInstance;
    }

    public void init() {
        mGrayMatrix = new ColorMatrix();
        mGrayPaint = new Paint();
        isGray = AppGrayHelper.getInstance().isGray();
        if (isGray) {
            mGrayMatrix.setSaturation(0);
        } else {
            mGrayMatrix.setSaturation(1);
        }
        ColorMatrixColorFilter colorMatrixColorFilter = new ColorMatrixColorFilter(mGrayMatrix);
        mGrayPaint.setColorFilter(colorMatrixColorFilter);
    }

    /**
     * 硬件加速置灰方法
     * @param view          view
     */
    public void setLayerGrayType(View view) {
        boolean gray = AppGrayHelper.getInstance().isGray();
        if (mGrayMatrix == null || mGrayPaint == null || gray != isGray) {
            init();
        }
        view.setLayerType(View.LAYER_TYPE_HARDWARE, mGrayPaint);
    }


}

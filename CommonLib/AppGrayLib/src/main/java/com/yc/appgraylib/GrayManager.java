package com.yc.appgraylib;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.view.View;

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

    private static GrayManager mInstance;
    private Paint mGrayPaint;
    private ColorMatrix mGrayMatrix;

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
        if (AppGrayHelper.getInstance().isGray()){
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
        if (mGrayMatrix == null || mGrayPaint == null) {
            init();
        }
        view.setLayerType(View.LAYER_TYPE_HARDWARE, mGrayPaint);
    }


}

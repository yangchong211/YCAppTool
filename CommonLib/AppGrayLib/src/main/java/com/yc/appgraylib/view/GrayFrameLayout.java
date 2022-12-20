package com.yc.appgraylib.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.yc.appgraylib.AppGrayHelper;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time   : 2018/5/11
 *     desc   : 灰色设置View
 *     revise :
 * </pre>
 */
public class GrayFrameLayout extends FrameLayout {

    private final Paint mPaint = new Paint();

    public GrayFrameLayout(@NonNull Context context) {
        super(context);
    }

    public GrayFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        ColorMatrix cm = new ColorMatrix();
        if (AppGrayHelper.getInstance().isGray()){
            cm.setSaturation(0);
        } else {
            cm.setSaturation(1);
        }
        mPaint.setColorFilter(new ColorMatrixColorFilter(cm));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.saveLayer(null, mPaint, Canvas.ALL_SAVE_FLAG);
        super.onDraw(canvas);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.saveLayer(null, mPaint, Canvas.ALL_SAVE_FLAG);
        super.dispatchDraw(canvas);
    }
}

package com.ycbjie.zoomimagelib.anim;

import android.animation.ValueAnimator;
import android.graphics.Matrix;

import com.ycbjie.zoomimagelib.config.ZoomConfig;
import com.ycbjie.zoomimagelib.view.ZoomImageView;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/09/12
 *     desc  : 缩放动画
 *     revise:
 * </pre>
 */
public class ScaleAnimator extends ValueAnimator implements ValueAnimator.AnimatorUpdateListener {
    /**
     * 开始矩阵
     */
    private float[] mStart = new float[9];
    /**
     * 结束矩阵
     */
    private float[] mEnd = new float[9];
    /**
     * 中间结果矩阵
     */
    private float[] mResult = new float[9];
    /**
     * 外层变换矩阵，如果是单位矩阵，那么图片是fit center状态
     */
    private Matrix mOuterMatrix;
    private ZoomImageView view;
    /**
     * 构建一个缩放动画，从一个矩阵变换到另外一个矩阵
     * @param start 开始矩阵
     * @param end 结束矩阵
     */
    public ScaleAnimator(Matrix start, Matrix end ,ZoomImageView view) {
        this(start, end, ZoomConfig.SCALE_ANIMATOR_DURATION,view);
    }

    /**
     * 构建一个缩放动画，从一个矩阵变换到另外一个矩阵
     *
     * @param start 开始矩阵
     * @param end 结束矩阵
     * @param duration 动画时间
     */
    public ScaleAnimator(Matrix start, Matrix end, long duration , ZoomImageView view) {
        super();
        this.mOuterMatrix = start;
        this.view = view;
        setFloatValues(0, 1f);
        setDuration(duration);
        addUpdateListener(this);
        start.getValues(mStart);
        end.getValues(mEnd);
    }

    /**
     * 通知另一个动画帧的出现
     * @param animation                 animation
     */
    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        //获取动画进度
        float value = (Float) animation.getAnimatedValue();
        //根据动画进度计算矩阵中间插值
        for (int i = 0; i < 9; i++) {
            mResult[i] = mStart[i] + (mEnd[i] - mStart[i]) * value;
        }
        //设置矩阵并重绘
        mOuterMatrix.setValues(mResult);
        view.invalidate();
    }
}

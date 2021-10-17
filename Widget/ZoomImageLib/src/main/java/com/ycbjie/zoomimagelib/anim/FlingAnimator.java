package com.ycbjie.zoomimagelib.anim;

import android.animation.ValueAnimator;

import com.ycbjie.zoomimagelib.config.ZoomConfig;
import com.ycbjie.zoomimagelib.utils.MathUtils;
import com.ycbjie.zoomimagelib.view.ZoomImageView;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/09/12
 *     desc  : 惯性动画
 *     revise:
 * </pre>
 */
public class FlingAnimator extends ValueAnimator implements ValueAnimator.AnimatorUpdateListener {

    /*
     * 惯性动画
     * 速度逐渐衰减,每帧速度衰减为原来的FLING_DAMPING_FACTOR,当速度衰减到小于1时停止.
     * 当图片不能移动时,动画停止.
     */

    /**
     * 速度向量
     */
    private float[] mVector;
    private ZoomImageView view;
    /**
     * 创建惯性动画，参数单位为 像素/帧
     *
     * @param vectorX 速度向量
     * @param vectorY 速度向量
     */
    public FlingAnimator(float vectorX, float vectorY , ZoomImageView view) {
        super();
        this.view = view;
        setFloatValues(0, 1f);
        setDuration(1000000);
        addUpdateListener(this);
        mVector = new float[]{vectorX, vectorY};
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        //移动图像并给出结果
        boolean result = view.scrollBy(mVector[0], mVector[1]);
        //衰减速度
        mVector[0] *= ZoomConfig.FLING_DAMPING_FACTOR;
        mVector[1] *= ZoomConfig.FLING_DAMPING_FACTOR;
        //速度太小或者不能移动了就结束
        if (!result || MathUtils.getDistance(0, 0, mVector[0], mVector[1]) < 1f) {
            animation.cancel();
        }
    }
}

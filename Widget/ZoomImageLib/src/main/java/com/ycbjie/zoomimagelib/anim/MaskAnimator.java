package com.ycbjie.zoomimagelib.anim;

import android.animation.ValueAnimator;
import android.graphics.RectF;

import com.ycbjie.zoomimagelib.view.ZoomImageView;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/09/12
 *     desc  : mask变换动画
 *     revise:
 * </pre>
 */
public class MaskAnimator extends ValueAnimator implements ValueAnimator.AnimatorUpdateListener {

    /*
     * mask变换动画
     * 将mask从一个rect动画到另外一个rect
     */

    /**
     * 开始mask
     */
    private float[] mStart = new float[4];
    /**
     * 结束mask
     */
    private float[] mEnd = new float[4];
    /**
     * 中间结果mask
     */
    private float[] mResult = new float[4];
    /**
     * 矩形遮罩
     */
    private RectF mMask;
    private ZoomImageView view;

    /**
     * 创建mask变换动画
     *
     * @param start 动画起始状态
     * @param end 动画终点状态
     * @param duration 动画持续时间
     */
    public MaskAnimator(RectF start, RectF end, long duration , ZoomImageView view) {
        super();
        this.mMask = start;
        this.view = view;
        setFloatValues(0, 1f);
        setDuration(duration);
        addUpdateListener(this);
        //将起点终点拷贝到数组方便计算
        mStart[0] = start.left;
        mStart[1] = start.top;
        mStart[2] = start.right;
        mStart[3] = start.bottom;
        mEnd[0] = end.left;
        mEnd[1] = end.top;
        mEnd[2] = end.right;
        mEnd[3] = end.bottom;
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        //获取动画进度,0-1范围
        float value = (Float) animation.getAnimatedValue();
        //根据进度对起点终点之间做插值
        for (int i = 0; i < 4; i++) {
            mResult[i] = mStart[i] + (mEnd[i] - mStart[i]) * value;
        }
        //期间mask有可能被置空了,所以判断一下
        if (mMask == null) {
            mMask = new RectF();
        }
        //设置新的mask并绘制
        mMask.set(mResult[0], mResult[1], mResult[2], mResult[3]);
        view.invalidate();
    }
}

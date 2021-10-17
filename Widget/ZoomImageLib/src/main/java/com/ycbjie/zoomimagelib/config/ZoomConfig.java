package com.ycbjie.zoomimagelib.config;



/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/05/30
 *     desc  : 一些基础配置信息
 *     revise:
 * </pre>
 */
public class ZoomConfig {

    /**
     * 图片缩放动画时间
     */
    public static final int SCALE_ANIMATOR_DURATION = 200;

    /**
     * 惯性动画衰减参数
     */
    public static final float FLING_DAMPING_FACTOR = 0.9f;

    /**
     * 图片最大放大比例
     */
    public static float MAX_SCALE = 2f;

    /**
     * 手势状态：自由状态
     */
    public static final int PINCH_MODE_FREE = 0;

    /**
     * 手势状态：单指滚动状态
     */
    public static final int PINCH_MODE_SCROLL = 1;

    /**
     * 手势状态：双指缩放状态
     */
    public static final int PINCH_MODE_SCALE = 2;

}

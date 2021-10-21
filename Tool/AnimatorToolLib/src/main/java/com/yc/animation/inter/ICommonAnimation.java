package com.yc.animation.inter;


/**
 * <pre>
 *     @author  yangchong
 *     email  : yangchong211@163.com
 *     time  :  2017/5/26
 *     desc  :  常用效果定义
 *     revise:
 * </pre>
 */
public interface ICommonAnimation<T> {

    /**
     * 淡入效果
     */
    T fadeIn();

    /**
     * 淡出效果
     */
    T fadeOut();

    /**
     * 从屏幕顶部移入
     */
    T translateInFromTop();

    /**
     * 从屏幕顶部移出
     */
    T translateOutToTop();

    /**
     * 从屏幕底部移入
     */
    T translateInFromBottom();

    /**
     * 从屏幕底部移出
     */
    T translateOutToBottom();

    /**
     * 缩放淡入效果
     */
    T scaleIn();

    /**
     * 缩小淡出效果
     */
    T scaleOut();

    /**
     * 顺时针方向旋转
     */
    T rotateCW();

    /**
     * 逆时针方向旋转
     */
    T rotateCCW();

}

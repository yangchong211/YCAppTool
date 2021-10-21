package com.yc.animation.constant;

/**
 * <pre>
 *     @author  yangchong
 *     email  : yangchong211@163.com
 *     time  :  2017/5/26
 *     desc  :  动画等级定义
 *     revise:
 * </pre>
 */
public interface AnimationLevel {
    /** 真实的动画处理：使用属性动画实现 */
    int LEVEL_REAL = 1;
    /** 降级处理：直接更新 view 属性为目标值 */
    int LEVEL_FAKE = 2;
}

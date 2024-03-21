package com.yc.audioplayer.powerful.bean;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     GitHub : <a href="https://github.com/yangchong211/YCVideoPlayer">...</a>
 *     time  : 2018/8/6
 *     desc  : tts优先级
 *     revise: 优先级顺序：HIGH_PRIORITY > MIDDLE_PRIORITY > NORMAL_PRIORITY
 * </pre>
 */
public enum AudioTtsPriority {

    /**
     * 正常普通级
     */
    NORMAL_PRIORITY,

    /**
     * 中优先级
     */
    MIDDLE_PRIORITY,

    /**
     * 最高优先级
     */
    HIGH_PRIORITY
}

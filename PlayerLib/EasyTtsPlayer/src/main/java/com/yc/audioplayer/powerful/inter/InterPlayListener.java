package com.yc.audioplayer.powerful.inter;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     GitHub : <a href="https://github.com/yangchong211/YCVideoPlayer">...</a>
 *     time  : 2018/8/6
 *     desc  : 状态接口
 *     revise:
 * </pre>
 */
public interface InterPlayListener {

    /**
     * 播放完成
     */
    void onCompleted();

    /**
     * 播放失败
     * @param error                         失败信息
     */
    void onError(String error);

}

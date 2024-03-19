package com.yc.audioplayer.powerful.inter;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     GitHub : <a href="https://github.com/yangchong211/YCVideoPlayer">...</a>
 *     time  : 2018/8/6
 *     desc  : 外部播放状态接口监听
 *     revise:
 * </pre>
 */
public interface PlayStateListener {

    void onStartPlay();

    void onStopPlay();

    void onCompletePlay();

}

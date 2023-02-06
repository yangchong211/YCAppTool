package com.yc.audioplayer.inter;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     GitHub : https://github.com/yangchong211/YCVideoPlayer
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

package com.yc.video.inter;

import android.graphics.Bitmap;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/11/21
 *     desc  : VideoPlayer屏幕相关接口
 *     revise:
 * </pre>
 */
public interface IVideoPlayerScreen {

    /**
     * 屏幕截图
     * @return                              返回bitmap对象
     */
    Bitmap doScreenShot();


    /**
     * 开启全屏幕展示
     */
    void startFullScreen();

    /**
     * 关闭全屏幕展示
     */
    void stopFullScreen();

    /**
     * 判断是否全屏幕展示
     * @return                              布尔
     */
    boolean isFullScreen();

    /**
     * 开启小屏幕展示
     */
    void startTinyScreen();

    /**
     * 关闭小屏幕展示
     */
    void stopTinyScreen();

    /**
     * 判断是否小屏幕
     * @return                              布尔
     */
    boolean isTinyScreen();


}

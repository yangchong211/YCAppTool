package com.yc.kernel.impl.exo;

import com.google.android.exoplayer2.video.VideoListener;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2021/11/15
 *     desc  : exo监听VideoListener
 *     revise:
 * </pre>
 */
public class DefaultVideoListener implements VideoListener {

    /**
     * 视频size变化监听
     * @param width
     * @param height
     * @param unappliedRotationDegrees
     * @param pixelWidthHeightRatio
     */
    @Override
    public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {

    }

    /**
     * Surface的size变化监听
     * @param width
     * @param height
     */
    @Override
    public void onSurfaceSizeChanged(int width, int height) {

    }

    @Override
    public void onRenderedFirstFrame() {

    }
}

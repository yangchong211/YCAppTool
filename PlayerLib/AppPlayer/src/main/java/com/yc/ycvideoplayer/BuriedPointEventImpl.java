package com.yc.ycvideoplayer;

import com.yc.toolutils.AppLogUtils;

import com.yc.video.config.BuriedPointEvent;

public class BuriedPointEventImpl implements BuriedPointEvent {

    /**
     * 进入视频播放
     * @param url                       视频url
     */
    @Override
    public void playerIn(String url) {
        AppLogUtils.i("BuriedPointEvent---进入视频播放--"+url);
    }

    /**
     * 退出视频播放
     * @param url                       视频url
     */
    @Override
    public void playerDestroy(String url) {
        AppLogUtils.i("BuriedPointEvent---退出视频播放--"+url);
    }

    /**
     * 视频播放完成
     * @param url                       视频url
     */
    @Override
    public void playerCompletion(String url) {
        AppLogUtils.i("BuriedPointEvent---视频播放完成--"+url);
    }

    /**
     * 视频播放异常
     * @param url                       视频url
     * @param isNetError                是否是网络异常
     */
    @Override
    public void onError(String url, boolean isNetError) {
        AppLogUtils.i("BuriedPointEvent---视频播放异常--"+url);
    }

    /**
     * 点击了视频广告
     * @param url                       视频url
     */
    @Override
    public void clickAd(String url) {
        AppLogUtils.i("BuriedPointEvent---点击了视频广告--"+url);
    }

    /**
     * 视频试看点击
     * @param url                       视频url
     */
    @Override
    public void playerAndProved(String url) {
        AppLogUtils.i("BuriedPointEvent---视频试看点击--"+url);
    }

    /**
     * 退出视频播放时候的播放进度百度比
     * @param url                       视频url
     * @param progress                  视频进度，计算百分比【退出时候进度 / 总进度】
     */
    @Override
    public void playerOutProgress(String url, float progress) {
        AppLogUtils.i("BuriedPointEvent---退出视频播放时候的播放进度百度比--"+url+"-----"+progress);
    }

    /**
     * 退出视频播放时候的播放进度
     * @param url                       视频url
     * @param duration                  总时长
     * @param currentPosition           当前进度时长
     */
    @Override
    public void playerOutProgress(String url, long duration, long currentPosition) {
        AppLogUtils.i("BuriedPointEvent---退出视频播放时候的播放进度百度比--"+url+"-----"+duration+"----"+currentPosition);
    }

    /**
     * 视频切换音频
     * @param url                       视频url
     */
    @Override
    public void videoToMedia(String url) {
        AppLogUtils.i("BuriedPointEvent---视频切换音频--"+url);
    }
}

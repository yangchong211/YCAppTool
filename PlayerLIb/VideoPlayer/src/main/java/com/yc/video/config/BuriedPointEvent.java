/*
Copyright 2017 yangchong211（github.com/yangchong211）

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package com.yc.video.config;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/1/29
 *     desc  : 视频全局埋点事件
 *     revise:
 * </pre>
 */
public interface BuriedPointEvent {

    /**
     * 进入视频播放
     * @param url                       视频url
     */
    void playerIn(String url);

    /**
     * 退出视频播放
     * @param url                       视频url
     */
    void playerDestroy(String url);

    /**
     * 视频播放完成
     * @param url                       视频url
     */
    void playerCompletion(String url);

    /**
     * 视频播放异常
     * @param url                       视频url
     * @param isNetError                是否是网络异常
     */
    void onError(String url , boolean isNetError);

    /**
     * 点击了视频广告
     * @param url                       视频url
     */
    void clickAd(String url);

    /**
     * 视频试看点击
     * @param url                       视频url
     */
    void playerAndProved(String url);


    /**
     * 退出视频播放时候的播放进度百度分
     * @param url                       视频url
     * @param progress                  视频进度，计算百分比【退出时候进度 / 总进度】
     */
    void playerOutProgress(String url , float progress);

    /**
     * 退出视频播放时候的播放进度
     * @param url                       视频url
     * @param duration                  总时长
     * @param currentPosition           当前进度时长
     */
    void playerOutProgress(String url, long duration , long currentPosition);

    /**
     * 视频切换音频
     * @param url                       视频url
     */
    void videoToMedia(String url);


}

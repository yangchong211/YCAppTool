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
package com.yc.kernel.inter;

import android.view.SurfaceHolder;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/11/9
 *     desc  : 播放器状态总接口【拓展成视频播放器和音频播放器lib的公共接口】
 *     revise:
 * </pre>
 */
public interface InterBasePlayer {

    /*----------------------------第二部分：视频播放器状态方法----------------------------------*/

    /**
     * 播放
     */
    void start();

    /**
     * 暂停
     */
    void pause();

    /**
     * 停止
     */
    void stop();

    /**
     * 重置播放器
     */
    void reset();

    /**
     * 是否正在播放
     * @return                                  是否正在播放
     */
    boolean isPlaying();

    /**
     * 调整进度
     */
    void seekTo(long time);

    /**
     * 释放播放器
     */
    void release();

    /**
     * 获取当前播放的位置
     * @return                                  获取当前播放的位置
     */
    long getCurrentPosition();

    /**
     * 获取视频总时长
     * @return                                  获取视频总时长
     */
    long getDuration();

    /**
     * 获取缓冲百分比
     * @return                                  获取缓冲百分比
     */
    int getBufferedPercentage();

    /**
     * 设置渲染视频的View,主要用于SurfaceView
     * @param holder                            holder
     */
    void setDisplay(SurfaceHolder holder);

    /**
     * 设置音量
     * @param v1                                v1
     * @param v2                                v2
     */
    void setVolume(float v1, float v2);

    /**
     * 设置是否循环播放
     * @param isLooping                         布尔值
     */
    void setLooping(boolean isLooping);

    /**
     * 设置其他播放配置
     */
    void setOptions();

    /**
     * 设置播放速度
     * @param speed                             速度
     */
    void setSpeed(float speed);

    /**
     * 获取播放速度
     * @return                                  播放速度
     */
    float getSpeed();

    /**
     * 获取当前缓冲的网速
     * @return                                  获取网络
     */
    long getTcpSpeed();

}

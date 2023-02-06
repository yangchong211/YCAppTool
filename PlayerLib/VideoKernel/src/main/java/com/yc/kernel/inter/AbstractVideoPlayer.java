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


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/11/9
 *     desc  : 抽象的播放器【所有的不同内核播放器统一继承该接口】
 *     revise: 第一部分：通用音视频初始化接口，比如初始化音视频播放器，设置播放资源，开始播放等
 *             第二部分：通用音视频状态接口，比如开始，暂停，结束，获取时长，设置音量等
 *             第三部分：播放器event监听接口，比如异常，完成，准备，size变化等
 * </pre>
 */
public abstract class AbstractVideoPlayer implements InterInitPlayer, InterBasePlayer{

    /**
     * 播放器事件回调
     */
    protected VideoPlayerListener mPlayerEventListener;

    /*----------------------------第三部分：player绑定view后，需要监听播放状态--------------------*/

    /**
     * 绑定VideoView，监听播放异常，完成，开始准备，视频size变化，视频信息等操作
     */
    public void setPlayerEventListener(VideoPlayerListener playerEventListener) {
        this.mPlayerEventListener = playerEventListener;
    }

}


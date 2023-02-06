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

import android.content.res.AssetFileDescriptor;
import android.view.Surface;

import java.util.Map;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/11/9
 *     desc  : 播放器初始化总接口【拓展成视频播放器和音频播放器lib的公共接口】
 *     revise:
 *     read  : 视频播放器一般分为四步，把这几步给抽象成接口，为通用播放器接口
 *             第一步：初始化创建播放器
 *             第二步：设置数据
 *             第三步：添加渲染视图
 *             第四步：开始播放
 * </pre>
 */
public interface InterInitPlayer {

    /*----------------------------第一部分：视频初始化实例对象方法----------------------------------*/


    /**
     * 初始化播放器实例
     * 视频播放器第一步：创建视频播放器
     */
    void initPlayer();

    /**
     * 设置播放地址
     * 视频播放器第二步：设置数据
     *
     * @param path    播放地址
     * @param headers 播放地址请求头
     */
    void setDataSource(String path, Map<String, String> headers);

    /**
     * 用于播放raw和asset里面的视频文件
     *
     * @param fd 资源文件
     */
    void setDataSource(AssetFileDescriptor fd);

    /**
     * 设置渲染视频的View,主要用于TextureView
     * 视频播放器第三步：设置surface
     *
     * @param surface surface
     */
    void setSurface(Surface surface);

    /**
     * 准备开始播放（异步）
     * 视频播放器第四步：开始加载【异步】
     */
    void prepareAsync();


}

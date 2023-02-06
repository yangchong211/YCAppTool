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
package com.yc.video.player;

/**
 * 播放进度管理器，继承此接口实现自己的进度管理器。
 */
public abstract class ProgressManager {

    /**
     * 此方法用于实现保存进度的逻辑
     * @param url 播放地址
     * @param progress 播放进度
     */
    public abstract void saveProgress(String url, long progress);

    /**
     * 此方法用于实现获取保存的进度的逻辑
     * @param url 播放地址
     * @return 保存的播放进度
     */
    public abstract long getSavedProgress(String url);

}

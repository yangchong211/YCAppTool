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
package com.yc.store.config


import java.io.File

/**
 * @author yangchong
 * email  : yangchong211@163.com
 * time   : 2022/5/11
 * desc   : 配置文件
 * revise :
 */
class CacheConfig private constructor(builder: Builder) {

    /**
     * lru缓存最大大小
     */
    val maxCacheSize: Int

    /**
     * 是否是debug环境
     */
    val isDebuggable: Boolean

    /**
     * log路径，通常这个缓存比较私有的内容
     * 比如sp，mmkv，存储的用户数据
     * 内部存储根目录，举个例子：
     * file:data/user/0/包名/files
     * cache:/data/user/0/包名/cache
     */
    val logDir: String?

    /**
     * 额外的log路径，通常缓存一些不私密的内存
     * 比如缓存图片，缓存视频，缓存下载文件，缓存日志等
     *
     * 外部存储根目录，举个例子
     * files:/storage/emulated/0/Android/data/包名/files
     * cache:/storage/emulated/0/Android/data/包名/cache
     */
    val extraLogDir: File?

    /**
     * mmkv文件名称
     */
    val mmkvName: String?

    /**
     * 降级接口
     */
    var monitorToggle: com.yc.appcommoninter.inter.IMonitorToggle?

    class Builder {

        var maxCacheSize = 1024
        var debuggable = false
        var logDir: String? = null
        var extraLogDir: File? = null
        var mmkvName: String? = null
        var monitorToggle: com.yc.appcommoninter.inter.IMonitorToggle? = null

        fun maxCacheSize(maxCacheSize: Int): Builder {
            this.maxCacheSize = maxCacheSize
            return this
        }

        fun debuggable(debuggable: Boolean): Builder {
            this.debuggable = debuggable
            return this
        }

        fun extraLogDir(extraLogDir: File?): Builder {
            this.extraLogDir = extraLogDir
            return this
        }

        fun logDir(logDir: String?): Builder {
            this.logDir = logDir
            return this
        }

        fun mmkvName(mmkvName: String?): Builder {
            this.mmkvName = mmkvName
            return this
        }

        fun monitorToggle(monitorToggle: com.yc.appcommoninter.inter.IMonitorToggle?): Builder {
            this.monitorToggle = monitorToggle
            return this
        }


        fun build(): CacheConfig {
            return CacheConfig(this)
        }
    }

    companion object {
        //使用默认
        fun newBuilder(): Builder {
            return Builder()
        }
    }

    init {
        maxCacheSize = builder.maxCacheSize
        isDebuggable = builder.debuggable
        extraLogDir = builder.extraLogDir
        logDir = builder.logDir
        mmkvName = builder.mmkvName
        monitorToggle = builder.monitorToggle
    }
}
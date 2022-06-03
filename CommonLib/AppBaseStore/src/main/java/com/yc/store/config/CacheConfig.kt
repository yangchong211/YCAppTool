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
     * log路径
     *
     */
    val logDir: String?

    /**
     * 额外的log路径
     */
    val extraLogDir: File?


    class Builder {

        var maxCacheSize = 1024
        var debuggable = false
        var logDir: String? = null
        var extraLogDir: File? = null

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
    }
}
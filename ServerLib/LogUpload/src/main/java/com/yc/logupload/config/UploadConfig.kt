package com.yc.logupload.config

import com.yc.logupload.inter.IUploadLog
import com.yc.logupload.report.HttpUploadImpl
import java.io.File

/**
 * @author yangchong
 * email  : yangchong211@163.com
 * time   : 2022/5/11
 * desc   : 配置文件
 * revise :
 */
class UploadConfig private constructor(builder: Builder) {

    /**
     * 网络上报接口
     */
    val serverUrl: String?

    /**
     * 总的文件大小限制
     */
    val totalFileSize: Long

    /**
     * 单个文件最大大小
     */
    val maxFileSize: Long

    /**
     * 是否是debug环境
     */
    val isDebuggable: Boolean

    /**
     * log路径
     *
     */
    val logDir: File?

    /**
     * 额外的log路径
     */
    val extraLogDir: File?

    /**
     * 上传方式，默认是http上传
     */
    val iUploadLog : IUploadLog?

    /**
     * 是否开启切换到后台上传日志
     */
    var isInBackgroundUpload : Boolean

    class Builder {

        var serverHost: String? = null
        var totalFileSize = (50 * 1024 * 1024).toLong()
        var maxFileSize = (5 * 1024 * 1024).toLong()
        var debuggable = false
        var logDir: File? = null
        var extraLogDir: File? = null
        var iUploadLog : IUploadLog? = null
        //默认不开启
        var isInBackgroundUpload = false

        fun serverUrl(serverHost: String?): Builder {
            this.serverHost = serverHost
            return this
        }

        fun totalFileSize(totalFileSize: Long): Builder {
            this.totalFileSize = totalFileSize
            return this
        }

        fun maxFileSize(maxFileSize: Long): Builder {
            this.maxFileSize = maxFileSize
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

        fun logDir(logDir: File?): Builder {
            this.logDir = logDir
            return this
        }

        fun iUploadLog(iUploadLog: IUploadLog?): Builder {
            this.iUploadLog = iUploadLog
            return this
        }

        fun isInBackgroundUpload(isInBackgroundUpload: Boolean): Builder {
            this.isInBackgroundUpload = isInBackgroundUpload
            return this
        }

        fun build(): UploadConfig {
            if (iUploadLog == null) {
                //默认是http上传
                iUploadLog = HttpUploadImpl()
            }
            return UploadConfig(this)
        }
    }

    companion object {
        fun newBuilder(): Builder {
            return Builder()
        }
    }

    init {
        serverUrl = builder.serverHost
        totalFileSize = builder.totalFileSize
        maxFileSize = builder.maxFileSize
        isDebuggable = builder.debuggable
        extraLogDir = builder.extraLogDir
        logDir = builder.logDir
        iUploadLog = builder.iUploadLog
        isInBackgroundUpload = builder.isInBackgroundUpload
        if (iUploadLog is HttpUploadImpl){
            //如果外部设置为http上传方式，则这个链接必须不能为空，否则抛出异常告知
            if (serverUrl == null || serverUrl.isEmpty()) {
                throw NullPointerException("server url must be not null")
            }
        }
    }
}
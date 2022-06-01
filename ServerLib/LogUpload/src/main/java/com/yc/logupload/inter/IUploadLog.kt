package com.yc.logupload.inter

import java.io.File

/**
 * @author yangchong
 * email  : yangchong211@163.com
 * time   : 2022/5/11
 * desc   : 日志上传接口
 * revise :
 */
interface IUploadLog {
    /**
     * 上传文件
     * @param file          file文件
     * @param content           内容
     * @param onUploadListener      监听
     */
    fun uploadFile(file: File?, content: String?, onUploadListener: OnUploadListener?)
}
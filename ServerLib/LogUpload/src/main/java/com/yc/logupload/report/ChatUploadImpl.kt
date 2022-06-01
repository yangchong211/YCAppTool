package com.yc.logupload.report

import com.yc.logupload.inter.BaseUploadLog
import com.yc.logupload.inter.OnUploadListener
import java.io.File

/**
 * @author yangchong
 * email  : yangchong211@163.com
 * time   : 2022/5/11
 * desc   : 将日志发送到聊天工具，钉钉
 * revise :
 */
class ChatUploadImpl : BaseUploadLog() {

    override fun sendReport(file: File?, onUploadListener: OnUploadListener?) {
        //发送到各位老师的丁丁
        //todo 还在思考中
    }
}
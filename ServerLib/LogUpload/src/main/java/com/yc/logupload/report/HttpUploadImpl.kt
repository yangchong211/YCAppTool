package com.yc.logupload.report

import com.yc.logupload.inter.BaseUploadLog
import com.yc.logupload.inter.OnUploadListener
import com.yc.logupload.request.RequestManager
import java.io.File

/**
 * @author yangchong
 * email  : yangchong211@163.com
 * time   : 2022/5/11
 * desc   : 上传网络，上传到咱们服务器
 * revise :
 */
class HttpUploadImpl : BaseUploadLog() {


    override fun sendReport(file: File?, onUploadListener: OnUploadListener?) {
        RequestManager.instance?.uploadFile(file, onUploadListener)
    }

}
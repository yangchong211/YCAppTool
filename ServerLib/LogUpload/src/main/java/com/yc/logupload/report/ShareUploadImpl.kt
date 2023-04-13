package com.yc.logupload.report


import android.widget.Toast
import com.yc.logupload.inter.BaseUploadLog
import com.yc.logupload.inter.OnUploadListener
import com.yc.activitymanager.ActivityManager
import com.yc.appmediastore.FileShareUtils
import java.io.File

/**
 * @author yangchong
 * email  : yangchong211@163.com
 * time   : 2022/5/11
 * desc   : 将日志发送到聊天工具，钉钉
 * revise :
 */
class ShareUploadImpl : BaseUploadLog() {

    override fun sendReport(file: File?, onUploadListener: OnUploadListener?) {
        //获取栈顶activity
        val activity = ActivityManager.getInstance().peek()
        activity?.let {
            activity.runOnUiThread {
                val shareFile = FileShareUtils.shareFile(it, file)
                if (shareFile){
                    onUploadListener?.onSuccess()
                    Toast.makeText(activity,"分享文件成功", Toast.LENGTH_SHORT).show()
                } else {
                    onUploadListener?.onError("share file error")
                    Toast.makeText(activity,"分享文件失败", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}
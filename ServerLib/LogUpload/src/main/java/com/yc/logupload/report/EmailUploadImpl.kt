package com.yc.logupload.report

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.core.content.FileProvider
import com.yc.logupload.config.UploadInitHelper
import com.yc.logupload.inter.BaseUploadLog
import com.yc.logupload.inter.OnUploadListener
import com.yc.activitymanager.ActivityManager
import java.io.File

/**
 * @author yangchong
 * email  : yangchong211@163.com
 * time   : 2022/5/11
 * desc   : 上传到邮件，发到邮箱
 * revise :
 */
class EmailUploadImpl : BaseUploadLog() {

    override fun sendReport(file: File?, onUploadListener: OnUploadListener?) {
        //获取栈顶activity
        val activity = ActivityManager.getInstance().peek()
        if (file != null && activity!=null){
            val addresses = arrayOf(
                "yangchong211@163.com"
                ,"niudong@tanyang.co"
                ,"peiyunfei@tanyang.co"
                ,"liuxiaolong04@tanyang.co",
                "guoyiming@tanyang.co")
            val sendFileEmail = sendFileEmail(file, activity, addresses)
            if (sendFileEmail){
                onUploadListener?.onSuccess()
                Toast.makeText(activity,"发送邮件成功", Toast.LENGTH_SHORT).show()
            } else {
                onUploadListener?.onError("share file error")
                Toast.makeText(activity,"发送邮件失败", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * 发送邮件 附件作为正文
     * @param activity  发起的Activity
     * @param addresses 发送地址
     */
    private fun sendFileEmail(file: File, activity: Activity, addresses: Array<String>) : Boolean{
        val subject = "主题：反馈信息 版本："
        // Any Append Info 一般用于携带设备信息，说明信息等
        val body = "发送本地log日志到邮件"
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:")
        intent.putExtra(Intent.EXTRA_EMAIL, addresses)
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        intent.putExtra(Intent.EXTRA_TEXT, body)
        try {
            val contentUri: Uri
            //判断7.0以上
            contentUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                //第二个参数表示要用哪个ContentProvider，这个唯一值在AndroidManifest.xml里定义了
                //若是没有定义MyFileProvider，可直接使用FileProvider替代
                //这个地方避免要进行初始化，保证上下文不为空
                val authority = UploadInitHelper.mContext?.packageName + ".uploadProvider"
                FileProvider.getUriForFile(UploadInitHelper.mContext!!, authority, file)
            } else {
                Uri.fromFile(file)
            }
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra(Intent.EXTRA_STREAM, contentUri)
            activity.startActivity(intent)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

}
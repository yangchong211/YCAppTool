package com.yc.logupload.inter

import android.content.Context
import com.yc.logupload.config.UploadInitHelper
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author yangchong
 * email  : yangchong211@163.com
 * time   : 2022/5/11
 * desc   : 抽象出日志上传父类
 * revise :
 */
abstract class BaseUploadLog() : IUploadLog {

    /**
     * 发送报告
     * @param file              file文件
     * @param onUploadListener      监听listener
     */
    protected abstract fun sendReport(file: File?, onUploadListener: OnUploadListener?)

    override fun uploadFile(file: File?, content: String?, onUploadListener: OnUploadListener?) {
        if (file == null || !file.exists()) {
            if (onUploadListener != null) {
                return onUploadListener.onError("文件不存在")
            }
        }
        UploadInitHelper.mContext?.let {
            buildTitle(it)
        }
        content?.let {
            buildBody(it)
        }
        sendReport(file, onUploadListener)
    }

    /**
     * 构建标题
     *
     * @param context 上下文
     * @return 返回标题的string文本
     */
    fun buildTitle(context: Context): String {
        return " " + context.getString(context.applicationInfo.labelRes) + " " +
                mTime.format(Calendar.getInstance().time)
    }

    /**
     * 构建正文
     * @param content           字符串
     * @return                  拼接后的内容
     */
    private fun buildBody(content: String): String {
        //获取包命
        val packageName = UploadInitHelper.mContext?.packageName
        return packageName + content
    }

    companion object {
        val mTime = SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss:SS", Locale.getDefault()
        )
    }
}
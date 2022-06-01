package com.yc.logupload.request

import android.widget.Toast
import com.yc.logupload.config.UploadInitHelper
import com.yc.logupload.inter.OnUploadListener
import com.yc.activitymanager.ActivityManager
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.File
import java.io.IOException
import java.net.URLConnection
import java.util.concurrent.TimeUnit

/**
 * @author yangchong
 * email  : yangchong211@163.com
 * time   : 2022/5/11
 * desc   : 网络请求管理类
 * revise :
 */
class RequestManager private constructor() {

    companion object {

        private var sHttpClient: OkHttpClient? = null

        init {
            sHttpClient = OkHttpClient.Builder()
                .connectTimeout(150, TimeUnit.SECONDS)
                .readTimeout(150, TimeUnit.SECONDS)
                .readTimeout(150, TimeUnit.SECONDS)
                .build()
        }

        @Volatile
        private var sInstance: RequestManager? = null
        val instance: RequestManager?
            get() {
                if (sInstance == null) {
                    synchronized(RequestManager::class.java) {
                        if (sInstance == null) {
                            sInstance = RequestManager()
                        }
                    }
                }
                return sInstance
            }

        /**
         * 上传文件，post请求
         * @param url               请求地址
         * @param pathName          文件路径名称
         * @param fileName          文件名
         * @param callback          callback
         */
        fun postUploadFile(url: String?, pathName: String, fileName: String?, callback: Callback?) {
            //判断文件类型
            val judgeType = judgeType(pathName)
            val mediaType = judgeType.toMediaTypeOrNull()
            //创建文件参数
            val builder: MultipartBody.Builder
            if (mediaType != null) {
                builder = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart(
                        mediaType.type, fileName,
                        RequestBody.create(mediaType, File(pathName))
                    )
                //发出请求参数
                val request: Request = Request.Builder()
                    .url(url.toString())
                    .post(builder.build())
                    .build()
                val call = sHttpClient?.newCall(request)
                call?.enqueue(callback!!)
            }
        }

        /**
         * 根据文件路径判断MediaType
         * @param path              文件路径
         * @return                  返回字符串类型的文件类型
         */
        private fun judgeType(path: String): String {
            val fileNameMap = URLConnection.getFileNameMap()
            var contentTypeFor = fileNameMap.getContentTypeFor(path)
            if (contentTypeFor == null) {
                contentTypeFor = "application/octet-stream"
            }
            return contentTypeFor
        }
    }

    /**
     * 上传文件到服务器
     * @param file                  file文件
     * @param onUploadListener      上传回调
     */
    fun uploadFile(file: File?, onUploadListener: OnUploadListener?) {
        val url = UploadInitHelper.config?.serverUrl
        if (url != null && file != null) {
            if (!url.startsWith("http") || !url.startsWith("https")) {
                onUploadListener?.onError("url must be contain http or https")
                return
            }
            val activity = ActivityManager.getInstance().peek()
            postUploadFile(url, file.path, file.name, object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    onUploadListener?.onError("post net error :" + e.message)
                    activity?.runOnUiThread {
                        Toast.makeText(activity,"上传失败",Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    onUploadListener?.onSuccess()
                    activity?.runOnUiThread {
                        Toast.makeText(activity,"上传成功",Toast.LENGTH_SHORT).show()
                    }
                }

            })
        } else {
            onUploadListener?.onError("url or file is null")
        }
    }

}
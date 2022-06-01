package com.yc.logupload.task

import android.app.IntentService
import android.content.Intent
import android.util.Log
import com.yc.logupload.config.UploadInitHelper
import com.yc.logupload.inter.OnUploadListener
import com.yc.logupload.utils.ZipUtils
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author yangchong
 * email  : yangchong211@163.com
 * time   : 2022/5/12
 * desc   : 核心上传文件类。
 *          实践文档：https://wiki.zuoyebang.cc/pages/viewpage.action?pageId=356760839
 * revise :
 * 0.彻底解耦合，方便拓展，将该上传功能抽离出来
 * 1.支持多种方式上传，默认是上传到网络
 * 2.支持外部多配置属性，设置最大上传文件，文件路径，上传链接url，日志查询条件等。后期把逻辑调通后沉淀为库lib
 * 3.支持重试，限制重试次数，思考文件过大则分片上传
 * 4.服务端给客户端发送日志回捞指令，要支持启动后上传，也要支持切换到后台上传
 * 5.关于上传结果回调，告知给外部开发者
 * 6.由于logu日志是按照日期命名文件，因此在不改动该库条件下，先实现上传某日的日志
 * 7.由于查询日志可能有多个文件，则需要找到符合条件日志，然后打包成zip包上传
 * 8.外部设置最大zip包文件大小，超过大小，创建下一个zip包压缩然后上传【这个待定有点复杂】
 * 9.支持上传小时级别日志文件【这个难度极大，首先logu并没有实现该功能】
 */
class UploadService : IntentService(TAG) {

    companion object {
        const val TAG = "UploadService: "
    }

    /**
     * 压缩包名称的一部分：时间戳
     */
    private val ZIP_FOLDER_TIME_FORMAT = SimpleDateFormat(
        "yyyyMMddHHmmssSS", Locale.getDefault()
    )

    override fun onHandleIntent(intent: Intent?) {
        //logu日志记录的路径：
        //路径：/storage/emulated/0/Android/data/com.zuoyebang.iotunion/files/log/app
        //文件如下显示：/storage/emulated/0/Android/data/com.zuoyebang.iotunion/files/log/app/tmlog.tm.2022-05-09_1
        //文件如下显示：/storage/emulated/0/Android/data/com.zuoyebang.iotunion/files/log/app/tmlog.tm.2022-05-08

        //todo 外部开发者需要配置日志文件的路径
        val logFile = UploadInitHelper.config?.logDir
        // 如果Log文件夹都不存在，说明不存在崩溃日志，检查缓存是否超出大小后退出
        // 如果Log文件夹都不存在，说明不存在崩溃日志，检查缓存是否超出大小后退出
        if (!(logFile != null && logFile.exists() && logFile.listFiles()?.isEmpty() != true)) {
            Log.d(TAG, "Log文件夹都不存在，无需上传")
            return
        }
        val fileList = getFileList(logFile)
        if (fileList.isEmpty()) {
            Log.d(TAG, "只是存在log文件夹，但是不存在log日志文件")
            return
        }

        //todo 获取文件大小，如果太大则需要注意


        //从日志列表中找到对应时间的文件
        val dateFileList = getDateFileList(fileList)

        //找到单个文件

        //创建zip文件夹
        //val zipFolder = File(logFile.absolutePath + File.separator + "ZipLog/")
        //Log.d(TAG, "zip folder path : " + zipFolder.path)
        //创建zip文件，直接在日志文件夹中创建zip文件
        val zipFile = File(
            logFile, "ty" + ZIP_FOLDER_TIME_FORMAT.format(
                System.currentTimeMillis()
            ) + ".zip"
        )

        val startTime = System.currentTimeMillis()
        Log.d(TAG, "zip folder path : " + zipFile.path)
        val entrySets: ArrayList<ZipUtils.EntrySet> = ArrayList<ZipUtils.EntrySet>()
        //将符合条件的日志文件打包
        val entrySet = ZipUtils.EntrySet(null, logFile, dateFileList)
        entrySets.add(entrySet)
        //将符合条件的日志文件(是个list列表)写到zip文件中
        val writeToZip = ZipUtils.writeToZip(entrySets, zipFile)
        val endTime = System.currentTimeMillis()
        Log.d(TAG, "find file and write file to zip , total time : ${endTime - startTime}")

        Log.d(TAG, "zip write state : $writeToZip")
        val iUploadLog = UploadInitHelper.config?.iUploadLog
        if (writeToZip) {
            //开始上传
            iUploadLog?.uploadFile(
                zipFile, "上传文件", object : OnUploadListener {
                    override fun onSuccess() {
                        Log.d(TAG, "zip file upload success")
                    }

                    override fun onError(error: String?) {
                        Log.d(TAG, "zip file upload fail : $error")
                    }
                })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "service destroy")
    }

    /**
     * 获取符合条件的日志文件，比如拉5月10日日志，那么搜索到所有10号日志，放到集合中
     *
     * @param dir   file文件
     * @return      list集合
     */
    private fun getDateFileList(list: List<File>): List<File> {
        val fileList: MutableList<File> = ArrayList()
        //目前logu文件名称：tmlog.tm.2022-05-12_15_x
        //那么截取时间字符串，从9截取到19之间的字符串
        //将时间字符串转化为 ： 2022-05-12 格式
        val time = UploadInitHelper.bean?.systemTime ?: System.currentTimeMillis()
        val pattern = "yyyy-MM-dd"
        val date = Date(time)
        val format = SimpleDateFormat(pattern)
        val formatTime = format.format(date)
        Log.d(TAG, "format time is : $formatTime")
        //todo 后期想打造成通用库，这块还要在优化下
        list.forEach { file ->
            if (file.name.length > 19) {
                val fileTime = file.name.substring(9, 19)
                Log.d(TAG, "file substring name : $fileTime")
                if (fileTime == formatTime) {
                    //找到了符合条件的日志文件
                    Log.d(TAG, "find file success , the file name : ${file.name}")
                    fileList.add(file)
                }
            }
        }
        return fileList
    }


    /**
     * 获取某个file对应的子file列表
     *
     * @param dir   file文件
     * @return      list集合
     */
    fun getFileList(dir: File): List<File> {
        val fileList: MutableList<File> = ArrayList()
        if (dir.listFiles() != null) {
            val files = dir.listFiles()
            if (files != null) {
                val length = files.size
                for (i in 0 until length) {
                    val file = files[i]
                    fileList.add(file)
                }
            }
        }
        return fileList
    }
}
package com.yc.store.config

import android.util.Log
import com.tencent.mmkv.MMKV
import com.yc.toolutils.AppToolUtils
import com.yc.toolutils.file.AppFileUtils
import java.io.File


/**
 * @author yangchong
 * email  : yangchong211@163.com
 * time   : 2022/5/11
 * desc   : 初始化配置
 * revise :
 */
object CacheInitHelper {

    private var config : CacheConfig?= null
    private var mmkvPath: String? = null
    private var filePath: String? = null

    @Synchronized
    fun init(config: CacheConfig?) {
        if (config == null){
            this.config = CacheConfig.newBuilder().build()
        }
        CacheInitHelper.config = config
        val logFile = config?.logDir
        if (logFile == null) {
            filePath = AppFileUtils.getCacheFilePath(AppToolUtils.getApp(), "ycCache")
        } else {
            filePath = logFile
        }
        Log.d("CacheHelper : " , "file path : $filePath")
        //路径：/storage/emulated/0/Android/data/你的包名/cache/ycCache
        //初始化腾讯mmkv
        mmkvPath = filePath + File.separator + "mmkv"
        //路径：/storage/emulated/0/Android/data/你的包名/cache/ycCache/mmkv
        Log.d("CacheHelper : " , "mmkv path : $mmkvPath")
        MMKV.initialize(mmkvPath)
    }

    fun getMmkvPath() : String{
        if (mmkvPath == null || mmkvPath?.length == 0){
            throw NullPointerException("please init store lib at fist")
        }
        return mmkvPath as String
    }

    fun getBaseCachePath() : String{
        if (filePath == null || filePath?.length == 0){
            throw NullPointerException("please init store lib at fist")
        }
        return filePath as String
    }
}
package com.yc.logupload.inter

/**
 * @author yangchong
 * email  : yangchong211@163.com
 * time   : 2022/5/11
 * desc   : 上传监听listener
 * revise :
 */
interface OnUploadListener {
    /**
     * 上传成功回调
     */
    fun onSuccess()

    /**
     * 上传失败回调
     * @param error     error信息
     */
    fun onError(error: String?)
}
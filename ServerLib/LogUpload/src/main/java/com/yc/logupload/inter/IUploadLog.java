package com.yc.logupload.inter;

import java.io.File;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time   : 2018/4/17
 *     desc   : 日志上传接口
 *     revise :
 * </pre>
 */
public interface IUploadLog {

    /**
     * 上传文件
     * @param file          file文件
     * @param content           内容
     * @param onUploadListener      监听
     */
    void uploadFile(File file, String content, OnUploadListener onUploadListener);

    /**
     * 上传zip压缩文件
     * @param file          file文件
     * @param content           内容
     * @param onUploadListener      监听
     */
    void uploadZipFile(File file, String content, OnUploadListener onUploadListener);


}

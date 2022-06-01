package com.yc.logupload.report;

import android.content.Context;

import com.yc.logupload.inter.BaseUploadLog;
import com.yc.logupload.inter.OnUploadListener;
import com.yc.logupload.request.RequestManager;

import java.io.File;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time   : 2018/4/17
 *     desc   : 上传网络，上传到咱们服务器
 *     revise :
 * </pre>
 */
public class HttpUploadImpl extends BaseUploadLog {


    @Override
    protected void sendReport(File file, OnUploadListener onUploadListener) {
        RequestManager.getInstance().uploadFile(file,onUploadListener);
    }
}

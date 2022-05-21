package com.yc.logupload.report;

import android.content.Context;

import com.yc.logupload.inter.BaseUploadLog;
import com.yc.logupload.inter.OnUploadListener;

import java.io.File;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time   : 2018/4/17
 *     desc   : 将日志发送到聊天工具，钉钉
 *     revise :
 * </pre>
 */
public class ChatUploadImpl extends BaseUploadLog {

    public ChatUploadImpl(Context context) {
        super(context);
    }

    @Override
    protected void sendReport(File file, OnUploadListener onUploadListener) {

    }
}

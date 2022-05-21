package com.yc.logupload.report;

import android.content.Context;

import com.yc.logupload.config.UploadInitHelper;
import com.yc.logupload.inter.BaseUploadLog;
import com.yc.logupload.inter.OnUploadListener;
import com.yc.logupload.report.email.MailInfo;

import java.io.File;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time   : 2018/4/17
 *     desc   : 上传到邮件，发到邮箱
 *     revise :
 * </pre>
 */
public class EmailUploadImpl extends BaseUploadLog {

    public EmailUploadImpl(Context context) {
        super(context);
    }

    @Override
    protected void sendReport(File file, OnUploadListener onUploadListener) {
        MailInfo sender = new MailInfo()
                .setUser(UploadInitHelper.getEmailConfig().getSendEmail())
                .setPass(UploadInitHelper.getEmailConfig().getSendPassword())
                .setFrom(UploadInitHelper.getEmailConfig().getSendEmail())
                .setTo(UploadInitHelper.getEmailConfig().getReceiveEmail())
                .setHost(UploadInitHelper.getEmailConfig().getHost())
                .setPort(UploadInitHelper.getEmailConfig().getPort())
                .setSubject("杨充")
                .setBody("发送内容");
        sender.init();
        try {
            sender.addAttachment(file.getPath(), file.getName());
            sender.send();
            onUploadListener.onSuccess();
        } catch (Exception e) {
            onUploadListener.onError("Send Email fail！Accout or SMTP verification error ！");
            e.printStackTrace();
        }
    }
}

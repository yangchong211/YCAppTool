package com.yc.logupload.report;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.yc.easyexecutor.BuildConfig;
import com.yc.logupload.inter.BaseUploadLog;
import com.yc.logupload.inter.OnUploadListener;
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

    @Override
    protected void sendReport(File file, OnUploadListener onUploadListener) {
        // 在Android中，调用Email有三种类型的Intent：
        // Intent.ACTION_SENDTO 无附件的发送
        // Intent.ACTION_SEND 带附件的发送
        // Intent.ACTION_SEND_MULTIPLE 带有多附件的发送
    }


    /**
     * 发送邮件 附件作为正文
     * @param activity  发起的Activity
     * @param addresses 发送地址
     */
    private static void sendFileEmail(File file, Activity activity, String[] addresses) {
        String subject = "主题：反馈信息 版本：" + BuildConfig.VERSION_NAME;
        // Any Append Info 一般用于携带设备信息，说明信息等
        String body = "发送本地log日志到邮件";
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, body);
        try {
            Uri contentUri = Uri.fromFile(file);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Intent.EXTRA_STREAM, contentUri);
        } catch (Exception e){
            e.printStackTrace();
        }
        activity.startActivity(intent);
    }

}

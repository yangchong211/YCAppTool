package com.yc.logupload.inter;

import android.content.Context;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time   : 2018/4/17
 *     desc   : 抽象出日志上传父类
 *     revise :
 * </pre>
 */
public abstract class BaseUploadLog implements IUploadLog{

    public Context mContext;
    public final static SimpleDateFormat mTime = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss:SS", Locale.getDefault());

    public BaseUploadLog(Context context) {
        mContext = context;
    }

    /**
     * 发送报告
     * @param file              file文件
     * @param onUploadListener      监听listener
     */
    protected abstract void sendReport(File file, OnUploadListener onUploadListener);

    @Override
    public void uploadFile(final File file, final String content, final OnUploadListener onUploadListener) {
        buildTitle(mContext);
        buildBody(content);
        sendReport(file, onUploadListener);
    }

    @Override
    public void uploadZipFile(File file, String content, OnUploadListener onUploadListener) {
        buildTitle(mContext);
        buildBody(content);
        sendReport(file, onUploadListener);
    }

    /**
     * 构建标题
     *
     * @param context 上下文
     * @return 返回标题的string文本
     */
    public String buildTitle(Context context) {
        return " " + context.getString(context.getApplicationInfo().labelRes) + " " +
                mTime.format(Calendar.getInstance().getTime());
    }

    /**
     * 构建正文
     * @param content           字符串
     * @return                  拼接后的内容
     */
    public String buildBody(String content) {
        return content;
    }


}

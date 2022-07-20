package com.yc.toollib.crash;

import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;

import androidx.annotation.ColorInt;

import com.yc.apprestartlib.RestartAppHelper;
import com.yc.apprestartlib.RestartFactory;
import com.yc.toolutils.AppSizeUtils;
import com.yc.toolutils.file.AppFileUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time  : 2020/7/10
 *     desc  : 工具类
 *     revise:
 * </pre>
 */
public class CrashLibUtils {

    public static final String CRASH_LOGS = "crashLogs";
    public static final String CRASH_PICS = "crashPics";

    /**
     * 目录地址
     * 崩溃日志记录地址
     * SDCard/Android/data/<application package>/cache
     * data/data/<application package>/cache
     */
    public static String getCrashLogPath(Context context) {
        String crashLogs = AppFileUtils.getExternalFilePath(context, CRASH_LOGS);
        return crashLogs;
    }

    /**
     * 目录地址
     * 崩溃截图记录地址
     * SDCard/Android/data/<application package>/cache
     * data/data/<application package>/cache
     */
    public static String getCrashPicPath(Context context) {
        String crashLogs = AppFileUtils.getExternalFilePath(context, CRASH_PICS);
        return crashLogs;
    }


    /**
     * 添加富文本
     *
     * @param spannable
     * @param matchContent    需要匹配的文本
     * @param foregroundColor 改变颜色
     * @param textSize        文字大小
     */
    public static Spannable addNewSpan(Context context, Spannable spannable, String allContent,
                                       String matchContent, @ColorInt int foregroundColor, int textSize) {
        Pattern pattern = Pattern.compile(Pattern.quote(matchContent));
        Matcher matcher = pattern.matcher(allContent);
        while (matcher.find()) {
            int start = matcher.start();
            if (start >= 0) {
                int end = start + matchContent.length();
                if (textSize > 0) {
                    spannable.setSpan(new AbsoluteSizeSpan(AppSizeUtils.sp2px(context, textSize)),
                            start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                }
                spannable.setSpan(new ForegroundColorSpan(foregroundColor),
                        start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            }
        }
        return spannable;
    }

    /**
     * 开启一个新的服务，用来重启本APP【使用handler延迟】
     * 软件重启，不清临时数据。
     * 重启整个APP
     * @param context                       上下文
     * @param Delayed                       延迟多少毫秒
     */
    public static void reStartApp1(Context context, long Delayed){
        RestartAppHelper.restartApp(context, RestartFactory.SERVICE);
    }

    /**
     * 用来重启本APP[使用闹钟，整体重启，临时数据清空（推荐）]
     * 重启整个APP
     * @param context                       上下文
     * @param Delayed                       延迟多少毫秒
     */
    public static void reStartApp2(Context context , long Delayed , Class clazz){
        RestartAppHelper.restartApp(context, RestartFactory.ALARM);
    }

    public static void reStartApp3(Context context) {
        RestartAppHelper.restartApp(context, RestartFactory.LAUNCHER);
    }


    public static void reStartApp4(Context context) {
        RestartAppHelper.restartApp(context, RestartFactory.MANIFEST);
    }

    /**
     * 日志列表页面
     *
     * @param context                       上下文
     */
    public static void startCrashListActivity(Context context) {
        Intent intent = new Intent(context.getApplicationContext(), CrashListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.getApplicationContext().startActivity(intent);
    }

    /**
     * 测试异常类
     *
     * @param context                       上下文
     */
    public static void startCrashTestActivity(Context context) {
        Intent intent = new Intent(context, CrashTestActivity.class);
        context.startActivity(intent);
    }




}

package com.yc.crash.lib;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;

import androidx.annotation.ColorInt;

import com.yc.activitymanager.ActivityManager;
import com.yc.appfilelib.AppFileUtils;
import com.yc.toolmemorylib.AppMemoryUtils;
import com.yc.toolutils.AppDeviceUtils;
import com.yc.toolutils.AppInfoUtils;
import com.yc.toolutils.AppLogUtils;
import com.yc.toolutils.AppProcessUtils;
import com.yc.toolutils.AppSizeUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time  : 2020/7/10
 *     desc  : 异常处理保存文件类
 *     revise:
 * </pre>
 */
public final class CrashHelperUtils {

    /**
     * 错误报告文件的扩展名
     */
    private static final String CRASH_REPORTER_EXTENSION = ".txt";
    /**
     * 额外信息写入
     */
    private static String headContent;
    /**
     * 时间转换
     */
    private static final SimpleDateFormat dataFormat = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss", Locale.CHINA);
    private static String crashTime;
    private static String crashHead;
    private static String crashMem;
    private static String crashThread;
    private static String versionName;
    private static String versionCode;

    public static void setHeadContent(String headContent) {
        CrashHelperUtils.headContent = headContent;
    }

    /**
     * 保存错误信息到文件中
     * 一个崩溃保存到一个txt文本文件中
     * 后期需求：1.过了7天自动清除日志；2.针对文件的大小限制；3.文件写入
     * @param context
     * @param ex
     */
    public static void saveCrashInfoInFile(Context context , Throwable ex){
        long start1 = System.currentTimeMillis();
        initCrashHead(context);
        long end1 = System.currentTimeMillis();
        AppLogUtils.d("save crash file head , use time : " + (end1 - start1));
        //save crash file head , use time : 4
        long start2 = System.currentTimeMillis();
        initPhoneHead(context);
        long end2 = System.currentTimeMillis();
        AppLogUtils.d("save crash file phone , use time : " + (end2 - start2));
        //save crash file phone , use time : 40
        long start3 = System.currentTimeMillis();
        initThreadHead(context);
        long end3 = System.currentTimeMillis();
        AppLogUtils.d("save crash file thread , use time : " + (end3 - start3));
        //save crash file thread , use time : 1
        long start4 = System.currentTimeMillis();
        dumpExceptionToFile(context,ex);
        long end4 = System.currentTimeMillis();
        AppLogUtils.d("save crash file throwable , use time : " + (end4 - start4));
        //save crash file throwable , use time : 2
        //saveCrashInfoToFile(context,ex);
    }

    private static void initCrashHead(Context context) {
        //崩溃时间
        crashTime = dataFormat.format(new Date(System.currentTimeMillis()));
        //版本信息
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);
            if (pi != null) {
                versionName = pi.versionName;
                versionCode = String.valueOf(pi.versionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        //组合Android相关信息
        StringBuilder sb = new StringBuilder();
        sb.append("\n软件App的Id:").append(context.getPackageName());
        sb.append("\n崩溃的时间:").append(crashTime);
        sb.append("\n是否root:").append(AppDeviceUtils.isDeviceRooted());
        sb.append("\n系统硬件商:").append(AppDeviceUtils.getManufacturer());
        sb.append("\n设备的品牌:").append(AppDeviceUtils.getBrand());
        sb.append("\n手机的型号:").append(AppDeviceUtils.getModel());
        sb.append("\n设备版本号:").append(AppDeviceUtils.getId());
        sb.append("\nCPU的类型:").append(AppDeviceUtils.getCpuType());
        sb.append("\n系统的版本:").append(AppDeviceUtils.getSDKVersionName());
        sb.append("\n系统版本值:").append(AppDeviceUtils.getSDKVersionCode());
        sb.append("\n当前的版本:").append(versionName).append("—").append(versionCode);
        sb.append("\n\n");
        crashHead = sb.toString();
    }


    private static void initPhoneHead(Context context) {
        StringBuilder sb = new StringBuilder();
        sb.append("手机内存分析:");
        AppMemoryUtils.getMemoryInfo(context, (pkgName, pid, ramMemoryInfo, pssInfo, dalvikHeapMem) -> {
            sb.append("\n是否低内存状态运行:  ").append(ramMemoryInfo.isLowMemory);
            sb.append("\n可用RAM:  ").append(ramMemoryInfo.availMem);
            sb.append("\n手机总RAM:  ").append(ramMemoryInfo.totalMem);
            sb.append("\n内存占用满的阀值:  ").append(ramMemoryInfo.lowMemThreshold);
            sb.append("\n总的PSS内存使用量:  ").append(pssInfo.totalPss).append("kb");
            sb.append("\ndalvik堆的比例设置大小:  ").append(pssInfo.dalvikPss);
            sb.append("\n本机堆的比例设置大小:  ").append(pssInfo.nativePss);
            sb.append("\n比例设置大小为其他所有:  ").append(pssInfo.otherPss);
            sb.append("\n空闲内存:  ").append(dalvikHeapMem.freeMem);
            sb.append("\n最大内存:  ").append(dalvikHeapMem.maxMem);
            sb.append("\n已用内存:  ").append(dalvikHeapMem.allocated);
            sb.append("\n\n");
            crashMem = sb.toString();
        });
    }

    private static void initThreadHead(Context context) {
        StringBuilder sb = new StringBuilder();
        sb.append("该App信息:");
        String currentProcessName = AppProcessUtils.getCurrentProcessName(context);
        if (currentProcessName!=null){
            sb.append("\nApp进程名称:").append(currentProcessName);
        }
        sb.append("\n进程号:").append(android.os.Process.myPid());
        sb.append("\n当前线程号:").append(android.os.Process.myTid());
        sb.append("\n当前调用该进程的用户号:").append(android.os.Process.myUid());
        sb.append("\n当前线程ID:").append(Thread.currentThread().getId());
        sb.append("\n当前线程名称:").append(Thread.currentThread().getName());
        sb.append("\n主线程ID:").append(context.getMainLooper().getThread().getId());
        sb.append("\n主线程名称:").append(context.getMainLooper().getThread().getName());
        sb.append("\n主线程优先级:").append(context.getMainLooper().getThread().getPriority());
        Activity activity = ActivityManager.getInstance().peek();
        if (activity!=null){
            sb.append("\n当前Activity名称:").append(activity.getComponentName().getClassName());
            sb.append("\n当前Activity所在栈的ID:").append(activity.getTaskId());
        }
        sb.append("\n\n");
        crashThread = sb.toString();
    }

    private static void dumpExceptionToFile(Context context , Throwable ex) {
        File file = null;
        PrintWriter pw = null;
        try {
            //Log保存路径
            // SDCard/Android/data/<application package>/cache
            // data/data/<application package>/cache
            File dir = new File(CrashHelperUtils.getCrashLogPath(context));
            if (!dir.exists()) {
                boolean ok = dir.mkdirs();
                if (!ok) {
                    return;
                }
            }
            //Log文件的名字
            String fileName = "V" + versionName + "_" + crashTime + CRASH_REPORTER_EXTENSION;
            file = new File(dir, fileName);
            if (!file.exists()) {
                boolean createNewFileOk = file.createNewFile();
                if (!createNewFileOk) {
                    return;
                }
            }
            AppLogUtils.i(CrashHandler.TAG, "保存异常的log文件名称：" + fileName);
            AppLogUtils.i(CrashHandler.TAG, "保存异常的log文件file：" + file);
            //开始写日志
            pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            //判断有没有额外信息需要写入
            if (!TextUtils.isEmpty(headContent)) {
                pw.println(headContent);
            }
            //print(ex);
            //写入设备信息
            pw.println(crashHead);
            pw.println(crashMem);
            pw.println(crashThread);
            //导出异常的调用栈信息
            ex.printStackTrace(pw);
            //异常信息
            Throwable cause = ex.getCause();
            while (cause != null) {
                cause.printStackTrace(pw);
                cause = cause.getCause();
            }
            //重新命名文件
            String string = ex.toString();
            String splitEx;
            if (string.contains(":")){
                splitEx = ex.toString().split(":")[0];
            } else {
                splitEx = "java.lang.Exception";
            }
            String newName = "V" + versionName + "_" + crashTime + "_" + splitEx + CRASH_REPORTER_EXTENSION;
            File newFile = new File(dir, newName);
            //重命名文件
            AppFileUtils.renameFile(file.getPath(), newFile.getPath());
            //路径：/storage/emulated/0/Android/data/包名/cache/crashLogs
            //file       V1.0_2020-09-02_09:05:01.txt
            //newFile    V1.0_2020-09-02_09:05:01_java.lang.NullPointerException.txt
            AppLogUtils.i(CrashHandler.TAG, "保存异常的log文件路径：" + file.getPath() + "----新路径---"+newFile.getPath());
        } catch (Exception e) {
            AppLogUtils.e(CrashHandler.TAG, "保存日志失败：" + e.toString());
        } finally {
            if (pw != null) {
                pw.close();
            }
        }
    }


    /**
     * 保存错误信息到文件中
     * @param ex
     * @return
     */
    @Deprecated
    public static void saveCrashInfoToFile(Context context ,Throwable ex) {
        Writer info = new StringWriter();
        PrintWriter printWriter = new PrintWriter(info);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        String result = info.toString();
        printWriter.close();
        StringBuilder sb = new StringBuilder();
        @SuppressLint("SimpleDateFormat")
        String now = dataFormat.format(new Date());
        //崩溃时间
        sb.append("TIME:").append(now);
        //程序信息
        sb.append("\nAPPLICATION_ID:").append(context.getPackageName());
        sb.append("\nVERSION_CODE:").append(AppInfoUtils.getAppVersionCode());
        sb.append("\nVERSION_NAME:").append(AppInfoUtils.getAppVersionName());
        //设备信息
        sb.append("\nMODEL:").append(Build.MODEL);
        sb.append("\nRELEASE:").append(Build.VERSION.RELEASE);
        sb.append("\nSDK:").append(Build.VERSION.SDK_INT);
        sb.append("\nEXCEPTION:").append(ex.getLocalizedMessage());
        sb.append("\nSTACK_TRACE:").append(result);
        String crashFilePath = CrashHelperUtils.getCrashLogPath(context);
        if (crashFilePath.length()>0){
            try {
                AppLogUtils.w(CrashHandler.TAG, "handleException---输出路径-----"+crashFilePath);
                FileWriter writer = new FileWriter( crashFilePath+ now + CRASH_REPORTER_EXTENSION);
                writer.write(sb.toString());
                writer.flush();
                writer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


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

}

package com.yc.toollib.crash;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;

import com.yc.toollib.BuildConfig;
import com.yc.toollib.R;
import com.yc.toollib.tool.NetDeviceUtils;
import com.yc.toollib.tool.ToolAppManager;
import com.yc.toollib.tool.ToolFileUtils;
import com.yc.toollib.tool.ToolLogUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time  : 2020/7/10
 *     desc  : 异常处理保存文件类
 *     revise:
 * </pre>
 */
public final class CrashFileUtils {


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
    private static final SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
    private static String crashTime;
    private static String crashHead;
    private static String crashMem;
    private static String crashThread;
    private static String versionName;
    private static String versionCode;

    public static void setHeadContent(String headContent) {
        CrashFileUtils.headContent = headContent;
    }

    /**
     * 保存错误信息到文件中
     * 一个崩溃保存到一个txt文本文件中
     * 后期需求：1.过了7天自动清除日志；2.针对文件的大小限制；3.文件写入
     * @param context
     * @param ex
     */
    public static void saveCrashInfoInFile(Context context , Throwable ex){
        initCrashHead(context);
        initPhoneHead(context);
        initThreadHead(context,ex);
        dumpExceptionToFile(context,ex);
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
        sb.append("\n软件App的Id:").append(BuildConfig.APPLICATION_ID);
        sb.append("\n是否是DEBUG版本:").append(BuildConfig.BUILD_TYPE);
        sb.append("\n崩溃的时间:").append(crashTime);
        sb.append("\n是否root:").append(NetDeviceUtils.isDeviceRooted());
        sb.append("\n系统硬件商:").append(NetDeviceUtils.getManufacturer());
        sb.append("\n设备的品牌:").append(NetDeviceUtils.getBrand());
        sb.append("\n手机的型号:").append(NetDeviceUtils.getModel());
        sb.append("\n设备版本号:").append(NetDeviceUtils.getId());
        sb.append("\nCPU的类型:").append(NetDeviceUtils.getCpuType());
        sb.append("\n系统的版本:").append(NetDeviceUtils.getSDKVersionName());
        sb.append("\n系统版本值:").append(NetDeviceUtils.getSDKVersionCode());
        sb.append("\n当前的版本:").append(versionName).append("—").append(versionCode);
        sb.append("\n\n");
        crashHead = sb.toString();
    }


    private static void initPhoneHead(Context context) {
        StringBuilder sb = new StringBuilder();
        sb.append("手机内存分析:");
        final int pid = MemoryUtils.getCurrentPid();
        MemoryUtils.PssInfo pssInfo = MemoryUtils.getAppPssInfo(context, pid);
        sb.append("\ndalvik堆大小:").append(MemoryUtils.getFormatSize(pssInfo.dalvikPss));
        sb.append("\n手机堆大小:").append(MemoryUtils.getFormatSize(pssInfo.nativePss));
        sb.append("\nPSS内存使用量:").append(MemoryUtils.getFormatSize(pssInfo.totalPss));
        sb.append("\n其他比例大小:").append(MemoryUtils.getFormatSize(pssInfo.otherPss));

        final MemoryUtils.DalvikHeapMem dalvikHeapMem = MemoryUtils.getAppDalvikHeapMem();
        sb.append("\n已用内存:").append(MemoryUtils.getFormatSize(dalvikHeapMem.allocated));
        sb.append("\n最大内存:").append(MemoryUtils.getFormatSize(dalvikHeapMem.maxMem));
        sb.append("\n空闲内存:").append(MemoryUtils.getFormatSize(dalvikHeapMem.freeMem));

        long appTotalDalvikHeapSize = MemoryUtils.getAppTotalDalvikHeapSize(context);
        sb.append("\n应用占用内存:").append(MemoryUtils.getFormatSize(appTotalDalvikHeapSize));
        sb.append("\n\n");
        crashMem = sb.toString();
    }


    private static void initThreadHead(Context context, Throwable ex) {
        StringBuilder sb = new StringBuilder();
        sb.append("该App信息:");
        String currentProcessName = ProcessUtils.getCurrentProcessName(context);
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
        Activity activity = ToolAppManager.getAppManager().currentActivity();
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
            File dir = new File(ToolFileUtils.getCrashLogPath(context));
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
            ToolLogUtils.i(CrashHandler.TAG, "保存异常的log文件名称：" + fileName);
            ToolLogUtils.i(CrashHandler.TAG, "保存异常的log文件file：" + file);
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
            ToolFileUtils.renameFile(file.getPath(), newFile.getPath());
            //路径：/storage/emulated/0/Android/data/包名/cache/crashLogs
            //file       V1.0_2020-09-02_09:05:01.txt
            //newFile    V1.0_2020-09-02_09:05:01_java.lang.NullPointerException.txt
            ToolLogUtils.i(CrashHandler.TAG, "保存异常的log文件路径：" + file.getPath() + "----新路径---"+newFile.getPath());
        } catch (Exception e) {
            ToolLogUtils.e(CrashHandler.TAG, "保存日志失败：" + e.toString());
        } finally {
            if (pw != null) {
                pw.close();
            }
        }
    }

    public static void print(Throwable thr) {
        StackTraceElement[] stackTraces = thr.getStackTrace();
        for (StackTraceElement stackTrace : stackTraces) {
            String clazzName = stackTrace.getClassName();
            String fileName = stackTrace.getFileName();
            int lineNumber = stackTrace.getLineNumber();
            String methodName = stackTrace.getMethodName();
            ToolLogUtils.i("printThrowable------"+clazzName+"----"
                    +fileName+"------"+lineNumber+"----"+methodName);
        }
    }

    private static StackTraceElement parseThrowable(Throwable ex , Context context) {
        if (ex == null || ex.getStackTrace() == null || ex.getStackTrace().length == 0) {
            return null;
        }
        if (context == null){
            return null;
        }
        StackTraceElement[] stackTrace = ex.getStackTrace();
        StackTraceElement element;
        String packageName = context.getPackageName();
        for (StackTraceElement ele : stackTrace) {
            if (ele.getClassName().contains(packageName)) {
                element = ele;
                String clazzName = element.getClassName();
                String fileName = element.getFileName();
                int lineNumber = element.getLineNumber();
                String methodName = element.getMethodName();
                ToolLogUtils.i("printThrowable----1--"+clazzName+"----"
                        +fileName+"------"+lineNumber+"----"+methodName);
                return element;
            }
        }
        element = stackTrace[0];
        String clazzName = element.getClassName();
        String fileName = element.getFileName();
        int lineNumber = element.getLineNumber();
        String methodName = element.getMethodName();
        ToolLogUtils.i("printThrowable----2--"+clazzName+"----"
                +fileName+"------"+lineNumber+"----"+methodName);
        return element;
    }

    /**
     * 获取错误报告文件路径
     *
     * @param ctx
     * @return
     */
    @Deprecated
    public static String[] getCrashReportFiles(Context ctx) {
        File filesDir = new File(getCrashFilePath(ctx));
        String[] fileNames = filesDir.list();
        int length = fileNames.length;
        String[] filePaths = new String[length];
        for (int i = 0; i < length; i++) {
            filePaths[i] = getCrashFilePath(ctx) + fileNames[i];
        }
        return filePaths;
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
        sb.append("TIME:").append(now);//崩溃时间
        //程序信息
        sb.append("\nAPPLICATION_ID:").append(BuildConfig.APPLICATION_ID);//软件APPLICATION_ID
        sb.append("\nVERSION_CODE:").append(BuildConfig.VERSION_CODE);//软件版本号
        sb.append("\nVERSION_NAME:").append(BuildConfig.VERSION_NAME);//VERSION_NAME
        sb.append("\nBUILD_TYPE:").append(BuildConfig.BUILD_TYPE);//是否是DEBUG版本
        //设备信息
        sb.append("\nMODEL:").append(Build.MODEL);
        sb.append("\nRELEASE:").append(Build.VERSION.RELEASE);
        sb.append("\nSDK:").append(Build.VERSION.SDK_INT);
        sb.append("\nEXCEPTION:").append(ex.getLocalizedMessage());
        sb.append("\nSTACK_TRACE:").append(result);
        String crashFilePath = getCrashFilePath(context);
        if (crashFilePath!=null && crashFilePath.length()>0){
            try {
                ToolLogUtils.w(CrashHandler.TAG, "handleException---输出路径-----"+crashFilePath);
                FileWriter writer = new FileWriter( crashFilePath+ now + CRASH_REPORTER_EXTENSION);
                writer.write(sb.toString());
                writer.flush();
                writer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取文件夹路径
     *
     * @param context
     * @return
     */
    @Deprecated
    private static String getCrashFilePath(Context context) {
        String path = null;
        try {
            path = Environment.getExternalStorageDirectory().getCanonicalPath()
                    + "/" + context.getResources().getString(R.string.app_name) + "/Crash/";
            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }

}

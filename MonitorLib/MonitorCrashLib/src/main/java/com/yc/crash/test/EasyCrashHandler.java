package com.yc.crash.test;

import android.content.Context;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * 最简洁版本的崩溃监听库
 */
public class EasyCrashHandler implements Thread.UncaughtExceptionHandler {

    private static final String TAG = EasyCrashHandler.class.getSimpleName();
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private Context mContent;
    private static final String CRASH_REPORTER_EXTENSION = ".txt";
    private static final SimpleDateFormat dataFormat = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss", Locale.CHINA);

    private EasyCrashHandler() {
    }

    public static EasyCrashHandler getInstance() {
        return Inner.INSTANCE;
    }

    public void init(Context context) {
        mContent = context;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(@NonNull Thread thread, @NonNull Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            // 如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            SystemClock.sleep(3000);
            // 退出程序
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex 异常
     * @return true:如果处理了该异常信息; 否则返回false.
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        return saveCrashInfoFile(ex);
    }

    /**
     * 保存错误信息到文件中
     *
     * @param ex 异常
     * @return 返回文件名称, 便于将文件传送到服务器
     */
    private boolean saveCrashInfoFile(Throwable ex) {
        Log.v(TAG, "saveCrashInfoFile");
        StringBuilder sb = new StringBuilder();
        boolean success;
        PrintWriter printWriter = null;
        try {
            String date = dataFormat.format(new java.util.Date());
            sb.append("\r\n").append(date).append("\n");
            Writer writer = new StringWriter();
            printWriter = new PrintWriter(writer);
            ex.printStackTrace(printWriter);
            printWriter.flush();
            String result = writer.toString();
            sb.append(result);
            Log.e(TAG, "", ex);
            success = true;
        } catch (Exception e) {
            Log.e(TAG, "an error occured while writing file...", e);
            sb.append("an error occured while writing file...\r\n");
            success = false;
        } finally {
            if (printWriter != null) {
                try {
                    printWriter.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            File filesDir = mContent.getExternalFilesDir(null);
            String path = filesDir.getPath() + File.separator + "AppCrash";
            File file = new File(path);
            if (!file.exists()) {
                //创建一个File对象所对应的目录，成功返回true，否则false。且File对象必须为路径而不是文件。
                //创建多级目录，创建路径中所有不存在的目录
                //noinspection ResultOfMethodCallIgnored
                file.mkdirs();
            }
            //Log文件的名字
            String fileName = "V" + "_" + System.currentTimeMillis() + CRASH_REPORTER_EXTENSION;
            File crashFile = new File(file, fileName);
            if (!crashFile.exists()) {
                try {
                    //noinspection ResultOfMethodCallIgnored
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            FileWriter writer = null;
            try {
                writer = new FileWriter(crashFile);
                writer.write(sb.toString());
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return success;
    }

    private static class Inner {
        public static final EasyCrashHandler INSTANCE = new EasyCrashHandler();
    }
}
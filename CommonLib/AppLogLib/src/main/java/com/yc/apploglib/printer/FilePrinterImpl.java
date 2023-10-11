package com.yc.apploglib.printer;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Process;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * <pre>
 *     @author yangchong
 *     GitHub : https://github.com/yangchong211/YCCommonLib
 *     email : yangchong211@163.com
 *     time  : 2018/11/9
 *     desc  : Printer写入文件
 *     revise:
 * </pre>
 */
public class FilePrinterImpl extends AbsPrinter {
    /**
     * 10MB
     */
    private static final long MAX_LOG_FILE = 1024 * 1024 * 10;
    /**
     * 名字
     */
    private static final String PRINTER_NAME = "FilePrinter";
    /**
     * file文件
     */
    private final File mLogDir;
    /**
     * header格式
     */
    private SimpleDateFormat sInfoHeaderFormat = null;
    /**
     * file文件格式
     */
    private SimpleDateFormat mFileNameFormat = null;

    /**
     * File Thread handler.
     */
    private final DelayInitializer<Handler> mHandler = new DelayInitializer<>(() -> {
        HandlerThread thread = new HandlerThread("CommonFileLogThread");
        thread.start();
        return new Handler(thread.getLooper());
    });

    private PrintWriter mWriter;

    public FilePrinterImpl(File logDir) {
        mLogDir = logDir;
    }

    private SimpleDateFormat getInfoHeaderFormat() {
        if (sInfoHeaderFormat == null) {
            sInfoHeaderFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());
        }
        return sInfoHeaderFormat;
    }

    private SimpleDateFormat getFileNameFormat() {
        if (mFileNameFormat == null) {
            mFileNameFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        }
        return mFileNameFormat;
    }

    private PrintWriter getWriter() {
        if (mWriter == null) {
            do {
                File logFile = getLogFile();
                if (logFile == null) {
                    break;
                }
                Log.d("AppLog" , "logFile path" + logFile.getPath());
                if (logFile.length() > MAX_LOG_FILE) {
                    if (!logFile.delete()) {
                        break;
                    }
                }
                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(logFile, true);
                    OutputStreamWriter writer = new OutputStreamWriter(fileOutputStream, "UTF-8");
                    //Writer writer = new StringWriter();
                    mWriter = new PrintWriter(writer);
                } catch (Throwable ignored) {
                }
            } while (false);
        }
        return mWriter;
    }

    @Override
    public String name() {
        return PRINTER_NAME;
    }

    @Override
    public void println(final int level, final String tag, final String format, final Throwable tr) {
        final int threadId = Process.myTid();
        postRunnable(() -> logToFileInner(level, threadId, tag, format, tr));
    }

    protected void postRunnable(Runnable runnable) {
        Handler handler = mHandler.get();
        if (handler != null) {
            handler.post(runnable);
        }
    }

    private void logToFileInner(int level, int threadId, String tag, String msg, Throwable tr) {
        try {
            PrintWriter writer = getWriter();
            if (writer == null) {
                return;
            }

            String log = String.format("%s %s-%s %s/%s %s/%s %s",
                    getInfoHeaderFormat().format(new Date()), Process.myPid(), threadId,
                    Process.myUid(), getProcessName(), levelToStr(level), tag, msg);
            writer.println(log);
            if (tr != null) {
                tr.printStackTrace(writer);
                writer.println();
            }
            writer.flush();
        } catch (Throwable ignored) {
            // 6.0 在没有申请权限之前是无法访问外部存储的。
            //ignored.printStackTrace();
        }
    }

    private static String levelToStr(int level) {
        switch (level) {
            case android.util.Log.VERBOSE:
                return "V";
            case android.util.Log.DEBUG:
                return "D";
            case android.util.Log.INFO:
                return "I";
            case android.util.Log.WARN:
                return "W";
            case android.util.Log.ERROR:
                return "E";
            case android.util.Log.ASSERT:
                return "A";
            default:
                return "UNKNOWN";
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();

        internalClose();
    }

    void internalClose() {
        if (mWriter != null) {
            mWriter.close();
            mWriter = null;
        }
    }

    File getLogFile() {
        File file = new File(mLogDir, String.format("Log_%s_%s.log",
                getFileNameFormat().format(new Date()), Process.myPid()));
        File dir = file.getParentFile();
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                if (!dir.exists()) {
                    return null;
                }
            }
        }
        return file;
    }

    private static String getProcessName() {
        return "?";
    }

}


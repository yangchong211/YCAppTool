package com.yc.apploglib;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Process;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FilePrinter extends Printer {
    //8MB
    private static final long MAX_LOG_FILE = 1024 * 1024 * 8;
    static final String PRINTER_NAME = "FilePrinter";

    /**
     * base dir to save logs.
     * */
    private final File mLogDir;


    /**
     * header for each line.
     * */
    private SimpleDateFormat sInfoHeaderFormat = null;


    /**
     * file name format.
     * */
    private SimpleDateFormat mFileNameFormat = null;

    /**
     * File Thread handler.
     * */
    private final DelayInitializer<Handler> mHandler = new DelayInitializer<>(() -> {
        HandlerThread thread = new HandlerThread("CommonFileLogThread");
        thread.start();
        return new Handler(thread.getLooper());
    });

    private PrintWriter mWriter;

    public FilePrinter(File logDir) {
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

                if (logFile.length() > MAX_LOG_FILE) {
                    if (!logFile.delete()) {
                        break;
                    }
                }
                try {
                    mWriter = new PrintWriter(
                            new OutputStreamWriter(new FileOutputStream(logFile, true), "UTF-8"));
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
        } catch (Throwable ignored) {
            // 6.0 在没有申请权限之前是无法访问外部存储的。
//            e.printStackTrace();
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


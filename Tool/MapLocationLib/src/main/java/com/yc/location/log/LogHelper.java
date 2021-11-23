package com.yc.location.log;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.yc.location.constant.Constants;
import com.yc.location.utils.LocationUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by liuchuang on 2015/9/21.
 */
public class LogHelper {

    private static String packageName = "";
    private static String phonenum = "";

    //处理收集数据相关操作的handler，工作在异步线程中。
    private static AtomicReference<Handler> mDataWorkHandler = new AtomicReference<>();
    //收集数据相关操作的工作线程。
    private static AtomicReference<HandlerThread> mDataWorkThread = new AtomicReference<>();
    private static Context mContext;
    private static File mBamaiPath = null;


    public static void stopWorkThread() {
        final Handler handler = mDataWorkHandler.get();
        if (handler != null) {
            mDataWorkHandler.set(null);
            mDataWorkThread.set(null);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    handler.removeCallbacksAndMessages(null);
                    Looper.myLooper().quit();
                }
            });
        }
    }

    public static void init(Context c) {
        if(c == null) {
            return;
        }

        // 避免static成员被回收的情况
        if (mDataWorkHandler == null) {
            mDataWorkHandler = new AtomicReference<>();
        }

        if (mDataWorkThread == null) {
            mDataWorkThread = new AtomicReference<>();
        }

        if (mDataWorkHandler.get() != null && mDataWorkThread.get() != null) {
            LogHelper.logBamai("DataWorkThread has started already");

            return;
        }

        mContext = c.getApplicationContext();
        packageName = c.getApplicationContext().getPackageName();

        mDataWorkThread.set(new HandlerThread("DataWorkThread"));
        mDataWorkThread.get().start();
        mDataWorkHandler.set(new Handler(mDataWorkThread.get().getLooper()));
        LogHelper.logBamai("init  DataWorkThread");
    }

    public static void setPhonenum(String phone) {
        phonenum = phone;
    }

    /**
     * 强制写日志，如果外部设置日志路径，则写入设置路径文件中；否则，用公共日志组件写到组件默认路径下。
     * @param log
     */
    public static void forceLogBamai(final String log) {
        if (TextUtils.isEmpty(log)) {
            return;
        }
        if (mBamaiPath == null) {
            logBamaiWithoutPath(log);
        } else {
            logBamai(log);
        }

    }
    /**
     * 按设置路径写日志
     * @param log 日志内容
     */
    public static void logBamai(final String log) {
        if (Constants.isDebug) {
            Log.i("UTILSLOG", log);
        }
        if (mBamaiPath == null) {
            return;
        }
        //lcc：apollo开关控制是否写日志。
//        if (!Apollo.getToggle(Const.APOLLO_ALLOW_BAMAI_LOG).allow()) {
//            return;
//        }

        Handler handler = mDataWorkHandler.get();
        if (handler != null) {
            final long threadid = Thread.currentThread().getId();

            handler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        phonenum = LocationUtils.getPhonenum(mContext);

                        if (TextUtils.isEmpty(phonenum)
                                || TextUtils.isEmpty(log)) {
                            return;
                        }

                        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
                            return;
//                        String pathName = Environment.getExternalStorageDirectory().toString();
//                        pathName += "/.WL/";
                        if (!mBamaiPath.exists() || !mBamaiPath.isDirectory()) {
                            if (!mBamaiPath.mkdirs()) {
                               return;
                            }
                        }
                        String bamaiPath = mBamaiPath.getAbsolutePath();
                        String separator = File.separator;
                        if (bamaiPath.endsWith(File.separator)) {
                            separator = "";
                        } else {
                            separator = File.separator;
                        }

                        String fileName = bamaiPath + separator + phonenum + "_locsdk_" + Constants.simpleDateFormatExt2.format(System.currentTimeMillis()) + ".txt";

                        String timeStr = Constants.simpleDateFormatExt1.format(System.currentTimeMillis()) + "-" + threadid + " ";
                        FileOutputStream fos = null;
                        try {
                            fos = new FileOutputStream(new File(fileName), true);
                            OutputStreamWriter osw = new OutputStreamWriter(fos, "utf-8");
                            osw.write(timeStr + log);
                            osw.write("\n");
                            osw.flush();
                            osw.close();
                        } catch (FileNotFoundException e1) {
                            e1.printStackTrace();
                        } catch (UnsupportedEncodingException e2) {
                            e2.printStackTrace();
                        } catch (Throwable e2) {
                            e2.printStackTrace();
                        } finally {
                            if (fos != null) {
                                try {
                                    fos.close();
                                } catch (Throwable e) {
                                }
                            }
                        }
                    } catch (Throwable e) {

                    }
                }
            });
        }

        return;
    }

    public static void write(String log) {
        LogHelper.write(log, false);
    }

    public static void write(final String log, boolean force) {
        LogHelper.write(log, force, "locsdk");
    }
    public static void write(final String log, boolean force, String tag) {

        final long threadid = Thread.currentThread().getId();

        force = false;
        if (log == null || log.length() <= 0) return;
        if (!force && !Constants.isDebug) return;

        if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) return;
        if(packageName == null || packageName.length() <= 0) return;

        Log.i("locsdk", "["+threadid+"] "+log);

        Handler handler = mDataWorkHandler.get();
        if (handler != null) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    String pathName = Environment.getExternalStorageDirectory().toString();
                    pathName += "/Android/data/" + packageName + "/";
                    String fileName = Constants.logFileName;

                    BufferedWriter outWriter = null;
                    String timeStr = "[" + Constants.simpleDateFormat.format(System.currentTimeMillis()) + "]["+System.currentTimeMillis()+"]["+threadid+"] ";
                    try {
                        File path = new File(pathName);
                        File file = new File(pathName + fileName);
                        if (!path.exists()) path.mkdirs();
                        if (!file.exists()) file.createNewFile();

                        outWriter = new BufferedWriter(new OutputStreamWriter(
                                new FileOutputStream(file, true), Charset.forName("utf8")
                        ));
                        outWriter.write(timeStr + log + Constants.logLRCF);
                    } catch (IOException e) {
                    } finally {
                        if (outWriter == null) return;
                        try {
                            outWriter.close();
                        } catch (IOException e) {}
                    }
                }
            });
        }
    }

    public static void writeException(final Throwable ex) {
        if(!Constants.isDebug || ex == null) return;
        if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) return;
        if(packageName == null || packageName.length() <= 0) return;

        if (Looper.myLooper() == Looper.getMainLooper()) {
            Handler handler = mDataWorkHandler.get();
            if (handler != null) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        writeExceptionInternal(ex);
                    }
                });
            }
        } else {
            writeExceptionInternal(ex);
        }
        return;
    }

    private static void writeExceptionInternal(Throwable ex) {
        String log = getExceptionString(ex);
        Log.w("locsdk", log);

        String pathName = Environment.getExternalStorageDirectory().toString();
        pathName += "/Android/data/" + packageName + "/";
        String fileName = Constants.logFileName;

        BufferedWriter outWriter = null;
        String timeStr = "[" + Constants.simpleDateFormat.format(System.currentTimeMillis()) + "] ";
        try {
            File path = new File(pathName);
            File file = new File(pathName + fileName);
            if (!path.exists()) path.mkdirs();
            if (!file.exists()) file.createNewFile();

            outWriter = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(file, true)
            ));
            outWriter.write(timeStr + log + Constants.logLRCF);

        } catch (IOException e) {
        } finally {
            if (outWriter == null) return;
            try {
                outWriter.close();
            } catch (IOException e) {}
        }
        return;
    }

    private static String getExceptionString(Throwable ex) {
        if(ex == null) return "";

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while(cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        return writer.toString();
    }

    private static void logBamaiWithoutPath(String log) {
        if (Constants.isDebug) {
            Log.i("%s", log);
        }
    }

    /****************以下是为调试输出log的接口，当Const.debug为false时全部屏蔽**********************/
    public static void i(String tag, String log) {
        if (Constants.isDebug) {
            Log.i(tag, log);
        }
    }
    public static void d(String tag, String log) {
        if (Constants.isDebug) {
            Log.d(tag, log);
        }
    }
    public static void e(String tag, String log) {
        if (Constants.isDebug) {
            Log.e(tag, log);
        }
    }

    /**
     * 设置写日志的路径
     */
    public static void setBamaiLogPath(File path) {
        if (null != path) {
            mBamaiPath = path;
//            try {
//                if (!mBamaiPath.exists() || !mBamaiPath.isDirectory()) {
//                    if (!mBamaiPath.mkdirs()) {
//                        mBamaiPath = null;
//                    }
//                }
//            } catch (Exception e) {
//                mBamaiPath = null;
//            }
        }
    }
}

package com.yc.logservice;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;

import com.yc.appfilelib.SdCardUtils;
import com.yc.eventuploadlib.ExceptionReporter;
import com.yc.logclient.bean.AppLogBean;
import com.yc.toolutils.AppLogUtils;
import com.yc.logclient.ILogService;
import com.yc.logclient.constant.LogConstant;
import com.yc.logclient.bean.ThrowableBean;
import com.yc.logclient.LogManager;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 职责描述: 对外提供LogServer跨进程服务调用
 * 记住本类不能使用logu这个类，否则会出现无限循环
 */
public class LogService extends Service {

    private static final String TAG = LogService.class.getSimpleName();
    private final HashMap<String, IWriteLogger> mLogMap = new HashMap<>();
    private final SparseArray<LogCleaner> mLogCleaners = new SparseArray<>();
    private final int EVENT_WRITE_LOG = 100; //单个日志消息
    private final int EVENT_WRITE_LOGS = 101;//批量日志消息
    private Handler mHandler;
    private final SparseArray<String> mDirName = new SparseArray<>();

    public static final String ACTION_TYPE = LogManager.ACTION_TYPE;
    public static final int ACTION_TYPE_WRITE_LOG = LogManager.ACTION_TYPE_WRITE_LOG;
    public static final int ACTION_TYPE_WRITE_LOGS = LogManager.ACTION_TYPE_WRITE_LOGS;
    public static final int ACTION_TYPE_COLLECT_LOGCAT = LogManager.ACTION_TYPE_COLLECTLOGCAT;
    public static final int ACTION_TYPE_SAVEFILE_WITHPATH = LogManager.ACTION_TYPE_SAVEFILE_WITHPATH;

    //参数名
    public static final String KEY_BEAN = LogManager.KEY_BEAN;
    public static final String KEY_BEANS = LogManager.KEY_BEANS;
    public static final String KEY_LOG_SAVE_PATH = LogManager.KEY_LOG_SAVE_PATH;
    public static final String KEY_LOGCAT_CLEAR = LogManager.KEY_LOGCAT_CLEAR;
    public static final String KEY_LOGCAT_COLLECT_DELAYTIME = LogManager.KEY_LOGCAT_COLLECT_DELAYTIME;
    public static final String KEY_LOG_CONTENT = LogManager.KEY_LOG_CONTENT;


    //日志文件保存路径
    public static String LOG_FILE_DIR = LogManager.LOG_FILE_DIR;
    //默认的日志文件名
    public static String DEFAULT_LOG_FILE_NAME = LogManager.DEFAULT_LOG_FILE_NAME;

    //Binder对象
    ILogService.Stub mBinder;

    public LogService() {
        mBinder = new ILogService.Stub() {
            @Override
            public void log(AppLogBean bean) throws RemoteException {
                Message message = new Message();
                message.what = EVENT_WRITE_LOG;
                message.obj = bean;
                if (bean != null) {
                    bean.setMsg("[B ] " + bean.getMsg());
                }
                mHandler.sendMessage(message);
            }

            @Override
            public void logs(List<AppLogBean> beans) throws RemoteException {
                Message message = new Message();
                message.what = EVENT_WRITE_LOGS;
                message.obj = beans;
                if (beans != null) {
                    for (AppLogBean bean : beans) {
                        if (bean != null) {
                            bean.setMsg("[BS] " + bean.getMsg());
                        }
                    }
                }
                mHandler.sendMessage(message);
            }

            @Override
            public void collectlogcat(String savePath, long delayTime, boolean clearLogcat) throws RemoteException {
                AppLogUtils.d(TAG, "collectlogcat:" + savePath + ",delayTime:" + delayTime + ",clearLogcat:" + clearLogcat);
                doCollectLogCat(savePath, delayTime, clearLogcat);
            }

            @Override
            public void saveLogWithPath(String log, String path) throws RemoteException {
                doSaveLogWithPath(log, path);
                AppLogUtils.d(TAG,"log file path saveLogWithPath : " + path);
            }

            @Override
            public void setDefaultLogSaveFolder(String path) {
                setLogSaveFolder(path);
                AppLogUtils.d(TAG,"log file path setDefaultLogSaveFolder : " + path);
                ///storage/emulated/0/Android/data/com.zuoyebang.iotunion/files/log/
            }
        };
    }


    // ------- 继承的方法 -------//
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AppLogUtils.d(TAG, "onCreate");
        initLogConfig();
        HandlerThread handlerThread = new HandlerThread("LogService");
        handlerThread.start();
        mHandler = new Handler(handlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    //单个日志消息
                    case EVENT_WRITE_LOG:
                        AppLogBean bean = (AppLogBean) msg.obj;
                        doWriteLog(bean);
                        break;
                    //批量日志消息
                    case EVENT_WRITE_LOGS:
                        List<AppLogBean> beans = (List<AppLogBean>) msg.obj;
                        doWriteLogs(beans);
                        break;
                    default:
                        break;
                }
            }
        };

        //主要是logService是一个独立的进程，跨进程异常捕获，仅仅只是设置一个默认全局异常处理器是不够的
        //因此这里采用拿到当前独立进程的当前线程，然后捕获该线程异常
        //弊端：仅仅只是捕获当前线程异常，比如logService如果是开启其他线程操作出现崩溃，则捕获不了
        //获取当前线程，然后捕获当前线程的异常
        Thread thread = Thread.currentThread();
        thread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(@NotNull Thread t, @NotNull Throwable e) {
                Log.d("LogService", t.getName() + " uncaught exception " + e.getMessage());
                //上报
                ExceptionReporter.report(thread.getName(), e);
            }
        });
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            int type = intent.getIntExtra(ACTION_TYPE, 0);
            if (type == ACTION_TYPE_WRITE_LOG) { //写单个日志
                AppLogBean bean = intent.getParcelableExtra(KEY_BEAN);
                if (bean != null) {
                    bean.setMsg("[C ] " + bean.getMsg());
                }
                doWriteLog(bean);
            } else if (type == ACTION_TYPE_WRITE_LOGS) { //写日志数组
                List<AppLogBean> beans = intent.getParcelableArrayListExtra(KEY_BEANS);
                if (beans != null) {
                    for (AppLogBean bean : beans) {
                        if (bean != null) {
                            bean.setMsg("[CS] " + bean.getMsg());
                        }
                    }
                }
                doWriteLogs(beans);
            } else if (type == ACTION_TYPE_COLLECT_LOGCAT) { //截取Logcat日志
                String savePath = intent.getStringExtra(KEY_LOG_SAVE_PATH);
                long delaytime = intent.getLongExtra(KEY_LOGCAT_COLLECT_DELAYTIME, 2000);
                boolean doClearLogcat = intent.getBooleanExtra(KEY_LOGCAT_CLEAR, false);
                doCollectLogCat(savePath, delaytime, doClearLogcat);
            } else if (type == ACTION_TYPE_SAVEFILE_WITHPATH) { //将字符串保存到文件
                String savePath = intent.getStringExtra(KEY_LOG_SAVE_PATH);
                String logContent = intent.getStringExtra(KEY_LOG_CONTENT);
                doSaveLogWithPath(logContent, savePath);
            }
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AppLogUtils.d(TAG, "onDestroy");
    }


    // ------- 扩展的方法 ------ //

    private void initLogConfig() {
        mDirName.put(LogConstant.LogType.app.value(), LogConstant.LogType.app.name());
        mDirName.put(LogConstant.LogType.crash.value(), LogConstant.LogType.crash.name());
        mDirName.put(LogConstant.LogType.statistics.value(), LogConstant.LogType.statistics.name());
        mDirName.put(LogConstant.LogType.anr.value(), LogConstant.LogType.anr.name());
    }


    public void doCollectLogCat(final String savePath, long delayTime, final boolean clearLogcat) {
        AppLogUtils.d(TAG, "doCollectLogCat savePath:" + savePath + ",delaytime:" + delayTime + ",clearLogcat:" + clearLogcat);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                File file = new File(savePath);
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                    AppLogUtils.d(TAG, "doCollectLogCat parent not exist,create it~");
                }
                String cmd = "logcat -d > " + savePath;
                AppLogUtils.d(TAG, "doCollectLogCat " + cmd);
                LogToolUtils.INSTANCE.execCommand(cmd, false);
                if (clearLogcat) {
                    String cmd_clear = "logcat -c";
                    LogToolUtils.INSTANCE.execCommand(cmd_clear, false);
                    AppLogUtils.d(TAG, "doCollectLogCat clear logcat  :" + cmd_clear);
                }
            }
        }, delayTime);

    }


    /**
     * 保存字符串到日志文件
     *
     * @param log
     * @param path
     */
    public void doSaveLogWithPath(final String log, final String path) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "doSaveLogWithPath:" + path + "\n content:" + log);
                LogToolUtils.INSTANCE.saveLogWithPath(log, path);
            }
        });
    }

    /**
     * 将日志Bean数组写到文件
     * 注意:writeLogs中的参数beans 必须是同一类型的LogBean (App、crash、anr、statistics)
     *
     * @param beans
     */
    private void doWriteLogs(List<AppLogBean> beans) {

        if (!SdCardUtils.isExistSDCard()) {
            AppLogUtils.e(TAG, "sdcard unmounted");
            return;
        }

        if (beans != null && beans.size() > 0) {
            String key = null;
            int type = LogConstant.LogType.app.value();
            for (AppLogBean b : beans) {
                if (b != null) {
                    key = b.getKey();
                    type = b.getType();
                    //ALog.d("test_1","writeLogs:type:"+type+",key:"+key);
                }
            }

            key = beans.get(0).getKey();
            type = beans.get(0).getType();

            //ALog.d(TAG, "writeLogs type:" + type+",key:"+key);

            IWriteLogger logger = getLogger(type);
            if (logger != null) {
                ArrayList<String> msgList = new ArrayList<>();
                for (AppLogBean b : beans) {
                    if (b != null) {
                        msgList.add(b.getMsg());
                    }
                }
                logger.append(msgList);
            }
            deleteByOverdue();
        }
    }

    /**
     * 将单个日志Bean写到文件
     * 需要区分处理: app、crash、anr、statistics
     *
     * @param bean
     */
    private void doWriteLog(AppLogBean bean) {
        if (!SdCardUtils.isExistSDCard()) {
            Log.e(TAG, "sdcard unmounted");
            return;
        }
        if (bean == null) {
            Log.e(TAG, "bean is null");
            return;
        }
        int type = bean.getType();
        int leve = bean.getLeve();
        IWriteLogger logger = getLogger(type);
        if (type == LogConstant.LogType.crash.value()) {
            //crash
            if (logger != null) {
                logger.log(leve, bean.getMsg());
                logger.flush();
            }

            ThrowableBean throwableBean = bean.getThrowableBean();
            if (throwableBean != null) {
                Throwable throwable = null;
                try {
                    throwable = (Throwable) Class.forName(throwableBean.throwableClsName).newInstance();
                    Field field = Throwable.class.getDeclaredField("detailMessage");
                    field.setAccessible(true);
                    field.set(throwable, throwableBean.msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (throwable == null) {
                    throwable = new Throwable(throwableBean.msg);
                }
                if (throwableBean.stackTraceElements != null) {
                    throwable.setStackTrace(throwableBean.stackTraceElements);
                }
                AppLogUtils.e("LogService", "crash throwable detail"+throwable);
            }
        } else if (type == LogConstant.LogType.anr.value()) {
            //Anr
            if (logger != null) {
                logger.log(leve, bean.getMsg());
                logger.flush();
            }
            AppLogUtils.d("LogService", "write anr:" + bean.getMsg());
        } else if (type == LogConstant.LogType.statistics.value()) {
            //打点数据
            if (logger != null) {
                logger.log(leve, bean.getMsg());
                logger.flush();
            }
        } else {
            //正常的app业务日志
            String logStr = bean.getMsg();
            if (logger != null) {
                logger.log(leve, logStr);
            }
        }
        deleteByOverdue();
    }

    /**
     * 构建Logger对象
     *
     * @param type
     * @return
     */
    public IWriteLogger getLogger(int type) {
        IWriteLogger logger = getMap(type + "");
        LogConstant.LogType logType = LogConstant.LogType.convert2LogType(type);
        if (logger == null) {
            String dir = mDirName.get(type);
            if (TextUtils.isEmpty(dir)) {
                dir = "app";
            }
            String logfileName = LOG_FILE_DIR + dir + File.separator + DEFAULT_LOG_FILE_NAME;

            DailyAppenderImpl rollingFileAppender = null;
            try {
                AppLogUtils.d(TAG, "init new Logger logfileName:" + logfileName);
                ///storage/emulated/0/Android/data/com.zuoyebang.iotunion/files/log/app/tmlog.tm
                rollingFileAppender = new DailyAppenderImpl(logfileName, "'.'yyyy-MM-dd", this);
                rollingFileAppender.setBufferedIO(false);
            } catch (IOException var5) {
                AppLogUtils.e(TAG, "getLogger IOException");
                return null;
            }
            putMap(type + "", rollingFileAppender);
            logger = rollingFileAppender;
        }
        return logger;
    }

    synchronized public void putMap(String key, IWriteLogger logger) {
        mLogMap.put(key, logger);
    }

    synchronized public IWriteLogger getMap(String key) {
        return mLogMap.get(key);
    }

    synchronized public void clearMap() {
        mLogMap.clear();
    }

    public LogCleaner getLogCleaner(LogConstant.LogType type) {
        LogCleaner logCleaner = mLogCleaners.get(type.value());
        if (logCleaner == null) {
            String folder = LOG_FILE_DIR + type.name();
            logCleaner = new LogCleaner(folder);
            logCleaner.setKeepFreeStore(type.getKeepFreeStore());
            logCleaner.setKeepDayNumber(type.getKeepDayNumber());
            logCleaner.setMaxFolderSize(type.getMaxFolderSize());
            mLogCleaners.put(type.value(), logCleaner);
        }
        return logCleaner;
    }


    /**
     * 小于指定空间或大于指定天数删除最旧的文件
     *
     */
    private synchronized void deleteByOverdue() {
        LogConstant.LogType[] types = LogConstant.LogType.values();
        for (LogConstant.LogType theType : types) {
            LogCleaner logCleaner = getLogCleaner(theType);
            if (logCleaner != null) {
                logCleaner.tryClearLog();
            }
        }
    }


    // -----------  getter and setter -----------//
    public void setLogSaveFolder(String logPath) {
        if (logPath != null && !LOG_FILE_DIR.equals(logPath)) {
            AppLogUtils.d(TAG, "resetLogPath orignal:" + LOG_FILE_DIR + ",logPath:" + logPath);
            LOG_FILE_DIR = logPath;
            resetLogPath();
        }
    }

    /**
     * 重置日志保存文件夹
     */
    public void resetLogPath() {
        AppLogUtils.d(TAG, "resetLogPath");
        clearMap();
        mLogCleaners.clear();
    }


    public void killSelf() {
        AppLogUtils.d(TAG, "===== KillSelf ==== ");
        android.os.Process.killProcess(android.os.Process.myPid());
    }


}

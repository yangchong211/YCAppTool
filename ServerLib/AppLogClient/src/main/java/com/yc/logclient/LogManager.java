package com.yc.logclient;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Environment;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import com.yc.logclient.client.IPCLargeProcessor;
import com.yc.logclient.client.LogCache;
import com.yc.logclient.client.LogQueueThread;
import com.yc.logclient.inter.ILogSend;
import com.yc.logclient.state.ConnectingState;
import com.yc.toolutils.AppLogUtils;
import com.yc.logclient.bean.AppLogBean;

import java.io.File;
import java.util.ArrayList;

/**
 * 职责描述: 将日志对象 从client端发送到server端
 */
public class LogManager implements ILogSend, IBinder.DeathRecipient {

    private static final String TAG = LogManager.class.getSimpleName();
    public static String LOG_FILE_DIR = Environment.getExternalStorageDirectory() + File.separator + "log" + File.separator;
    public static final String DEFAULT_LOG_FILE_NAME = "tmlog.tm";
    //参数 key值
    public static final String KEY_BEAN = "logbean";
    public static final String KEY_BEANS = "logbeans";
    public static final String ACTION_TYPE = "action_type";

    //操作类型
    public static final int ACTION_TYPE_WRITE_LOG = 101; //发送单个日志操作
    public static final int ACTION_TYPE_WRITE_LOGS = 102; //发送日志数组操作
    public static final int ACTION_TYPE_COLLECTLOGCAT = 103; //截取logcat日志操作
    public static final int ACTION_TYPE_SAVEFILE_WITHPATH = 104;//单独保存字符串到文件操作

    //collectLogcat(String savepath,long delaytime,boolean clearlogcat); 参数
    public static final String KEY_LOG_SAVE_PATH = "logsavepath";
    public static final String KEY_LOGCAT_CLEAR = "clearlogcat";
    public static final String KEY_LOGCAT_COLLECT_DELAYTIME = "collectdelaytime";

    //saveLogWithPath(String log, String path) 参数
    public static final String KEY_LOG_CONTENT = "logcontent";

    /**
     * MsgBean/String 占用内存设定的比例,用于调整打印字符串的单条字符串的大小。
     */
    public static float ratio = 1.2F;
    private volatile static LogManager instance;
    /**
     * 上下文
     */
    private final Context mContext;
    /**
     * LogCache缓存队列的长度
     * 当LogService 是内部进程，启动晚于主进程，为了保证不丢日志
     * 解决方法：1。调大buffer，等待主进程起来  2。等待binder连接上再传输日志
     */
    private final int MAX_LOG_BEANS = 200;

    /**
     * 检测到关机广播时,flush日志,防止日志丢失
     */
    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Intent.ACTION_SHUTDOWN.equals(intent.getAction())) {
                //监听关机广播
                LogUtils.d("" + Intent.ACTION_SHUTDOWN);
                flush();
            }
        }
    };


    /**
     * Log队列
     */
    private LogQueueThread mLogQueue = null;
    /**
     * LogServier连接状态
     */
    private ConnectingState mConnectState = ConnectingState.NotConnected;

    private LogManager(Context context) {
        Log.i(TAG, "LogManager init");
        mContext = context;
        bindService();
        startLogQueue();
        registerReceiver();
    }

    public static LogManager getInstance(Context context) {
        synchronized (LogManager.class) {
            if (instance == null) {
                instance = new LogManager(context);
            }
        }
        return instance;
    }

    /**
     * 开启日志队列线程
     */
    private void startLogQueue() {
        mLogQueue = new LogQueueThread(this, new LogCache(MAX_LOG_BEANS));
        if (enableTooLargeTransactDevide) {
            //是否启动 大日志 自动分隔策略
            IPCLargeProcessor tooLargeProcessor = new IPCLargeProcessor(MAX_LOG_BEANS, ratio);
            mLogQueue.setLargeProcessor(tooLargeProcessor);
        }
        mLogQueue.start();
        Log.i(TAG, "start log queue , new handler thread");
    }


    /**
     * 发送日志到LogServer
     *
     * @param beans
     */
    @Override
    public void sendLogToService(ArrayList<AppLogBean> beans) {
        doSend2Service(beans);
    }

    /**
     * aidl接口对象，这个是编译器生成的ILogService类(生成的是.java文件)
     */
    private ILogService mLogServiceStub = null;

    /**
     * 创建 serviceConnection 对象，用于建立绑定连接
     */
    private final ServiceConnection serviceConnection = new ServiceConnection() {
        /**
         * 绑定服务连接
         * @param name          name
         * @param service       binder
         */
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //绑定成功后，将服务端返回的Binder对象转换成AIDL接口所属的类型
            //首先当bindService之后，客户端会得到一个Binder引用
            //在拿到Binder引用后，调用IxxService.Stub.asInterface(IBinder obj) 即可得到一个IxxService实例
            mLogServiceStub = ILogService.Stub.asInterface(service);
            //设置连接状态
            mConnectState = ConnectingState.Connected;
            Log.i(TAG, "bind service , start connect");
            //使用Stub.asInterface接口获取服务器的Binder，根据需要调用服务提供的接口方法
            try {
                //通过aidl将路径传递给服务端
                //客户端调用远程服务的方法，被调用的方法运行在服务端的 Binder 线程池中，同时客户端的线程会被挂起
                //如果服务端方法执行比较耗时，就会导致客户端线程长时间阻塞，导致 ANR 。
                //客户端的 onServiceConnected 和 onServiceDisconnected 方法都在 UI 线程中。
                mLogServiceStub.setDefaultLogSaveFolder(LOG_FILE_DIR);
                //如果该活页夹消失，请为收件人注册一个通知。如果绑定器对象意外消失(通常是因为它的宿主进程被杀死)
                service.linkToDeath(LogManager.this, 0);
                Log.i(TAG, "bind service , connect link to death");
            } catch (RemoteException e) {
                e.printStackTrace();
                Log.i(TAG, "bind service , connect exception" + e.getLocalizedMessage());
            }
            Log.i(TAG, "bind service , connect end");
        }

        /**
         * 断开连接
         * @param name              name
         */
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mLogServiceStub = null;
            mConnectState = ConnectingState.NotConnected;
            Log.i(TAG, "bind service , connect disconnected");
        }
    };

    public String getShortPackageName() {
        return LogClientUtils.getShortPackageName(mContext);
    }

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_SHUTDOWN);
        mContext.registerReceiver(mBroadcastReceiver, intentFilter);
        Log.i(TAG, "register receiver");
    }


    /**
     * 绑定LogService
     */
    private void bindService() {
        mConnectState = ConnectingState.Connecting;
        try {
            Log.i(TAG, "bind service");
            Intent intent = LogClientUtils.getLogServiceIntent(mContext);
            //绑定服务,传入intent和ServiceConnection对象
            mContext.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
            Log.i(TAG, "bind service end");
        } catch (Exception e) {
            mConnectState = ConnectingState.NotConnected;
            Log.i(TAG, "bind service exception " + e.getMessage());
        }
    }

    /**
     * 死亡代理监听回调
     */
    @Override
    public void binderDied() {
        if (mLogServiceStub != null) {
            //解除死亡代理
            mLogServiceStub.asBinder().unlinkToDeath(this, 0);
            mLogServiceStub = null;
        }
        //未连接
        mConnectState = ConnectingState.NotConnected;
    }


    public static void setLogSaveFolder(String logFolder) {
        LOG_FILE_DIR = logFolder;
        AppLogUtils.d(TAG, "setLogSaveFolder:" + logFolder);
    }

    /**
     * 解除绑定
     */
    public void release() {
        mContext.unbindService(serviceConnection);
        Log.i(TAG, "bind service release ");
    }

    public void logu(int type, int leve, String msg) {
        AppLogBean bean = new AppLogBean(type, leve, getShortPackageName(), "", msg);
        mLogQueue.put(bean);
    }


    //是否启动 大日志 自动分隔策略
    public static boolean enableTooLargeTransactDevide = true;


    private void go2Connect() {
        if (mConnectState == ConnectingState.NotConnected) {
            bindService();
        }
    }

    private void doSend2Service(ArrayList<AppLogBean> beans) {
        Log.i(TAG, "service send bean");
        if (beans == null || beans.size() <= 0) {
            return;
        }
        try {
            Log.i(TAG, "service send bean size" + beans.size() + " is bind : " + isLogBound());
            //LogService是否已经绑定
            if (isLogBound()) {
                log2Binder(beans);
            } else {
                go2Connect();
                logToServices(beans);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "[log]", e);
        }
    }


    /**
     * LogService建立连接后,通过Binder 传递日志实体
     *
     * @param beans
     */
    public void log2Binder(ArrayList<AppLogBean> beans) {
        try {
            if (beans.size() > 1) {
                mLogServiceStub.logs(beans);
            } else {
                mLogServiceStub.log(beans.get(0));
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * LogService未连接时,通过startService的方式传递数据
     *
     * @param beans
     */
    public void logToServices(ArrayList<AppLogBean> beans) {
        Log.i(TAG, "service log to start");
        if (beans == null || beans.size() <= 0) {
            return;
        }
        try {
            Log.i(TAG, "service log send start");
            Intent intent = LogClientUtils.getLogServiceIntent(mContext);
            if (beans.size() > 1) {
                intent.putExtra(ACTION_TYPE, ACTION_TYPE_WRITE_LOGS);
                intent.putParcelableArrayListExtra(KEY_BEANS, beans);
            } else {
                intent.putExtra(ACTION_TYPE, ACTION_TYPE_WRITE_LOG);
                intent.putExtra(KEY_BEAN, beans.get(0));
            }
            mContext.startService(intent);
            Log.i(TAG, "service log send start success");
        } catch (Exception e) {
            Log.i(TAG, "start service exception " + e.getMessage());
        }
    }


    /**
     * 清空缓存队列
     */
    public void flush() {
        try {
            mLogQueue.flushCache();
        } catch (Exception e) {
            Log.d(TAG, "flushLog exception:" + e.getLocalizedMessage());
        }
    }

    /**
     * LogService是否已经绑定
     *
     * @return
     */
    private boolean isLogBound() {
        return mConnectState == ConnectingState.Connected && mLogServiceStub != null;
    }

    /**
     * 收集Logcat日志
     *
     * @param savepath
     * @param delaytime
     * @param clearlogcat
     */
    public void collectLogcat(String savepath, long delaytime, boolean clearlogcat) {
        try {
            if (isLogBound()) {
                Log.i(TAG, "mStub.collectlogcat");
                mLogServiceStub.collectlogcat(savepath, delaytime, clearlogcat);
            } else {
                Log.i(TAG, "collectLogcatByStartService");
                try {
                    Intent intent = LogClientUtils.getLogServiceIntent(mContext);
                    intent.putExtra(KEY_LOG_SAVE_PATH, savepath);
                    intent.putExtra(KEY_LOGCAT_CLEAR, delaytime);
                    intent.putExtra(KEY_LOGCAT_COLLECT_DELAYTIME, clearlogcat);
                    mContext.startService(intent);
                } catch (Exception e) {
                    Log.i(TAG, e.getMessage());
                }
                go2Connect();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "[collectLogcat]", e);
        }
    }

    /**
     * 保存字符串到独立文件(非追加的方式操作文件)
     *
     * @param log
     * @param path
     */
    public void saveLogWithPath(String log, String path) {
        try {
            if (isLogBound()) {
                Log.i(TAG, "mStub.collectlogcat");
                mLogServiceStub.saveLogWithPath(log, path);
            } else {
                Log.i(TAG, "collectLogcatByStartService");
                try {
                    Intent intent = LogClientUtils.getLogServiceIntent(mContext);
                    intent.putExtra(KEY_LOG_SAVE_PATH, path);
                    intent.putExtra(KEY_LOG_CONTENT, log);
                    mContext.startService(intent);

                } catch (Exception e) {
                    Log.i(TAG, e.getMessage());
                }

                go2Connect();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "[collectLogcat]", e);
        }
    }


}

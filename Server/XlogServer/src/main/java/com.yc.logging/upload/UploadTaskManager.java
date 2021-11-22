package com.yc.logging.upload;

import android.annotation.SuppressLint;
import android.content.*;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.RestrictTo;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Pair;
import com.yc.logging.LoggerConfig;
import com.yc.logging.LoggerContext;
import com.yc.logging.LoggerFactory;
import com.yc.logging.upload.persist.*;
import com.yc.logging.util.*;

import java.io.File;
import java.util.*;
import java.util.concurrent.TimeUnit;


@RestrictTo(RestrictTo.Scope.LIBRARY)
public class UploadTaskManager extends BroadcastReceiver {

    public static final String ACTION_UPLOAD_LOG = "bamai_upload_log";
    public static final String ACTION_GET_TREE = "bamai_get_tree";

    @SuppressLint("StaticFieldLeak")
    private static UploadTaskManager sInstance;
    //private Context mContext;
    private boolean mInitial = false;
    private Handler mScheduledHandler = new Handler(Looper.getMainLooper());
    private ConnectivityManager mConnectivityManager;
    private final Object mLock = new Object();
    private boolean mDatabaseLoaded;
    private Context mContext;
    private Set<String> mPendingTaskQueue = Collections.synchronizedSet(new HashSet<String>());

    private SharedPreferences mPreferences;

    public static UploadTaskManager getInstance() {
        if (sInstance == null) {
            sInstance = new UploadTaskManager();
        }
        return sInstance;
    }

    private UploadTaskManager() {
    }

    public synchronized void init(final Context context) {
        if (mInitial) return;
        mInitial = true;
        mContext = context;
        LOG("init");
        mPreferences = mContext.getSharedPreferences("logging_record", Context.MODE_PRIVATE);
        mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (mConnectivityManager == null) {
            return;
        }
        onInit(context);
    }

    private void onInit(final Context context) {
        synchronized (mLock) {
            if (mDatabaseLoaded) {
                return;
            }
        }
        registerReceiver(context);
        ArchTaskExecutor.getInstance()
                .executeOnDiskIO(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            LOG("onInit");
                            UploadTaskDatabase.initDatabase(context);
                            loopQueryTask();
                        } catch (Throwable e) {
                            Debug.logOrThrow("init err", e);
                            ReportUtils.reportProgramError("logging_init_err", e);
                        } finally {
                            synchronized (mLock) {
                                mDatabaseLoaded = true;
                                mLock.notifyAll();
                            }
                        }
                    }
                });
    }

    private void registerReceiver(Context context) {
        IntentFilter filter1 = new IntentFilter();
        filter1.addAction(ACTION_GET_TREE);
        filter1.addAction(ACTION_UPLOAD_LOG);

        if (Debug.isDebuggable()) {
            context.registerReceiver(this, filter1);
            LocalBroadcastManager.getInstance(context).registerReceiver(this, filter1);
        } else {
            LocalBroadcastManager.getInstance(context).registerReceiver(this, filter1);
        }

        IntentFilter filter2 = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        context.registerReceiver(this, filter2);
    }

    @Override
    public void onReceive(final Context context, final Intent intent) {
        if (intent == null) return;
        ArchTaskExecutor.getInstance()
                .executeOnDiskIO(new Runnable() {
                    @Override
                    public void run() {
                        awaitDatabaseLoadedLocked();
                        try {
                            performReceive(intent);
                        } catch (Exception e) {
                            Debug.logOrThrow("perform receive err", e);
                            ReportUtils.reportProgramError("logging_receiver_err", e);
                        }
                    }
                });
    }

    private void awaitDatabaseLoadedLocked() {
        synchronized (mLock) {
            while (!mDatabaseLoaded) {
                try {
                    LOG("awaitDatabaseLoadedLocked");
                    mLock.wait();
                } catch (InterruptedException ignore) {
                }
            }
        }
    }

    private void performReceive(Intent intent) {
        String action = intent.getAction();
        String extra = intent.getStringExtra("im_message_extra");
        LOG("receive msg, action: " + action + " extra: " + extra);
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
            startUploadService();
        } else if (ACTION_UPLOAD_LOG.equals(action)) {
            ReportUtils.reportReceivePush(action, extra);
            receiveNewTask(extra);
        } else if (ACTION_GET_TREE.equals(action)) {
            ReportUtils.reportReceivePush(action, extra);
            uploadFileTree(extra);
        }
    }

    private void uploadFileTree(String extra) {
        GetTreeTask getTreeTask = GetTreeTask.parseGetTreeTask(extra);
        //Upload file tree
        if (getTreeTask != null) {
            long timeDif = System.currentTimeMillis() - getTreeTask.getPushTimestamp();
            LOG("getTree timeDif = " + timeDif);
            if (timeDif < GetTreeTask.MAX_MESSAGE_TIME_DELTA) {
                uploadFileTree(getTreeTask, mContext);
            } else {
                LOG("timeout");
            }
        }
    }

    private void startUploadService() {
        //mScheduledHandler.removeCallbacksAndMessages(null);
        if (isNetworkConnected()) {
            UploadService.getInstance().start(mContext);
        }
    }

    void delayStart() {
        if (!isNetworkConnected()) {
            return;
        }
        mScheduledHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startUploadService();
            }
        }, 5 * 1000 * 60);//delay 5 min retry
    }


    void queryTask() {
        try {
            LoggerConfig config = LoggerFactory.getConfig();
            Supplier<String> supplier = config.getPhoneNumSupplier();
            if (supplier != null && !TextUtils.isEmpty(supplier.get())) {
                Pair<TaskRecord, String> result = RequestManager.queryTask(supplier.get());
                if (result.first != null) {
                    ReportUtils.reportReceivePush("query_task_result", result.second);
                    createNewTask(result.first);
                }
            }
        } catch (Exception e) {
            Debug.logOrThrow("query Task err", e);
            ReportUtils.reportProgramError("logging_query_task_err", e);
        }

    }

    void loopQueryTask() {
        final Runnable queryTask = new Runnable() {
            @Override
            public void run() {
                LoggerConfig config = LoggerFactory.getConfig();
                Supplier<String> supplier = config.getPhoneNumSupplier();
                if (supplier != null && !TextUtils.isEmpty(supplier.get())) {
                    queryTask();
                }
            }
        };

        final long delayMillis = TimeUnit.MINUTES.toMillis(30);//loop 30 min retry
        Runnable loopTask = new Runnable() {
            @Override
            public void run() {
                ArchTaskExecutor.getInstance().executeOnDiskIO(queryTask);
                mScheduledHandler.postDelayed(this, delayMillis);
            }
        };
        mScheduledHandler.postDelayed(loopTask, 15_000);
    }

    private void uploadFileTree(final GetTreeTask getTreeTask, final Context context) {
        if (getTreeTask == null || !getTreeTask.hasTaskId()) {
            return;
        }

        String networkType = LoggerUtils.getNetworkType(context);
        File appRootDir = context.getFilesDir().getParentFile();

        FileTree cacheRoot = new FileTree();

        File[] subFiles = appRootDir.listFiles();
        for (File subFile : subFiles) {
            cacheRoot.addSubTree(new FileEntry(subFile));
        }
        RequestManager.uploadFileTree(getTreeTask.getTaskId(), networkType, cacheRoot);
    }

    private void receiveNewTask(String args) {
        TaskRecord record = TaskRecord.fromJson(args);
        createNewTask(record);
    }

    private void createNewTask(TaskRecord record) {
        Debug.i("create task record: " + record);

        if (record == null || !record.isValid()) {
            String err = "参数异常:" + (record != null ? record.getRawData() : null);
            ReportUtils.reportUploadTaskResult(false, "", "", err);
            if (record != null && !record.isValid()) {
                RequestManager.uploadTaskStatus(record.getTaskId(), 102, err);
            }
            return;
        }

        String taskId = record.getTaskId();

        if (mPreferences.getBoolean(taskId, false)) {
            Debug.i("task already done: " + record);
            return;
        }

        try {
            TaskRecordDao taskRecordDao = UploadTaskDatabase.getDatabase().getTaskRecordDao();
            TaskRecord rcd = taskRecordDao.getRecordsByTaskId(taskId);
            if (mPendingTaskQueue.contains(taskId) || rcd != null) {
                Debug.i("task already exists: " + record);
                return;
            }
            mPendingTaskQueue.add(taskId);

            RequestManager.uploadTaskStatus(taskId, 3, "已收到日志上传任务");

            List<File> mainLogFiles = LoggerUtils.collectLogFilesWithRange(
                    LoggerContext.getDefault().getMainLogPathDir(),
                    record.getBuffers(),
                    record.getStartTime(),
                    record.getEndTime());
            Debug.i("Task " + record.getTaskId() + " collect main log dir files: " + mainLogFiles);

            List<File> secondaryLogFiles = null;
            File secondaryLogPathDir = LoggerContext.getDefault().getSecondaryLogPathDir();

            if (secondaryLogPathDir != null) {
                secondaryLogFiles = LoggerUtils.collectLogFilesWithRange(
                        secondaryLogPathDir,
                        record.getBuffers(),
                        record.getStartTime(),
                        record.getEndTime());
            }
            Debug.i("Task " + record.getTaskId() + " collect secondary log dir files: " + secondaryLogFiles);

            List<File> extraLogFiles = LoggerUtils.collectExtraLogFiles(LoggerFactory.getConfig().getExtraLogDir());

            if (LoggerUtils.isEmpty(mainLogFiles)
                    && LoggerUtils.isEmpty(secondaryLogFiles)
                    && LoggerUtils.isEmpty(extraLogFiles)) {
                RequestManager.uploadTaskStatus(taskId, 101, "该任务时间段无待上传文件");
                return;
            }

            File zipFile = new File(LoggerContext.getDefault().getLogCacheDir(), taskId + ".zip");
            List<ZipUtil.EntrySet> entrySets = new ArrayList<>();

            if (!LoggerUtils.isEmpty(mainLogFiles)) {
                File baseDir = LoggerContext.getDefault().getMainLogPathDir();
                entrySets.add(new ZipUtil.EntrySet(null, baseDir, mainLogFiles));
            }

            if (!LoggerUtils.isEmpty(secondaryLogFiles)) {
                entrySets.add(new ZipUtil.EntrySet("secondary_log", secondaryLogPathDir, secondaryLogFiles));
            }

            if (!LoggerUtils.isEmpty(extraLogFiles)) {
                File baseDir = LoggerFactory.getConfig().getExtraLogDir();
                entrySets.add(new ZipUtil.EntrySet("extra_log", baseDir, extraLogFiles));
            }

            ZipUtil.writeToZip(entrySets, zipFile);

            List<SliceRecord> sliceRecords = splitTask(taskId, zipFile);
            TaskFileRecord taskFileRecord = new TaskFileRecord(taskId, zipFile.getAbsolutePath());

            taskRecordDao.add(record);
            UploadTaskDatabase.getDatabase().getFileRecordDao().addAll(sliceRecords);
            UploadTaskDatabase.getDatabase().getTaskFileRecordDao().add(taskFileRecord);
            Debug.i("create task successfully: "
                    + "record:" + record
                    + " sliceRecords:" + sliceRecords
                    + " taskFileRecord:" + taskFileRecord);
        } finally {
            mPendingTaskQueue.remove(taskId);
        }

        RequestManager.uploadTaskStatus(taskId, 4, "文件上传中");
        UploadService.getInstance().start(mContext);
    }

    static List<SliceRecord> splitTask(String taskId, File zipFile) {
        List<SliceRecord> records = new ArrayList<>();

        long fileSize = zipFile.length();
        String file = zipFile.getAbsolutePath();

        long startPos = 0;
        int sliceId = 0;

        int sectionLength = LoggerFactory.getConfig().getFileSectionLength();
        int mod = (int) (fileSize % sectionLength);
        int sliceCount = (int) (fileSize / sectionLength + (mod == 0 ? 0 : 1));

        while (startPos < fileSize) {
            long mayBe = startPos + sectionLength;
            long endPos = mayBe >= fileSize ? fileSize : mayBe;

            SliceRecord record =
                    new SliceRecord(taskId, sliceCount, sliceId, file, zipFile.length(), startPos, endPos);
            records.add(record);

            sliceId++;
            startPos = endPos;
        }
        return records;
    }

    void taskFailed(Context context, String taskId, String reason) {
        LOG("task failed:" + taskId);

        mPreferences.edit().putBoolean(taskId, true).apply();

        //report
        String message = reason == null ? "文件上传失败" : reason;
        RequestManager.uploadTaskStatus(taskId, 102, message);
        String networkType = LoggerUtils.getNetworkType(context);
        ReportUtils.reportUploadTaskResult(false, networkType, taskId, message);

        //delete records
        UploadTaskDatabase.getDatabase().getTaskRecordDao().deleteByTaskId(taskId);
        UploadTaskDatabase.getDatabase().getFileRecordDao().deleteByTaskId(taskId);

        cleanTask(taskId);
    }

    void taskSuccess(Context context, String taskId) {
        LOG("task success:" + taskId);

        mPreferences.edit().putBoolean(taskId, true).apply();

        String networkType = LoggerUtils.getNetworkType(context);
        ReportUtils.reportUploadTaskResult(true, networkType, taskId, "success");

        //delete records
        UploadTaskDatabase.getDatabase().getTaskRecordDao().deleteByTaskId(taskId);

        cleanTask(taskId);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    void cleanTask(String taskId) {
        TaskFileRecordDao taskFileRecordDao = UploadTaskDatabase.getDatabase().getTaskFileRecordDao();
        List<TaskFileRecord> records = taskFileRecordDao.getRecordsByTaskId(taskId);
        //delete zip file records
        taskFileRecordDao.deleteByTaskId(taskId);

        //delete zip files
        for (TaskFileRecord record : records) {
            File file = new File(record.getFile());
            file.delete();
            LOG("clean task zip file taskId:" + taskId + " file:" + file);
        }
        LoggerConfig config = LoggerFactory.getConfig();
        if (config.isExtraLogCleanEnabled()) {
            File extraDir = config.getExtraLogDir();
            FileUtils.cleanDir(extraDir);
        }
    }

    private boolean isNetworkConnected() {
        NetworkInfo networkInfo = mConnectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private static void LOG(String msg) {
        Debug.d("UploadTaskManager: " + msg);
    }
}

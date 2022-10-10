package com.yc.ycupdatelib;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.yc.apppermission.PermissionUtils;
import com.yc.easyexecutor.DelegateTaskExecutor;
import com.yc.eventuploadlib.LoggerReporter;

import java.io.File;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/05/29
 *     desc  : 版本更新弹窗
 *     revise:
 * </pre>
 */
public class UpdateFragment extends BaseDialogFragment implements View.OnClickListener {


    /**
     * 是否强制更新
     */
    private boolean isForceUpdate;
    private String apkUrl;
    private String saveApkPath;
    private String apkName;
    private String desc;
    private String packageName;
    private String appMd5;
    private File file;

    private static final String[] M_PERMISSION = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};
    private static final int REQUEST_CODE = 1314;
    private int downloadStatus = AppUpdateUtils.DownloadStatus.START;
    public static final String TAG = "UpdateFragment: ";
    private FragmentActivity mActivity;
    private BaseDownloadTask downloadTask;
    private TextView mTvDesc;
    private ProgressBar mProgress;
    private TextView mTvCancel;
    private TextView mTvOk;
    private NotificationManager manager;


    /**
     * 版本更新
     *
     * @param isForceUpdate 是否强制更新
     * @param apkUrl        下载链接
     * @param apkName       下载apk名称
     * @param desc          更新文案
     * @param packageName   包名
     * @param appMd5        安装包md5值，传null表示不校验
     */
    public static void showFragment(FragmentActivity activity, boolean isForceUpdate,
                                    String apkUrl, String apkName, String desc,
                                    String packageName, String appMd5) {
        UpdateFragment updateFragment = new UpdateFragment();
        Bundle bundle = new Bundle();
        bundle.putString("apk_url", apkUrl);
        bundle.putString("desc", desc);
        bundle.putString("apkName", apkName);
        bundle.putBoolean("isUpdate", isForceUpdate);
        bundle.putString("packageName", packageName);
        bundle.putString("appMd5", appMd5);
        updateFragment.setArguments(bundle);
        FragmentManager supportFragmentManager = activity.getSupportFragmentManager();
        updateFragment.show(supportFragmentManager);
        FileDownloader.setup(activity);
    }

    /**
     * 版本更新
     *
     * @param isForceUpdate 是否强制更新
     * @param apkUrl        下载链接
     * @param apkName       下载apk名称
     * @param desc          更新文案
     * @param packageName   包名
     * @param appMd5        安装包md5值，传null表示不校验
     */
    public static void showFragment(Fragment fragment, boolean isForceUpdate,
                                    String apkUrl, String apkName, String desc,
                                    String packageName, String appMd5) {
        UpdateFragment updateFragment = new UpdateFragment();
        Bundle bundle = new Bundle();
        bundle.putString("apk_url", apkUrl);
        bundle.putString("desc", desc);
        bundle.putString("apkName", apkName);
        bundle.putBoolean("isUpdate", isForceUpdate);
        bundle.putString("packageName", packageName);
        bundle.putString("appMd5", appMd5);
        updateFragment.setArguments(bundle);
        Context context = fragment.getActivity();
        if (context != null) {
            FragmentManager supportFragmentManager = fragment.getChildFragmentManager();
            updateFragment.show(supportFragmentManager);
            FileDownloader.setup(context);
        } else {
            LoggerReporter.report(TAG, "showFragment fragment content null");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setLocal(Local.CENTER);
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            apkUrl = arguments.getString("apk_url");
            desc = arguments.getString("desc");
            apkName = arguments.getString("apkName");
            isForceUpdate = arguments.getBoolean("isUpdate");
            packageName = arguments.getString("packageName");
            appMd5 = arguments.getString("appMd5");
            LoggerReporter.report(TAG, "onCreate");
        }
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (outState != null) {
            outState.putString("apk_url", apkUrl);
            outState.putString("desc", desc);
            outState.putString("apkName", apkName);
            outState.putBoolean("isUpdate", isForceUpdate);
            outState.putString("packageName", packageName);
            outState.putString("appMd5", appMd5);
            LoggerReporter.report(TAG, "onSaveInstanceState");
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            apkUrl = savedInstanceState.getString("apk_url");
            desc = savedInstanceState.getString("desc");
            apkName = savedInstanceState.getString("apkName");
            isForceUpdate = savedInstanceState.getBoolean("isUpdate");
            packageName = savedInstanceState.getString("packageName");
            appMd5 = savedInstanceState.getString("appMd5");
            LoggerReporter.report(TAG, "onViewStateRestored");
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mActivity = (FragmentActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivity = null;
    }

    /**
     * 如果isForceUpdate是true，那么就是强制更新，则设置cancel为false
     * 如果isForceUpdate是false，那么不是强制更新，则设置cancel为true
     */
    @Override
    protected boolean isCancel() {
        return !isForceUpdate;
    }


    @Override
    public int getLayoutRes() {
        return R.layout.fragment_update_app;
    }

    @Override
    public void bindView(View view) {
        initView(view);
        setDataView();
        onKeyListener();
        createFilePath();
    }

    private void initView(View view) {
        mTvDesc = view.findViewById(R.id.tv_desc);
        mProgress = view.findViewById(R.id.progress);
        mTvCancel = view.findViewById(R.id.tv_cancel);
        mTvOk = view.findViewById(R.id.tv_ok);
    }


    private void setDataView() {
        mProgress.setMax(100);
        mProgress.setProgress(0);
        mTvDesc.setText(desc == null ? "" : desc);
        if (isForceUpdate) {
            mTvOk.setVisibility(View.VISIBLE);
            mTvCancel.setVisibility(View.GONE);
        } else {
            mTvOk.setVisibility(View.VISIBLE);
            mTvCancel.setVisibility(View.VISIBLE);
        }
        mTvOk.setOnClickListener(this);
        mTvCancel.setOnClickListener(this);
    }


    /**
     * 这里主要是处理返回键逻辑
     */
    private void onKeyListener() {
        if (getDialog() != null) {
            getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    // 返回键
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        return isForceUpdate;
                    }
                    cancelTaskAndNotify();
                    return false;
                }
            });
            getDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    LoggerReporter.report(TAG, "dialog on dismiss");
                }
            });
        }
    }

    private void createFilePath() {
        saveApkPath = AppUpdateUtils.getLocalApkDownSavePath(mActivity, apkName);
        file = new File(saveApkPath);
        if (file.exists()) {
            changeUploadStatus(AppUpdateUtils.DownloadStatus.FINISH);
        } else {
            changeUploadStatus(AppUpdateUtils.DownloadStatus.START);
        }
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_ok) {
            clickSure();
        } else if (i == R.id.tv_cancel) {
            clickCancel();
        }
    }

    /**
     * 点击确定
     */
    private void clickSure() {
        //当下载中，点击后则是暂停下载
        //当不是下载中，点击后先判断apk是否存在，若存在则提示安装；如果不存在，则下载
        //当出现错误时，点击后继续开始下载
        switch (downloadStatus) {
            case AppUpdateUtils.DownloadStatus.START:
            case AppUpdateUtils.DownloadStatus.UPLOADING:
                if (downloadTask != null) {
                    LoggerReporter.report(TAG, "download click pause");
                    downloadTask.pause();
                } else {
                    LoggerReporter.report(TAG, "download click , task is null , start download");
                    checkPermissionAndDownApk();
                }
                break;
            case AppUpdateUtils.DownloadStatus.FINISH:
                File file = new File(saveApkPath);
                if (file.exists()) {
                    LoggerReporter.report(TAG, "download click finish , install apk");
                    packageName = mActivity.getPackageName();
                    String md5 = AppUpdateUtils.getMD5(file);
                    LoggerReporter.report(TAG, "download click finish , apk md5 : " + md5);
                    if (appMd5 != null && !appMd5.equals(md5)) {
                        Toast.makeText(mActivity, "安装包Md5数据非法", Toast.LENGTH_SHORT).show();
                        AppUpdateUtils.clearDownload(mActivity, apkName);
                        DelegateTaskExecutor.getInstance().postToMainThread(new Runnable() {
                            @Override
                            public void run() {
                                changeUploadStatus(AppUpdateUtils.DownloadStatus.START);
                            }
                        }, 1000);
                        return;
                    }
                    //检测是否有apk文件，如果有直接普通安装
                    AppUpdateUtils.installNormal(mActivity, saveApkPath, packageName);
                } else {
                    LoggerReporter.report(TAG, "download click finish , no file");
                    checkPermissionAndDownApk();
                }
                break;
            case AppUpdateUtils.DownloadStatus.PAUSED:
                LoggerReporter.report(TAG, "download click paused");
                checkPermissionAndDownApk();
                break;
            case AppUpdateUtils.DownloadStatus.ERROR:
                LoggerReporter.report(TAG, "download click error");
                checkPermissionAndDownApk();
                break;
            default:
                LoggerReporter.report(TAG, "download click default");
                break;
        }
    }

    /**
     * 点击取消
     */
    private void clickCancel() {
        cancelTaskAndNotify();
        dismissDialog();
    }

    private void cancelTaskAndNotify() {
        //如果正在下载，那么就先暂停，然后finish
        if (downloadStatus == AppUpdateUtils.DownloadStatus.UPLOADING) {
            if (downloadTask != null && downloadTask.isRunning()) {
                LoggerReporter.report(TAG, "download click cancel , pause");
                downloadTask.pause();
            }
            LoggerReporter.report(TAG, "download click cancel , pause ok");
        }
        if (manager != null) {
            LoggerReporter.report(TAG, "download click cancel , cancel notification");
            manager.cancel(1314);
        }
    }

    private void changeUploadStatus(int upload_status) {
        this.downloadStatus = upload_status;
        switch (upload_status) {
            case AppUpdateUtils.DownloadStatus.START:
                mTvOk.setText("开始下载");
                mProgress.setVisibility(View.GONE);
                break;
            case AppUpdateUtils.DownloadStatus.UPLOADING:
                mTvOk.setText("下载中……");
                mProgress.setVisibility(View.VISIBLE);
                break;
            case AppUpdateUtils.DownloadStatus.FINISH:
                mTvOk.setText("开始安装");
                mProgress.setVisibility(View.INVISIBLE);
                break;
            case AppUpdateUtils.DownloadStatus.PAUSED:
                mProgress.setVisibility(View.VISIBLE);
                mTvOk.setText("暂停下载");
                break;
            case AppUpdateUtils.DownloadStatus.ERROR:
                mProgress.setVisibility(View.VISIBLE);
                mTvOk.setText("错误，点击继续");
                AppUpdateUtils.clearDownload(mActivity, apkName);
                break;
            default:
                break;
        }
    }


    private void checkPermissionAndDownApk() {
        if (mActivity == null) {
            LoggerReporter.report(TAG, "dialog sure , activity is null");
            return;
        }
        if (apkUrl.isEmpty()) {
            LoggerReporter.report(TAG, "dialog sure , apk url is empty");
            return;
        }
        boolean granted = PermissionUtils.isGranted(mActivity, M_PERMISSION);
        LoggerReporter.report(TAG, "permission granted : " + granted);
        if (granted) {
            setNotification(0);
            downloadTask = downApk(apkUrl, saveApkPath, listener);
        } else {
            PermissionDialog.storageDialog(new PermissionListener() {
                @Override
                public void dialogClickSure() {
                    //去申请权限
                    ActivityCompat.requestPermissions(mActivity, M_PERMISSION, REQUEST_CODE);
                }

                @Override
                public void dialogClickCancel() {
                    LoggerReporter.report(TAG, "permission cancel ok");
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length >= 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                setNotification(0);
                downloadTask = downApk(apkUrl, saveApkPath, listener);
            } else {
                Toast.makeText(mActivity, "请先申请读写权限", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private BaseDownloadTask downApk(String apkUrl, String saveApkPath, FileDownloadListener listener) {
        BaseDownloadTask baseDownloadTask = FileDownloader
                .getImpl()
                .create(apkUrl)
                .setPath(saveApkPath)
                .setListener(listener);
        baseDownloadTask.start();
        return baseDownloadTask;
    }

    private final FileDownloadListener listener = new FileDownloadListener() {
        @Override
        protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            changeUploadStatus(AppUpdateUtils.DownloadStatus.UPLOADING);
        }

        @Override
        protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            float total = task.getSmallFileTotalBytes();
            float downsize = task.getSmallFileSoFarBytes();
            int progress = (int) ((downsize / total) * 100);
            mProgress.setProgress(progress);
            setNotification(progress);
        }

        @Override
        protected void completed(BaseDownloadTask task) {
            setNotification(100);
            if (isForceUpdate) {
                mProgress.setProgress(100);
            }
            changeUploadStatus(AppUpdateUtils.DownloadStatus.FINISH);
            if (appMd5 != null && !appMd5.equals(AppUpdateUtils.getMD5(file))) {
                Toast.makeText(mActivity, "安装包Md5数据非法", Toast.LENGTH_SHORT).show();
                mTvOk.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        changeUploadStatus(AppUpdateUtils.DownloadStatus.START);
                    }
                }, 500);
                return;
            }
            AppUpdateUtils.installNormal(mActivity, saveApkPath, packageName);
            LoggerReporter.report(TAG, "download completed");
        }

        @Override
        protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            changeUploadStatus(AppUpdateUtils.DownloadStatus.PAUSED);
            LoggerReporter.report(TAG, "download pause");
        }

        @Override
        protected void error(BaseDownloadTask task, Throwable e) {
            setNotification(-1);
            changeUploadStatus(AppUpdateUtils.DownloadStatus.ERROR);
            LoggerReporter.report(TAG, "download error " + e.getMessage());
        }

        @Override
        protected void warn(BaseDownloadTask task) {
            changeUploadStatus(AppUpdateUtils.DownloadStatus.ERROR);
        }
    };


    protected void setNotification(int progress) {
        if (mActivity == null) {
            return;
        }
        Intent intent = new Intent();
        PendingIntent pendingIntent;
        if (Build.VERSION.SDK_INT >= 31){
            //处理Android 12。对应的level 是 31
            pendingIntent = PendingIntent.getActivity(mActivity,
                    0, intent, PendingIntent.FLAG_IMMUTABLE);
        } else {
            pendingIntent = PendingIntent.getActivity(mActivity,
                    0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
        RemoteViews remoteViews = new RemoteViews(mActivity.getPackageName(), R.layout.remote_notification_view);
        remoteViews.setTextViewText(R.id.tvTitle, "下载apk");
        remoteViews.setProgressBar(R.id.pb, 100, progress, false);
        NotificationUtils notificationUtils = new NotificationUtils(mActivity);
        manager = notificationUtils.getManager();
        Notification notification = notificationUtils.setContentIntent(pendingIntent)
                .setContent(remoteViews)
                .setFlags(Notification.FLAG_AUTO_CANCEL)
                .setOnlyAlertOnce(true)
                .getNotification("来了一条消息", "下载apk", R.mipmap.ic_launcher);
        //下载成功或者失败
        if (progress == 100 || progress == -1) {
            notificationUtils.clearNotification();
        } else {
            manager.notify(1314, notification);
        }
    }

}

package com.yc.downloadhelper;

import android.app.DownloadManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.Objects;


public class UpdateManager {

    /**
     * DownLoadManager.Request:主要用于发起一个下载请求。
     * DownLoadManager.Query:主要用于查询下载信息。
     */
    private static final String TAG = "UpdateManager";
    /**
     * context的弱引用
     */
    private WeakReference<Context> wrfContext;
    /**
     * 系统DownloadManager
     */
    private DownloadManager downloadManager;
    /**
     * 上次下载的id
     */
    private long lastDownloadId = -1;
    /**
     * 下载监听
     */
    private DownloadObserver downloadObserver;

    /**
     * 开启下载更新
     *
     * @param context   上下文
     */
    public void startUpdate(Context context) {
        if (context == null) {
            throw new NullPointerException("UpdateManager======context不能为null");
        }
        wrfContext = new WeakReference<>(context);
    }

    /**
     * 下载apk
     */
    private void downLoadApk(String downloadUrl , String savePath , String title) {
        try {
            Context context = wrfContext.get();
            if (context == null) {
                return;
            }
            // 获取下载管理器
            downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            clearCurrentTask();
            // 下载地址如果为null,抛出异常
            Uri uri = Uri.parse(downloadUrl);
            DownloadManager.Request request = new DownloadManager.Request(uri);
            // 下载中和下载完成显示通知栏
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            if (TextUtils.isEmpty(savePath)) {
                //使用系统默认的下载路径 此处为应用内 /android/data/packages ,所以兼容7.0
                request.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, context.getPackageName() + ".apk");
                deleteApkFile(Objects.requireNonNull(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS + File.separator + context.getPackageName() + ".apk")));
            } else {
                // 自定义的下载目录,注意这是涉及到android Q的存储权限，建议不要用getExternalStorageDirectory（）
                request.setDestinationInExternalFilesDir(context, savePath, context.getPackageName() + ".apk");
                deleteApkFile(Objects.requireNonNull(context.getExternalFilesDir(savePath + File.separator + context.getPackageName() + ".apk")));
            }
            //设置下载的网络类型
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE
                    | DownloadManager.Request.NETWORK_WIFI);
            // 部分机型（暂时发现Nexus 6P）无法下载，猜测原因为默认下载通过计量网络连接造成的，通过动态判断一下
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager != null) {
                boolean activeNetworkMetered = connectivityManager.isActiveNetworkMetered();
                request.setAllowedOverMetered(activeNetworkMetered);
            }
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q){
                request.allowScanningByMediaScanner();
            }
            //是否显示下载 从Android Q开始会被忽略
            request.setVisibleInDownloadsUi(true);
            // 设置通知栏的标题
            request.setTitle(title);
            // 设置通知栏的描述
            request.setDescription("正在下载中...");
            // 设置媒体类型为apk文件
            request.setMimeType("application/vnd.android.package-archive");
            // 开启下载，返回下载id
            lastDownloadId = downloadManager.enqueue(request);
            // 如需要进度及下载状态，增加下载监听
            DownloadHandler downloadHandler = new DownloadHandler(this);
            downloadObserver = new DownloadObserver(downloadHandler, downloadManager, lastDownloadId);
            context.getContentResolver().registerContentObserver(Uri.parse("content://downloads/my_downloads"), true, downloadObserver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置下载的进度
     *
     * @param progress 进度
     */
    public void setProgress(int progress) {

    }

    /**
     * 取消下载的监听
     */
    public void unregisterContentObserver() {
        if (wrfContext.get() != null) {
            wrfContext.get().getContentResolver().unregisterContentObserver(downloadObserver);
        }
    }

    /**
     * 显示下载失败
     */
    public void showFail() {

    }


    /**
     * 下载前清空本地缓存的文件
     */
    private void deleteApkFile(File destFileDir) {
        if (!destFileDir.exists()) {
            return;
        }
        if (destFileDir.isDirectory()) {
            File[] files = destFileDir.listFiles();
            if (files != null) {
                for (File f : files) {
                    deleteApkFile(f);
                }
            }
        }
        destFileDir.delete();
    }


    /**
     * 清除上一个任务，防止apk重复下载
     */
    public void clearCurrentTask() {
        try {
            if (lastDownloadId != -1) {
                downloadManager.remove(lastDownloadId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

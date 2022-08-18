package com.yc.downloadhelper;

import android.app.DownloadManager;
import android.content.Context;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

import java.io.File;


public class DownloadHelper {


    public static void test(Context context){
        DownloadReceiver receiver = new DownloadReceiver();
        IntentFilter intentFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        context.registerReceiver(receiver, intentFilter);
    }

    /**
     * 此方法可以将下载任务和已经下载的文件同时删除
     * @param downloadManager           下载管理器
     * @param downloadId                下载id
     * @return
     */
    public static boolean remove(DownloadManager downloadManager ,long downloadId){
        //下载量实际上被删除了
        int remove = downloadManager.remove(downloadId);
        return true;
    }


    /**
     * 获取下载状态
     *
     * @param downloadManager           下载管理器
     * @param downloadId                下载id
     * @return int
     * @see DownloadManager#STATUS_PENDING  　　 下载等待开始时
     * @see DownloadManager#STATUS_PAUSED   　　 下载暂停
     * @see DownloadManager#STATUS_RUNNING　     正在下载中　
     * @see DownloadManager#STATUS_SUCCESSFUL   下载成功
     * @see DownloadManager#STATUS_FAILED       下载失败
     */
    public static int getDownloadStatus(DownloadManager downloadManager ,long downloadId) {
        //setFilterById(long... ids)：根据任务编号查询下载任务信息
        DownloadManager.Query query = new DownloadManager.Query().setFilterById(downloadId);
        Cursor c = downloadManager.query(query);
        if (c != null) {
            try {
                if (c.moveToFirst()) {
                    return c.getInt(c.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS));
                }
            } finally {
                c.close();
            }
        }
        return -1;
    }

    /**
     * 获取当前下载进度
     * @param downloadManager           下载管理器
     * @param downloadId                下载id
     * @return
     */
    public static int getDownloadProgress(DownloadManager downloadManager , long downloadId) {
        DownloadManager.Query query = new DownloadManager.Query().setFilterById(downloadId);
        Cursor c = downloadManager.query(query);
        if (c != null) {
            try {
                if (c.moveToFirst()) {
                    return c.getInt(c.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                }
            } finally {
                c.close();
            }
        }
        return -1;

    }

    /**
     * 获取下载总大小
     * @param downloadManager           下载管理器
     * @param downloadId                下载id
     * @return
     */
    public static int getDownloadTotal(DownloadManager downloadManager , long downloadId) {
        DownloadManager.Query query = new DownloadManager.Query().setFilterById(downloadId);
        Cursor c = downloadManager.query(query);
        if (c != null) {
            try {
                if (c.moveToFirst()) {
                    return c.getInt(c.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                }
            } finally {
                c.close();
            }
        }
        return -1;
    }


    /**
     * 获取下载的文件
     *
     * @return file
     */
    public static File getDownloadFile(DownloadManager downloadManager , long downloadId) {
        DownloadManager.Query query = new DownloadManager.Query();
        Cursor cursor = downloadManager.query(query.setFilterById(downloadId));
        if (cursor != null && cursor.moveToFirst()) {
            String fileUri = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
            String apkPath = Uri.parse(fileUri).getPath();
            if (!TextUtils.isEmpty(apkPath)) {
                return new File(apkPath);
            }
            cursor.close();
        }
        return null;
    }

}

package com.yc.downloadhelper;

import android.app.DownloadManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * description:通过ContentObserver监听下载的进度
 */
public class DownloadObserver extends ContentObserver {

    private final String TAG = getClass().getCanonicalName();

    private final Handler handler;

    /**
     * 记录成功或者失败的状态，主要用来只发送一次成功或者失败
     */
    private boolean isEnd = false;

    private final DownloadManager downloadManager;

    private final DownloadManager.Query query;

    /**
     * Creates a content observer.
     *
     * @param handler The handler to run {@link #onChange} on, or null if none.
     */
    public DownloadObserver(Handler handler, DownloadManager downloadManager, long downloadId) {
        super(handler);
        this.handler = handler;
        this.downloadManager = downloadManager;
        query = new DownloadManager.Query().setFilterById(downloadId);
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        queryDownloadStatus();
    }

    /**
     * 检查下载的状态
     */
    private void queryDownloadStatus() {
        // Java 7 新的 try-with-resources ，凡是实现了AutoCloseable接口的可自动close()，所以此处不需要手动cursor.close()
        try (Cursor cursor = downloadManager.query(query)) {
            if (cursor != null && cursor.moveToNext()) {
                int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                long totalSize = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                long currentSize = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                // 当前进度
                int mProgress;
                if (totalSize != 0) {
                    mProgress = (int) ((currentSize * 100) / totalSize);
                } else {
                    mProgress = 0;
                }
                Log.d(TAG, String.valueOf(mProgress));
                switch (status) {
                    case DownloadManager.STATUS_PAUSED:
                        // 下载暂停
                        handler.sendEmptyMessage(DownloadManager.STATUS_PAUSED);
                        Log.d(TAG, "STATUS_PAUSED");
                        break;
                    case DownloadManager.STATUS_PENDING:
                        // 开始下载
                        handler.sendEmptyMessage(DownloadManager.STATUS_PENDING);
                        Log.d(TAG, "STATUS_PENDING");
                        break;
                    case DownloadManager.STATUS_RUNNING:
                        // 正在下载，不做任何事情
                        Message message = Message.obtain();
                        message.what = DownloadManager.STATUS_RUNNING;
                        message.arg1 = mProgress;
                        handler.sendMessage(message);
                        Log.d(TAG, "STATUS_RUNNING");
                        break;
                    case DownloadManager.STATUS_SUCCESSFUL:
                        if (!isEnd) {
                            // 完成
                            handler.sendEmptyMessage(DownloadManager.STATUS_SUCCESSFUL);
                            Log.d(TAG, "STATUS_SUCCESSFUL");
                        }
                        isEnd = true;
                        break;
                    case DownloadManager.STATUS_FAILED:
                        if (!isEnd) {
                            handler.sendEmptyMessage(DownloadManager.STATUS_FAILED);
                            Log.d(TAG, "STATUS_FAILED");
                        }
                        isEnd = true;
                        break;
                    default:
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}

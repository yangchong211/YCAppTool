package com.ycbjie.music.executor.download;

import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.webkit.MimeTypeMap;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.ycbjie.music.R;
import com.ycbjie.music.base.BaseAppHelper;
import com.ycbjie.music.executor.inter.IExecutor;
import com.ycbjie.music.model.bean.DownloadMusicInfo;
import com.ycbjie.music.utils.FileMusicUtils;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/01/24
 *     desc  : 下载音乐
 *     revise:
 * </pre>
 */
public abstract class AbsDownloadMusic implements IExecutor<Void> {


    private Activity mActivity;
    AbsDownloadMusic(Activity activity) {
        mActivity = activity;
    }

    @Override
    public void execute() {
        //开始检测网络
        checkNetwork();
    }


    private void checkNetwork() {
        if (!NetworkUtils.isConnected()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
            builder.setTitle(R.string.tips);
            builder.setMessage(R.string.download_tips);
            builder.setPositiveButton(R.string.download_tips_sure, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    downloadWrapper();
                }
            });
            builder.setNegativeButton(R.string.cancel, null);
            Dialog dialog = builder.create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        } else {
            downloadWrapper();
        }
    }


    private void downloadWrapper() {
        onPrepare();
        download();
    }


    /**抽象方法，供子类实现*/
    abstract void download();


    /**
     * 开始下载
     * @param url                   url
     * @param artist                artist
     * @param title                 title
     * @param coverPath             path
     */
    void downloadMusic(String url, String artist, String title, String coverPath) {
        try {
            String fileName = FileMusicUtils.getMp3FileName(artist, title);
            Uri uri = Uri.parse(url);
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setTitle(FileMusicUtils.getFileName(artist, title));
            request.setDescription("正在下载…");
            request.setDestinationInExternalPublicDir(FileMusicUtils.getRelativeMusicDir(), fileName);
            request.setMimeType(MimeTypeMap.getFileExtensionFromUrl(url));
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
            // 不允许漫游
            request.setAllowedOverRoaming(false);
            DownloadManager downloadManager = (DownloadManager) Utils.getApp().getSystemService(Context.DOWNLOAD_SERVICE);
            long id = 0;
            if (downloadManager != null) {
                id = downloadManager.enqueue(request);
            }
            String musicAbsPath = FileMusicUtils.getMusicDir().concat(fileName);
            DownloadMusicInfo downloadMusicInfo = new DownloadMusicInfo(title, musicAbsPath, coverPath);
            BaseAppHelper.get().getDownloadList().put(id, downloadMusicInfo);
        } catch (Throwable th) {
            th.printStackTrace();
            ToastUtils.showShort("下载失败");
        } finally {

        }
    }


}

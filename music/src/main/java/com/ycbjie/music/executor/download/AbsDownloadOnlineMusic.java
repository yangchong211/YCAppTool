package com.ycbjie.music.executor.download;

import android.app.Activity;
import android.text.TextUtils;

import com.pedaily.yc.ycdialoglib.toast.ToastUtils;
import com.ycbjie.music.api.OnLineMusicModel;
import com.ycbjie.music.model.bean.DownloadInfo;
import com.ycbjie.music.model.bean.OnlineMusicList;
import com.ycbjie.music.utils.FileMusicUtils;

import java.io.File;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * <pre>
 *     author: yangchong
 *     blog  :
 *     time  : 2018/01/24
 *     desc  : 下载音乐
 *     revise:
 * </pre>
 */
public abstract class AbsDownloadOnlineMusic extends AbsDownloadMusic {

    private Activity mActivity;
    private OnlineMusicList.SongListBean mOnlineMusic;

    protected AbsDownloadOnlineMusic(Activity activity, OnlineMusicList.SongListBean onlineMusic) {
        super(activity);
        mActivity = activity;
        mOnlineMusic = onlineMusic;
    }

    @Override
    protected void download() {
        final String artist = mOnlineMusic.getArtist_name();
        final String title = mOnlineMusic.getTitle();

        // 下载歌词
        String lrcFileName = FileMusicUtils.getLrcFileName(artist, title);
        File lrcFile = new File(FileMusicUtils.getLrcDir() + lrcFileName);
        if (!TextUtils.isEmpty(mOnlineMusic.getLrclink()) && !lrcFile.exists()) {
//            downloadLrc(mOnlineMusic.getLrclink(), FileMusicUtils.getLrcDir(), lrcFileName);
        }else {
            ToastUtils.showToast("无法下载歌词");
        }

        // 下载封面
        String albumFileName = FileMusicUtils.getAlbumFileName(artist, title);
        final File albumFile = new File(FileMusicUtils.getAlbumDir(), albumFileName);
        String picUrl = mOnlineMusic.getPic_big();
        if (TextUtils.isEmpty(picUrl)) {
            picUrl = mOnlineMusic.getPic_small();
        }
        if (!albumFile.exists() && !TextUtils.isEmpty(picUrl)) {
//            downloadFile(picUrl, FileMusicUtils.getAlbumDir(), albumFileName);
        }else {
            ToastUtils.showRoundRectToast("无法下载封面");
        }

        // 获取歌曲下载链接
        getMusicDownloadInfo(mOnlineMusic.getSong_id(),artist,title,albumFile);
    }


    private void getMusicDownloadInfo(String songId, final String artist,
                                      final String title, final File albumFile) {
        OnLineMusicModel model = OnLineMusicModel.getInstance();
        model.getMusicDownloadInfo(OnLineMusicModel.METHOD_DOWNLOAD_MUSIC,songId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DownloadInfo>() {
                    @Override
                    public void accept(DownloadInfo downloadInfo) throws Exception {
                        if (downloadInfo == null || downloadInfo.getBitrate() == null) {
                            onExecuteFail(null);
                            return;
                        }
                        downloadMusic(downloadInfo.getBitrate().getFile_link(), artist, title, albumFile.getPath());
                        onExecuteSuccess(null);
                    }
                });
    }


}

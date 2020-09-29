package com.ycbjie.music.executor.download;

import android.app.Activity;

import com.pedaily.yc.ycdialoglib.toast.ToastUtils;
import com.ycbjie.music.api.OnLineMusicModel;
import com.ycbjie.music.model.bean.DownloadInfo;
import com.ycbjie.music.model.bean.SearchMusic;
import com.ycbjie.music.utils.FileMusicUtils;

import java.io.File;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * <pre>
 *     author: yangchong
 *     blog  : www.pedaily.cn
 *     time  : 2018/01/24
 *     desc  : 下载音乐
 *     revise:
 * </pre>
 */
public abstract class AbsDownloadSearchMusic extends AbsDownloadMusic {

    private Activity mActivity;
    private SearchMusic.Song mSong;

    protected AbsDownloadSearchMusic(Activity activity, SearchMusic.Song song) {
        super(activity);
        mActivity = activity;
        this.mSong = song;
    }


    @Override
    protected void download() {
        final String artist = mSong.getArtistname();
        final String title = mSong.getSongname();

        // 下载歌词
        String lrcFileName = FileMusicUtils.getLrcFileName(artist, title);
        File lrcFile = new File(FileMusicUtils.getLrcDir() + lrcFileName);
        if (!lrcFile.exists()) {
        }

        // 下载封面
        String albumFileName = FileMusicUtils.getAlbumFileName(artist, title);
        final File albumFile = new File(FileMusicUtils.getAlbumDir(), albumFileName);

        // 获取歌曲下载链接
        getMusicDownloadInfo(mSong.getSongid(),artist,title,albumFile);
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

package com.ycbjie.music.executor.online;

import android.app.Activity;
import android.text.TextUtils;

import com.pedaily.yc.ycdialoglib.toast.ToastUtils;
import com.ycbjie.music.api.OnLineMusicModel;
import com.ycbjie.music.model.bean.AudioBean;
import com.ycbjie.music.model.bean.DownloadInfo;
import com.ycbjie.music.model.bean.OnlineMusicList;
import com.ycbjie.music.utils.FileMusicUtils;

import java.io.File;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * 播放在线音乐
 */
public abstract class AbsPlayOnlineMusic extends AbsPlayMusic {

    private OnlineMusicList.SongListBean mOnlineMusic;
    private Activity mActivity;
    private AudioBean music;

    public AbsPlayOnlineMusic(Activity activity, OnlineMusicList.SongListBean onlineMusic) {
        super(activity);
        this.mActivity = activity;
        mOnlineMusic = onlineMusic;

    }

    @Override
    public void onPrepare() {

    }

    @Override
    protected void getPlayInfo() {
        String artist = mOnlineMusic.getArtist_name();
        String title = mOnlineMusic.getTitle();

        music = new AudioBean();
        music.setType(AudioBean.Type.ONLINE);
        music.setTitle(title);
        music.setArtist(artist);
        music.setAlbum(mOnlineMusic.getAlbum_title());

        // 下载歌词
        String lrcFileName = FileMusicUtils.getLrcFileName(artist, title);
        File lrcFile = new File(FileMusicUtils.getLrcDir() + lrcFileName);
        if (!lrcFile.exists() && !TextUtils.isEmpty(mOnlineMusic.getLrclink())) {
//            downloadLrc(mOnlineMusic.getLrclink(), FileMusicUtils.getLrcDir(), lrcFileName);
        }

        // 下载封面
        String albumFileName = FileMusicUtils.getAlbumFileName(artist, title);
        File albumFile = new File(FileMusicUtils.getAlbumDir(), albumFileName);
        String picUrl = mOnlineMusic.getPic_big();
        if (TextUtils.isEmpty(picUrl)) {
            picUrl = mOnlineMusic.getPic_small();
        }
        if (!albumFile.exists() && !TextUtils.isEmpty(picUrl)) {
//            downloadFile(picUrl, FileMusicUtils.getAlbumDir(), albumFileName);
        } else {
            mCounter++;
        }
        music.setCoverPath(albumFile.getPath());

        // 获取歌曲播放链接
        getMusicInfo(mOnlineMusic.getSong_id());
    }


    private void getMusicInfo(String songId) {
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
                        music.setPath(downloadInfo.getBitrate().getFile_link());
                        music.setDuration(downloadInfo.getBitrate().getFile_duration() * 1000);
                        checkCounter(music);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (throwable instanceof RuntimeException) {
                            onExecuteFail(null);
                        }
                    }
                },new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                });
    }


}

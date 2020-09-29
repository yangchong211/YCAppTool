package com.ycbjie.music.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.util.LongSparseArray;

import com.blankj.utilcode.util.Utils;
import com.ns.yc.ycutilslib.activityManager.AppManager;
import com.ycbjie.music.model.bean.AudioBean;
import com.ycbjie.music.model.bean.DownloadMusicInfo;
import com.ycbjie.music.model.bean.OnLineSongListInfo;
import com.ycbjie.music.service.PlayService;
import com.ycbjie.music.ui.activity.MusicActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/03/22
 *     desc  : BaseAppHelper
 *     revise:
 * </pre>
 */
public class BaseAppHelper {

    private Context mContext;
    /**
     * 播放音乐service
     */
    private PlayService mPlayService;
    /**
     * 本地歌曲列表
     */
    private final List<AudioBean> mMusicList = new ArrayList<>();
    /**
     * 歌单列表
     */
    private final List<OnLineSongListInfo> mSongListInfo = new ArrayList<>();

    private final LongSparseArray<DownloadMusicInfo> mDownloadList = new LongSparseArray<>();

    private BaseAppHelper() {
        mContext = Utils.getApp().getApplicationContext();
        //这里可以做一些初始化的逻辑
    }

    private static class SingletonHolder {
        @SuppressLint("StaticFieldLeak")
        private final static BaseAppHelper INSTANCE = new BaseAppHelper();
    }

    public static BaseAppHelper get() {
        return SingletonHolder.INSTANCE;
    }

    public Context getContext() {
        return mContext;
    }

    /**
     * 获取PlayService对象
     * @return              返回PlayService对象
     */
    public PlayService getPlayService() {
        return mPlayService;
    }

    /**
     * 设置PlayService服务
     */
    public void setPlayService(PlayService service) {
        mPlayService = service;
    }

    /**
     * 获取扫描到的音乐数据集合
     * @return              返回list集合
     */
    public List<AudioBean> getMusicList() {
        return mMusicList;
    }


    /**
     * 设置音频结合
     * @param list              音频集合
     */
    public void setMusicList(List<AudioBean> list) {
        mMusicList.clear();
        mMusicList.addAll(list);
    }

    /**
     * 获取扫描到的音乐信息数据集合
     * @return              返回list集合
     */
    public List<OnLineSongListInfo> getSongListInfos() {
        return mSongListInfo;
    }

    /**
     * 获取扫描到的音乐下载数据集合
     * @return              返回list集合
     */
    public LongSparseArray<DownloadMusicInfo> getDownloadList() {
        return mDownloadList;
    }

    /**
     * 获取到播放音乐的服务
     * @return              PlayService对象
     */
    public PlayService getMusicService () {
        PlayService playService = BaseAppHelper.get().getPlayService();
        if (playService == null) {
            //待解决：当长期处于后台，如何保活？避免service被杀死……
            throw new NullPointerException("play service is null");
        }
        return playService;
    }

    public boolean checkServiceAlive() {
        if (BaseAppHelper.get().getMusicService() == null) {
            AppManager.getAppManager().finishActivity(MusicActivity.class);
            return false;
        }
        return true;
    }


}

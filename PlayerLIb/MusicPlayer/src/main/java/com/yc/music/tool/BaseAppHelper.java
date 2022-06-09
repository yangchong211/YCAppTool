package com.yc.music.tool;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;

import com.yc.music.inter.OnPlayerEventListener;
import com.yc.music.model.AudioBean;
import com.yc.music.service.PlayAudioService;

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

    /**
     * 播放音乐service
     */
    private PlayAudioService mPlayService;
    /**
     * 本地歌曲列表
     */
    private final List<AudioBean> mMusicList = new ArrayList<>();
    /**
     * 全局上下文
     */
    private Context mContext;
    /**
     * 播放进度监听器
     */
    private final List<OnPlayerEventListener> mOnPlayerEventListeners = new ArrayList<>();
    /**
     * 点击通知栏跳转的页面
     */
    private Class<? extends Activity> mMusicActivity;

    private BaseAppHelper() {
        //这里可以做一些初始化的逻辑
    }

    private static class SingletonHolder {
        @SuppressLint("StaticFieldLeak")
        private final static BaseAppHelper INSTANCE = new BaseAppHelper();
    }

    public static BaseAppHelper get() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 获取PlayService对象
     * @return              返回PlayService对象
     */
    private PlayAudioService getPlayService() {
        return mPlayService;
    }

    /**
     * 设置PlayService服务
     */
    public void setPlayService(PlayAudioService service) {
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
     * 设置音频结合
     * @param list              音频集合
     */
    public void addMusicList(List<AudioBean> list){
        mMusicList.addAll(list);
    }

    /**
     * 获取到播放音乐的服务
     * @return              PlayService对象
     */
    public PlayAudioService getMusicService () {
        return getPlayService();
    }

    public void setContext(Context context){
        this.mContext = context;
    }

    public Context getContext() {
        return mContext;
    }

    /**
     * 设置播放进度监听器
     * @param listener          listener
     */
    public void setOnPlayEventListener(OnPlayerEventListener listener) {
        mOnPlayerEventListeners.add(listener);
    }

    /**
     * 设置播放进度监听器
     * @param listener          listener
     */
    public void removeOnPlayEventListener(OnPlayerEventListener listener) {
        mOnPlayerEventListeners.remove(listener);
    }

    public List<OnPlayerEventListener> getOnPlayerEventListeners() {
        return mOnPlayerEventListeners;
    }

    public Class<? extends Activity> getMusicActivity() {
        return mMusicActivity;
    }

    public void setMusicActivity(Class<? extends Activity> mMusicActivity) {
        this.mMusicActivity = mMusicActivity;
    }
}

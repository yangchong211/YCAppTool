package com.yc.videosqllite.manager;

import android.content.Context;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time  : 2020/8/6
 *     desc  : 配置类
 *     revise:
 * </pre>
 */
public final class CacheConfig {

    /**
     * 是否生效
     */
    private boolean mIsEffective = true;
    /**
     * 内存缓存最大值
     */
    private int mCacheMax;
    /**
     * 对视频链接加盐字符串
     * 处理md5加密的盐
     */
    private String mSalt = "yc_video";
    /**
     * 0，表示内存缓存
     * 1，表示磁盘缓存
     * 2，表示内存缓存+磁盘缓存
     */
    private int type = 0;
    /**
     * 上下文
     */
    private Context context;
    /**
     * 是否开启日志
     */
    private boolean isLog = false;


    public boolean isEffective() {
        return mIsEffective;
    }

    public void setIsEffective(boolean mIsEffective) {
        this.mIsEffective = mIsEffective;
    }

    public int getCacheMax() {
        if (mCacheMax<=0){
            mCacheMax = 1000;
        }
        return mCacheMax;
    }

    public void setCacheMax(int mCacheMax) {
        this.mCacheMax = mCacheMax;
    }

    public String getSalt() {
        return mSalt;
    }

    public void setSalt(String salt) {
        //设置盐处理
        if (salt!=null && salt.length()>0){
            this.mSalt = salt;
        }
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public boolean isLog() {
        return isLog;
    }

    public void setLog(boolean log) {
        isLog = log;
    }
}

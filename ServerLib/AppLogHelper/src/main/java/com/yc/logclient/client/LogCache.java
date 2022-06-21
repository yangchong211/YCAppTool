package com.yc.logclient.client;


import android.util.SparseArray;

import com.yc.logclient.bean.AppLogBean;
import com.yc.logclient.inter.OnFlushListener;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * 职责描述: 日志实体 - 二级缓存.
 */


public class LogCache {

    public int maxCacheSize = 200;

    private ConcurrentLinkedDeque<AppLogBean> mLogCacheBeans = new ConcurrentLinkedDeque<>();

    private OnFlushListener mFlushListener;

    public LogCache(int maxCacheSize) {
        this.maxCacheSize = maxCacheSize;
    }

    public void put(AppLogBean bean) {
        mLogCacheBeans.add(bean);
        checkFull();
    }

    public int getCacheSize() {
        return mLogCacheBeans.size();
    }

    public void checkFull() {
        if (getCacheSize() > maxCacheSize) {
            doFlush();
        }
    }

    public void doFlush() {
        //将mLogCacheBeans 按照type 进行分组,然后分别送LogServer
        SparseArray<ArrayList<AppLogBean>> groupArray = divideGroup();
        //AppLogUtils.d("doFlush groupArray  - size():"+groupArray.size());
        if (groupArray.size() > 0) {
            for (int i = 0; i < groupArray.size(); i++) {
                ArrayList<AppLogBean> typeList = groupArray.valueAt(i);
                //AppLogUtils.d("doFlush groupArray keyAt("+i+"):"+groupArray.keyAt(i)+",array.size:"+groupArray.valueAt(i).size());
                if (typeList != null) {
                    if (mFlushListener != null) {
                        mFlushListener.doFlush(typeList);
                    }
                }
            }
        }
    }

    /**
     * 将mLogCacheBeans 按照type 进行分组
     *
     * @return
     */
    public SparseArray<ArrayList<AppLogBean>> divideGroup() {
        SparseArray<ArrayList<AppLogBean>> mGroupArray = new SparseArray<>();

        if (mLogCacheBeans.size() == 0) {
            return mGroupArray;
        }
        ArrayList<AppLogBean> tmpList = new ArrayList<>();
        tmpList.addAll(mLogCacheBeans);
        mLogCacheBeans.clear();

        //AppLogUtils.d("divideGroup orginal mLogCacheBeans:"+tmpList.size());
        for (int i = 0; i < tmpList.size(); i++) {
            AppLogBean bean = tmpList.get(i);
            int type = bean.getType();

            ArrayList<AppLogBean> typeGroup = mGroupArray.get(type);
            if (typeGroup == null) {
                typeGroup = new ArrayList<AppLogBean>();
                mGroupArray.put(type, typeGroup);
            }
            typeGroup.add(bean);
            //AppLogUtils.d("divideGroup orginal bean.type:"+type+",after insert:"+typeGroup.size());
        }
        return mGroupArray;

    }

    public void setFlushListener(OnFlushListener listener) {
        this.mFlushListener = listener;
    }

    public void setMaxCacheSize(int size) {
        this.maxCacheSize = size;
    }

}

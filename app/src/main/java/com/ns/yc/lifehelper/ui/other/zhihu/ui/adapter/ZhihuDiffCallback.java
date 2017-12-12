package com.ns.yc.lifehelper.ui.other.zhihu.ui.adapter;

import android.support.v7.util.DiffUtil;

import com.ns.yc.lifehelper.ui.other.zhihu.model.bean.ZhiHuDailyBean;

import java.util.List;


public class ZhiHuDiffCallback extends DiffUtil.Callback{

    private List<ZhiHuDailyBean.StoriesBean> mOldData, mNewData;

    public ZhiHuDiffCallback(List<ZhiHuDailyBean.StoriesBean> mOldData, List<ZhiHuDailyBean.StoriesBean> mNewData) {
        this.mOldData = mOldData;
        this.mNewData = mNewData;
    }

    @Override
    public int getOldListSize() {
        return mOldData != null ? mOldData.size() : 0;
    }

    @Override
    public int getNewListSize() {
        return mNewData != null ? mNewData.size() : 0;
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldData.get(oldItemPosition).getId() == mNewData.get(newItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        ZhiHuDailyBean.StoriesBean beanOld = mOldData.get(oldItemPosition);
        ZhiHuDailyBean.StoriesBean beanNew = mNewData.get(newItemPosition);
        if (!beanOld.getTitle().equals(beanNew.getTitle())) {
            return false;
        }
        if (beanOld.isReadState() != beanNew.isReadState()) {
            return false;
        }
        if (beanOld.getImages().size() > 0 && beanNew.getImages().size() > 0) {
            if (!beanOld.getImages().get(0).equals(beanNew.getImages().get(0))) {
                return false;
            }
        }
        return true;
    }
}

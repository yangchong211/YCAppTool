package com.ycbjie.zhihu.ui.adapter;

import android.support.v7.util.DiffUtil;

import com.ycbjie.zhihu.model.ZhiHuDailyListBean;

import java.util.List;


public class ZhihuDiffCallback extends DiffUtil.Callback{

    private List<ZhiHuDailyListBean.StoriesBean> mOldData, mNewData;
    public ZhihuDiffCallback(List<ZhiHuDailyListBean.StoriesBean> mOldData, List<ZhiHuDailyListBean.StoriesBean> mNewData) {
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
        ZhiHuDailyListBean.StoriesBean beanOld = mOldData.get(oldItemPosition);
        ZhiHuDailyListBean.StoriesBean beanNew = mNewData.get(newItemPosition);
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

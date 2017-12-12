package com.ns.yc.lifehelper.ui.other.zhihu.ui.adapter;

import android.app.Activity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.ui.other.zhihu.model.bean.ZhiHuSectionChildBean;
import com.ns.yc.lifehelper.utils.ImageUtils;

public class ZhiHuSectionListAdapter extends RecyclerArrayAdapter<ZhiHuSectionChildBean.StoriesBean> {

    public ZhiHuSectionListAdapter(Activity activity) {
        super(activity);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ExpressDeliveryViewHolder(parent);
    }

    private class ExpressDeliveryViewHolder extends BaseViewHolder<ZhiHuSectionChildBean.StoriesBean> {

        ImageView iv_logo;
        TextView tv_title , tv_time;

        ExpressDeliveryViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_tx_news_list);
            tv_title = $(R.id.tv_title);
            tv_time = $(R.id.tv_time);
            iv_logo = $(R.id.iv_logo);
        }

        @Override
        public void setData(ZhiHuSectionChildBean.StoriesBean data) {
            super.setData(data);
            tv_title.setText(data.getTitle());
            tv_time.setText(data.getDisplay_date());
            ImageUtils.loadImgByPicasso(getContext(),data.getImages().get(0),R.drawable.image_default,iv_logo);
        }
    }
}

package com.ns.yc.lifehelper.ui.me.view.adapter;

import android.app.Activity;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.TimeUtils;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.ui.other.gank.bean.GanKFavorite;

public class MeGanKCollectAdapter extends RecyclerArrayAdapter<GanKFavorite> {

    public MeGanKCollectAdapter(Activity activity) {
        super(activity);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ExpressDeliveryViewHolder(parent);
    }


    private class ExpressDeliveryViewHolder extends BaseViewHolder<GanKFavorite> {

        TextView tv_title , tv_type ,tv_publisher ,tv_time;

        ExpressDeliveryViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_gank_collect);
            tv_title = $(R.id.tv_title);
            tv_type = $(R.id.tv_type);
            tv_publisher = $(R.id.tv_publisher);
            tv_time = $(R.id.tv_time);
        }

        @Override
        public void setData(GanKFavorite data) {
            super.setData(data);
            tv_title.setText(data.getTitle()==null ? "标题" : data.getTitle());
            tv_type.setText(data.getType()==null ? "类型" : data.getType());
            tv_publisher.setText(data.getAuthor()==null ? "佚名" : data.getAuthor());
            tv_time.setText(TimeUtils.getFriendlyTimeSpanByNow(data.getCreatetime()));
        }
    }
}

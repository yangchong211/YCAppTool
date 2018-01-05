package com.ns.yc.lifehelper.ui.other.gank.view.adapter;

import android.app.Activity;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.ui.other.gank.bean.SearchResult;

import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter;
import org.yczbj.ycrefreshviewlib.viewHolder.BaseViewHolder;

public class GanKSearchListAdapter extends RecyclerArrayAdapter<SearchResult.ResultsBean> {

    public GanKSearchListAdapter(Activity activity) {
        super(activity);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ExpressDeliveryViewHolder(parent);
    }

    private class ExpressDeliveryViewHolder extends BaseViewHolder<SearchResult.ResultsBean> {

        TextView tv_title , tv_type ,tv_publisher ,tv_time;

        ExpressDeliveryViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_gank_collect);
            tv_title = $(R.id.tv_title);
            tv_type = $(R.id.tv_type);
            tv_publisher = $(R.id.tv_publisher);
            tv_time = $(R.id.tv_time);
        }

        @Override
        public void setData(SearchResult.ResultsBean data) {
            super.setData(data);
            tv_title.setText(data.desc==null ? "标题" : data.desc);
            tv_type.setText(data.type==null ? "类型" : data.type);
            tv_publisher.setText(data.who==null ? "佚名" : data.who);
            tv_time.setText(data.publishedAt);
        }
    }
}

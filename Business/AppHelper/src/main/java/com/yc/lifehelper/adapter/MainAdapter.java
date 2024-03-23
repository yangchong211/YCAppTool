package com.yc.lifehelper.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.yc.library.bean.ListNewsData;
import com.yc.lifehelper.R;
import com.yc.toolutils.time.AppTimeUtils;

import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter;
import org.yczbj.ycrefreshviewlib.holder.BaseViewHolder;

import java.util.Date;

public class MainAdapter extends RecyclerArrayAdapter<ListNewsData> {

    public MainAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder<ListNewsData> OnCreateViewHolder(ViewGroup parent, int i) {
        return new MyViewHolder(parent);
    }

    private static class MyViewHolder extends BaseViewHolder<ListNewsData> {

        TextView tv_title;
        TextView tv_time;
        ImageView iv_logo;

        public MyViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_news_base_view);
            tv_title = getView(R.id.tv_title);
            tv_time = getView(R.id.tv_time);
            iv_logo = getView(R.id.iv_logo);
        }

        @Override
        public void setData(ListNewsData data) {
            super.setData(data);
            tv_title.setText(data.getTitle());
            tv_time.setText(AppTimeUtils.date2String(new Date()));
            iv_logo.setBackgroundResource(data.getImage());
        }
    }
}

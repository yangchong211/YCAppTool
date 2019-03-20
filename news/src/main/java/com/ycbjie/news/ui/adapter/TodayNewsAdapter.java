package com.ycbjie.news.ui.adapter;

import android.app.Activity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ycbjie.news.R;
import com.ycbjie.news.model.TodayNewsDetail;
import com.ycbjie.library.utils.image.ImageUtils;

import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter;
import org.yczbj.ycrefreshviewlib.viewHolder.BaseViewHolder;


/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/8/31
 * 描    述：头条新闻页面适配器
 * 修订历史：
 * ================================================
 */
public class TodayNewsAdapter extends RecyclerArrayAdapter<TodayNewsDetail.ResultBean.ListBean> {

    public TodayNewsAdapter(Activity activity) {
        super(activity);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ExpressDeliveryViewHolder(parent);
    }


    private class ExpressDeliveryViewHolder extends BaseViewHolder<TodayNewsDetail.ResultBean.ListBean> {

        ImageView iv_img;
        TextView tv_title , tv_time , tv_author;

        ExpressDeliveryViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_news_list);
            tv_title = $(R.id.tv_title);
            tv_time = $(R.id.tv_time);
            tv_author = $(R.id.tv_author);
            iv_img = $(R.id.iv_img);
        }

        @Override
        public void setData(TodayNewsDetail.ResultBean.ListBean data) {
            super.setData(data);
            tv_title.setText(data.getTitle());
            tv_author.setText(data.getSrc());
            tv_time.setText(data.getTime());
            ImageUtils.loadImgByPicasso(getContext(),data.getPic(),R.drawable.bg_frame_deep_gray,iv_img);
        }
    }
}

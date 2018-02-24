package com.ns.yc.lifehelper.ui.other.douban.douMovie.view.adapter;

import android.app.Activity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.ui.other.douban.douMovie.bean.DouHotMovieBean;
import com.ns.yc.lifehelper.utils.image.ImageUtils;

import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter;
import org.yczbj.ycrefreshviewlib.viewHolder.BaseViewHolder;

/**
 * Created by PC on 2017/8/21.
 * 作者：PC
 */

public class MovieTopAdapter extends RecyclerArrayAdapter<DouHotMovieBean.SubjectsBean> {

    public MovieTopAdapter(Activity activity) {
        super(activity);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ExpressDeliveryViewHolder(parent);
    }


    private class ExpressDeliveryViewHolder extends BaseViewHolder<DouHotMovieBean.SubjectsBean> {

        ImageView iv_top_photo;
        TextView tv_name , tv_rate;

        ExpressDeliveryViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_movie_top_list);
            iv_top_photo = $(R.id.iv_top_photo);
            tv_name = $(R.id.tv_name);
            tv_rate = $(R.id.tv_rate);
        }

        @Override
        public void setData(DouHotMovieBean.SubjectsBean data) {
            super.setData(data);
            ImageUtils.loadImgByPicasso(getContext(),data.getImages().getLarge(),iv_top_photo);
            tv_name.setText(data.getTitle());
            tv_rate.setText("评分："+ data.getRating().getAverage());

        }
    }
}

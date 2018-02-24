package com.ns.yc.lifehelper.ui.other.douban.douBook.view.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.ui.other.douban.douBook.model.DouBookBean;
import com.ns.yc.lifehelper.utils.image.ImageUtils;

import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter;
import org.yczbj.ycrefreshviewlib.viewHolder.BaseViewHolder;


/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/8/22
 * 描    述：豆瓣电影评分榜适配器
 * 修订历史：
 * ================================================
 */
public class DouBookAdapter extends RecyclerArrayAdapter<DouBookBean.BooksBean> {

    public DouBookAdapter(Activity activity) {
        super(activity);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ExpressDeliveryViewHolder(parent);
    }


    private class ExpressDeliveryViewHolder extends BaseViewHolder<DouBookBean.BooksBean> {

        ImageView iv_top_photo;
        TextView tv_name , tv_rate;

        ExpressDeliveryViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_book_dou_list);
            iv_top_photo = $(R.id.iv_top_photo);
            tv_name = $(R.id.tv_name);
            tv_rate = $(R.id.tv_rate);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void setData(DouBookBean.BooksBean data) {
            super.setData(data);
            ImageUtils.loadImgByPicasso(getContext(),data.getImages().getLarge(),iv_top_photo);
            tv_name.setText(data.getTitle());
            tv_rate.setText("评分："+ data.getRating().getAverage());
        }
    }
}

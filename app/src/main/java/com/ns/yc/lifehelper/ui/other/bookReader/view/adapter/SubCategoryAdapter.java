package com.ns.yc.lifehelper.ui.other.bookReader.view.adapter;

import android.app.Activity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.api.constantApi.ConstantZssqApi;
import com.ns.yc.lifehelper.ui.other.bookReader.bean.ReaderCategoryList;
import com.ns.yc.lifehelper.utils.image.ImageUtils;

import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter;
import org.yczbj.ycrefreshviewlib.viewHolder.BaseViewHolder;


/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：22017/9/21
 * 描    述：小说阅读器分类list页面适配器
 * 修订历史：
 * ================================================
 */
public class SubCategoryAdapter extends RecyclerArrayAdapter<ReaderCategoryList.BooksBean> {

    private Activity activity;

    public SubCategoryAdapter(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ExpressDeliveryViewHolder(parent);
    }


    private class ExpressDeliveryViewHolder extends BaseViewHolder<ReaderCategoryList.BooksBean> {

        ImageView ic_image;
        TextView tv_title , tv_author , tv_cate , tv_msg;

        ExpressDeliveryViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_reader_subject);
            ic_image = $(R.id.ic_image);
            tv_title = $(R.id.tv_title);
            tv_author = $(R.id.tv_author);
            tv_cate = $(R.id.tv_cate);
            tv_msg = $(R.id.tv_msg);
        }

        @Override
        public void setData(ReaderCategoryList.BooksBean data) {
            super.setData(data);
            ImageUtils.loadImgByPicassoWithRound(getContext(), ConstantZssqApi.IMG_BASE_URL+data.getCover(),10,R.drawable.cover_default,ic_image);
            tv_title.setText(data.getTitle());
            tv_author.setText(data.getAuthor());
            tv_cate.setText(data.getShortIntro());
            tv_msg.setText(String.format(activity.getResources().getString(R.string.category_book_msg),data.getLatelyFollower(),data.getRetentionRatio()));
        }
    }
}

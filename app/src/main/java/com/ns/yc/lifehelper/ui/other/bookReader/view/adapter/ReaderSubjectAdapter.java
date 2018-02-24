package com.ns.yc.lifehelper.ui.other.bookReader.view.adapter;

import android.app.Activity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.ui.other.bookReader.bean.ReaderSubjectBean;
import com.ns.yc.lifehelper.utils.image.ImageUtils;

import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter;
import org.yczbj.ycrefreshviewlib.viewHolder.BaseViewHolder;


/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/9/18
 * 描    述：小说阅读器主题书单适配器
 * 修订历史：
 * ================================================
 */
public class ReaderSubjectAdapter extends RecyclerArrayAdapter<ReaderSubjectBean.BookListsBean> {

    private Activity activity;

    public ReaderSubjectAdapter(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ExpressDeliveryViewHolder(parent);
    }


    private class ExpressDeliveryViewHolder extends BaseViewHolder<ReaderSubjectBean.BookListsBean> {

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
        public void setData(ReaderSubjectBean.BookListsBean data) {
            super.setData(data);
            ImageUtils.loadImgByPicassoWithRound(getContext(),data.getCover(),10,R.drawable.cover_default,ic_image);
            tv_title.setText(data.getTitle());
            tv_author.setText(data.getAuthor());
            tv_cate.setText(data.getDesc());
            tv_msg.setText(String.format(activity.getResources().getString(R.string.subject_book_msg),data.getBookCount(),data.getCollectorCount()));
        }
    }
}

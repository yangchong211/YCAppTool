
package com.ns.yc.lifehelper.ui.other.bookReader.view.adapter;

import android.app.Activity;
import android.view.View;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.api.constantApi.ConstantZssqApi;
import com.ns.yc.lifehelper.inter.listener.OnRvItemClickListener;
import com.ns.yc.lifehelper.ui.other.bookReader.bean.ReaderDetailReviews;
import com.ns.yc.lifehelper.weight.XLHRatingBar;

import java.util.List;

public class HotReviewAdapter extends EasyRVAdapter<ReaderDetailReviews.ReviewsBean> {

    private OnRvItemClickListener itemClickListener;

    public HotReviewAdapter(Activity context, List<ReaderDetailReviews.ReviewsBean> list, OnRvItemClickListener listener) {
        super(context, list, R.layout.item_book_detai_hot_review_list);
        this.itemClickListener = listener;
    }

    @Override
    protected void onBindData(final EasyRVHolder holder, final int position, final ReaderDetailReviews.ReviewsBean item) {
        holder.setCircleImageUrl(R.id.ivBookCover, ConstantZssqApi.IMG_BASE_URL + item.getAuthor().getAvatar(), R.drawable.avatar_default)
                .setText(R.id.tvBookTitle, item.getAuthor().getNickname())
                .setText(R.id.tvBookType, String.format(mContext.getString(R.string.book_detail_user_lv), item.getAuthor().getLv()))
                .setText(R.id.tvTitle, item.getTitle())
                .setText(R.id.tvContent, String.valueOf(item.getContent()))
                .setText(R.id.tvHelpfulYes, String.valueOf(item.getHelpful().getYes()));
        XLHRatingBar ratingBar = holder.getView(R.id.rating);
        ratingBar.setCountSelected(item.getRating());
        holder.setOnItemViewClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onItemClick(holder.getItemView(), position, item);
            }
        });
    }

}
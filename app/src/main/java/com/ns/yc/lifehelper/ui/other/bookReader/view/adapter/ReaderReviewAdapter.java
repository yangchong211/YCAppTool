package com.ns.yc.lifehelper.ui.other.bookReader.view.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.comment.ConstantBookReader;
import com.ns.yc.lifehelper.api.constantApi.ConstantZssqApi;
import com.ns.yc.lifehelper.ui.other.bookReader.bean.ReaderBookReviewList;
import com.ns.yc.lifehelper.ui.other.bookReader.manager.SettingManager;
import com.ns.yc.lifehelper.utils.time.FormatUtils;
import com.ns.yc.lifehelper.utils.image.ImageUtils;

import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter;
import org.yczbj.ycrefreshviewlib.viewHolder.BaseViewHolder;


/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/9/28
 * 描    述：书评区适配器
 * 修订历史：
 * ================================================
 */
public class ReaderReviewAdapter extends RecyclerArrayAdapter<ReaderBookReviewList.ReviewsBean> {

    private Activity activity;

    public ReaderReviewAdapter(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ExpressDeliveryViewHolder(parent);
    }


    private class ExpressDeliveryViewHolder extends BaseViewHolder<ReaderBookReviewList.ReviewsBean> {

        ImageView ivBookCover;
        TextView tvBookTitle , tvBookType , tvTime , tvHot ,tvDistillate ,tvTitle ,tvHelpfulYes;

        ExpressDeliveryViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_community_book_review_list);
            ivBookCover = $(R.id.ivBookCover);
            tvBookTitle = $(R.id.tvBookTitle);
            tvBookType = $(R.id.tvBookType);
            tvTime = $(R.id.tvTime);
            tvHot = $(R.id.tvHot);
            tvDistillate = $(R.id.tvDistillate);
            tvTitle = $(R.id.tvTitle);
            tvHelpfulYes = $(R.id.tvHelpfulYes);
        }

        @Override
        public void setData(ReaderBookReviewList.ReviewsBean data) {
            super.setData(data);
            if (!SettingManager.getInstance().isNoneCover()) {
                ImageUtils.loadImgByPicassoPerson(getContext(), ConstantZssqApi.IMG_BASE_URL + data.getBook().getCover(),R.drawable.avatar_default,ivBookCover);

            } else {
                ivBookCover.setImageResource(R.drawable.cover_default);
            }

            tvBookTitle.setText(data.getBook().getTitle());
            tvBookType.setText(String.format(getContext().getString(R.string.book_review_book_type), ConstantBookReader.bookType.get(data.getBook().getType())));
            tvTitle.setText(data.getTitle());
            tvHelpfulYes.setText(String.format(getContext().getString(R.string.book_review_helpful_yes), data.getHelpful().getYes()));


            if (TextUtils.equals(data.getState(), "hot")) {
                tvHot.setVisibility(View.VISIBLE);
                tvTime.setVisibility(View.GONE);
                tvDistillate.setVisibility(View.GONE);
            } else if (TextUtils.equals(data.getState(), "distillate")) {
                tvHot.setVisibility(View.GONE);
                tvTime.setVisibility(View.GONE);
                tvDistillate.setVisibility(View.VISIBLE);
            } else {
                tvHot.setVisibility(View.VISIBLE);
                tvTime.setVisibility(View.GONE);
                tvDistillate.setVisibility(View.GONE);
                tvTime.setText(FormatUtils.getDescriptionTimeFromDateString(data.getCreated()));
            }
        }

    }
}

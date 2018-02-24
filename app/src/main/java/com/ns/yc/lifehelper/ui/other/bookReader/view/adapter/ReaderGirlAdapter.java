package com.ns.yc.lifehelper.ui.other.bookReader.view.adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.api.constantApi.ConstantZssqApi;
import com.ns.yc.lifehelper.ui.other.bookReader.bean.ReaderBookDiscussionList;
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
 * 描    述：女生爱好区适配器
 * 修订历史：
 * ================================================
 */
public class ReaderGirlAdapter extends RecyclerArrayAdapter<ReaderBookDiscussionList.PostsBean> {

    private Activity activity;

    public ReaderGirlAdapter(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ExpressDeliveryViewHolder(parent);
    }


    private class ExpressDeliveryViewHolder extends BaseViewHolder<ReaderBookDiscussionList.PostsBean> {

        ImageView ivBookCover;
        TextView tvBookTitle , tvBookType , tvTime , tvHot ,tvDistillate ,tvTitle ,tvHelpfulYes,tvLikeCount;

        ExpressDeliveryViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_book_discussion_list);
            ivBookCover = $(R.id.ivBookCover);
            tvBookTitle = $(R.id.tvBookTitle);
            tvBookType = $(R.id.tvBookType);
            tvTime = $(R.id.tvTime);
            tvHot = $(R.id.tvHot);
            tvDistillate = $(R.id.tvDistillate);
            tvTitle = $(R.id.tvTitle);
            tvHelpfulYes = $(R.id.tvHelpfulYes);
            tvLikeCount = $(R.id.tvLikeCount);
        }

        @Override
        public void setData(ReaderBookDiscussionList.PostsBean data) {
            super.setData(data);
            if (!SettingManager.getInstance().isNoneCover()) {
                ImageUtils.loadImgByPicassoPerson(getContext(),ConstantZssqApi.IMG_BASE_URL + data.getAuthor().getAvatar(),R.drawable.avatar_default,ivBookCover);
            } else {
                ivBookCover.setImageResource(R.drawable.avatar_default);
            }
            tvBookTitle.setText(data.getAuthor().getNickname());
            tvBookType.setText(String.format(getContext().getString(R.string.book_detail_user_lv), data.getAuthor().getLv()));
            tvTitle.setText(data.getTitle());
            tvHelpfulYes.setText(data.getCommentCount()+"");
            tvLikeCount.setText(data.getLikeCount()+"");

            try {

                if (data.getType().equals("vote")) {
                    Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_notif_vote);
                    drawable.setBounds(0, 0, SizeUtils.dp2px(15), SizeUtils.dp2px(15));
                    tvHelpfulYes.setCompoundDrawables(drawable, null, null, null);
                } else {
                    Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_notif_post);
                    drawable.setBounds(0, 0, SizeUtils.dp2px(15), SizeUtils.dp2px(15));
                    tvHelpfulYes.setCompoundDrawables(drawable, null, null, null);
                }

                if (TextUtils.equals(data.getState(), "hot")) {
                    tvHot.setVisibility(View.VISIBLE);
                    tvTime.setVisibility(View.GONE);
                    tvDistillate.setVisibility(View.GONE);
                } else if (TextUtils.equals(data.getState(), "distillate")) {
                    tvDistillate.setVisibility(View.VISIBLE);
                    tvHot.setVisibility(View.GONE);
                    tvTime.setVisibility(View.GONE);
                } else {
                    tvTime.setVisibility(View.VISIBLE);
                    tvHot.setVisibility(View.GONE);
                    tvDistillate.setVisibility(View.GONE);
                    tvTime.setText(FormatUtils.getDescriptionTimeFromDateString(data.getCreated()));
                }
            } catch (Exception e) {

            }
        }

    }
}

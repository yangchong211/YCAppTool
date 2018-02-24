package com.ns.yc.lifehelper.ui.other.bookReader.view.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.api.constantApi.ConstantZssqApi;
import com.ns.yc.lifehelper.ui.other.bookReader.bean.DetailDiscussionComment;
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
 * 描    述：综合讨论区详情页面适配器
 * 修订历史：
 * ================================================
 */
public class BookDetailDiscussionAdapter extends RecyclerArrayAdapter<DetailDiscussionComment.CommentsBean> {

    private Activity activity;

    public BookDetailDiscussionAdapter(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ExpressDeliveryViewHolder(parent);
    }


    private class ExpressDeliveryViewHolder extends BaseViewHolder<DetailDiscussionComment.CommentsBean> {

        ImageView ivBookCover;
        TextView tvFloor , tvBookTitle , tvBookType , tvTime ,tvContent ,tvReplyNickName ,tvReplyFloor;

        ExpressDeliveryViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_detail_discussion_list);
            ivBookCover = $(R.id.ivBookCover);
            tvFloor = $(R.id.tvFloor);
            tvBookTitle = $(R.id.tvBookTitle);
            tvBookType = $(R.id.tvBookType);
            tvTime = $(R.id.tvTime);
            tvContent = $(R.id.tvContent);
            tvReplyNickName = $(R.id.tvReplyNickName);
            tvReplyFloor = $(R.id.tvReplyFloor);
        }

        @Override
        public void setData(DetailDiscussionComment.CommentsBean data) {
            super.setData(data);

            if (!SettingManager.getInstance().isNoneCover()) {
                ImageUtils.loadImgByPicassoPerson(activity, ConstantZssqApi.IMG_BASE_URL+data.getAuthor().getAvatar(),R.drawable.avatar_default,ivBookCover);
            } else {
                ivBookCover.setImageResource(R.drawable.avatar_default);
            }

            tvBookTitle.setText(data.getAuthor().getNickname());
            tvContent.setText(data.getContent());
            tvBookType.setText(String.format(activity.getString(R.string.book_detail_user_lv), data.getAuthor().getLv()));
            tvFloor.setText(String.format(activity.getString(R.string.comment_floor), data.getFloor()));
            tvTime.setText(FormatUtils.getDescriptionTimeFromDateString(data.getCreated()));

            if (data.getReplyTo() == null) {
                tvReplyNickName.setVisibility(View.GONE);
                tvReplyFloor.setVisibility(View.GONE);
            } else {
                tvReplyNickName.setVisibility(View.VISIBLE);
                tvReplyFloor.setVisibility(View.VISIBLE);
                tvReplyFloor.setText(String.format(activity.getString(R.string.comment_reply_floor), data.getReplyTo().floor));
                tvReplyNickName.setText(String.format(activity.getString(R.string.comment_reply_nickname), data.getReplyTo().author.nickname));
            }
        }

    }
}

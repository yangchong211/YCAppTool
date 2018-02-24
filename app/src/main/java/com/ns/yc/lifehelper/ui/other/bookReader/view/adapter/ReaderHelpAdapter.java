package com.ns.yc.lifehelper.ui.other.bookReader.view.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.api.constantApi.ConstantZssqApi;
import com.ns.yc.lifehelper.ui.other.bookReader.bean.ReaderBookHelpList;
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
 * 描    述：书荒互助区适配器
 * 修订历史：
 * ================================================
 */
public class ReaderHelpAdapter extends RecyclerArrayAdapter<ReaderBookHelpList.HelpsBean> {

    private Activity activity;

    public ReaderHelpAdapter(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ExpressDeliveryViewHolder(parent);
    }


    private class ExpressDeliveryViewHolder extends BaseViewHolder<ReaderBookHelpList.HelpsBean> {

        ImageView ivBookCover;
        TextView tvBookTitle , tvBookType , tvTime , tvHot ,tvDistillate ,tvTitle ,tvHelpfulYes;

        ExpressDeliveryViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_community_book_help_list);
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
        public void setData(ReaderBookHelpList.HelpsBean data) {
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

        }

    }
}

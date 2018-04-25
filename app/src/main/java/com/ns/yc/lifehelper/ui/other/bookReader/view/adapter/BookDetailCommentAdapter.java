package com.ns.yc.lifehelper.ui.other.bookReader.view.adapter;

import android.content.Context;
import android.view.View;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.api.constantApi.ConstantZssqApi;
import com.ns.yc.lifehelper.inter.listener.OnRvItemClickListener;
import com.ns.yc.lifehelper.ui.other.bookReader.bean.DetailDiscussionComment;

import java.util.List;


public class BookDetailCommentAdapter extends EasyRVAdapter<DetailDiscussionComment.CommentsBean> {

    private OnRvItemClickListener listener;

    public BookDetailCommentAdapter(Context context, List<DetailDiscussionComment.CommentsBean> list) {
        super(context, list, R.layout.item_comment_best_list);
    }

    @Override
    protected void onBindData(final EasyRVHolder viewHolder, final int position, final DetailDiscussionComment.CommentsBean item) {
        viewHolder.setCircleImageUrl(R.id.ivBookCover, ConstantZssqApi.IMG_BASE_URL + item.getAuthor().getAvatar(), R.drawable.avatar_default)
                .setText(R.id.tvBookTitle, item.getAuthor().getNickname())
                .setText(R.id.tvContent, item.getContent())
                .setText(R.id.tvBookType, String.format(mContext.getString(R.string.book_detail_user_lv), item.getAuthor().getLv()))
                .setText(R.id.tvFloor, String.format(mContext.getString(R.string.comment_floor), item.getFloor()))
                .setText(R.id.tvLikeCount, String.format(mContext.getString(R.string.comment_like_count), item.getLikeCount()));

        viewHolder.setOnItemViewClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null)
                    listener.onItemClick(viewHolder.getItemView(), position, item);
            }
        });
    }

    public void setOnItemClickListener(OnRvItemClickListener listener){
        this.listener = listener;
    }
}

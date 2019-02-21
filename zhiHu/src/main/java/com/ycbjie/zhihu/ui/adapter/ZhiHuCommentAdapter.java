package com.ycbjie.zhihu.ui.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.TimeUtils;
import com.ycbjie.zhihu.R;
import com.ycbjie.zhihu.model.ZhiHuCommentBean;
import com.ycbjie.library.utils.image.ImageUtils;
import java.util.List;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/04/21
 *     desc  : 评论
 *     revise:
 * </pre>
 */
public class ZhiHuCommentAdapter extends RecyclerView.Adapter<ZhiHuCommentAdapter.ViewHolder>{

    private LayoutInflater inflater;
    private List<ZhiHuCommentBean.CommentsBean> mList;
    private Context mContext;

    private static final int STATE_NULL = 0;    //未知
    private static final int STATE_NONE = 1;    //无需展开
    private static final int STATE_EXPAND = 2;  //已展开
    private static final int STATE_SHRINK = 3;  //已收缩
    private static final int MAX_LINE = 2;      //起始最多显示2行

    public ZhiHuCommentAdapter(Context mContext, List<ZhiHuCommentBean.CommentsBean> mList) {
        this.mList = mList;
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_zh_comment,parent,false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        ZhiHuCommentBean.CommentsBean info = mList.get(position);
        ImageUtils.loadImgByPicassoPerson(mContext,info.getAvatar(),R.drawable.avatar_default,holder.ivFace);
        holder.tvName.setText(info.getAuthor());
        holder.tvContent.setText(info.getContent());
        holder.tvTime.setText(TimeUtils.getFriendlyTimeSpanByNow(info.getTime()));
        holder.tvLike.setText(String.valueOf(info.getLikes()));
        if (info.getReply_to() != null && info.getReply_to().getId() != 0) {
            holder.tvReply.setVisibility(View.VISIBLE);
            SpannableString ss = new SpannableString("@" + info.getReply_to().getAuthor() + ": " + info.getReply_to().getContent());
            ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext,R.color.blackText4)), 0,info.getReply_to().getAuthor().length() + 2 , Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            //holder.tvReply.setText(String.format("@%s: %s",info.getReply_to().getAuthor(),info.getReply_to().getContent()));
            holder.tvReply.setText(ss);
            if (info.getReply_to().getExpandState() == STATE_NULL) {    //未知
                holder.tvReply.post(new Runnable() {
                    @Override
                    public void run() {
                        if (holder.tvReply.getLineCount() > MAX_LINE) {
                            holder.tvReply.setMaxLines(MAX_LINE);
                            holder.tvExpand.setVisibility(View.VISIBLE);
                            holder.tvExpand.setText("展开");
                            mList.get(holder.getAdapterPosition()).getReply_to().setExpandState(STATE_SHRINK);
                            holder.tvExpand.setOnClickListener(new OnStateClickListener(holder.getAdapterPosition(), holder.tvReply));
                        } else {
                            holder.tvExpand.setVisibility(View.GONE);
                            mList.get(holder.getAdapterPosition()).getReply_to().setExpandState(STATE_NONE);
                        }
                    }
                });
            } else if(info.getReply_to().getExpandState() == STATE_NONE) {  //无需展开
                holder.tvExpand.setVisibility(View.GONE);
            } else if(info.getReply_to().getExpandState() == STATE_EXPAND) {    //已展开
                holder.tvReply.setMaxLines(Integer.MAX_VALUE);
                holder.tvExpand.setText("收起");
                holder.tvExpand.setVisibility(View.VISIBLE);
                holder.tvExpand.setOnClickListener(new OnStateClickListener(holder.getAdapterPosition(), holder.tvReply));
            } else {    //已收缩
                holder.tvReply.setMaxLines(MAX_LINE);
                holder.tvExpand.setText("展开");
                holder.tvExpand.setVisibility(View.VISIBLE);
                holder.tvExpand.setOnClickListener(new OnStateClickListener(holder.getAdapterPosition(), holder.tvReply));
            }
        } else {
            holder.tvReply.setVisibility(View.GONE);
            holder.tvExpand.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    private class OnStateClickListener implements View.OnClickListener {

        TextView replyView;
        int position;

        public OnStateClickListener(int position,TextView replyView) {
            this.position = position;
            this.replyView = replyView;
        }

        @Override
        public void onClick(View view) {
            TextView tv = (TextView) view;
            if (mList.get(position).getReply_to().getExpandState() == STATE_SHRINK) {
                tv.setText("收起");
                replyView.setMaxLines(Integer.MAX_VALUE);
                replyView.setEllipsize(null);
                mList.get(position).getReply_to().setExpandState(STATE_EXPAND);
            } else {
                tv.setText("展开");
                replyView.setMaxLines(MAX_LINE);
                replyView.setEllipsize(TextUtils.TruncateAt.END);
                mList.get(position).getReply_to().setExpandState(STATE_SHRINK);
            }
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView ivFace;
        TextView tvName;
        TextView tvContent;
        TextView tvTime;
        TextView tvExpand;
        TextView tvLike;
        TextView tvReply;

        public ViewHolder(View itemView) {
            super(itemView);
            ivFace = itemView.findViewById(R.id.iv_comment_face);
            tvName = itemView.findViewById(R.id.tv_comment_name);
            tvContent = itemView.findViewById(R.id.tv_comment_content);
            tvTime = itemView.findViewById(R.id.tv_comment_time);
            tvExpand = itemView.findViewById(R.id.tv_comment_expand);
            tvLike = itemView.findViewById(R.id.tv_comment_like);
            tvReply = itemView.findViewById(R.id.tv_comment_reply);
        }
    }

}

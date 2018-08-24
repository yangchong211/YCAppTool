package com.ns.yc.lifehelper.ui.other.vtex.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.inter.listener.OnListItemClickListener;
import com.ns.yc.lifehelper.ui.other.vtex.model.bean.NodeBean;
import com.ns.yc.lifehelper.ui.other.vtex.model.bean.NodeListBean;
import com.ns.yc.lifehelper.ui.other.vtex.presenter.WTexPagerPresenter;
import com.ns.yc.lifehelper.utils.image.ImageUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class WTNodeListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private LayoutInflater inflater;
    private List<NodeListBean> mList;
    private NodeBean mTopBean;
    private OnListItemClickListener mItemClickListener;

    public WTNodeListAdapter(Context context, List<NodeListBean> mList) {
        this.mContext = context;
        this.mList = mList;
        inflater = LayoutInflater.from(mContext);
    }

    public NodeListBean get(int i) {
        if(mList!=null){
            return mList.get(i);
        }
        return null;
    }

    private enum ITEM_TYPE {
        ITEM_TOP,           //节点信息
        ITEM_CONTENT,       //节点列表
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ITEM_TYPE.ITEM_TOP.ordinal();
        }
        return ITEM_TYPE.ITEM_CONTENT.ordinal();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE.ITEM_TOP.ordinal()) {
            return new TopViewHolder(inflater.inflate(R.layout.item_wtex_node_top, parent, false));
        }
        return new ViewHolder(inflater.inflate(R.layout.item_wtex_news, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof TopViewHolder) {
            TopViewHolder topHolder = ((TopViewHolder) holder);
            if (mTopBean == null)
                return;
            ImageUtils.loadImgByPicasso(mContext, WTexPagerPresenter.parseImg(mTopBean.getavatar_normal()),R.drawable.image_default,topHolder.ivNodeFace);
            topHolder.tvNodeContent.setText(mTopBean.getHeader());
            topHolder.tvNodesTips.setText(String.format("%d个主题\n%d次收藏", mTopBean.getTopics(), mTopBean.getStars()));
            topHolder.tvNodeName.setText(mTopBean.getTitle());
        } else {
            ViewHolder contentHolder = ((ViewHolder) holder);
            NodeListBean bean = mList.get(position - 1);
            if (bean == null)
                return;
            ImageUtils.loadImgByPicasso(mContext, WTexPagerPresenter.parseImg(bean.getMember().getavatar_normal()),R.drawable.image_default,contentHolder.ivTopicFace);
            contentHolder.tvTopicName.setText(bean.getMember().getUsername());
            contentHolder.tvTopicTips.setText(bean.getCreated()+"");
            contentHolder.tvTopicComment.setText(String.valueOf(bean.getReplies()));
            contentHolder.tvTopicTitle.setText(bean.getTitle());
            contentHolder.tvTopicNode.setText(bean.getNode().getTitle());
            contentHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemClickListener.onItemClick(view,position);
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        if (mTopBean == null) {
            return mList.size();
        }
        return mList.size() + 1;
    }

    public static class TopViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_node_face)
        ImageView ivNodeFace;
        @BindView(R.id.tv_node_name)
        TextView tvNodeName;
        @BindView(R.id.tv_nodes_tips)
        TextView tvNodesTips;
        @BindView(R.id.tv_node_content)
        TextView tvNodeContent;

        TopViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_topic_face)
        ImageView ivTopicFace;
        @BindView(R.id.tv_topic_name)
        TextView tvTopicName;
        @BindView(R.id.tv_topic_tips)
        TextView tvTopicTips;
        @BindView(R.id.tv_topic_comment)
        TextView tvTopicComment;
        @BindView(R.id.tv_topic_node)
        TextView tvTopicNode;
        @BindView(R.id.tv_topic_title)
        TextView tvTopicTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setTopData(NodeBean mTopBean) {
        this.mTopBean = mTopBean;
        notifyItemChanged(0);
    }

    public void setContentData(List<NodeListBean> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }


    public void setOnItemClickListener(OnListItemClickListener listener) {
        this.mItemClickListener = listener;
    }

}
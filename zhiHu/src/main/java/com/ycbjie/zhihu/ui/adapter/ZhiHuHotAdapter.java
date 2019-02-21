package com.ycbjie.zhihu.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ycbjie.zhihu.R;
import com.ycbjie.zhihu.model.ZhiHuHotBean;
import com.ycbjie.library.utils.image.ImageUtils;

import java.util.List;


public class ZhiHuHotAdapter extends RecyclerView.Adapter<ZhiHuHotAdapter.ViewHolder>{

    private List<ZhiHuHotBean.RecentBean> mList;
    private Context mContext;
    private LayoutInflater inflater;
    private OnItemClickListener onItemClickListener;

    public ZhiHuHotAdapter(Context mContext, List<ZhiHuHotBean.RecentBean> mList) {
        this.mList = mList;
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_zh_news_list, parent, false));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.tvTitle.setText(mList.get(position).getTitle());
        ImageUtils.loadImgByPicasso(mContext,mList.get(position).getThumbnail(),R.drawable.image_default , holder.ivLogo);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onItemClickListener != null) {
                    onItemClickListener.onItemClick(holder.getAdapterPosition(),view);
                }
            }
        });
    }

    public void setReadState(int position, boolean readState) {
        mList.get(position).setReadState(readState);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        TextView tvTime;
        ImageView ivLogo;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            ivLogo = (ImageView) itemView.findViewById(R.id.iv_logo);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, View view);
    }

}

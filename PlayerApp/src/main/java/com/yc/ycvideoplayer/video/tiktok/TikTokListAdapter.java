package com.yc.ycvideoplayer.video.tiktok;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.yc.ycvideoplayer.R;
import com.yc.video.config.VideoInfoBean;

import java.util.List;

public class TikTokListAdapter extends RecyclerView.Adapter<TikTokListAdapter.TikTokListViewHolder> {

    public List<VideoInfoBean> data;

    private int mId;

    public TikTokListAdapter(List<VideoInfoBean> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public TikTokListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tiktok_list, parent, false);
        return new TikTokListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TikTokListViewHolder holder, int position) {
        VideoInfoBean item = data.get(position);
        holder.mTitle.setText(item.getTitle());
        Glide.with(holder.mThumb.getContext())
                .load(item.getCover())
                .into(holder.mThumb);

        holder.mPosition = position;
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public void setImpl(int id) {
        mId = id;
    }

    public class TikTokListViewHolder extends RecyclerView.ViewHolder {

        public ImageView mThumb;
        public TextView mTitle;

        public int mPosition;

        public TikTokListViewHolder(@NonNull View itemView) {
            super(itemView);
            mThumb = itemView.findViewById(R.id.iv_thumb);
            mTitle = itemView.findViewById(R.id.tv_title);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TikTok2Activity.start(itemView.getContext(), mPosition);
                }
            });
        }
    }
}

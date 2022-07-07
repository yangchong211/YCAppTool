package com.yc.ycvideoplayer.music;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yc.music.model.AudioBean;
import com.yc.music.tool.CoverLoader;
import com.yc.ycvideoplayer.R;
import com.yc.ycvideoplayer.video.list.OnItemClickListener;

import java.util.List;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.VideoHolder> {

    private List<AudioBean> videos;
    private OnItemClickListener mOnItemClickListener;

    /**
     * 正在播放音乐的索引位置
     */
    private int mPlayingPosition;

    public MusicAdapter(List<AudioBean> videos) {
        this.videos = videos;
    }

    @Override
    @NonNull
    public VideoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_local_music, parent, false);
        return new VideoHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoHolder holder, int position) {

        AudioBean data = videos.get(position);
        Bitmap cover = CoverLoader.getInstance().loadThumbnail(data);
        holder.ivCover.setImageBitmap(cover);
        holder.tvTitle.setText(data.getTitle());
        String artist = getArtistAndAlbum(data.getArtist(), data.getAlbum());
        holder.tvArtist.setText(artist);
        holder.ivMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        if (position == mPlayingPosition) {
            holder.vPlaying.setVisibility(View.VISIBLE);
        } else {
            holder.vPlaying.setVisibility(View.INVISIBLE);
        }
        holder.mPosition = position;
    }

    @Override
    public int getItemCount() {
        return videos==null ? 0 : videos.size();
    }

    public void addData(List<AudioBean> videoList) {
        int size = videos.size();
        videos.addAll(videoList);
        //使用此方法添加数据，使用notifyDataSetChanged会导致正在播放的视频中断
        notifyItemRangeChanged(size, videos.size());
    }


    /**
     * 当播放位置发生了变化，那么就可以更新播放位置视图
     */
    public void updatePlayingPosition(int playingPosition) {
        mPlayingPosition = playingPosition;
    }

    public class VideoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public int mPosition;
        View vPlaying;
        ImageView ivCover;
        TextView tvTitle;
        TextView tvArtist;
        ImageView ivMore;
        View vDivider;

        VideoHolder(View itemView) {
            super(itemView);
            vPlaying = itemView.findViewById(R.id.v_playing);
            ivCover = itemView.findViewById(R.id.iv_cover);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvArtist = itemView.findViewById(R.id.tv_artist);
            ivMore = itemView.findViewById(R.id.iv_more);
            vDivider = itemView.findViewById(R.id.v_divider);
            if (mOnItemClickListener != null) {
                itemView.setOnClickListener(this);
            }
            //通过tag将ViewHolder和itemView绑定
            itemView.setTag(this);
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(mPosition);
            }
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public String getArtistAndAlbum(String artist, String album) {
        if (TextUtils.isEmpty(artist) && TextUtils.isEmpty(album)) {
            return "";
        } else if (!TextUtils.isEmpty(artist) && TextUtils.isEmpty(album)) {
            return artist;
        } else if (TextUtils.isEmpty(artist) && !TextUtils.isEmpty(album)) {
            return album;
        } else {
            return artist + " - " + album;
        }
    }
}
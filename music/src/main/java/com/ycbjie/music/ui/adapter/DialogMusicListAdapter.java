package com.ycbjie.music.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ycbjie.library.inter.listener.OnListItemClickListener;
import com.ycbjie.music.R;
import com.ycbjie.music.model.bean.AudioBean;
import com.ycbjie.music.service.PlayService;

import java.util.List;


/**
 * Created by yc on 2018/1/26.
 */

public class DialogMusicListAdapter extends RecyclerView.Adapter<DialogMusicListAdapter.ViewHolder> {

    private final LayoutInflater inflater;
    private Context context;
    private List<AudioBean> localMusics;
    /**
     * 正在播放音乐的索引位置
     */
    private int mPlayingPosition;

    public DialogMusicListAdapter(Context context, List<AudioBean> musicList) {
        this.localMusics = musicList;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_dialog_list_view, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        if(localMusics!=null && localMusics.size()>0){
            holder.tvTitle.setText(localMusics.get(position).getTitle());
            holder.tvAuthor.setText(localMusics.get(position).getArtist());
            if (position == mPlayingPosition) {
                holder.vPlaying.setVisibility(View.VISIBLE);
            } else {
                holder.vPlaying.setVisibility(View.GONE);
            }
        }
        //条目点击事件
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(view, position);
                }
            }
        });
        holder.ivDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return localMusics.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        View vPlaying;
        ImageView ivDel;
        TextView tvTitle;
        TextView tvAuthor;

        ViewHolder(View itemView) {
            super(itemView);
            vPlaying = itemView.findViewById(R.id.v_playing);
            ivDel = itemView.findViewById(R.id.iv_del);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvAuthor = itemView.findViewById(R.id.tv_author);
        }
    }

    private OnListItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnListItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * 当播放位置发生了变化，那么就可以更新播放位置视图
     * @param playService       PlayService
     */
    public void updatePlayingPosition(PlayService playService) {
        if (playService.getPlayingMusic() != null &&
                playService.getPlayingMusic().getType() == AudioBean.Type.LOCAL) {
            mPlayingPosition = playService.getPlayingPosition();
        } else {
            mPlayingPosition = -1;
        }
    }


}

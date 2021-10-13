package com.ycbjie.music.ui.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ycbjie.music.R;
import com.ycbjie.music.inter.OnMoreClickListener;
import com.ycbjie.music.model.bean.AudioBean;
import com.ycbjie.music.service.PlayService;
import com.ycbjie.music.utils.CoverLoader;
import com.ycbjie.music.utils.FileMusicUtils;

import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter;
import org.yczbj.ycrefreshviewlib.viewHolder.BaseViewHolder;


public class LocalMusicAdapter extends RecyclerArrayAdapter<AudioBean> {

    /**
     * 正在播放音乐的索引位置
     */
    private int mPlayingPosition;

    public LocalMusicAdapter(Activity activity) {
        super(activity);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent);
    }

    class ViewHolder extends BaseViewHolder<AudioBean> {

        View vPlaying;
        ImageView ivCover;
        TextView tvTitle;
        TextView tvArtist;
        ImageView ivMore;
        View vDivider;

        ViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_local_music);
            vPlaying = getView(R.id.v_playing);
            ivCover = getView(R.id.iv_cover);
            tvTitle = getView(R.id.tv_title);
            tvArtist = getView(R.id.tv_artist);
            ivMore = getView(R.id.iv_more);
            vDivider = getView(R.id.v_divider);
        }

        @Override
        public void setData(AudioBean data) {
            super.setData(data);
            if(data!=null){
                Bitmap cover = CoverLoader.getInstance().loadThumbnail(data);
                ivCover.setImageBitmap(cover);
                tvTitle.setText(data.getTitle());
                String artist = FileMusicUtils.getArtistAndAlbum(data.getArtist(), data.getAlbum());
                tvArtist.setText(artist);
                ivMore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListener != null) {
                            mListener.onMoreClick(getAdapterPosition());
                        }
                    }
                });
                vDivider.setVisibility(isShowDivider(getAdapterPosition()) ? View.VISIBLE : View.GONE);
                if (getAdapterPosition() == mPlayingPosition) {
                    vPlaying.setVisibility(View.VISIBLE);
                } else {
                    vPlaying.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    private boolean isShowDivider(int position) {
        return position != getAllData().size() - 1;
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


    private OnMoreClickListener mListener;
    public void setOnMoreClickListener(OnMoreClickListener listener) {
        mListener = listener;
    }

}

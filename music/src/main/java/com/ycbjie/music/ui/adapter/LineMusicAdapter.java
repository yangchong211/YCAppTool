package com.ycbjie.music.ui.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ycbjie.library.base.glide.GlideApp;
import com.ycbjie.music.R;
import com.ycbjie.music.inter.OnMoreClickListener;
import com.ycbjie.music.model.bean.OnlineMusicList;
import com.ycbjie.music.utils.FileMusicUtils;

import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter;
import org.yczbj.ycrefreshviewlib.viewHolder.BaseViewHolder;



public class LineMusicAdapter extends RecyclerArrayAdapter<OnlineMusicList.SongListBean> {

    public LineMusicAdapter(Activity activity) {
        super(activity);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent);
    }


    private class ViewHolder extends BaseViewHolder<OnlineMusicList.SongListBean> {

        View v_playing , v_divider;
        TextView tv_title , tv_artist ;
        ImageView iv_cover , iv_more;

        ViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_line_music);
            v_playing = $(R.id.v_playing);
            iv_cover = $(R.id.iv_cover);
            tv_title = $(R.id.tv_title);
            tv_artist = $(R.id.tv_artist);
            iv_more = $(R.id.iv_more);
            v_divider = $(R.id.v_divider);

            iv_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onMoreClick(getAdapterPosition());
                }
            });
        }

        @Override
        public void setData(OnlineMusicList.SongListBean data) {
            super.setData(data);
            GlideApp.with(getContext())
                    .load(data.getPic_small())
                    .placeholder(R.drawable.default_cover)
                    .error(R.drawable.default_cover)
                    .into(iv_cover);
            tv_title.setText(data.getTitle());
            String artist = FileMusicUtils.getArtistAndAlbum(data.getArtist_name(), data.getAlbum_title());
            tv_artist.setText(artist);
            v_divider.setVisibility(isShowDivider(getAdapterPosition()) ? View.VISIBLE : View.GONE);
        }


        private boolean isShowDivider(int position) {
            return position != getAllData().size() - 1;
        }

    }

    private OnMoreClickListener mListener;
    public void setOnMoreClickListener(OnMoreClickListener listener) {
        mListener = listener;
    }


}

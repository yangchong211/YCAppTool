package com.ycbjie.music.ui.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ycbjie.library.base.glide.GlideApp;
import com.ycbjie.music.R;
import com.ycbjie.music.api.OnLineMusicModel;
import com.ycbjie.music.model.bean.OnLineSongListInfo;
import com.ycbjie.music.model.bean.OnlineMusicList;

import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter;
import org.yczbj.ycrefreshviewlib.viewHolder.BaseViewHolder;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class OnLineMusicAdapter extends RecyclerArrayAdapter<OnLineSongListInfo> {

    public OnLineMusicAdapter(Activity activity) {
        super(activity);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent);
    }

    public class ViewHolder extends BaseViewHolder<OnLineSongListInfo> {

        TextView tvProfile;
        LinearLayout llTitle;
        ImageView ivCover;
        TextView tvMusic1;
        TextView tvMusic2;
        TextView tvMusic3;
        View vDivider;
        FrameLayout flMusic;

        ViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_on_line_music);
            tvProfile = getView(R.id.tv_profile);
            llTitle = getView(R.id.ll_title);
            ivCover = getView(R.id.iv_cover);
            tvMusic1 = getView(R.id.tv_music_1);
            tvMusic2 = getView(R.id.tv_music_2);
            tvMusic3 = getView(R.id.tv_music_3);
            vDivider = getView(R.id.v_divider);
            flMusic = getView(R.id.fl_music);
        }

        @Override
        public void setData(OnLineSongListInfo data) {
            super.setData(data);
            if(data!=null){
                if (data.getType().contains("#")){
                    llTitle.setVisibility(View.VISIBLE);
                    flMusic.setVisibility(View.GONE);
                    tvProfile.setText(data.getTitle());
                }else {
                    llTitle.setVisibility(View.GONE);
                    flMusic.setVisibility(View.VISIBLE);
                    vDivider.setVisibility(isShowDivider(getAdapterPosition()) ? View.VISIBLE : View.GONE);
                    getMusicListInfo(data);
                }
            }
        }

        private boolean isShowDivider(int position) {
            return position != getAllData().size() - 1;
        }

        private void getMusicListInfo(final OnLineSongListInfo data) {
            if(data.getCoverUrl()==null){
                tvMusic1.setTag(data.getTitle());
                ivCover.setImageResource(R.drawable.default_cover);
                tvMusic1.setText("1.加载中…");
                tvMusic2.setText("2.加载中…");
                tvMusic3.setText("3.加载中…");
                //开始请求网络
                OnLineMusicModel model = OnLineMusicModel.getInstance();
                model.getList(OnLineMusicModel.METHOD_LINE_MUSIC , data.getType(), 0 ,3)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<OnlineMusicList>() {
                            @Override
                            public void accept(OnlineMusicList onlineMusicList) throws Exception {
                                Log.e("OnLineMusicModel","onNext");
                                if (onlineMusicList == null || onlineMusicList.getSong_list() == null) {
                                    return;
                                }
                                if (!data.getTitle().equals(tvMusic1.getTag())) {
                                    return;
                                }
                                parse(onlineMusicList,data);
                                setMusicData(data);
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {

                            }
                        }, new Action() {
                            @Override
                            public void run() throws Exception {

                            }
                        });
            }else {
                setMusicData(data);
            }
        }

        private void setMusicData(OnLineSongListInfo data) {
            tvMusic1.setText(data.getMusic1());
            tvMusic2.setText(data.getMusic2());
            tvMusic3.setText(data.getMusic3());
            GlideApp.with(getContext())
                    .asBitmap()
                    .load(data.getCoverUrl())
                    .error(R.drawable.default_cover)
                    .into(ivCover);
        }


        private void parse(OnlineMusicList response, OnLineSongListInfo songListInfo) {
            List<OnlineMusicList.SongListBean> onlineMusics = response.getSong_list();
            songListInfo.setCoverUrl(response.getBillboard().getPic_s260());
            if (onlineMusics.size() >= 1) {
                songListInfo.setMusic1(getContext().getString(R.string.song_list_item_title_1,
                        onlineMusics.get(0).getTitle(), onlineMusics.get(0).getArtist_name()));
            } else {
                songListInfo.setMusic1("");
            }
            if (onlineMusics.size() >= 2) {
                songListInfo.setMusic2(getContext().getString(R.string.song_list_item_title_2,
                        onlineMusics.get(1).getTitle(), onlineMusics.get(1).getArtist_name()));
            } else {
                songListInfo.setMusic2("");
            }
            if (onlineMusics.size() >= 3) {
                songListInfo.setMusic3(getContext().getString(R.string.song_list_item_title_3,
                        onlineMusics.get(2).getTitle(), onlineMusics.get(2).getArtist_name()));
            } else {
                songListInfo.setMusic3("");
            }
        }

    }

}

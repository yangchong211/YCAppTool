package com.ycbjie.douban.view.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ycbjie.douban.R;
import com.ycbjie.douban.bean.DouMusicBean;
import com.ycbjie.library.utils.image.ImageUtils;

import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter;
import org.yczbj.ycrefreshviewlib.viewHolder.BaseViewHolder;

/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/8/22
 *     desc  : 豆瓣音乐适配器
 *     revise:
 * </pre>
 */
public class DouMusicAdapter extends RecyclerArrayAdapter<DouMusicBean.MusicsBean> {

    public DouMusicAdapter(Activity activity) {
        super(activity);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ExpressDeliveryViewHolder(parent);
    }


    private class ExpressDeliveryViewHolder extends BaseViewHolder<DouMusicBean.MusicsBean> {

        ImageView iv_music_photo;
        TextView tv_music_title , tv_music_directors ,tv_music_casts,tv_music_rating_rate ;
        View view_color;

        ExpressDeliveryViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_music_dou_list);
            tv_music_title = $(R.id.tv_music_title);
            tv_music_directors = $(R.id.tv_music_directors);
            tv_music_casts = $(R.id.tv_music_casts);
            tv_music_rating_rate = $(R.id.tv_music_rating_rate);
            iv_music_photo = $(R.id.iv_music_photo);
            view_color = $(R.id.view_color);
        }

        @Override
        public void setData(DouMusicBean.MusicsBean data) {
            super.setData(data);
            tv_music_title.setText(data.getTitle());
            if(data.getAuthor()!=null && data.getAuthor().size()>0){
                tv_music_directors.setText(data.getAuthor().get(0).getName());
            } else {
                tv_music_directors.setText("暂无");
            }

            StringBuilder sb = new StringBuilder();
            if(data.getTags()!=null){
                if(data.getTags().size()>4){
                    for(int a=0 ; a<4 ; a++){
                        sb.append(data.getTags().get(a).getName());
                        sb.append("/");
                    }
                }else {
                    for(int a=0 ; a<data.getTags().size() ; a++){
                        sb.append(data.getTags().get(a).getName());
                        sb.append("/");
                    }
                }
                tv_music_casts.setText(sb.toString());
            }
            tv_music_rating_rate.setText("评分：" +data.getRating().getAverage());
            ImageUtils.loadImgByPicasso(getContext(),data.getImage(),iv_music_photo);
        }
    }
}

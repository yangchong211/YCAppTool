package com.ycbjie.douban.view.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ycbjie.douban.R;
import com.ycbjie.douban.bean.DouHotMovieBean;
import com.ycbjie.library.utils.image.ImageUtils;


import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter;
import org.yczbj.ycrefreshviewlib.viewHolder.BaseViewHolder;

import java.util.List;

/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/8/22
 *     desc  : 豆瓣电影评分榜适配器
 *     revise:
 * </pre>
 */
public class DouMovieAdapter extends RecyclerArrayAdapter<DouHotMovieBean.SubjectsBean> {

    public DouMovieAdapter(Activity activity) {
        super(activity);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ExpressDeliveryViewHolder(parent);
    }


    private class ExpressDeliveryViewHolder extends BaseViewHolder<DouHotMovieBean.SubjectsBean> {

        ImageView iv_movie_photo;
        TextView tv_movie_title , tv_movie_directors ,tv_movie_casts,tv_movie_genres ,tv_movie_rating_rate;
        View view_color;

        ExpressDeliveryViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_movie_dou_list);
            iv_movie_photo = getView(R.id.iv_movie_photo);
            tv_movie_title = getView(R.id.tv_movie_title);
            tv_movie_directors = getView(R.id.tv_movie_directors);
            tv_movie_casts = getView(R.id.tv_movie_casts);
            tv_movie_genres = getView(R.id.tv_movie_genres);
            tv_movie_rating_rate = getView(R.id.tv_movie_rating_rate);
            view_color = getView(R.id.view_color);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void setData(DouHotMovieBean.SubjectsBean data) {
            super.setData(data);
            if(data!=null){
                ImageUtils.loadImgByPicasso(getContext(),data.getImages().getLarge(),iv_movie_photo);
                tv_movie_title.setText(data.getTitle());
                if(data.getDirectors().size()>0){
                    tv_movie_directors.setText(data.getDirectors().get(0).getName());
                }
                List<DouHotMovieBean.SubjectsBean.CastsBean> casts = data.getCasts();
                if(casts.size()>0){
                    StringBuilder sb = new StringBuilder();
                    for(int a=0 ; a<casts.size() ; a++){
                        sb.append(casts.get(a).getName());
                        sb.append("/");
                    }
                    tv_movie_casts.setText(sb.toString());
                }
                if(data.getGenres().size()>0){
                    tv_movie_genres.setText("类型："+ data.getGenres().get(0));
                }
                tv_movie_rating_rate.setText("评分："+ data.getRating().getAverage());
            }
        }
    }
}

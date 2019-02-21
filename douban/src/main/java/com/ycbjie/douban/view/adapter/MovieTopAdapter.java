package com.ycbjie.douban.view.adapter;

import android.app.Activity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ycbjie.douban.R;
import com.ycbjie.douban.bean.DouHotMovieBean;
import com.ycbjie.library.utils.image.ImageUtils;

import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter;
import org.yczbj.ycrefreshviewlib.viewHolder.BaseViewHolder;


/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/8/22
 *     desc  : 豆瓣排行榜适配器
 *     revise:
 * </pre>
 */
public class MovieTopAdapter extends RecyclerArrayAdapter<DouHotMovieBean.SubjectsBean> {

    public MovieTopAdapter(Activity activity) {
        super(activity);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ExpressDeliveryViewHolder(parent);
    }


    private class ExpressDeliveryViewHolder extends BaseViewHolder<DouHotMovieBean.SubjectsBean> {

        ImageView iv_top_photo;
        TextView tv_name , tv_rate;

        ExpressDeliveryViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_movie_top_list);
            iv_top_photo = $(R.id.iv_top_photo);
            tv_name = $(R.id.tv_name);
            tv_rate = $(R.id.tv_rate);
        }

        @Override
        public void setData(DouHotMovieBean.SubjectsBean data) {
            super.setData(data);
            ImageUtils.loadImgByPicasso(getContext(),data.getImages().getLarge(),iv_top_photo);
            tv_name.setText(data.getTitle());
            tv_rate.setText("评分："+ data.getRating().getAverage());

        }
    }
}

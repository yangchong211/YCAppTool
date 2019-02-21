package com.ycbjie.douban.view.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ycbjie.douban.R;
import com.ycbjie.douban.bean.DouBookBean;
import com.ycbjie.library.utils.image.ImageUtils;

import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter;
import org.yczbj.ycrefreshviewlib.viewHolder.BaseViewHolder;



/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/8/22
 *     desc  : 豆瓣电影评分榜适配器
 *     revise:
 * </pre>
 */
public class DouBookAdapter extends RecyclerArrayAdapter<DouBookBean.BooksBean> {

    public DouBookAdapter(Activity activity) {
        super(activity);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ExpressDeliveryViewHolder(parent);
    }


    private class ExpressDeliveryViewHolder extends BaseViewHolder<DouBookBean.BooksBean> {

        ImageView iv_top_photo;
        TextView tv_name , tv_rate;

        ExpressDeliveryViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_book_dou_list);
            iv_top_photo = getView(R.id.iv_top_photo);
            tv_name = getView(R.id.tv_name);
            tv_rate = getView(R.id.tv_rate);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void setData(DouBookBean.BooksBean data) {
            super.setData(data);
            ImageUtils.loadImgByPicasso(getContext(),data.getImages().getLarge(),iv_top_photo);
            tv_name.setText(data.getTitle());
            tv_rate.setText("评分："+ data.getRating().getAverage());
        }
    }
}

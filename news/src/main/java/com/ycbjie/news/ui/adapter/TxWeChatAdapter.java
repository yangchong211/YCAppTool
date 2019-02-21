package com.ycbjie.news.ui.adapter;

import android.app.Activity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.ycbjie.library.utils.image.ImageUtils;
import com.ycbjie.news.R;
import com.ycbjie.news.model.WeChatBean;

import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter;
import org.yczbj.ycrefreshviewlib.viewHolder.BaseViewHolder;


/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/8/29
 * 描    述：天行新闻页面适配器
 * 修订历史：
 * ================================================
 */
public class TxWeChatAdapter extends RecyclerArrayAdapter<WeChatBean.NewslistBean> {

    public TxWeChatAdapter(Activity activity) {
        super(activity);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ExpressDeliveryViewHolder(parent);
    }


    private class ExpressDeliveryViewHolder extends BaseViewHolder<WeChatBean.NewslistBean> {

        ImageView iv_img;
        TextView tv_title , tv_time , tv_author;

        ExpressDeliveryViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_tx_news_wx);
            tv_title = $(R.id.tv_title);
            tv_time = $(R.id.tv_time);
            tv_author = $(R.id.tv_author);
            iv_img = $(R.id.iv_img);
        }

        @Override
        public void setData(WeChatBean.NewslistBean data) {
            super.setData(data);
            if(data!=null){
                if(data.getTitle()!=null && data.getTitle().length()>0){
                    tv_title.setText(data.getTitle());
                }
                if(data.getCtime()!=null && data.getCtime().length()>0){
                    tv_time.setText(data.getCtime());
                }
                if(data.getDescription()!=null && data.getDescription().length()>0){
                    tv_author.setText(data.getDescription());
                }
                ImageUtils.loadImgByPicasso(getContext(),data.getPicUrl(), iv_img);
            }
        }
    }
}

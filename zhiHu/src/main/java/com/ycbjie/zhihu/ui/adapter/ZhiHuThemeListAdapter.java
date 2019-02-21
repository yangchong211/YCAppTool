package com.ycbjie.zhihu.ui.adapter;

import android.app.Activity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ycbjie.zhihu.R;
import com.ycbjie.zhihu.model.ZhiHuThemeChildBean;
import com.ycbjie.library.utils.image.ImageUtils;

import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter;
import org.yczbj.ycrefreshviewlib.viewHolder.BaseViewHolder;

public class ZhiHuThemeListAdapter extends RecyclerArrayAdapter<ZhiHuThemeChildBean.StoriesBean> {

    public ZhiHuThemeListAdapter(Activity activity) {
        super(activity);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ExpressDeliveryViewHolder(parent);
    }

    public void setReadState(int position, boolean readState) {
        getAllData().get(position).setReadState(readState);
    }

    private class ExpressDeliveryViewHolder extends BaseViewHolder<ZhiHuThemeChildBean.StoriesBean>{

        ImageView iv_logo;
        TextView tv_title , tv_time;

        ExpressDeliveryViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_zh_news_list);
            tv_title = $(R.id.tv_title);
            tv_time = $(R.id.tv_time);
            iv_logo = $(R.id.iv_logo);
        }

        @Override
        public void setData(ZhiHuThemeChildBean.StoriesBean data) {
            super.setData(data);
            tv_title.setText(data.getTitle());
            if(data.getImages()!=null){
                ImageUtils.loadImgByPicasso(getContext(),data.getImages().get(0),R.drawable.image_default,iv_logo);
            }else {
                ImageUtils.loadImgByPicasso(getContext(),R.drawable.image_default,iv_logo);
            }

        }
    }
}

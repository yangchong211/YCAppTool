package com.ycbjie.gank.view.adapter;

import android.app.Activity;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.blankj.utilcode.util.TimeUtils;
import com.ycbjie.gank.R;
import com.ycbjie.gank.bean.bean.CategoryResult;
import com.ycbjie.library.base.config.AppConfig;
import com.ycbjie.library.utils.image.ImageUtils;

import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter;
import org.yczbj.ycrefreshviewlib.viewHolder.BaseViewHolder;

public class GanKHomeAdapter extends RecyclerArrayAdapter<CategoryResult.ResultsBean> {

    public GanKHomeAdapter(Activity activity) {
        super(activity);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ExpressDeliveryViewHolder(parent);
    }

    private class ExpressDeliveryViewHolder extends BaseViewHolder<CategoryResult.ResultsBean> {

        ImageView iv_item_img;
        AppCompatTextView tv_item_title , tv_item_publisher ,tv_item_time;

        ExpressDeliveryViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_gank_home);
            tv_item_title = $(R.id.tv_item_title);
            tv_item_publisher = $(R.id.tv_item_publisher);
            tv_item_time = $(R.id.tv_item_time);
            iv_item_img = $(R.id.iv_item_img);
        }

        @Override
        public void setData(CategoryResult.ResultsBean data) {
            super.setData(data);
            // 列表显示图片
            if (AppConfig.INSTANCE.isShowListImg()) {
                iv_item_img.setVisibility(View.VISIBLE);
                String quality = "";
                if (data.images != null && data.images.size() > 0) {
                    switch (AppConfig.INSTANCE.getThumbnailQuality()) {
                        // 原图
                        case 0:
                            quality = "?imageView2/0/w/400";
                            break;
                        // 默认
                        case 1:
                            quality = "?imageView2/0/w/280";
                            break;
                        // 省流
                        case 2:
                            quality = "?imageView2/0/w/190";
                            break;
                        default:
                            break;
                    }
                    iv_item_img.setVisibility(View.VISIBLE);
                    String imageUrl = data.images.get(0) + quality;
                    ImageUtils.loadImgByPicasso(getContext(),imageUrl,R.drawable.image_default,iv_item_img);
                } else {                                                        // 图片 URL 不存在
                    iv_item_img.setVisibility(View.GONE);
                }
            } else {                                                            // 列表不显示图片
                iv_item_img.setVisibility(View.GONE);
            }

            tv_item_title.setText(data.desc==null ? "标题" : data.desc);
            tv_item_publisher.setText(data.who == null ? "佚名" : data.who);
            tv_item_time.setText(TimeUtils.getFriendlyTimeSpanByNow(data.publishedAt));
        }
    }
}

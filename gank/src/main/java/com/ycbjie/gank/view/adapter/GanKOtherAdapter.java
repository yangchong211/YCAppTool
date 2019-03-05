package com.ycbjie.gank.view.adapter;

import android.app.Activity;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ycbjie.gank.R;
import com.ycbjie.gank.bean.bean.GanKIoDataBean;
import com.ycbjie.library.utils.AppUtils;
import com.ycbjie.library.utils.image.ImageUtils;

import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter;
import org.yczbj.ycrefreshviewlib.viewHolder.BaseViewHolder;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/3/28
 *     desc  : 安卓适配器
 *     revise:
 * </pre>
 */
public class GanKOtherAdapter extends RecyclerArrayAdapter<GanKIoDataBean.ResultsBean> {

    public GanKOtherAdapter(Activity activity) {
        super(activity);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ExpressDeliveryViewHolder(parent);
    }


    private class ExpressDeliveryViewHolder extends BaseViewHolder<GanKIoDataBean.ResultsBean> {

        ImageView ivNiceImg;


        ExpressDeliveryViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_nice_pic_image);
            ivNiceImg = $(R.id.iv_nice_img);
        }

        @Override
        public void setData(GanKIoDataBean.ResultsBean data) {
            super.setData(data);
            /*
             * 注意：DensityUtil.setViewMargin(itemView,true,5,3,5,0);
             * 如果这样使用，则每个item的左右边距是不一样的，
             * 这样item不能复用，所以下拉刷新成功后显示会闪一下
             * 换成每个item设置上下左右边距是一样的话，系统就会复用，就消除了图片不能复用 闪跳的情况
             */
            if (getAdapterPosition() % 2 == 0) {
                AppUtils.setViewMargin(itemView, false, 12, 6, 12, 0);
            } else {
                AppUtils.setViewMargin(itemView, false, 6, 12, 12, 0);
            }
            if(data!=null){
                ImageUtils.loadImgByPicasso(getContext(),data.getUrl(),R.drawable.image_default, ivNiceImg);
            }
        }
    }
}

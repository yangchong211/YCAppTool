package com.ns.yc.lifehelper.ui.other.myKnowledge.adapter;

import android.app.Activity;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.ui.other.myKnowledge.bean.GanKIoDataBean;
import com.ns.yc.lifehelper.utils.AppUtil;
import com.ns.yc.lifehelper.utils.image.ImageUtils;

import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter;
import org.yczbj.ycrefreshviewlib.viewHolder.BaseViewHolder;


/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/8/28
 * 描    述：我的干货页面 福利  安卓适配器
 * 修订历史：
 * ================================================
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

        ImageView iv_nice_img ;


        ExpressDeliveryViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_nice_pic_image);
            iv_nice_img = $(R.id.iv_nice_img);
        }

        @Override
        public void setData(GanKIoDataBean.ResultsBean data) {
            super.setData(data);
            /**
             * 注意：DensityUtil.setViewMargin(itemView,true,5,3,5,0);
             * 如果这样使用，则每个item的左右边距是不一样的，
             * 这样item不能复用，所以下拉刷新成功后显示会闪一下
             * 换成每个item设置上下左右边距是一样的话，系统就会复用，就消除了图片不能复用 闪跳的情况
             */
            if (getAdapterPosition() % 2 == 0) {
                AppUtil.setViewMargin(itemView, false, 12, 6, 12, 0);
            } else {
                AppUtil.setViewMargin(itemView, false, 6, 12, 12, 0);
            }

            if(data!=null){
                ImageUtils.loadImgByPicasso(getContext(),data.getUrl(),iv_nice_img);
            }
        }
    }
}

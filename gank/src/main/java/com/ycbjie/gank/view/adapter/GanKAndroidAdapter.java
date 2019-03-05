package com.ycbjie.gank.view.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ycbjie.gank.R;
import com.ycbjie.gank.bean.bean.GanKIoDataBean;
import com.ycbjie.library.utils.image.ImageUtils;

import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter;
import org.yczbj.ycrefreshviewlib.viewHolder.BaseViewHolder;


/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/8/28
 * 描    述：我的干货页面  安卓适配器
 * 修订历史：
 * ================================================
 */
public class GanKAndroidAdapter extends RecyclerArrayAdapter<GanKIoDataBean.ResultsBean> {

    public GanKAndroidAdapter(Activity activity) {
        super(activity);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ExpressDeliveryViewHolder(parent);
    }


    private class ExpressDeliveryViewHolder extends BaseViewHolder<GanKIoDataBean.ResultsBean> {

        ImageView iv_all_welfare , iv_android_pic;
        TextView tv_android_des , tv_android_who ,tv_content_type ,tv_android_time;
        LinearLayout ll_welfare_other;

        ExpressDeliveryViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_gank_android);
            iv_all_welfare = $(R.id.iv_all_welfare);
            tv_android_des = $(R.id.tv_android_des);
            iv_android_pic = $(R.id.iv_android_pic);
            tv_android_who = $(R.id.tv_android_who);
            tv_content_type = $(R.id.tv_content_type);
            tv_android_time = $(R.id.tv_android_time);
            ll_welfare_other = $(R.id.ll_welfare_other);
        }

        @Override
        public void setData(GanKIoDataBean.ResultsBean data) {
            super.setData(data);
            if("福利".equals(data.getType())){
                if(data.getImages()!=null && data.getImages().size()>0){
                    iv_all_welfare.setVisibility(View.VISIBLE);
                    ll_welfare_other.setVisibility(View.GONE);
                    ImageUtils.loadImgByGlide(getContext(),data.getImages().get(0),R.drawable.shape_load_error_img,iv_all_welfare);
                } else {
                    iv_all_welfare.setVisibility(View.GONE);
                    ll_welfare_other.setVisibility(View.VISIBLE);
                }
            }else if("Android".equals(data.getType())){
                iv_all_welfare.setVisibility(View.GONE);
                if(data.getImages()!=null && data.getImages().size()>0){
                    iv_android_pic.setVisibility(View.VISIBLE);
                    ImageUtils.loadImgByGlide(getContext(),data.getImages().get(0),R.drawable.shape_load_error_img,iv_android_pic);
                } else {
                    iv_android_pic.setVisibility(View.GONE);
                }
            }

            if(data.getDesc()!=null && data.getDesc().length()>0){
                tv_android_des.setText(data.getDesc());
            }

            if(data.getWho()!=null && data.getWho().length()>0){
                tv_android_who.setText(data.getWho());
            } else {
                tv_android_who.setText("佚名");
            }

            if(data.getType()!=null && data.getType().length()>0){
                tv_content_type.setText(data.getType());
            }


        }
    }
}

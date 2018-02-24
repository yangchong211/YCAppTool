package com.ns.yc.lifehelper.ui.other.myKnowledge.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.ui.other.myKnowledge.bean.GanKEveryDay;
import com.ns.yc.lifehelper.utils.image.ImageUtils;

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
public class GanKEveryDayAdapter extends RecyclerArrayAdapter<GanKEveryDay.ResultsBean.AndroidBean> {

    public GanKEveryDayAdapter(Activity activity) {
        super(activity);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ExpressDeliveryViewHolder(parent);
    }

    private class ExpressDeliveryViewHolder extends BaseViewHolder<GanKEveryDay.ResultsBean.AndroidBean> {

        ImageView iv_title_type , iv_one_photo ,iv_two_one_one , iv_two_one_two ,iv_three_one_one ,iv_three_one_two
                ,iv_three_one_three;
        TextView tv_title_type , tv_one_photo_title ,tv_two_one_one_title ,tv_two_one_two_title ,tv_three_one_one_title
                ,tv_three_one_two_title ,tv_three_one_three_title;
        LinearLayout ll_title_more , ll_one_photo , ll_two_one ,ll_three_one ,ll_three_one_two;
        RelativeLayout rl_title;

        ExpressDeliveryViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_gank_every);
            rl_title = $(R.id.rl_title);
            iv_title_type = $(R.id.iv_title_type);
            tv_title_type = $(R.id.tv_title_type);
            ll_title_more = $(R.id.ll_title_more);

            ll_one_photo = $(R.id.ll_one_photo);
            iv_one_photo = $(R.id.iv_one_photo);
            tv_one_photo_title = $(R.id.tv_one_photo_title);

            iv_title_type = $(R.id.iv_title_type);
            tv_title_type = $(R.id.tv_title_type);
            ll_title_more = $(R.id.ll_title_more);

            ll_two_one = $(R.id.ll_two_one);
            iv_two_one_one = $(R.id.iv_two_one_one);
            tv_two_one_one_title = $(R.id.tv_two_one_one_title);
            iv_two_one_two = $(R.id.iv_two_one_two);
            tv_two_one_two_title = $(R.id.tv_two_one_two_title);

            ll_three_one = $(R.id.ll_three_one);
            iv_three_one_one = $(R.id.iv_three_one_one);
            tv_three_one_one_title = $(R.id.tv_three_one_one_title);
            ll_three_one_two = $(R.id.ll_three_one_two);
            iv_three_one_two = $(R.id.iv_three_one_two);
            tv_three_one_two_title = $(R.id.tv_three_one_two_title);
            iv_three_one_three = $(R.id.iv_three_one_three);
            tv_three_one_three_title = $(R.id.tv_three_one_three_title);
        }

        @Override
        public void setData(GanKEveryDay.ResultsBean.AndroidBean data) {
            super.setData(data);
            if(data!=null){
                String type = data.getType();
                switch (type){
                    case "Android":
                        showView(true,true,false,false);
                        ImageUtils.loadImgByPicasso(getContext(),R.drawable.home_title_android,iv_title_type);
                        //ImageUtils.loadImgByPicasso(getContext(),data.getImages().get(0),R.drawable.bg_stack_blur_default,iv_one_photo);
                        tv_one_photo_title.setText(data.getDesc());
                        break;
                    case "iOS":
                        showView(true,true,false,false);
                        ImageUtils.loadImgByPicasso(getContext(),R.drawable.home_title_ios,iv_title_type);
                        //ImageUtils.loadImgByPicasso(getContext(),data.getImages().get(0),R.drawable.bg_stack_blur_default,iv_one_photo);
                        tv_one_photo_title.setText(data.getDesc());
                        break;
                    case "休息视频":
                        showView(true,true,false,false);
                        ImageUtils.loadImgByPicasso(getContext(),R.drawable.home_title_movie,iv_title_type);
                        tv_one_photo_title.setText(data.getDesc());
                        break;
                    case "拓展资源":
                        showView(true,false,true,false);
                        ImageUtils.loadImgByPicasso(getContext(),R.drawable.home_title_source,iv_title_type);
                        break;
                    case "福利":
                        showView(true,true,false,false);
                        ImageUtils.loadImgByPicasso(getContext(),R.drawable.home_title_meizi,iv_title_type);
                        break;
                    case "App":
                        showView(true,true,false,false);
                        ImageUtils.loadImgByPicasso(getContext(),R.drawable.home_title_app,iv_title_type);
                        tv_one_photo_title.setText(data.getDesc());
                        break;
                    case "前端":
                        showView(true,false,false,true);
                        ImageUtils.loadImgByPicasso(getContext(),R.drawable.home_title_qian,iv_title_type);
                        tv_three_one_one_title.setText(data.getDesc());
                        break;
                    case "瞎推荐":
                        showView(true,false,true,false);
                        ImageUtils.loadImgByPicasso(getContext(),R.drawable.home_title_xia,iv_title_type);
                        break;
                }
                tv_title_type.setText(data.getType());
            }
        }


        private void showView(boolean showTitle , boolean showOnePhoto , boolean showTwoPhoto , boolean showThreePhoto){
            if(showTitle){
                rl_title.setVisibility(View.VISIBLE);
            }else {
                rl_title.setVisibility(View.GONE);
            }

            if(showOnePhoto){
                ll_one_photo.setVisibility(View.VISIBLE);
            }else {
                ll_one_photo.setVisibility(View.GONE);
            }

            if(showTwoPhoto){
                ll_two_one.setVisibility(View.VISIBLE);
            }else {
                ll_two_one.setVisibility(View.GONE);
            }

            if(showThreePhoto){
                ll_three_one.setVisibility(View.VISIBLE);
            }else {
                ll_three_one.setVisibility(View.GONE);
            }
        }
    }
}

package com.ns.yc.lifehelper.ui.find.view.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.ui.find.model.bean.GanKNicePicBean;
import com.ns.yc.lifehelper.ui.other.myPicture.view.MyPicNiceActivity;
import com.ns.yc.lifehelper.utils.ImageUtils;
import com.yc.cn.ycbaseadapterlib.first.BaseAdapter;
import com.yc.cn.ycbaseadapterlib.first.BaseViewHolder;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/8/21
 * 描    述：Home主页面适配器
 * 修订历史：
 * ================================================
 */
public class PicNiceAdapter extends BaseAdapter<GanKNicePicBean.ResultsBean> {

    public MyPicNiceActivity activity;


    public PicNiceAdapter(Context context) {
        super(context,R.layout.item_nice_pic_card);
    }


    @Override
    protected void bindData(BaseViewHolder holder, GanKNicePicBean.ResultsBean resultsBean) {
        int width = activity.getWindowManager().getDefaultDisplay().getWidth();
        ImageView iv_image = holder.getView(R.id.iv_nice_img);
        ViewGroup.LayoutParams params = iv_image.getLayoutParams();
        //设置图片的相对于屏幕的宽高比
        params.width = width / 2;
        params.height =  (int) (200 + Math.random() * 400) ;
        holder.getView(R.id.iv_nice_img).setLayoutParams(params);
        ImageUtils.loadImgByPicasso(activity,resultsBean.getUrl(), R.drawable.image_default , iv_image);
    }

}

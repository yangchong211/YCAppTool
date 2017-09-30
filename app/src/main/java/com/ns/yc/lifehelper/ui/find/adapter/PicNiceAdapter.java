package com.ns.yc.lifehelper.ui.find.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.ui.find.bean.GanKNicePicBean;
import com.ns.yc.lifehelper.ui.other.myPicture.view.MyPicNiceActivity;
import com.ns.yc.lifehelper.utils.ImageUtils;

import java.util.List;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/8/21
 * 描    述：Home主页面适配器
 * 修订历史：
 * ================================================
 */
public class PicNiceAdapter extends RecyclerView.Adapter<PicNiceAdapter.MyViewHolder> {

    public MyPicNiceActivity activity;
    public List<GanKNicePicBean.ResultsBean> gankPic;

    public PicNiceAdapter(MyPicNiceActivity activity, List<GanKNicePicBean.ResultsBean> gankPic) {
        this.activity = activity;
        this.gankPic = gankPic;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_nice_pic_card, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        int width = activity.getWindowManager().getDefaultDisplay().getWidth();
        ViewGroup.LayoutParams params = holder.iv_nice_img.getLayoutParams();
        //设置图片的相对于屏幕的宽高比
        params.width = width / 2;
        params.height =  (int) (200 + Math.random() * 400) ;
        holder.iv_nice_img.setLayoutParams(params);
        ImageUtils.loadImgByPicasso(activity,gankPic.get(position).getUrl(),holder.iv_nice_img);
    }

    @Override
    public int getItemCount() {
        return gankPic!=null ? gankPic.size() : 0;
    }

    class MyViewHolder extends  RecyclerView.ViewHolder{

        ImageView iv_nice_img;

        public MyViewHolder(View itemView) {
            super(itemView);
            iv_nice_img = (ImageView) itemView.findViewById(R.id.iv_nice_img);
        }
    }
}

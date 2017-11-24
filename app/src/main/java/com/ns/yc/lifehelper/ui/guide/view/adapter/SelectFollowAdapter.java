package com.ns.yc.lifehelper.ui.guide.view.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.bean.SelectPoint;
import com.ns.yc.lifehelper.listener.OnListItemClickListener;
import com.yc.cn.ycrecycleviewlib.select.SelectRecyclerViewAdapter;

import java.util.List;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2016/5/18
 * 描    述：关注点页面
 * 修订历史：
 * ================================================
 */
public class SelectFollowAdapter extends SelectRecyclerViewAdapter<SelectFollowAdapter.MyViewHolder> {

    public Activity activity;
    public List<SelectPoint> data;

    public SelectFollowAdapter(Activity activity, List<SelectPoint> data) {
        this.activity = activity;
        this.data = data;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.tag_select_follow, parent, false);
        return new MyViewHolder(view , mItemClickListener);
    }


    @SuppressLint("MissingSuperCall")
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if(data!=null && data.size()>0){
            holder.tv_name.setText(data.get(position).getName());
            if(isIndexSelected(position)){
                holder.tv_name.setBackgroundResource(R.drawable.shape_btn_color_bg_press);
                holder.tv_name.setTextColor(activity.getResources().getColor(R.color.colorWhite));
            }else {
                holder.tv_name.setBackgroundResource(R.drawable.shape_btn_color_bg);
                holder.tv_name.setTextColor(activity.getResources().getColor(R.color.blackText));
            }
        }
    }


    @Override
    public int getItemCount() {
        return data!=null ? data.size() : 0;
    }


    class MyViewHolder extends  RecyclerView.ViewHolder{

        private  OnListItemClickListener listener;
        TextView tv_name;
        MyViewHolder(View itemView , OnListItemClickListener mItemClickListener) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            this.listener = mItemClickListener;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(v,getAdapterPosition());
                }
            });
        }

    }


    private OnListItemClickListener mItemClickListener;
    public void setOnItemClickListener(OnListItemClickListener listener) {
        this.mItemClickListener = listener;
    }


}

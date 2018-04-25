package com.ns.yc.lifehelper.ui.other.douban.douMovie.view.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.inter.listener.OnListItemClickListener;
import com.ns.yc.lifehelper.ui.other.douban.douMovie.bean.DouMovieDetailBean;
import com.ns.yc.lifehelper.utils.image.ImageUtils;

import java.util.List;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/8/22
 * 描    述：豆瓣电影详情页面适配器
 * 修订历史：
 * ================================================
 */
public class MovieDetailAdapter extends RecyclerView.Adapter<MovieDetailAdapter.MyViewHolder> {

    private final List<DouMovieDetailBean.CastsBean> list;
    private final Activity activity;
    private OnListItemClickListener mItemClickListener;

    public MovieDetailAdapter(List<DouMovieDetailBean.CastsBean> list, Activity activity) {
        this.list = list;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_movie_detail_person, parent, false);
        MyViewHolder holder = new MyViewHolder(view, mItemClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if(list!=null){
            ImageUtils.loadImgByPicassoPerson(activity,list.get(position).getAvatars().getLarge(),
                    R.drawable.ic_person_logo, holder.iv_detail_avatar);
            holder.tv_detail_name.setText(list.get(position).getName());
            holder.tv_detail_duty.setText("演员");
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setOnItemClickListener(OnListItemClickListener listener) {
        this.mItemClickListener = listener;
    }


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final OnListItemClickListener mListener;
        ImageView iv_detail_avatar;
        TextView tv_detail_name;
        TextView tv_detail_duty;


        MyViewHolder(View view, OnListItemClickListener listener) {
            super(view);
            iv_detail_avatar = (ImageView) view.findViewById(R.id.iv_detail_avatar);
            tv_detail_name = (TextView) view.findViewById(R.id.tv_detail_name);
            tv_detail_duty = (TextView) view.findViewById(R.id.tv_detail_duty);
            this.mListener = listener;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onItemClick(view, getAdapterPosition());
            }
        }
    }
}

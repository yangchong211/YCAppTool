package com.ns.yc.lifehelper.ui.other.myKnowledge.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.inter.listener.OnListItemClickListener;

import java.util.List;


/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/8/28
 * 描    述：我的干货搜索页面适配器
 * 修订历史：
 * ================================================
 */
public class KnowledgeSearchAdapter extends RecyclerView.Adapter<KnowledgeSearchAdapter.MyViewHolder> {

    private final List<String> list;
    private final Activity activity;
    private OnListItemClickListener mItemClickListener;

    public KnowledgeSearchAdapter(List<String> list, Activity activity) {
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

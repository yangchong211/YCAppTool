package com.ycbjie.other.ui.adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ycbjie.library.utils.image.ImageUtils;
import com.ycbjie.other.R;

import java.util.ArrayList;
import java.util.List;

public class MeBannerAdapter extends RecyclerView.Adapter <MeBannerAdapter.MyViewHolder>{


    private Context mContext;
    private boolean isSnap;
    public MeBannerAdapter(Context context, boolean isSnap){
        this.mContext =context;
        this.isSnap = isSnap;
    }

    private List<Integer> urlList = new ArrayList<>();
    public void setData(List<Integer> list) {
        urlList.clear();
        this.urlList = list;
    }

    public List<Integer> getData() {
        return urlList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (isSnap){
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_snap_view, parent, false);
            return new MyViewHolder(view);
        }else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_banner_view, parent, false);
            return new MyViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (urlList == null || urlList.isEmpty()){
            return;
        }

        Integer url = urlList.get(position%urlList.size());
        ImageUtils.loadImgByGlide(mContext,url,url,holder.imageView);
        //holder.imageView.setBackgroundResource(url);
    }


    class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        MyViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_image);
        }

    }

    @Override
    public int getItemCount() {
        if (urlList.size() != 1) {
            Log.e("getItemCount","getItemCount---------");
            return Integer.MAX_VALUE; // 无限轮播
        } else {
            Log.e("getItemCount","getItemCount++++----");
            return urlList.size();
        }
    }

}

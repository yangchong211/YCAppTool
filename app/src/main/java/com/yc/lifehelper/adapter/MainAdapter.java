package com.yc.lifehelper.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter;
import org.yczbj.ycrefreshviewlib.holder.BaseViewHolder;

public class MainAdapter extends RecyclerArrayAdapter<String> {

    public MainAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder<String> OnCreateViewHolder(ViewGroup parent, int i) {
        return new MyViewHolder(parent);
    }

    private static class MyViewHolder extends BaseViewHolder<String> {

        public MyViewHolder(View itemView) {
            super(itemView);
        }


        @Override
        public void setData(String data) {
            super.setData(data);
        }
    }
}

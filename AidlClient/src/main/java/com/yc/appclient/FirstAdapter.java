package com.yc.appclient;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.yc.appservice.AppInfo;

import java.util.List;

public class FirstAdapter extends RecyclerView.Adapter<FirstAdapter.MyViewHolder> {

    private Context context;
    private List<AppInfo> list;
    private OnItemClickListener mItemClickListener;

    FirstAdapter(List<AppInfo> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_app_info, parent, false);
        MyViewHolder holder = new MyViewHolder(view, mItemClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        if(list!=null && list.size()>0){
            holder.tvName.setText(list.get(position).getKey());
            holder.tvInfo.setText(list.get(position).getValue());
        }
    }


    @Override
    public int getItemCount() {
        return list==null ? 0 : list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private OnItemClickListener mListener;
        private final TextView tvName;
        private final TextView tvInfo;

        MyViewHolder(View view, OnItemClickListener listener) {
            super(view);

            tvName = (TextView) view.findViewById(R.id.tv_name);
            tvInfo = (TextView) view.findViewById(R.id.tv_info);
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

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

}

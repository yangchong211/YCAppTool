package com.ns.yc.lifehelper.ui.other.gank.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.inter.listener.OnListItemClickListener;
import com.ns.yc.lifehelper.ui.other.gank.bean.SearchHistory;

import java.util.List;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/9/21
 * 描    述：搜索历史
 * 修订历史：
 * ================================================
 */
public class GanKSearchHisAdapter extends RecyclerView.Adapter<GanKSearchHisAdapter.MyViewHolder> {

    private Context context;
    private List<SearchHistory> list;
    private OnListItemClickListener mItemClickListener;
    public void setOnItemClickListener(OnListItemClickListener listener) {
        this.mItemClickListener = listener;
    }


    public GanKSearchHisAdapter(Activity activity, List<SearchHistory> his) {
        this.context = activity;
        this.list = his;
    }

    public void setData(List<SearchHistory> his) {
        if(his!=null && his.size()>0){
            list.clear();
            list.addAll(his);
            notifyItemRangeInserted(0,list.size());
        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_gank_his_search, parent, false);
        return new MyViewHolder(view , mItemClickListener);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        if(list!=null && list.size()>0){
            holder.tv_his.setText(list.get(position).getContent());
        }
    }

    @Override
    public int getItemCount() {
        return list==null ? 0 : list.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private OnListItemClickListener mListener;
        TextView tv_his ;

        MyViewHolder(View view, OnListItemClickListener listener) {
            super(view);
            tv_his = (TextView) view.findViewById(R.id.tv_his);

            this.mListener = listener;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onItemClick(view,getAdapterPosition());
            }
        }
    }

}

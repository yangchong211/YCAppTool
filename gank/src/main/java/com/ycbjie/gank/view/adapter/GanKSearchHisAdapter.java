package com.ycbjie.gank.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ycbjie.gank.R;
import com.ycbjie.library.inter.listener.OnListItemClickListener;
import com.ycbjie.gank.bean.cache.CacheSearchHistory;

import java.util.List;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/5/14
 *     desc  : 搜索历史
 *     revise:
 * </pre>
 */
public class GanKSearchHisAdapter extends RecyclerView.Adapter<GanKSearchHisAdapter.MyViewHolder> {

    private Context context;
    private List<CacheSearchHistory> list;
    private OnListItemClickListener mItemClickListener;
    public void setOnItemClickListener(OnListItemClickListener listener) {
        this.mItemClickListener = listener;
    }


    public GanKSearchHisAdapter(Activity activity, List<CacheSearchHistory> his) {
        this.context = activity;
        this.list = his;
    }

    public void setData(List<CacheSearchHistory> his) {
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

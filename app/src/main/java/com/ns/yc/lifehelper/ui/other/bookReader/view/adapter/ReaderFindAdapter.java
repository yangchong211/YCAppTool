package com.ns.yc.lifehelper.ui.other.bookReader.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.inter.listener.OnListItemClickListener;
import com.ns.yc.lifehelper.ui.other.bookReader.bean.support.FindBean;

import java.util.List;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/9/18
 * 描    述：小说阅读器主页面
 * 修订历史：
 * ================================================
 */
public class ReaderFindAdapter extends RecyclerView.Adapter<ReaderFindAdapter.MyViewHolder> {

    private final List<FindBean> list;
    private final Context context;
    private OnListItemClickListener mItemClickListener;

    public ReaderFindAdapter(List<FindBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void setOnItemClickListener(OnListItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_reader_find, parent, false);
        return new MyViewHolder(view,mItemClickListener);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        if(list!=null){
            holder.tv_title.setText(list.get(position).getTitle());
            holder.iv_image.setImageResource(list.get(position).getIconResId());
        }
    }

    @Override
    public int getItemCount() {
        return list==null ? 0 : list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private OnListItemClickListener mListener;
        ImageView iv_image ;
        TextView tv_title ;

        MyViewHolder(View view, OnListItemClickListener listener) {
            super(view);
            iv_image = (ImageView) view.findViewById(R.id.iv_image);
            tv_title = (TextView) view.findViewById(R.id.tv_title);

            //关于回调接口的两种写法，都行
            this.mListener = listener;
            view.setOnClickListener(this);
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemLongClickListener.onLongClick(v,getAdapterPosition());
                    return true;
                }
            });
        }

        @Override
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onItemClick(view, getAdapterPosition());
            }
        }
    }


    //回调接口
    public interface OnItemLongClickListener {
        void onLongClick(View v, int position);
    }
    private OnItemLongClickListener mOnItemLongClickListener;
    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        mOnItemLongClickListener = onItemLongClickListener;
    }

}

package com.ns.yc.lifehelper.ui.other.bookReader.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.inter.listener.OnRvItemClickListener;
import com.ns.yc.lifehelper.ui.other.bookReader.bean.CategoryList;
import com.ns.yc.lifehelper.ui.other.bookReader.view.activity.ReaderCategoryActivity;

import java.util.List;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/9/21
 * 描    述：小说阅读器主题书单适配器
 * 修订历史：
 * ================================================
 */
public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.MyViewHolder> {

    private List<CategoryList.MaleBean> list;
    private Context context;
    private OnRvItemClickListener mItemClickListener;

    public CategoryListAdapter(ReaderCategoryActivity activity, List<CategoryList.MaleBean> list, OnRvItemClickListener clickListener) {
        this.context = activity;
        this.list = list;
        this.mItemClickListener = clickListener;
    }

    public void setOnItemClickListener(OnRvItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_reader_category, parent, false);
        return new MyViewHolder(view,mItemClickListener);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        if(list!=null){
            holder.tv_name.setText(list.get(position).getName());
            holder.tv_book_count.setText(String.format(context.getString(R.string.category_book_count),list.get(position).getBookCount()));
        }
    }

    @Override
    public int getItemCount() {
        return list==null ? 0 : list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private OnRvItemClickListener mListener;
        TextView tv_name , tv_book_count;

        MyViewHolder(View view, OnRvItemClickListener listener) {
            super(view);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_book_count = (TextView) view.findViewById(R.id.tv_book_count);

            this.mListener = listener;
            view.setOnClickListener(this);
            //这种方法不行
        }

        @Override
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onItemClick(view, getAdapterPosition(),list.get(getAdapterPosition()));
            }
        }
    }

}

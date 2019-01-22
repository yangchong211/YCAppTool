package com.ycbjie.todo.ui.adapter;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.ycbjie.library.api.ConstantImageApi;
import com.ycbjie.todo.R;

import java.util.ArrayList;
import java.util.List;


public class ChoosePriorityAdapter  extends RecyclerView.Adapter {

    private final LayoutInflater mInflater;
    private List<Item> mList = new ArrayList<>();
    private Context mContext;

    private static class Item {
        String name;
        @DrawableRes
        int resId;
        private Item(String name, int resId) {
            this.name = name;
            this.resId = resId;
        }
    }

    private int mCheckItem;{
        int[] priorityIcons = ConstantImageApi.createPriorityIcons();
        mList.add(new Item("日常", priorityIcons[0]));
        mList.add(new Item("一般", priorityIcons[1]));
        mList.add(new Item("重要", priorityIcons[2]));
        mList.add(new Item("紧急", priorityIcons[3]));
    }

    public ChoosePriorityAdapter(Context context) {
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    public int getCheckItem() {
        return mCheckItem;
    }

    public void setCheckItem(int checkItem) {
        // notify the old
        notifyItemChanged(mCheckItem);
        // notify the new
        notifyItemChanged(checkItem);
        mCheckItem = checkItem;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_choose_priority, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Holder h = (Holder) holder;
        Item item = mList.get(position);
        h.mIvPriorityIcon.setImageResource(item.resId);
        h.mTvPriorityText.setText(item.name);
        h.mIvMask.setVisibility(View.INVISIBLE);
        if (mCheckItem == position) {
            h.mIvMask.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    private class Holder extends RecyclerView.ViewHolder {

        ImageView mIvPriorityIcon;
        ImageView mIvMask;
        TextView mTvPriorityText;

        Holder(View view) {
            super(view);
            mIvPriorityIcon = view.findViewById(R.id.iv_priority_icon);
            mIvMask = view.findViewById(R.id.iv_mask);
            mTvPriorityText = view.findViewById(R.id.tv_priority_text);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onClick(v,getAdapterPosition());
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onClick(View v, int position);
    }
    private OnItemClickListener mOnItemClickListener;
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }


}

package com.ycbjie.todo.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ycbjie.library.api.ConstantImageApi;
import com.ycbjie.library.utils.image.ImageUtils;
import com.ycbjie.todo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/9/15
 * 描    述：时光日志弹出窗适配器
 * 修订历史：
 * ================================================
 */
public class WorkChooseAdapter extends RecyclerView.Adapter {


    private static class Item {
        Integer uri;
        private Item(Integer uri) {
            this.uri = uri;
        }
    }

    private final LayoutInflater mInflater;
    private List<Item> mList = new ArrayList<>();
    private Context mContext;


    private int mCheckItem;{
        List<Integer> img = ConstantImageApi.createBgImg();
        for (Integer s : img) {
            mList.add(new Item(s));
        }
    }

    public WorkChooseAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    public int getCheckItem() {
        return mCheckItem;
    }

    public void setCheckItem(String checkItem) {
        for (int i = 0; i < mList.size(); i++) {
            if (mList.get(i).uri.equals(checkItem)) {
                mCheckItem = i;
                break;
            }
        }
        notifyItemChanged(mCheckItem);
    }

    public void setCheckItem(int checkItem) {
        for (int i = 0; i < mList.size(); i++) {
            Integer uri = mList.get(i).uri;
            if (uri == checkItem) {
                mCheckItem = i;
                break;
            }
        }
        notifyItemChanged(mCheckItem);
    }

    public void setCheck(int checkItem) {
        mCheckItem = checkItem;
        notifyItemChanged(mCheckItem);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_choose_paper_color, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Holder h = (Holder) holder;
        ImageUtils.loadImgByPicasso(h.mIvIcon.getContext(),mList.get(position).uri,h.mIvIcon);
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
        ImageView mIvMask;
        ImageView mIvIcon;

        Holder(View view) {
            super(view);
            mIvMask = (ImageView) view.findViewById(R.id.iv_mask);
            mIvIcon = (ImageView) view.findViewById(R.id.sdv_icon);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onClick(v,getAdapterPosition());
                }
            });
        }
    }

    //回调接口
    public interface OnItemClickListener {
        void onClick(View v, int position);
    }

    private OnItemClickListener mOnItemClickListener;
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }
}

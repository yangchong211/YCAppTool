package com.ns.yc.lifehelper.ui.other.toDo.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.api.constantApi.ConstantImageApi;
import com.ns.yc.lifehelper.listener.OnListItemClickListener;
import com.ns.yc.lifehelper.ui.other.toDo.bean.ToDoDetail;
import com.ns.yc.lifehelper.utils.image.ImageUtils;

import java.util.List;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/9/15
 * 描    述：时光日志编辑页面适配器
 * 修订历史：
 * ================================================
 */
public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.MyViewHolder> {

    private final List<ToDoDetail.ToDo> list;
    private final Context context;
    private OnListItemClickListener mItemClickListener;
    private boolean showPriority = true;

    public ToDoAdapter(List<ToDoDetail.ToDo> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void setOnItemClickListener(OnListItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_to_do_note, parent, false);
        return new MyViewHolder(view,mItemClickListener);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        if(list!=null){
            holder.tv_title.setText(list.get(position).getTitle());
            holder.tv_content.setText(list.get(position).getContent());
            ImageUtils.loadImgByPicasso(holder.ic_icon.getContext(),list.get(position).getIcon(),holder.ic_icon);
            if (showPriority) {
                if (!holder.iv_curr_priority.isShown()){
                    holder.iv_curr_priority.setVisibility(View.VISIBLE);
                }
                String priority = list.get(position).getPriority();
                int img = ConstantImageApi.createPriorityIcons()[0];
                switch (priority){
                    case "日常":
                        img = ConstantImageApi.createPriorityIcons()[0];
                        break;
                    case "一般":
                        img = ConstantImageApi.createPriorityIcons()[1];
                        break;
                    case "重要":
                        img = ConstantImageApi.createPriorityIcons()[2];
                        break;
                    case "紧急":
                        img = ConstantImageApi.createPriorityIcons()[3];
                        break;
                    default:
                        break;
                }
                holder.iv_curr_priority.setImageResource(img);
            } else {
                if (holder.iv_curr_priority.isShown()){
                    holder.iv_curr_priority.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return list==null ? 0 : list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private OnListItemClickListener mListener;
        ImageView ic_icon , iv_curr_priority;
        TextView tv_title , tv_content;
        LinearLayout ll_task_finished_mask;

        MyViewHolder(View view, OnListItemClickListener listener) {
            super(view);
            ic_icon = (ImageView) view.findViewById(R.id.ic_icon);
            tv_title = (TextView) view.findViewById(R.id.tv_title);
            tv_content = (TextView) view.findViewById(R.id.tv_content);
            iv_curr_priority = (ImageView) view.findViewById(R.id.iv_curr_priority);
            ll_task_finished_mask = (LinearLayout) view.findViewById(R.id.ll_task_finished_mask);

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

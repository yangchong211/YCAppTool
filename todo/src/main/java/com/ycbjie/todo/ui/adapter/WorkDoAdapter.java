package com.ycbjie.todo.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ycbjie.library.api.ConstantImageApi;
import com.ycbjie.library.constant.Constant;
import com.ycbjie.library.db.cache.CacheTaskDetailEntity;
import com.ycbjie.todo.R;

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
public class WorkDoAdapter extends RecyclerView.Adapter<WorkDoAdapter.MyViewHolder> {

    private List<CacheTaskDetailEntity> list;
    private Context context;
    private int[] priorityIcons;

    public WorkDoAdapter(List<CacheTaskDetailEntity> list, Context context) {
        this.list = list;
        this.context = context;
        priorityIcons = ConstantImageApi.createPriorityIcons();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_to_do_note, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.tv_title.setText(list.get(position).getTitle());

        String content = list.get(position).getContent();
        int length = content.length();
        String s = content.substring(0, Math.min(length, 28));
        if (length >= 28) s += "...";
        holder.tv_content.setText(s);

        holder.ic_icon.setImageResource(list.get(position).getIcon());

        if (showPriority) {
            if (!holder.iv_curr_priority.isShown()){
                holder.iv_curr_priority.setVisibility(View.VISIBLE);
            }
            int priority = list.get(position).getPriority();
            holder.iv_curr_priority.setImageResource(priorityIcons[priority]);
        } else {
            if (holder.iv_curr_priority.isShown()){
                holder.iv_curr_priority.setVisibility(View.INVISIBLE);
            }
        }

        int state = list.get(position).getState();
        if (state == Constant.TaskState.FINISHED){
            holder.ll_task_finished_mask.setVisibility(View.VISIBLE);
        } else{
            holder.ll_task_finished_mask.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return list==null ? 0 : list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView ic_icon , iv_curr_priority;
        TextView tv_title , tv_content;
        LinearLayout ll_task_finished_mask;

        MyViewHolder(View view) {
            super(view);
            ic_icon = (ImageView) view.findViewById(R.id.ic_icon);
            tv_title = (TextView) view.findViewById(R.id.tv_title);
            tv_content = (TextView) view.findViewById(R.id.tv_content);
            iv_curr_priority = (ImageView) view.findViewById(R.id.iv_curr_priority);
            ll_task_finished_mask = (LinearLayout) view.findViewById(R.id.ll_task_finished_mask);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClick(getAdapterPosition(), list.get(getAdapterPosition()));
                }
            });
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mListener.onItemLongClick(getAdapterPosition(), list.get(getAdapterPosition()));
                    return true;
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position, CacheTaskDetailEntity entity);
        void onItemLongClick(int position, CacheTaskDetailEntity entity);
    }
    private OnItemClickListener mListener;

    public void setListener(final OnItemClickListener listener) {
        mListener = listener;
    }

    private boolean showPriority = true;
    public void setShowPriority(boolean showPriority) {
        this.showPriority = showPriority;
    }

}

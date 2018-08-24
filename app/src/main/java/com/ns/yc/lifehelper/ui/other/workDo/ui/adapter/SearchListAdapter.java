package com.ns.yc.lifehelper.ui.other.workDo.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.comment.Constant;
import com.ns.yc.lifehelper.ui.other.workDo.data.DateUtils;
import com.ns.yc.lifehelper.ui.other.workDo.model.TaskDetailEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/9/15
 * 描    述：时光日志编辑页面适配器
 * 修订历史：
 * ================================================
 */
public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.MyViewHolder> {


    private Context context;
    private List<TaskDetailEntity> mList = new ArrayList<>();

    public SearchListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_to_do_note, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.setView(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ic_icon)
        ImageView icIcon;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_content)
        TextView tvContent;
        @BindView(R.id.iv_curr_priority)
        ImageView ivCurrPriority;
        @BindView(R.id.ll_task_finished_mask)
        LinearLayout llTaskFinishedMask;

        MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (mListener != null) mListener.onItemClick(position, mList.get(position));
                }
            });
        }

        public void setView(TaskDetailEntity entity) {
            String title = entity.getTitle();
            tvTitle.setText(title);
            String content = entity.getContent();
            tvContent.setText(content);
            long timeStamp = entity.getTimeStamp();
            String date = DateUtils.formatDateWeek(timeStamp);
            //mTvDate.setText(date);
            int state = entity.getState();
            String sState = (state == Constant.TaskState.FINISHED) ? "已完成" : "未完成";
            //mTvState.setText(sState);
        }
    }


    public void setList(List<TaskDetailEntity> list) {
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }


    private OnItemClickListener mListener;
    public void setListener(OnItemClickListener listener) {
        mListener = listener;
    }
    public interface OnItemClickListener {
        void onItemClick(int position, TaskDetailEntity entity);
    }


}

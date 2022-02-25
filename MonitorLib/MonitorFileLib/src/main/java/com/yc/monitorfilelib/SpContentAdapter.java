package com.yc.monitorfilelib;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


/**
 * <pre>
 *     author : 杨充
 *     email  : yangchong211@163.com
 *     time   : 2021/8/11
 *     desc   : sp适配器
 *     revise :
 * </pre>
 */
public class SpContentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context mContext;
    private final List<SpDataBean> mContentList;

    public SpContentAdapter(Context context, List<SpDataBean> contentList) {
        mContext = context;
        mContentList = contentList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_sp_content_view,
                parent, false);
        return new MyViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof MyViewHolder) {
            MyViewHolder myViewHolder = (MyViewHolder) viewHolder;
            SpDataBean spBean = mContentList.get(position);
            myViewHolder.tv_sp_key.setText(spBean.key);
            myViewHolder.tv_sp_type.setText(spBean.value.getClass().getSimpleName());
            myViewHolder.tv_sp_value.setText(spBean.value.toString());
        }
    }

    @Override
    public int getItemCount() {
        return mContentList == null ? 0 : mContentList.size();
    }

    private static class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView tv_sp_key;
        private final TextView tv_sp_type;
        private final TextView tv_sp_value;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_sp_key = itemView.findViewById(R.id.tv_sp_key);
            tv_sp_type = itemView.findViewById(R.id.tv_sp_type);
            tv_sp_value = itemView.findViewById(R.id.tv_sp_value);
        }
    }
}

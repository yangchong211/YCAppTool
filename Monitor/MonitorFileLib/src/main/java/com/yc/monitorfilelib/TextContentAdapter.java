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
 * description: 文本适配器
 * @author  杨充
 * @since   2021/8/11
 */
public class TextContentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context mContext;
    private final List<String> mContentList;

    public TextContentAdapter(Context context , List<String> contentList) {
        mContext = context;
        mContentList = contentList;
    }

    public void append(String item) {
        if (item != null) {
            int start = mContentList.size();
            mContentList.add(item);
            notifyItemRangeInserted(start,1);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_text_content_view,
                parent, false);
        return new MyViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof MyViewHolder) {
            MyViewHolder myViewHolder = (MyViewHolder) viewHolder;
            String s = mContentList.get(position);
            myViewHolder.mTextView.setText(s);
        }
    }

    @Override
    public int getItemCount() {
        return mContentList==null ? 0 : mContentList.size();
    }

    private static class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView mTextView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.tv_name);
        }
    }
}

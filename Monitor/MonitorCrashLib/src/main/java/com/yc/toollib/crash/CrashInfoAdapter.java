package com.yc.toollib.crash;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.yc.toollib.R;
import com.yc.toollib.tool.OnItemClickListener;

import java.io.File;
import java.util.List;

public class CrashInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<File> fileList;
    private OnItemClickListener mOnItemClickLitener;

    public CrashInfoAdapter(Context context, List<File> fileList) {
        this.context = context;
        this.fileList = fileList;
        layoutInflater = LayoutInflater.from(context);
    }

    public void setOnItemClickLitener(OnItemClickListener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public void updateDatas(List<File> fileList) {
        this.fileList = fileList;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = layoutInflater.inflate(R.layout.item_crash_view, parent, false);
        return new MyViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        if (holder instanceof MyViewHolder) {
            MyViewHolder myViewHolder = (MyViewHolder) holder;
            File file = fileList.get(position);
            myViewHolder.tv_path.setText(file.getAbsolutePath());

            //动态修改颜色
            String fileName = file.getName().replace(".txt", "");
            String[] splitNames = fileName.split("_");
            Spannable spannable = null;
            if (splitNames.length == 3) {
                String errorMsgType = splitNames[2];
                if (!TextUtils.isEmpty(errorMsgType)) {
                    spannable = Spannable.Factory.getInstance().newSpannable(fileName);
                    spannable = CrashLibUtils.addNewSpan(context, spannable, fileName, errorMsgType,
                            Color.parseColor("#FF0006"), 0);
                }
            }
            if (spannable != null) {
                myViewHolder.tv_title.setText(spannable);
            } else {
                myViewHolder.tv_title.setText(fileName);
            }
            if (mOnItemClickLitener != null) {
                myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mOnItemClickLitener.onItemClick(view, position);
                    }
                });
                myViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        mOnItemClickLitener.onLongClick(view, position);
                        return false;
                    }
                });
            }

        }
    }

    @Override
    public int getItemCount() {
        return fileList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_title;
        private TextView tv_path;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_path = itemView.findViewById(R.id.tv_path);

        }
    }

}

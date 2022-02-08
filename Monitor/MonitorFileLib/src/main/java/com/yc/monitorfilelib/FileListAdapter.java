package com.yc.monitorfilelib;

import android.content.Context;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;

/**
 * description: 文件管理适配器
 * @author  杨充
 * @since   2021/8/11
 */
public class FileListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    private final List<File> mFileList;
    private OnViewClickListener mOnViewClickListener;
    private OnViewLongClickListener mOnViewLongClickListener;

    public FileListAdapter(Context context, List<File> fileList) {
        this.context = context;
        mFileList = fileList;
    }

    public void setOnViewClickListener(OnViewClickListener onViewClickListener) {
        this.mOnViewClickListener = onViewClickListener;
    }

    public void setOnViewLongClickListener(OnViewLongClickListener onViewLongClickListener) {
        this.mOnViewLongClickListener = onViewLongClickListener;
    }

    public interface OnViewLongClickListener {
        boolean onViewLongClick(View var1, File var2 ,int pos);
    }

    public interface OnViewClickListener {
        void onViewClick(View var1, File var2);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.item_list_file_view,
                parent, false);
        return new MyViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder,final int position) {
        if (holder instanceof MyViewHolder) {
            MyViewHolder myViewHolder = (MyViewHolder) holder;
            File fileInfo = mFileList.get(position);

            myViewHolder.mName.setText(fileInfo.getName());
            if (fileInfo.isDirectory()) {
                myViewHolder.mIcon.setImageResource(R.drawable.dk_dir_icon);
                myViewHolder.mMoreBtn.setVisibility(View.VISIBLE);
                myViewHolder.mSize.setVisibility(View.GONE);
            } else {
                if (FileExplorerUtils.getSuffix(fileInfo).equals("jpg")) {
                    myViewHolder.mIcon.setImageResource(R.drawable.dk_jpg_icon);
                } else if (FileExplorerUtils.getSuffix(fileInfo).equals("txt")) {
                    myViewHolder.mIcon.setImageResource(R.drawable.dk_txt_icon);
                } else if (FileExplorerUtils.getSuffix(fileInfo).equals("db")) {
                    myViewHolder.mIcon.setImageResource(R.drawable.dk_file_db);
                } else {
                    myViewHolder.mIcon.setImageResource(R.drawable.dk_file_icon);
                }
                myViewHolder.mMoreBtn.setVisibility(View.GONE);
                myViewHolder.mSize.setVisibility(View.VISIBLE);
                long directorySize = FileExplorerUtils.getDirectorySize(fileInfo);
                SpannableString printSizeForSpannable = FileExplorerUtils.getPrintSizeForSpannable(directorySize);
                myViewHolder.mSize.setText(printSizeForSpannable);
            }

            if (mOnViewClickListener != null) {
                myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mOnViewClickListener.onViewClick(view, fileInfo);
                    }
                });
                myViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        return mOnViewLongClickListener != null &&
                                mOnViewLongClickListener.onViewLongClick(view, fileInfo,position);
                    }
                });
            }

        }
    }

    @Override
    public int getItemCount() {
        return mFileList==null ? 0 : mFileList.size();
    }

    private static class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView mName;
        private final ImageView mIcon;
        private final ImageView mMoreBtn;
        private final TextView mSize;

        public MyViewHolder(View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.tv_name);
            mIcon = itemView.findViewById(R.id.iv_icon);
            mMoreBtn = itemView.findViewById(R.id.iv_more);
            mSize = itemView.findViewById(R.id.tv_size);
        }
    }

}

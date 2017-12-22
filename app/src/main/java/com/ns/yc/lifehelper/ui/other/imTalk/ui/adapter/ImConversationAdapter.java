package com.ns.yc.lifehelper.ui.other.imTalk.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.ui.other.imTalk.model.ConversationList;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class ImConversationAdapter extends RecyclerView.Adapter<ImConversationAdapter.ViewHolder> {


    private LayoutInflater inflater;
    private List<ConversationList> mList;
    private Context mContext;
    private OnItemClickListener onItemClickListener;

    public ImConversationAdapter(Context mContext, List<ConversationList> mList) {
        this.mList = mList;
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_zh_theme, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(position,1);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.iv_theme_bg)
        ImageView themeBg;
        @Bind(R.id.tv_theme_kind)
        TextView themeKind;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position , int id);
    }
}

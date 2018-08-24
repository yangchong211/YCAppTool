package com.ns.yc.lifehelper.ui.other.vtex.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.ui.other.vtex.view.activity.WTNodeListActivity;
import com.zhy.view.flowlayout.FlowLayout;

import butterknife.BindView;
import butterknife.ButterKnife;


public class WTNodeAdapter extends RecyclerView.Adapter<WTNodeAdapter.ViewHolder> {

    private Context mContext;
    private LayoutInflater inflater;
    private ArrayMap<String, ArrayMap<String,String>> mMap;

    public WTNodeAdapter(Context context, ArrayMap<String, ArrayMap<String,String>> mMap) {
        this.mContext = context;
        this.mMap = mMap;
        inflater = LayoutInflater.from(mContext);
    }


    @Override
    public WTNodeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_wtex_node, parent, false));
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvTitle.setText(mMap.keyAt(position));
        holder.flContent.removeAllViews();
        ArrayMap<String,String> mNodeBlock = mMap.valueAt(position);
        for (ArrayMap.Entry<String,String> node : mNodeBlock.entrySet()) {
            TextView tvNode = new TextView(mContext);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(SizeUtils.dp2px(5),
                    SizeUtils.dp2px(5), SizeUtils.dp2px(5),
                    SizeUtils.dp2px(5));
            tvNode.setLayoutParams(layoutParams);
            tvNode.setText(node.getValue());
            tvNode.setTextSize(SizeUtils.sp2px(9));
            tvNode.setTextColor(ContextCompat.getColor(mContext, R.color.blackText1));
            tvNode.setBackground(mContext.getResources().getDrawable(R.drawable.shape_btn_color_bg));
            tvNode.setPadding(SizeUtils.dp2px(6f),
                    SizeUtils.dp2px(2f), SizeUtils.dp2px(6f),
                    SizeUtils.dp2px(2f));
            tvNode.setOnClickListener(new OnNodeClickListener(node.getKey()));
            holder.flContent.addView(tvNode);
        }
    }


    @Override
    public int getItemCount() {
        return mMap.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_node_title)
        TextView tvTitle;
        @BindView(R.id.fl_node_content)
        FlowLayout flContent;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    private class OnNodeClickListener implements View.OnClickListener {

        private String nodeName;
        OnNodeClickListener(String nodeName) {
            this.nodeName = nodeName;
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            intent.setClass(mContext, WTNodeListActivity.class);
            intent.putExtra("node_name", nodeName);
            mContext.startActivity(intent);
        }
    }


}

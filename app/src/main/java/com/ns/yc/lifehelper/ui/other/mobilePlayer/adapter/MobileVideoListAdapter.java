package com.ns.yc.lifehelper.ui.other.mobilePlayer.adapter;

import android.content.Context;
import android.database.Cursor;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.ui.other.mobilePlayer.bean.VideoItem;
import com.ns.yc.lifehelper.utils.AppUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/8/31
 * 描    述：视频播放器页面适配器
 * 修订历史：
 * ================================================
 */
public class MobileVideoListAdapter extends CursorAdapter {

    public MobileVideoListAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // 1、填充出一个View
        View view = View.inflate(context, R.layout.item_video_list, null);
        // 2、创建ViewHolder，并且findViewById
        ViewHolder holder = new ViewHolder(view);
        // 3、把ViewHolder保存到View中
        view.setTag(holder);
        return view;
    }

    // 显示数据
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        VideoItem item = VideoItem.fromCursor(cursor);
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.tvTitle.setText(item.title);                                     // 显示视频标题
        holder.tvSize.setText(Formatter.formatFileSize(context, item.size));    // 显示视频大小
        holder.tvDuration.setText(AppUtil.formatMillis(item.duration));           // 显示视频时间
    }

    static class ViewHolder {
        @Bind(R.id.tv_title)
        TextView tvTitle;
        @Bind(R.id.tv_size)
        TextView tvSize;
        @Bind(R.id.tv_duration)
        TextView tvDuration;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}

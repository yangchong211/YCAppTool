package com.ns.yc.lifehelper.ui.find.view.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.ui.data.view.activity.RiddleActivity;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/8/31
 * 描    述：谜语页面适配器
 * 修订历史：
 * ================================================
 */
public class PicRiddleAdapter extends BaseAdapter {

    private RiddleActivity activity;
    private String[] titles;

    public PicRiddleAdapter(RiddleActivity activity, String[] titles) {
        this.activity = activity;
        this.titles = titles;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View inflate = activity.getLayoutInflater().inflate(R.layout.item_grid_view, parent, false);
        TextView tv_grid = (TextView) inflate.findViewById(R.id.tv_grid);
        tv_grid.setText(titles[position]);
        return inflate;
    }

}

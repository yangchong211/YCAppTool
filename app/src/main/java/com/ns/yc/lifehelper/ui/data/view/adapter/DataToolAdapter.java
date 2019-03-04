package com.ns.yc.lifehelper.ui.data.view.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.ui.main.view.MainActivity;

import java.util.ArrayList;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/8/31
 * 描    述：实用工具适配器
 * 修订历史：
 * ================================================
 */
public class DataToolAdapter extends BaseAdapter {

    private String[] toolName;
    private ArrayList<Integer> toolLogo;
    private MainActivity activity;

    public DataToolAdapter(MainActivity activity, String[] toolName, ArrayList<Integer> toolLogo) {
        this.activity = activity;
        this.toolName = toolName;
        this.toolLogo = toolLogo;
    }

    @Override
    public int getCount() {
        return toolName.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder ;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = View.inflate(activity, R.layout.item_vp_grid_iv, null);
            holder.tv_name = convertView.findViewById(R.id.tv_new_seed_title);
            holder.iv_nul = convertView.findViewById(R.id.iv_new_seed_ic);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.tv_name.setText(toolName[position]);
        holder.iv_nul.setImageResource(toolLogo.get(position));
        return convertView;
    }

    private static class ViewHolder{
        private TextView tv_name;
        private ImageView iv_nul;
    }

}

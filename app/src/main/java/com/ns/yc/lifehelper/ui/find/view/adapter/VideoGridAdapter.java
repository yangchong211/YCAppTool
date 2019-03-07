package com.ns.yc.lifehelper.ui.find.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.ui.find.model.VideoIconBean;

import java.util.List;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/4/20
 * 描    述：ViewPagerGridAdapter
 * 修订历史：
 * ================================================
 */
public class VideoGridAdapter extends BaseAdapter {

    private Context context;
    private List<VideoIconBean> lists;
    private int mIndex;
    private int mPagerSize;

    public VideoGridAdapter(Context context, List<VideoIconBean> lists, int mIndex, int mPagerSize) {
        this.context = context;
        this.lists = lists;
        this.mIndex = mIndex;
        this.mPagerSize = mPagerSize;
    }

    /**
     * 先判断数据及的大小是否显示满本页lists.size() > (mIndex + 1)*mPagerSize
     * 如果满足，则此页就显示最大数量lists的个数
     * 如果不够显示每页的最大数量，那么剩下几个就显示几个
     */
    @Override
    public int getCount() {
        return lists.size() > (mIndex + 1) * mPagerSize ? mPagerSize : (lists.size() - mIndex*mPagerSize);
    }

    @Override
    public VideoIconBean getItem(int arg0) {
        return lists.get(arg0 + mIndex * mPagerSize);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0 + mIndex * mPagerSize;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder ;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_vp_grid_iv, null);
            holder.tvName = convertView.findViewById(R.id.tv_new_seed_title);
            holder.ivNul = convertView.findViewById(R.id.iv_new_seed_ic);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder)convertView.getTag();
        }
        //重新确定position因为拿到的总是数据源，数据源是分页加载到每页的GridView上的
        final int pos = position + mIndex * mPagerSize;
        //假设mPagerSize=8，假如点击的是第二页（即mIndex=1）上的第二个位置item(position=1),那么这个item的实际位置就是pos=9
        holder.tvName.setText(lists.get(pos).getName());
        holder.tvName.setTextSize(12);
        holder.ivNul.setImageResource(lists.get(pos).getResId());
        return convertView;
    }

    private static class ViewHolder{
        private TextView tvName;
        private ImageView ivNul;
    }
}

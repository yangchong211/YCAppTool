package com.ns.yc.lifehelper.ui.other.safe360.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.ui.weight.imageView.CircleImageView;


public class SafeHomeAdapter extends BaseAdapter {

    //图片数组
    int[] imgId={R.drawable.ic_safe_safe,R.drawable.ic_safe_callmsgsafe,R.drawable.ic_safe_app, R.drawable.ic_safe_trojan,
            R.drawable.ic_safe_sysoptimize,R.drawable.ic_safe_taskmanager, R.drawable.ic_safe_netmanager,R.drawable.ic_safe_atools,R.drawable.ic_safe_settings};
    String[] names={"手机防盗","通讯卫士","软件管家","手机杀毒","缓存清理","进程管理","流量统计","高级工具","设置中心"};
    private Context context;

    public SafeHomeAdapter(Context context) {
        this.context=context;
    }

    @Override
    public int getCount() {
        return names.length;
    }


    @Override
    public Object getItem(int position) {
        return null;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view= View.inflate(context,R.layout.item_vp_grid_iv,null);
        CircleImageView iv_icon= (CircleImageView) view.findViewById(R.id.iv_new_seed_ic);
        TextView tv_name = (TextView) view.findViewById(R.id.tv_new_seed_title);
        iv_icon.setImageResource(imgId[position]);
        tv_name.setText(names[position]);
        return view;
    }

}

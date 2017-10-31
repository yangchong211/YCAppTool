package com.ns.yc.lifehelper.ui.other.bookReader.view.adapter;

import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.ns.yc.lifehelper.R;

import java.util.List;


public class MinorAdapter extends EasyLVAdapter<String> {

    private int current = 0;

    public MinorAdapter(Context context, List<String> list) {
        super(context, list, R.layout.item_reader_minor_list);
    }

    @Override
    public void convert(EasyLVHolder holder, int position, String s) {
        holder.setText(R.id.tvMinorItem, s);

        if (current == position) {
            holder.setVisible(R.id.ivMinorChecked, true);
        } else {
            holder.setVisible(R.id.ivMinorChecked, false);
        }

        if (position != 0) { // 子项右移
            TextView textView = holder.getView(R.id.tvMinorItem);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) textView.getLayoutParams();
            params.leftMargin = SizeUtils.dp2px(25);
            textView.setLayoutParams(params);
        }
    }

    public void setChecked(int position) {
        current = position;
        notifyDataSetChanged();
    }
}

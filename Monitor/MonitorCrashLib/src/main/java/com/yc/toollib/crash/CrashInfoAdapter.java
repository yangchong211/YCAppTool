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

import com.yc.eastadapterlib.BaseRecycleAdapter;
import com.yc.eastadapterlib.BaseViewHolder;
import com.yc.toollib.R;

import java.io.File;
import java.util.List;

public class CrashInfoAdapter extends BaseRecycleAdapter<File> {

    public CrashInfoAdapter(Context context) {
        super(context, R.layout.item_crash_view);
    }

    @Override
    protected void bindData(BaseViewHolder holder, File file) {
        TextView tv_title = holder.getView(R.id.tv_title);
        TextView tv_path = holder.getView(R.id.tv_path);

        tv_path.setText(file.getAbsolutePath());
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
            tv_title.setText(spannable);
        } else {
            tv_title.setText(fileName);
        }
    }

}

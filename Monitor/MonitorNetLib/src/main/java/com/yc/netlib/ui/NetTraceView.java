package com.yc.netlib.ui;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.yc.netlib.R;
import com.yc.netlib.utils.NetWorkUtils;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

public class NetTraceView extends LinearLayout {

    private Context context;

    public NetTraceView(Context context) {
        super(context);
        init(context);
    }

    public NetTraceView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public NetTraceView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        setOrientation(VERTICAL);
    }

    public void addTraceDetail(LinkedHashMap<String, Long> traceDetailList){
        Set<String> strings = traceDetailList.keySet();
        Iterator<String> iterator = strings.iterator();
        int index = 0;
        while (iterator.hasNext()){
            String next = iterator.next();
            Long aLong = traceDetailList.get(next);
            boolean isLast;
            if (index == traceDetailList.size() - 1){
                isLast = true;
            } else {
                isLast = false;
            }
            index = index+1;
            addTraceItem(next,aLong,isLast);
        }
    }

    private void addTraceItem(String name , long costTime , boolean isLast){
        View view = LayoutInflater.from(context).inflate(R.layout.item_trace_view, null, false);
        View pointView = view.findViewById(R.id.tv_trace_point);
        TextView traceTextView = view.findViewById(R.id.tv_trace_content);
        View lineView = view.findViewById(R.id.view_trace_ling);
        StringBuffer sb = new StringBuffer();
        sb.append(name);
        sb.append("消耗时间");
        sb.append(" : ");
        sb.append(costTime);
        sb.append("ms");
        traceTextView.setText(sb.toString());
        if (isLast){
            lineView.setVisibility(GONE);
        } else {
            lineView.setVisibility(VISIBLE);
        }
        if (!TextUtils.isEmpty(name) && NetWorkUtils.isTimeout(context, name, costTime)){
            traceTextView.setTextColor(context.getResources().getColor(R.color.red));
        } else {
            traceTextView.setTextColor(context.getResources().getColor(R.color.trace_normal));
        }
        addView(view);
    }

}

package com.yc.lifehelper.component;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yc.cn.ycbannerlib.marquee.MarqueeView;
import com.yc.lifehelper.R;

import org.yczbj.ycrefreshviewlib.inter.InterItemView;

import java.util.ArrayList;

public class MarqueeComponent implements InterItemView {

    private Context context;

    @Override
    public View onCreateView(ViewGroup parent) {
        View headerView = View.inflate(parent.getContext(), R.layout.head_home_marquee, null);
        context = headerView.getContext();
        return headerView;
    }

    @Override
    public void onBindView(View header) {
        MarqueeView marqueeView = header.findViewById(R.id.marqueeView);
        setData(marqueeView);
    }

    private void setData(MarqueeView marqueeView) {
        if (marqueeView == null) {
            return;
        }
        ArrayList<String> list = getMarqueeTitle();
        //根据公告字符串列表启动轮播
        marqueeView.startWithList(list);
        //设置点击事件
        marqueeView.setOnItemClickListener(new MarqueeView.OnItemClickListener() {
            @Override
            public void onItemClick(int position, TextView textView) {

            }
        });
    }

    public ArrayList<String> getMarqueeTitle() {
        ArrayList<String> list = new ArrayList<>();
        String[] title = context.getResources().getStringArray(R.array.main_marquee_title);
        SpannableString ss1 = new SpannableString(title[0]);
        ss1.setSpan(new ForegroundColorSpan(Color.BLACK),  2, title[0].length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        list.add(ss1.toString());
        SpannableString ss2 = new SpannableString(title[1]);
        ss2.setSpan(new ForegroundColorSpan(Color.BLACK),  2, title[1].length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        list.add(ss2.toString());
        SpannableString ss3 = new SpannableString(title[2]);
        ss3.setSpan(new URLSpan("http://www.ximalaya.com/zhubo/71989305/"), 2, title[2].length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        list.add(ss3.toString());
        return list;
    }

}

package com.ns.yc.lifehelper.ui.find.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.ui.webView.view.WebViewActivity;
import com.ns.yc.lifehelper.ui.other.myNews.weChat.model.bean.WxNewsDetailBean;
import com.ns.yc.lifehelper.weight.textSpan.AwesomeTextHandler;


/**
 * 可以点击
 */
public class MentionSpanRenderer implements AwesomeTextHandler.ViewSpanRenderer, AwesomeTextHandler.ViewSpanClickListener {

    private int textSizeInDips = 14;
    private int backgroundResource = R.color.colorTransparent;
    private int textColorResource = R.color.redTab;
    private boolean clickable;
    private WxNewsDetailBean.ResultBean.ListBean data;


    public MentionSpanRenderer(int textSizeInDips, int backgroundResource, int textColorResource, WxNewsDetailBean.ResultBean.ListBean data) {
        this.textSizeInDips = textSizeInDips;
        this.backgroundResource = backgroundResource;
        this.textColorResource = textColorResource;
        this.data = data;
    }

    public void setClickable(boolean clickable) {
        this.clickable = clickable;
    }


    @Override
    public View getView(String text, Context context) {
        TextView view = new TextView(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            view.setForegroundGravity(Gravity.CENTER_VERTICAL);
        }
        view.setGravity(Gravity.CENTER_VERTICAL);
        view.setText(text.substring(1));
        view.setTextSize(SizeUtils.dp2px(textSizeInDips));
        view.setBackgroundResource(backgroundResource);
        int textColor = context.getResources().getColor(textColorResource);
        view.setTextColor(textColor);
        return view;
    }

    @Override
    public void onClick(String text, Context context) {
        if (clickable) {
            if (!TextUtils.isEmpty(data.getUrl())) {
                Intent intent = new Intent(context, WebViewActivity.class);
                intent.putExtra("name",data.getWeixinname());
                intent.putExtra("url",data.getUrl());
                context.startActivity(intent);
            }
        }
    }
}

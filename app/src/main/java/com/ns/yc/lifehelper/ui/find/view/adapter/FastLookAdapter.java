package com.ns.yc.lifehelper.ui.find.view.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.ui.other.myNews.wxNews.model.bean.WxNewsDetailBean;
import com.ns.yc.lifehelper.ui.weight.textSpan.AwesomeTextHandler;

import java.util.ArrayList;


/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/11/14
 * 描    述：快看页面
 * 修订历史：
 * ================================================
 */
public class FastLookAdapter extends RecyclerArrayAdapter<WxNewsDetailBean.ResultBean.ListBean> {

    private String regex = "(#[\\p{L}0-9-_]+)";
    private ArrayList<String> list = new ArrayList<>();


    public FastLookAdapter(Activity activity) {
        super(activity);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ExpressDeliveryViewHolder(parent);
    }


    private class ExpressDeliveryViewHolder extends BaseViewHolder<WxNewsDetailBean.ResultBean.ListBean>
            implements View.OnClickListener {

        TextView tv_time , tv_title , tv_content , tv_unfold , tv_share;

        ExpressDeliveryViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_fast_look_view);
            tv_time = $(R.id.tv_time);
            tv_title = $(R.id.tv_title);
            tv_content = $(R.id.tv_content);
            tv_unfold = $(R.id.tv_unfold);
            tv_share = $(R.id.tv_share);

            tv_content.setVisibility(View.GONE);
            tv_unfold.setOnClickListener(this);
            tv_share.setOnClickListener(this);
        }

        @Override
        public void setData(WxNewsDetailBean.ResultBean.ListBean data) {
            super.setData(data);
            tv_time.setText(data.getTime()==null ? "" : data.getTime());
            tv_title.setText(data.getTitle());
            tv_content.setText(data.getWeixinsummary() + "#" + "查看原文");
            list.add("展开");
            tv_unfold.setText("展开");

            MentionSpanRenderer renderer = new MentionSpanRenderer(16, R.color.colorTransparent, R.color.redTab, data);
            renderer.setClickable(true);
            AwesomeTextHandler handler = new AwesomeTextHandler();
            handler.addViewSpanRenderer(regex, renderer)
                    .setView(tv_content);
        }



        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.tv_unfold:
                    String content = list.get(getAdapterPosition());
                    if(content.equals("展开")){
                        tv_unfold.setText("收起");
                        list.set(getAdapterPosition(),"收起");
                        tv_unfold.setCompoundDrawablesWithIntrinsicBounds(null, null,
                                getContext().getResources().getDrawable(R.drawable.ic_fold_above), null);
                        tv_content.setVisibility(View.GONE);
                    }else {
                        tv_unfold.setText("展开");
                        list.set(getAdapterPosition(),"展开");
                        tv_unfold.setCompoundDrawablesWithIntrinsicBounds(null, null,
                                getContext().getResources().getDrawable(R.drawable.ic_fold_bottom), null);
                        tv_content.setVisibility(View.VISIBLE);
                    }
                    break;
                case R.id.tv_share:

                    break;
            }
        }
    }
}

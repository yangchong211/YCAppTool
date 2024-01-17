package com.yc.appmonitor.apm;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.yc.appmonitor.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: 杨充
 * @email : yangchong211@163.com
 * @time : 2022/04/28
 * @desc : apm小工具
 * @revise :
 */
public class ListFragment extends Fragment {

    private ListView mLv;
    private List<LvItem> mData;
    private LvAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mLv = new ListView(getContext());
        mLv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        return mLv;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    private void initData() {
        if (mData == null) {
            mData = new ArrayList<>();
        }
        mAdapter = new LvAdapter(getContext());
        mLv.setAdapter(mAdapter);
    }


    public void addAllList(List<LvItem> items) {
        if (mData == null) {
            mData = new ArrayList<>();
        }
        mData.addAll(items);
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }

    }


    public static class LvItem {
        public String name;
        public OnClick onClick;

        public LvItem(String name, OnClick onClick) {
            this.name = name;
            this.onClick = onClick;
        }
    }

    public interface OnClick {
        void click(View view);
    }

    private class LvAdapter extends BaseAdapter {

        private Context context;

        public LvAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LinearLayout linearLayout = new LinearLayout(context);
            Button accountButton = new Button(context);
            accountButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mData.get(position).onClick.click(v);
                }
            });
            accountButton.setText(mData.get(position).name);
            accountButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            accountButton.setTextColor(getResources().getColor(R.color.white));
            LinearLayout.LayoutParams btParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            btParams.topMargin = dip2px(10, context);
            btParams.bottomMargin = dip2px(10, context);
            linearLayout.addView(accountButton, btParams);
            return linearLayout;
        }
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(float dpValue, @NonNull Context context) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}

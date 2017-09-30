package com.ns.yc.lifehelper.ui.other.timeListener.view;

import android.content.Context;
import android.os.Bundle;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.BaseFragment;
import com.ns.yc.lifehelper.ui.other.timeListener.TimeListenerActivity;

/**
 * Created by PC on 2017/9/1.
 * 作者：PC
 */

public class TimeListenerFragment extends BaseFragment {

    private static final String TYPE = "type";
    private TimeListenerActivity activity;
    private String mType;

    public static TimeListenerFragment getInstance(String param1) {
        TimeListenerFragment fragment = new TimeListenerFragment();
        Bundle args = new Bundle();
        args.putString(TYPE, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (TimeListenerActivity) context;

    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mType = getArguments().getString(TYPE);
        }

    }


    @Override
    public int getContentView() {
        return R.layout.base_easy_recycle;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }
}

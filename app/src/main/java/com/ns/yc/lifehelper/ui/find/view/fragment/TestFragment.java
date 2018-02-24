package com.ns.yc.lifehelper.ui.find.view.fragment;

import android.content.Context;
import android.os.Bundle;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.mvp1.BaseFragment;
import com.ns.yc.lifehelper.ui.main.view.MainActivity;

/**
 * Created by PC on 2017/11/16.
 * 作者：PC
 */

public class TestFragment extends BaseFragment {

    private static final String TYPE = "";
    private String mType;
    private MainActivity activity;

    public static TestFragment newInstance(String param1) {
        TestFragment fragment = new TestFragment();
        Bundle args = new Bundle();
        args.putString(TYPE, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
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
            Bundle arguments = getArguments();
            mType = arguments.getString(TYPE);
        }
    }


    @Override
    public int getContentView() {
        return R.layout.activity_about_me;
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

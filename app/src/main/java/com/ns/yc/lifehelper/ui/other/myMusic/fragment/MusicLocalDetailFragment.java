package com.ns.yc.lifehelper.ui.other.myMusic.fragment;

import android.content.Context;
import android.os.Bundle;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.mvp1.BaseFragment;
import com.ns.yc.lifehelper.ui.other.myMusic.activity.MyMusicLocalActivity;


/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/8/25
 * 描    述：我的音乐，本地音乐详情页面
 * 修订历史：
 * ================================================
 */
public class MusicLocalDetailFragment extends BaseFragment {

    private MyMusicLocalActivity activity;

    private static final String TYPE = "param";
    private String mType;

    public static MusicLocalDetailFragment newInstance(String param) {
        MusicLocalDetailFragment fragment = new MusicLocalDetailFragment();
        Bundle args = new Bundle();
        args.putString(TYPE, param);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mType = getArguments().getString(TYPE);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MyMusicLocalActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
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

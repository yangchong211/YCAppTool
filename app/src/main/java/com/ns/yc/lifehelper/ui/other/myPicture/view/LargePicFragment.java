package com.ns.yc.lifehelper.ui.other.myPicture.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.BaseFragment;
import com.ns.yc.lifehelper.ui.weight.imageView.TouchImageView;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/9/4
 * 描    述：美图查看大图
 * 修订历史：
 * ================================================
 */
public class LargePicFragment extends BaseFragment {

    @Bind(R.id.image)
    TouchImageView image;
    private MyPicLargeActivity activity;
    private String url;
    private boolean initialShown;

    public static Fragment newFragment(String url, boolean initialShown) {
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        bundle.putBoolean("initial_shown", initialShown);
        LargePicFragment fragment = new LargePicFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MyPicLargeActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        url = getArguments().getString("url");
        initialShown = getArguments().getBoolean("initial_shown", false);
    }

    @Override
    public void onResume() {
        super.onResume();
        Picasso.with(activity)
                .load(url)
                .into(image);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_my_pic_detail;
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

    View getSharedElement() {
        return image;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}

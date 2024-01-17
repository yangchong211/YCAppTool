package com.yc.common.vp;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.yc.common.R;
import com.yc.library.base.mvp.BaseFragment;

public class ViewPagerFragment extends BaseFragment {

    private Toolbar toolbar;
    private TextView tvTitleLeft;
    private FrameLayout llTitleMenu;
    private TextView toolbarTitle;
    private FrameLayout llSearch;
    private ImageView ivRightImg;
    private TextView tvTitleRight;
    private TabLayout tabLayout;
    private ViewPager vpContent;

    public static Fragment newInstance(String title){
        Fragment fragment = new Fragment();
        Bundle bundle = new Bundle();
        bundle.putString("title" , title);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getContentView() {
        return com.yc.library.R.layout.base_tab_view;
    }

    @Override
    public void initView(View view) {
        toolbar = view.findViewById(R.id.toolbar);
        tvTitleLeft = view.findViewById(R.id.tv_title_left);
        llTitleMenu = view.findViewById(R.id.ll_title_menu);
        toolbarTitle = view.findViewById(R.id.toolbar_title);
        llSearch = view.findViewById(R.id.ll_search);
        ivRightImg = view.findViewById(R.id.iv_right_img);
        tvTitleRight = view.findViewById(R.id.tv_title_right);
        tabLayout = view.findViewById(R.id.tab_layout);
        vpContent = view.findViewById(R.id.vp_content);
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        if (getArguments() != null) {
            toolbarTitle.setText(getArguments().getString("title"));
        }
    }


}

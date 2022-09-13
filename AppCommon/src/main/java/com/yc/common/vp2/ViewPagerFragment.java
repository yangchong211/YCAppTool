package com.yc.common.vp2;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.yc.baseclasslib.adapter.BaseFragmentPagerAdapter;
import com.yc.common.R;
import com.yc.library.base.mvp.BaseFragment;

import java.util.ArrayList;
import java.util.List;

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
        ViewPagerFragment fragment = new ViewPagerFragment();
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
            String title = getArguments().getString("title");
            toolbarTitle.setText(title);

            List<Fragment> fragments = new ArrayList<>();
            ArrayList<String> mTitleList = new ArrayList<>();
            if ("发现".equals(title)){
                // fragments
                fragments.add(TextFragment.newInstance("聊天"));
                fragments.add(TextFragment.newInstance("通讯录"));

                mTitleList.add("请求");
                mTitleList.add("流量");

                FragmentManager supportFragmentManager = this.getChildFragmentManager();
                BaseFragmentPagerAdapter adapter = new BaseFragmentPagerAdapter(supportFragmentManager);
                adapter.addFragmentList(fragments,mTitleList);
                vpContent.setAdapter(adapter);

                vpContent.setAdapter(adapter);
                vpContent.setCurrentItem(0);
                tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
                tabLayout.setupWithViewPager(vpContent);
            } else {
                fragments.add(TextFragment.newInstance("聊天"));
                mTitleList.add("");
                FragmentManager supportFragmentManager = this.getChildFragmentManager();
                BaseFragmentPagerAdapter adapter = new BaseFragmentPagerAdapter(supportFragmentManager);
                adapter.addFragmentList(fragments,mTitleList);
                vpContent.setAdapter(adapter);
                tabLayout.setVisibility(View.GONE);
            }
        }
    }


}

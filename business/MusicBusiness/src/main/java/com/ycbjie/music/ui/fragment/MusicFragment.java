package com.ycbjie.music.ui.fragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.ycbjie.library.base.adapter.BasePagerAdapter;
import com.ycbjie.library.base.mvp.BaseFragment;
import com.ycbjie.music.R;
import com.ycbjie.music.ui.activity.MusicActivity;

import java.util.ArrayList;
import java.util.List;


public class MusicFragment extends BaseFragment implements View.OnClickListener {


    private Toolbar toolbar;
    private ImageView ivMenu;
    private LinearLayout llOther;
    private SegmentTabLayout stlLayout;
    private FrameLayout flSearch;
    private ViewPager vpContent;
    private MusicActivity activity;
    private LocalMusicFragment mLocalMusicFragment;
    private List<Fragment> fragments;
    private String[] mMusicTitles = {"我的音乐", "在线音乐"};


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MusicActivity) context;
    }


    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }


    @Override
    public int getContentView() {
        return R.layout.fragment_music;
    }


    @Override
    public void initView(View view) {
        initFindById(view);
        initViewPager();
        initFragment();
    }

    private void initFindById(View view) {
        toolbar = view.findViewById(R.id.toolbar);
        ivMenu = view.findViewById(R.id.iv_menu);
        llOther = view.findViewById(R.id.ll_other);
        stlLayout = view.findViewById(R.id.stl_layout);
        flSearch = view.findViewById(R.id.fl_search);
        vpContent = view.findViewById(R.id.vp_content);
    }


    @Override
    public void initListener() {

    }


    @Override
    public void initData() {
        stlLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                vpContent.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
    }


    @Override
    public void onClick(View v) {

    }


    private void initViewPager() {
        stlLayout.setTabData(mMusicTitles);
        vpContent.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position>=0){
                    stlLayout.setCurrentTab(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    private void initFragment() {
        fragments = new ArrayList<>();
        mLocalMusicFragment = new LocalMusicFragment();
        fragments.add(mLocalMusicFragment);
        fragments.add(new OnLineMusicFragment());
        BasePagerAdapter adapter = new BasePagerAdapter(getChildFragmentManager(), fragments);
        vpContent.setAdapter(adapter);
        vpContent.setCurrentItem(0);
        vpContent.setOffscreenPageLimit(fragments.size());
    }


    public void onItemPlay() {
        if (mLocalMusicFragment != null && mLocalMusicFragment.isAdded()) {
            mLocalMusicFragment.onItemPlay();
        }
    }


    public void onDoubleClick() {
        if (fragments != null && fragments.size() > 0) {
            int item = vpContent.getCurrentItem();
            switch (item){
                case 0:
                    ((LocalMusicFragment) fragments.get(item)).onRefresh();
                    break;
                case 1:
                    ((OnLineMusicFragment) fragments.get(item)).onRefresh();
                    break;
                default:
                    break;
            }
        }
    }


}

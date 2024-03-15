package com.yc.common.tab;

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
import com.yc.common.vp2.TextFragment;
import com.yc.library.base.mvp.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class VpTabActivity extends BaseActivity {
    
    private Toolbar toolbar;
    private TextView tvTitleLeft;
    private FrameLayout llTitleMenu;
    private TextView toolbarTitle;
    private FrameLayout llSearch;
    private ImageView ivRightImg;
    private TextView tvTitleRight;
    private TabLayout tabLayout;
    private ViewPager vpContent;

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int getContentView() {
        return R.layout.base_tab_view;
    }

    @Override
    public void initView() {
        toolbar = findViewById(R.id.toolbar);
        tvTitleLeft = findViewById(R.id.tv_title_left);
        llTitleMenu = findViewById(R.id.ll_title_menu);
        toolbarTitle = findViewById(R.id.toolbar_title);
        llSearch = findViewById(R.id.ll_search);
        ivRightImg = findViewById(R.id.iv_right_img);
        tvTitleRight = findViewById(R.id.tv_title_right);
        tabLayout = findViewById(R.id.tab_layout);
        vpContent = findViewById(R.id.vp_content);
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

        List<Fragment> fragments = new ArrayList<>();
        // fragments
        fragments.add(TextFragment.newInstance("聊天"));
        fragments.add(TextFragment.newInstance("通讯录"));
        fragments.add(TextFragment.newInstance("发现"));
        fragments.add(TextFragment.newInstance("我"));

        ArrayList<String> mTitleList = new ArrayList<>();
        mTitleList.add("请求数据");
        mTitleList.add("流量");
        mTitleList.add("时间");
        mTitleList.add("容量");

        FragmentManager supportFragmentManager = getSupportFragmentManager();
        BaseFragmentPagerAdapter adapter = new BaseFragmentPagerAdapter(supportFragmentManager);
        adapter.addFragmentList(fragments,mTitleList);
        //BasePagerStateAdapter adapter = new BasePagerStateAdapter(this.getSupportFragmentManager(), fragments);
        vpContent.setAdapter(adapter);
        vpContent.setCurrentItem(0);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(vpContent);
        adapter.notifyDataSetChanged();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        vpContent.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}

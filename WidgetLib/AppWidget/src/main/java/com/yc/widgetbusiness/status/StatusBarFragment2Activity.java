package com.yc.widgetbusiness.status;

import android.os.Bundle;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.yc.statusbar.utils.StatusBarUtils;

import java.util.ArrayList;
import java.util.List;

import cn.ycbjie.ycstatusbar.R;

public class StatusBarFragment2Activity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewpager;
    private ViewPager mViewpager;
    private TabLayout mTabLayout;

    /**
     * 方法2：布局里添加占位状态栏
     * 对一个Activity中有多个Fragment，每个Fragment都有不同的状态栏颜色
     * 1.在activity中设置状态栏隐藏或者透明
     * 2.在activity中设置[如果不在activity中设置，则需要在activity中的fragment都设置]
     * android:fitsSystemWindows="true"
     * 3.添加空的view就是用来填充状态栏的
     * 4.设置view的颜色
     * 遇到问题：由于设置了主题背景颜色为白色，默认白色。因此，即使设置View的颜色透明也无法生效
     * <p>
     * <p>
     * <p>
     * 方法3：
     */


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statusbar_fragment);
        initView();


        StatusBarUtils.StatusBarLightMode(this);

        ArrayList<String> titleList = new ArrayList<>();
        ArrayList<Fragment> fragmentList = new ArrayList<>();
        BasePagerAdapter pagerAdapter = new BasePagerAdapter(getSupportFragmentManager(), fragmentList, titleList);
        viewpager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewpager);
        titleList.add("白色");
        titleList.add("黄色");
        titleList.add("红色");
        titleList.add("透明色");
        fragmentList.add(new StatusBarFirst2Fragment());
        fragmentList.add(new StatusBarSecond2Fragment());
        fragmentList.add(new StatusBarThird2Fragment());
        fragmentList.add(new StatusBarFour2Fragment());
        pagerAdapter.notifyDataSetChanged();
        for (int i = 0; i < titleList.size(); i++) {
            tabLayout.addTab(tabLayout.newTab().setText(titleList.get(i)));
        }
        pagerAdapter.notifyDataSetChanged();
    }

    private void initView() {
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
    }


    public class BasePagerAdapter extends FragmentPagerAdapter {

        private List<?> mFragment;
        private List<String> mTitleList;

        /**
         * 普通，主页使用
         */
        public BasePagerAdapter(FragmentManager fm, List<?> mFragment) {
            super(fm);
            this.mFragment = mFragment;
        }

        /**
         * 接收首页传递的标题
         */
        public BasePagerAdapter(FragmentManager fm, List<?> mFragment, List<String> mTitleList) {
            super(fm);
            this.mFragment = mFragment;
            this.mTitleList = mTitleList;
        }

        @Override
        public Fragment getItem(int position) {
            return (Fragment) mFragment.get(position);
        }

        @Override
        public int getCount() {
            return mFragment.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }

        /**
         * 首页显示title，每日推荐等..
         * 若有问题，移到对应单独页面
         */
        @Override
        public CharSequence getPageTitle(int position) {
            if (mTitleList != null) {
                return mTitleList.get(position);
            } else {
                return "";
            }
        }

        public void addFragmentList(List<?> fragment) {
            this.mFragment.clear();
            this.mFragment = null;
            this.mFragment = fragment;
            notifyDataSetChanged();
        }

    }

}

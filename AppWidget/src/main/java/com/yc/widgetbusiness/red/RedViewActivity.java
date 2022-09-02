package com.yc.widgetbusiness.red;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.yc.reddot.YCRedDotView;
import com.yc.widgetbusiness.R;

import java.util.ArrayList;


public class RedViewActivity extends AppCompatActivity {


    private TabLayout tab_view;
    private ViewPager view_pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_red_view);
        initView();
    }

    private void initView() {
        TextView tv1 = findViewById(R.id.tv_1);
        TextView tv2 = findViewById(R.id.tv_2);
        TextView tv3 = findViewById(R.id.tv_3);
        LinearLayout ll_view_4 = findViewById(R.id.ll_view_4);
        tab_view = findViewById(R.id.tab_view);
        view_pager = findViewById(R.id.view_pager);

        //创建红点View
        YCRedDotView ycRedDotView = new YCRedDotView(this);
        //设置依附的View
        ycRedDotView.setTargetView(tv1);
        //设置红点的数字
        ycRedDotView.setBadgeCount(10);
        //设置红点位置
        ycRedDotView.setRedHotViewGravity(Gravity.END);
        //获取小红点的数量
        int count = ycRedDotView.getBadgeCount();
        //如果是设置小红点，不设置数字，则可以用这个，设置属性是直径
        //ycRedDotView.setBadgeView(10);


        YCRedDotView ycRedDotView2 = new YCRedDotView(this);
        ycRedDotView2.setTargetView(tv2);
        ycRedDotView2.setBadgeCount(1);
        ycRedDotView2.setRedHotViewGravity(Gravity.START);

        YCRedDotView ycRedDotView3 = new YCRedDotView(this);
        ycRedDotView3.setTargetView(tv3);
        ycRedDotView3.setBadgeView(10);
        ycRedDotView3.setRedHotViewGravity(Gravity.END);
        //设置margin
        ycRedDotView3.setBadgeMargin(0,10,20,0);


        YCRedDotView ycRedDotView4 = new YCRedDotView(this);
        ycRedDotView4.setTargetView(ll_view_4);
        ycRedDotView4.setBadgeCount(100);
        ycRedDotView4.setHideNull(true);
        int badgeCount = ycRedDotView4.getBadgeCount();
        Log.d("小红点数量4---------",badgeCount+"");
        ycRedDotView4.setRedHotViewGravity(Gravity.END);
        ycRedDotView4.setBadgeMargin(0,10,20,0);

        initFragmentList();
    }



    private void initFragmentList() {
        ArrayList<String> mTitleList = new ArrayList<>();
        ArrayList<Fragment> mFragments = new ArrayList<>();
        mTitleList.add("日报");
        mTitleList.add("主题");
        mTitleList.add("专栏");
        mTitleList.add("热门");
        mFragments.add(new TabFragment());
        mFragments.add(new TabFragment());
        mFragments.add(new TabFragment());
        mFragments.add(new TabFragment());
        /*
         * 这样setOffscreenPageLimit()就可以添加上，保留相邻2个实例，切换时不会卡
         * 但会内存溢出，在显示时加载数据
         */
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        ContentPageAdapter myAdapter = new ContentPageAdapter(supportFragmentManager,
                mFragments, mTitleList,this);
        view_pager.setAdapter(myAdapter);
        myAdapter.notifyDataSetChanged();
        // 左右预加载页面的个数
        view_pager.setOffscreenPageLimit(mFragments.size());
        tab_view.setTabMode(TabLayout.MODE_FIXED);
        tab_view.setupWithViewPager(view_pager);
        tab_view.setTabTextColors(getResources().getColor(R.color.colorAccent),
                getResources().getColor(R.color.colorAccent));
        tab_view.setSelectedTabIndicatorHeight(2);
        tab_view.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorAccent));
        tab_view.removeAllTabs();

        for (int i = 0; i < mTitleList.size(); i++) {
            TabLayout.Tab tab = tab_view.newTab();
            tab.setCustomView(myAdapter.getTabView(i));
            tab_view.addTab(tab);
        }


        YCRedDotView ycRedDotView5 = new YCRedDotView(this);
        ycRedDotView5.setTargetView(tab_view,0);
        ycRedDotView5.setBadgeCount(100);
        ycRedDotView5.setRedHotViewGravity(Gravity.END);
        ycRedDotView5.setBadgeMargin(0,0,0,0);


        YCRedDotView ycRedDotView = new YCRedDotView(this);
        ycRedDotView.setTargetView(tab_view,1);
        ycRedDotView.setBadgeCount(10);


        YCRedDotView ycRedDotView6 = new YCRedDotView(this);
        ycRedDotView6.setTargetView(tab_view,2);
        ycRedDotView6.setBadgeView(10);
        ycRedDotView6.setRedHotViewGravity(Gravity.END);
        ycRedDotView6.setBadgeMargin(0,10,10,0);
    }

    public class ContentPageAdapter extends FragmentStatePagerAdapter {

        private ArrayList<Fragment> fragmentList;
        private ArrayList<String> tabBeanList;
        private FragmentActivity activity;

        ContentPageAdapter(FragmentManager fm, ArrayList<Fragment> fragmentList,
                           ArrayList<String> tabBeanList, FragmentActivity mainActivity) {
            super(fm);
            this.activity = mainActivity;
            this.fragmentList = fragmentList;
            this.tabBeanList = tabBeanList;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList==null ? 0 : fragmentList.size();
        }


        View getTabView(int position) {
            View view = LayoutInflater.from(activity).inflate(R.layout.main_tab_layout,
                    null, false);
            TextView textView = view.findViewById(R.id.tv_tab_text);
            textView.setText(tabBeanList.get(position));
            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        }
    }


}

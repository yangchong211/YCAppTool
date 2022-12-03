package com.yc.netlib.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.yc.basevpadapter.adapter.BaseFragmentPagerAdapter;
import com.yc.netlib.R;
import com.yc.toolutils.AppIntentUtils;

import java.util.ArrayList;

public class NetRequestActivity extends AppCompatActivity {

    private LinearLayout mLlBack;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private TextView mTvAbout;

    public static void start(Context context){
        try {
            Intent intent = new Intent(context, NetRequestActivity.class);
            //为activity开启新的栈，Intent.FLAG_ACTIVITY_NEW_TASK 设置状态，
            //首先查找是否存在和被启动的Activity具有相同的任务栈，如果有则直接把这个栈整体移到前台，并保持栈中的状态不变，
            //既栈中的activity顺序不变，如果没有，则新建一个栈来存放被启动的Activity。
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);
        initFindViewById();
        initTabLayout();
        initListener();
    }

    private void initFindViewById() {
        mLlBack = findViewById(R.id.ll_back);
        mTabLayout = findViewById(R.id.tab_layout);
        mViewPager = findViewById(R.id.view_pager);
        mTvAbout = findViewById(R.id.tv_about);
    }

    private void initTabLayout() {
        ArrayList<String> mTitleList = new ArrayList<>();
        ArrayList<Fragment> mFragments = new ArrayList<>();
        mTitleList.add("请求内容");
        mTitleList.add("流量统计");
        mTitleList.add("消耗时间");
        //mTitleList.add("设备信息");
        mFragments.add(new NetRequestListFragment());
        mFragments.add(new NetRequestInfoFragment());
        mFragments.add(new NetRequestTraceFragment());
        /*
         * 注意使用的是：getChildFragmentManager，
         * 这样setOffscreenPageLimit()就可以添加上，保留相邻2个实例，切换时不会卡
         * 但会内存溢出，在显示时加载数据
         */
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        BaseFragmentPagerAdapter myAdapter = new BaseFragmentPagerAdapter(supportFragmentManager);
        myAdapter.addFragmentList(mFragments,mTitleList);
        mViewPager.setAdapter(myAdapter);
        // 左右预加载页面的个数
        mViewPager.setOffscreenPageLimit(mFragments.size());
        myAdapter.notifyDataSetChanged();
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabLayout.setupWithViewPager(mViewPager);
    }


    private void initListener() {
        mLlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTvAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppIntentUtils.openLink(NetRequestActivity.this,
                        "https://github.com/yangchong211/YCAndroidTool");
            }
        });
    }
}

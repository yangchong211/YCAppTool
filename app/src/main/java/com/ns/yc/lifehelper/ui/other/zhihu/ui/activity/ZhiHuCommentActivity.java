package com.ns.yc.lifehelper.ui.other.zhihu.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.mvp1.BaseActivity;
import com.ns.yc.lifehelper.base.adapter.BasePagerAdapter;
import com.ns.yc.lifehelper.ui.other.zhihu.ui.fragment.ZhiHuCommentFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;


/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/12/1
 * 描    述：知乎评论
 * 修订历史：
 * ================================================
 */
public class ZhiHuCommentActivity extends BaseActivity {

    @Bind(R.id.ll_title_menu)
    FrameLayout llTitleMenu;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.tab_layout)
    TabLayout tabLayout;
    @Bind(R.id.vp_content)
    ViewPager vpContent;


    @Override
    public int getContentView() {
        return R.layout.base_tab_view;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void initView() {
        Intent intent = getIntent();
        int allNum = intent.getExtras().getInt("allNum");
        int shortNum = intent.getExtras().getInt("shortNum");
        int longNum = intent.getExtras().getInt("longNum");
        int id = intent.getExtras().getInt("id");
        toolbarTitle.setText(String.format("%d条评论",allNum));

        List<ZhiHuCommentFragment> fragments = new ArrayList<>();
        ZhiHuCommentFragment shortCommentFragment = new ZhiHuCommentFragment();
        Bundle shortBundle = new Bundle();
        shortBundle.putInt("id", id);
        shortBundle.putInt("kind", 0);
        shortCommentFragment.setArguments(shortBundle);

        ZhiHuCommentFragment longCommentFragment = new ZhiHuCommentFragment();
        Bundle longBundle = new Bundle();
        longBundle.putInt("id", id);
        longBundle.putInt("kind", 1);
        longCommentFragment.setArguments(longBundle);
        fragments.add(shortCommentFragment);
        fragments.add(longCommentFragment);
        BasePagerAdapter mAdapter = new BasePagerAdapter(getSupportFragmentManager(), fragments);
        vpContent.setAdapter(mAdapter);
        tabLayout.addTab(tabLayout.newTab().setText(String.format("短评论(%d)",shortNum)));
        tabLayout.addTab(tabLayout.newTab().setText(String.format("长评论(%d)",longNum)));
        tabLayout.setupWithViewPager(vpContent);
        TabLayout.Tab tabAt0 = tabLayout.getTabAt(0);
        TabLayout.Tab tabAt1 = tabLayout.getTabAt(1);
        if(tabAt0!=null){
            tabAt0.setText(String.format("短评论(%d)",shortNum));
        }
        if(tabAt1!=null){
            tabAt1.setText(String.format("长评论(%d)",longNum));
        }
    }


    @Override
    public void initListener() {
        llTitleMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    public void initData() {

    }
}

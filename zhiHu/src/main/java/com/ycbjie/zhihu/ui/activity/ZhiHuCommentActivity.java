package com.ycbjie.zhihu.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.ycbjie.library.base.mvp.BaseActivity;
import com.ycbjie.library.base.adapter.BasePagerAdapter;
import com.ycbjie.zhihu.R;
import com.ycbjie.zhihu.ui.fragment.ZhiHuCommentFragment;
import java.util.ArrayList;
import java.util.List;



/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/04/21
 *     desc  : 知乎评论
 *     revise:
 * </pre>
 */
public class ZhiHuCommentActivity extends BaseActivity {

    FrameLayout llTitleMenu;
    TextView toolbarTitle;
    TabLayout tabLayout;
    ViewPager vpContent;


    @Override
    public int getContentView() {
        return R.layout.base_tab_view;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void initView() {
        llTitleMenu = findViewById(R.id.ll_title_menu);
        toolbarTitle = findViewById(R.id.toolbar_title);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        vpContent = (ViewPager) findViewById(R.id.vp_content);


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

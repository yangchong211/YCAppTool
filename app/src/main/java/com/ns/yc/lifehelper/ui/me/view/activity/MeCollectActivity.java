package com.ns.yc.lifehelper.ui.me.view.activity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;

import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.ui.me.view.fragment.MeDocCollectFragment;
import com.ns.yc.lifehelper.ui.me.view.fragment.MeNewsCollectFragment;
import com.ns.yc.lifehelper.ui.me.view.fragment.MePicCollectFragment;
import com.ycbjie.library.base.adapter.BasePagerAdapter;
import com.ycbjie.library.base.mvp.BaseActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/9/12
 *     desc  : 我的收藏页面
 *     revise: v1.4 17年6月8日
 *             v1.5 17年10月3日修改
 * </pre>
 */
public class MeCollectActivity extends BaseActivity implements View.OnClickListener {

    private FrameLayout flTitleMenu;
    private SlidingTabLayout stlTable;
    private ViewPager vpContent;

    @Override
    public int getContentView() {
        return R.layout.activity_me_collect;
    }

    @Override
    public void initView() {
        flTitleMenu = findViewById(R.id.fl_title_menu);
        stlTable = findViewById(R.id.stl_table);
        vpContent = findViewById(R.id.vp_content);
        initViewPager();
        initTabLayout();
    }

    @Override
    public void initListener() {
        flTitleMenu.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fl_title_menu:
                finish();
                break;
            default:
                break;
        }
    }


    private void initViewPager() {
        String[] mTitles = {"干货", "新闻", "图片","文件"};
        ArrayList<Fragment> mFragments = new ArrayList<>();
        List<String> list = new ArrayList<>();
        mFragments.clear();
        mFragments.add(new MeNewsCollectFragment());
        mFragments.add(new MeNewsCollectFragment());
        mFragments.add(new MePicCollectFragment());
        mFragments.add(new MeDocCollectFragment());
        Collections.sort(list);
        List<String> strings = Arrays.asList(mTitles);
        list.addAll(strings);
        BasePagerAdapter adapter = new BasePagerAdapter(getSupportFragmentManager(),mFragments,list);
        vpContent.setAdapter(adapter);
        //限制预加载页面为4，这一步至关重要
        vpContent.setOffscreenPageLimit(4);
    }

    private void initTabLayout() {
        stlTable.setViewPager(vpContent);
        stlTable.setCurrentTab(0);
        stlTable.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                vpContent.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
    }


}

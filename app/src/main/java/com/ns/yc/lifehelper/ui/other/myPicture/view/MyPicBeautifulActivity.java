package com.ns.yc.lifehelper.ui.other.myPicture.view;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.adapter.BasePagerAdapter;
import com.ns.yc.lifehelper.base.mvp1.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/9/1
 * 描    述：美图欣赏
 * 修订历史：
 * ================================================
 */
public class MyPicBeautifulActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.ll_title_menu)
    FrameLayout llTitleMenu;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.ll_search)
    FrameLayout llSearch;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.stl_tab)
    SlidingTabLayout stlTab;
    @Bind(R.id.vp_content)
    ViewPager vpContent;
    private List<Fragment> fragments = new ArrayList<>();


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int getContentView() {
        return R.layout.base_stl_tab_view;
    }

    @Override
    public void initView() {
        initToolBar();
        initTabData();
    }

    private void initToolBar() {
        toolbarTitle.setText("图片欣赏");
        llSearch.setVisibility(View.VISIBLE);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //去除默认Title显示
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }


    @Override
    public void initListener() {
        llSearch.setOnClickListener(this);
        llTitleMenu.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_search:

                break;
            case R.id.ll_title_menu:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_news_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.other:

                break;
            case R.id.share:
                ToastUtils.showShort("分享");
                break;
            case R.id.collect:
                ToastUtils.showShort("收藏");
                break;
            case R.id.about:
                ToastUtils.showShort("关于");
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private String[] tabTitles = {"热门","推荐","段子","养生","私房话","八卦"};
    private String[] tabIds = {"japan","xinggan","japan","taiwan","mm","mm"};
    private void initTabData() {
        List<String> titles = new ArrayList<>();
        for(int a=0 ; a<tabTitles.length ; a++){
            fragments.add(MyPicBeautifulFragment.newInstance(tabIds[a]));
            titles.add(tabTitles[a]);
        }
        BasePagerAdapter adapter = new BasePagerAdapter(getSupportFragmentManager(),fragments,titles);
        vpContent.setAdapter(adapter);
        vpContent.setCurrentItem(0);
        vpContent.setOffscreenPageLimit(5);

        stlTab.setIndicatorGravity(Gravity.BOTTOM);
        stlTab.setViewPager(vpContent);
        stlTab.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {

            }

            @Override
            public void onTabReselect(int position) {

            }
        });
    }


}

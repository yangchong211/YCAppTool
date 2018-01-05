package com.ns.yc.lifehelper.ui.other.myNews.wxNews;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.mvp1.BaseActivity;
import com.ns.yc.lifehelper.base.adapter.BasePagerAdapter;
import com.ns.yc.lifehelper.ui.other.myNews.weChat.view.activity.TxWeChatNewsActivity;
import com.ns.yc.lifehelper.ui.other.myNews.wxNews.contract.WxNewsContract;
import com.ns.yc.lifehelper.ui.other.myNews.wxNews.presenter.WxNewsPresenter;
import com.ns.yc.lifehelper.ui.other.myNews.wxNews.view.NewsSearchActivity;
import com.ns.yc.lifehelper.ui.other.myNews.wxNews.view.WxNewsFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/4/30
 * 描    述：微信新闻
 * 修订历史：
 *          v1.3 修改于2017年8月6日
 * ================================================
 */
public class WxNewsActivity extends BaseActivity implements View.OnClickListener ,WxNewsContract.View {

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

    private WxNewsContract.Presenter presenter = new WxNewsPresenter(this);

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.unSubscribe();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        presenter.subscribe();
        super.onCreate(savedInstanceState);
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
        toolbarTitle.setText("头条新闻");
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
        presenter.readCacheChannel();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_search:
                Intent intent = new Intent(WxNewsActivity.this,NewsSearchActivity.class);
                intent.putExtra("type","wx");
                startActivity(intent);
                break;
            case R.id.ll_title_menu:
                finish();
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
                startActivity(TxWeChatNewsActivity.class);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void initTabData() {
        String[] tabTitles = {"热门","推荐","八卦"};
        String[] tabIds = {"1","2","6"};
        List<String> titles = new ArrayList<>();
        List<Fragment> fragments = new ArrayList<>();
        for(int a=0 ; a<tabTitles.length ; a++){
            fragments.add(WxNewsFragment.newInstance(tabIds[a]));
            titles.add(tabTitles[a]);
        }
        BasePagerAdapter adapter = new BasePagerAdapter(getSupportFragmentManager(),fragments,titles);
        vpContent.setAdapter(adapter);
        vpContent.setCurrentItem(0);
        vpContent.setOffscreenPageLimit(6);

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

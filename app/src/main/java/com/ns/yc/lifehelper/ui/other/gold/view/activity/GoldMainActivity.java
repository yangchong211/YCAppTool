package com.ns.yc.lifehelper.ui.other.gold.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.comment.Constant;
import com.ns.yc.lifehelper.base.mvp1.BaseActivity;
import com.ns.yc.lifehelper.base.adapter.BasePagerAdapter;
import com.ns.yc.lifehelper.ui.other.gold.contract.GoldMainContract;
import com.ns.yc.lifehelper.ui.other.gold.model.GoldManagerBean;
import com.ns.yc.lifehelper.ui.other.gold.model.GoldManagerItemBean;
import com.ns.yc.lifehelper.ui.other.gold.presenter.GoldMainPresenter;
import com.ns.yc.lifehelper.ui.other.gold.view.fragment.GoldPagerFragment;

import java.util.ArrayList;

import butterknife.Bind;
import io.realm.RealmList;


/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/12/5
 * 描    述：稀土掘金模块
 * 修订历史：
 * ================================================
 */
public class GoldMainActivity extends BaseActivity implements View.OnClickListener , GoldMainContract.View{

    @Bind(R.id.ll_title_menu)
    FrameLayout llTitleMenu;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.tab_layout)
    TabLayout tabLayout;
    @Bind(R.id.vp_content)
    ViewPager vpContent;
    @Bind(R.id.iv_add)
    FrameLayout ivAdd;

    public static String[] typeStr = {"Android", "iOS", "前端", "后端", "设计", "产品", "阅读", "工具资源"};
    public static String[] type = {"android", "ios", "frontend", "backend", "design", "product", "article", "freebie"};

    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private GoldMainContract.Presenter presenter = new GoldMainPresenter(this);
    private int currentIndex = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.subscribe();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.unSubscribe();
    }


    @Override
    public int getContentView() {
        return R.layout.base_add_tab_view;
    }


    @Override
    public void initView() {
        initToolBar();
        presenter.initRealm();
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setupWithViewPager(vpContent);
        presenter.initManagerList();
    }


    private void initToolBar() {
        toolbarTitle.setText("稀土掘金");
    }


    @Override
    public void initListener() {
        llTitleMenu.setOnClickListener(this);
        ivAdd.setOnClickListener(this);
    }


    @Override
    public void initData() {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_title_menu:
                finish();
                break;
            case R.id.iv_add:
                presenter.setManagerList();
                break;
        }
    }


    @Override
    public void updateTab(RealmList<GoldManagerItemBean> mList) {
        mFragments.clear();
        tabLayout.removeAllTabs();
        for (GoldManagerItemBean item : mList) {
            if (item.getIsSelect()) {
                GoldPagerFragment fragment = new GoldPagerFragment();
                Bundle bundle = new Bundle();
                bundle.putString(Constant.DetailKeys.IT_GOLD_TYPE, type[item.getIndex()]);
                bundle.putString(Constant.DetailKeys.IT_GOLD_TYPE_STR, typeStr[item.getIndex()]);
                tabLayout.addTab(tabLayout.newTab().setText(typeStr[item.getIndex()]));
                fragment.setArguments(bundle);
                mFragments.add(fragment);
            }
        }
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        BasePagerAdapter mAdapter = new BasePagerAdapter(supportFragmentManager, mFragments);
        vpContent.setAdapter(mAdapter);
        for (GoldManagerItemBean item : mList) {
            if (item.getIsSelect()) {
                TabLayout.Tab tabAt = tabLayout.getTabAt(currentIndex++);
                if(tabAt!=null){
                    tabAt.setText(typeStr[item.getIndex()]);
                }
            }
        }
        currentIndex = 0;
    }


    @Override
    public void jumpToManager(GoldManagerBean goldManagerList) {
        Intent intent = new Intent(this, GoldManagerActivity.class);
        intent.putExtra(Constant.DetailKeys.IT_GOLD_MANAGER, goldManagerList);
        startActivity(intent);
    }


}

package com.ns.yc.lifehelper.ui.other.bookReader.view;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.api.Constant;
import com.ns.yc.lifehelper.base.BaseActivity;
import com.ns.yc.lifehelper.base.BasePagerAdapter;
import com.ns.yc.lifehelper.ui.other.bookReader.adapter.MinorAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：22017/9/21
 * 描    述：小说阅读器分类list页面
 * 修订历史：
 * ================================================
 */
public class SubCategoryListActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.ll_title_menu)
    FrameLayout llTitleMenu;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tab_layout)
    TabLayout tabLayout;
    @Bind(R.id.vp_content)
    ViewPager vpContent;
    private String gender;
    private String name;
    private String bookCount;

    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private List<String> titles;
    private ListPopupWindow mListPopupWindow;
    private List<String> mMinors = new ArrayList<>();
    private String[] types = new String[]{
            Constant.CateType.NEW,
            Constant.CateType.HOT,
            Constant.CateType.REPUTATION,
            Constant.CateType.OVER
    };
    private MinorAdapter minorAdapter;
    private String currentMinor = "";


    @Override
    public int getContentView() {
        return R.layout.base_tab_view;
    }

    @Override
    public void initView() {
        initIntentData();
        initToolBar();
        initFragmentList();
        initViewPagerAndTab();
    }

    private void initIntentData() {
        Intent intent = getIntent();
        if(intent!=null){
            gender = intent.getStringExtra("gender");
            name = intent.getStringExtra("name");
            bookCount = intent.getStringExtra("bookCount");
        }
    }

    private void initToolBar() {
        toolbarTitle.setText(name);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //去除默认Title显示
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    @Override
    public void initListener() {
        llTitleMenu.setOnClickListener(this);
    }

    @Override
    public void initData() {
        getSubCategory();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_title_menu:
                finish();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sub_category, menu);
        MenuItem menuItem = menu.findItem(R.id.menu_major);
        if (!TextUtils.isEmpty(name)) {
            menuItem.setTitle(name);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_major) {
            showMinorPopupWindow();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initFragmentList() {
        titles = Arrays.asList(getResources().getStringArray(R.array.sub_tabs));
        mFragments.add(SubCategoryFragment.newInstance(name, "", gender, Constant.CateType.NEW));
        mFragments.add(SubCategoryFragment.newInstance(name, "", gender, Constant.CateType.HOT));
        mFragments.add(SubCategoryFragment.newInstance(name, "", gender, Constant.CateType.REPUTATION));
        mFragments.add(SubCategoryFragment.newInstance(name, "", gender, Constant.CateType.OVER));
    }

    private void initViewPagerAndTab() {
        /**
         * 注意使用的是：getChildFragmentManager，
         * 这样setOffscreenPageLimit()就可以添加上，保留相邻2个实例，切换时不会卡
         * 但会内存溢出，在显示时加载数据
         */
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        BasePagerAdapter myAdapter = new BasePagerAdapter(supportFragmentManager, mFragments, titles);
        vpContent.setAdapter(myAdapter);
        // 左右预加载页面的个数
        vpContent.setOffscreenPageLimit(4);
        myAdapter.notifyDataSetChanged();
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(vpContent);
    }


    private void showMinorPopupWindow() {
        if (mMinors.size() > 0 && minorAdapter != null) {
            if (mListPopupWindow == null) {
                mListPopupWindow = new ListPopupWindow(this);
                mListPopupWindow.setAdapter(minorAdapter);
                mListPopupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
                mListPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
                mListPopupWindow.setAnchorView(toolbar);
                mListPopupWindow.setModal(true);
                mListPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        minorAdapter.setChecked(position);
                        if (position > 0) {
                            currentMinor = mMinors.get(position);
                        } else {
                            currentMinor = "";
                        }
                        //int current = vpContent.getCurrentItem();
                        //EventManager.refreshSubCategory(currentMinor, types[current]);
                        mListPopupWindow.dismiss();
                        toolbar.setTitle(mMinors.get(position));
                    }
                });
            }
            mListPopupWindow.show();
        }
    }


    private void getSubCategory() {

    }


}

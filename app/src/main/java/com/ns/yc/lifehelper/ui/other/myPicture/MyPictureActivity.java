package com.ns.yc.lifehelper.ui.other.myPicture;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.mvp1.BaseActivity;
import com.ns.yc.lifehelper.base.adapter.BasePagerAdapter;
import com.ns.yc.lifehelper.ui.data.view.activity.GalleryImageActivity;
import com.ns.yc.lifehelper.ui.other.myPicture.view.MyPicBeautifulActivity;
import com.ns.yc.lifehelper.ui.other.myPicture.view.fragment.PictureHomeFragment;
import com.ns.yc.lifehelper.ui.other.myPicture.view.fragment.PictureOtherFragment;

import java.util.ArrayList;

import butterknife.BindView;


/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/8/30
 * 描    述：图片欣赏页面
 * 修订历史：
 * <p>
 * case R.id.btn:
 * startActivity(MyPicPileActivity.class);
 * break;
 * case R.id.btn2:
 * startActivity(MyPicNiceActivity.class);
 * break;
 * case R.id.btn3:
 * startActivity(MyPicBeautifulActivity.class);
 * break;
 * ================================================
 */
public class MyPictureActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.ll_title_menu)
    FrameLayout llTitleMenu;
    @BindView(R.id.ll_search)
    FrameLayout llSearch;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.iv_right_img)
    ImageView ivRightImg;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.vp_content)
    ViewPager vpContent;

    private ArrayList<String> mTitleList = new ArrayList<>();
    private ArrayList<Fragment> mFragments = new ArrayList<>();

    @Override
    public int getContentView() {
        return R.layout.base_tab_view;
    }

    @Override
    public void initView() {
        initToolBar();
        initFragmentList();
        initViewPagerAndTab();
    }

    private void initToolBar() {
        llSearch.setVisibility(View.VISIBLE);
        //ivRightImg.setImageResource(R.drawable.book_detail_info_add_img);
        //ivRightImg.setBackgroundResource(R.drawable.book_detail_info_add_img);
        toolbarTitle.setText("图片欣赏");
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
        switch (v.getId()) {
            case R.id.ll_title_menu:
                finish();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_picture_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                startActivity(GalleryImageActivity.class);
                break;
            case R.id.item2:
                startActivity(MyPicBeautifulActivity.class);
                break;
            case R.id.item3:

                break;
            case R.id.item4:

                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }



    private String[] titles = {"热门推荐","动漫","唯美","美女","风景","旅游","军事","电影壁纸","萌宠","游戏壁纸"};
    private String[] type = {"social","guonei","huabian","tiyu","nba","startup","military","travel","health","qiwen"};
    private void initFragmentList() {
        mTitleList.clear();
        mFragments.clear();
        for(int a=0 ; a<titles.length ; a++){
            mTitleList.add(titles[a]);
            if(a==0){
                mFragments.add(new PictureHomeFragment());
            }else {
                mFragments.add(PictureOtherFragment.newInstance(type[a]));
            }

        }
    }

    private void initViewPagerAndTab() {
        /**
         * 注意使用的是：getChildFragmentManager，
         * 这样setOffscreenPageLimit()就可以添加上，保留相邻2个实例，切换时不会卡
         * 但会内存溢出，在显示时加载数据
         */
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        BasePagerAdapter myAdapter = new BasePagerAdapter(supportFragmentManager, mFragments, mTitleList);
        vpContent.setAdapter(myAdapter);
        // 左右预加载页面的个数
        //vpContent.setOffscreenPageLimit(5);
        myAdapter.notifyDataSetChanged();
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setupWithViewPager(vpContent);
    }



}

package com.ycbjie.news.ui.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.ycbjie.library.base.mvp.BaseActivity;
import com.ycbjie.library.base.adapter.BasePagerAdapter;
import com.ycbjie.library.arounter.ARouterConstant;
import com.ycbjie.library.arounter.ARouterUtils;
import com.ycbjie.news.R;
import com.ycbjie.news.ui.fragment.TxNewsFragment;
import java.util.ArrayList;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/5/21
 *     desc  : 天行新闻
 *     revise:
 * </pre>
 */
@Route(path = ARouterConstant.ACTIVITY_TX_NEWS_ACTIVITY)
public class TxNewsActivity extends BaseActivity implements View.OnClickListener {

    private FrameLayout llTitleMenu;
    private TextView toolbarTitle;
    private TextView tvTitleRight;
    private TabLayout tabLayout;
    private ViewPager vpContent;



    @Override
    public int getContentView() {
        return R.layout.base_tab_view;
    }

    @Override
    public void initView() {
        llTitleMenu = findViewById(R.id.ll_title_menu);
        toolbarTitle = findViewById(R.id.toolbar_title);
        tvTitleRight = findViewById(R.id.tv_title_right);
        tabLayout = findViewById(R.id.tab_layout);
        vpContent = findViewById(R.id.vp_content);
        initToolBar();
        initFragmentList();
    }

    private void initToolBar() {
        toolbarTitle.setText("天行新闻");
        tvTitleRight.setText("意见");
    }


    @Override
    public void initListener() {
        llTitleMenu.setOnClickListener(this);
        tvTitleRight.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.ll_title_menu) {
            finish();
        } else if (i == R.id.tv_title_right){
            ARouterUtils.navigation(ARouterConstant.ACTIVITY_OTHER_FEEDBACK,
                    null,R.anim.screen_zoom_in, R.anim.screen_zoom_out);
        }
    }


    private void initFragmentList() {
        String[] titles = {"社会","国内","娱乐","体育","NBA","创业","军事","旅游","健康","奇闻"};
        String[] type = {"social","guonei","huabian","tiyu","nba","startup","military",
                "travel","health","qiwen"};

        ArrayList<String> mTitleList = new ArrayList<>();
        ArrayList<Fragment> mFragments = new ArrayList<>();
        for(int a=0 ; a<titles.length ; a++){
            mTitleList.add(titles[a]);
            mFragments.add(TxNewsFragment.newInstance(type[a]));
        }
        /*
         * 注意使用的是：getChildFragmentManager，
         * 这样setOffscreenPageLimit()就可以添加上，保留相邻2个实例，切换时不会卡
         * 但会内存溢出，在显示时加载数据
         */
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        BasePagerAdapter myAdapter = new BasePagerAdapter(supportFragmentManager, mFragments, mTitleList);
        vpContent.setAdapter(myAdapter);
        // 左右预加载页面的个数
        vpContent.setOffscreenPageLimit(mFragments.size());
        myAdapter.notifyDataSetChanged();
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setupWithViewPager(vpContent);
    }


}

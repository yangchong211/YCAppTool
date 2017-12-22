package com.ns.yc.lifehelper.ui.other.imTalk.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.BaseActivity;
import com.ns.yc.lifehelper.base.BasePagerAdapter;
import com.ns.yc.lifehelper.ui.me.view.activity.MeLoginActivity;
import com.ns.yc.lifehelper.ui.other.imTalk.contract.ImTalkContract;
import com.ns.yc.lifehelper.ui.other.imTalk.presenter.ImTalkPresenter;
import com.ns.yc.lifehelper.ui.other.imTalk.ui.fragment.ImContactFragment;
import com.ns.yc.lifehelper.ui.other.imTalk.ui.fragment.ImConversationFragment;
import com.ns.yc.lifehelper.ui.other.imTalk.ui.fragment.ImSettingFragment;
import com.ns.yc.lifehelper.utils.IMEmClientUtils;
import com.ns.yc.ycutilslib.viewPager.NoSlidingViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;


/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/12/18
 * 描    述：即时通讯训练案例
 * 修订历史：
 * ================================================
 */
public class ImTalkActivity extends BaseActivity implements View.OnClickListener , ImTalkContract.View{

    @Bind(R.id.ll_title_menu)
    FrameLayout llTitleMenu;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.vp_home)
    NoSlidingViewPager vpHome;
    @Bind(R.id.ctl_table)
    CommonTabLayout ctlTable;

    private ImTalkContract.Presenter presenter = new ImTalkPresenter(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        presenter.bindView(this);
        presenter.subscribe();

        // 判断sdk是否登录成功过，并没有退出和被踢，否则跳转到登陆界面
        if (!IMEmClientUtils.getIMemClientInstance().isLoggedInBefore()) {
            Intent intent = new Intent(this, MeLoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        super.onCreate(savedInstanceState);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.unSubscribe();
    }


    @Override
    public int getContentView() {
        return R.layout.activity_im_main;
    }

    @Override
    public void initView() {
        initTabLayout();
        initViewPager();
    }


    @Override
    public void initListener() {
        llTitleMenu.setOnClickListener(this);
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
        }
    }

    private void initTabLayout() {
        ArrayList<CustomTabEntity> mTabEntities = presenter.getTabEntity();
        ctlTable.setTabData(mTabEntities);
        //ctlTable.showDot(3);                   //显示红点
        //ctlTable.showMsg(2,5);                 //显示未读信息
        //ctlTable.showMsg(1,3);                 //显示未读信息
        //ctlTable.setMsgMargin(1, 2, 2);        //显示红点信息位置
        toolbarTitle.setText("会话页面");
        ctlTable.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                vpHome.setCurrentItem(position);
                switch (position) {
                    case 0:
                        toolbarTitle.setText("会话页面");
                        break;
                    case 1:
                        toolbarTitle.setText("通讯录");
                        break;
                    case 2:
                        toolbarTitle.setText("设置中心");
                        break;
                }
            }

            @Override
            public void onTabReselect(int position) {}
        });
    }


    private void initViewPager() {
        List<Fragment> fragments = new ArrayList<>();
        ImConversationFragment imConversationFragment = new ImConversationFragment();
        ImContactFragment imContactFragment = new ImContactFragment();
        ImSettingFragment imSettingFragment = new ImSettingFragment();
        fragments.add(imConversationFragment);
        fragments.add(imContactFragment);
        fragments.add(imSettingFragment);
        BasePagerAdapter adapter = new BasePagerAdapter(getSupportFragmentManager(), fragments);
        vpHome.setAdapter(adapter);
        vpHome.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                ctlTable.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        vpHome.setOffscreenPageLimit(3);
        vpHome.setCurrentItem(0);
    }


}

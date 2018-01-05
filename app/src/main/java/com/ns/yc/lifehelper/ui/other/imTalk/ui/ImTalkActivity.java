package com.ns.yc.lifehelper.ui.other.imTalk.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.NetworkUtils;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.hyphenate.EMError;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.adapter.BasePagerAdapter;
import com.ns.yc.lifehelper.base.mvp1.BaseActivity;
import com.ns.yc.lifehelper.ui.me.view.activity.MeLoginActivity;
import com.ns.yc.lifehelper.ui.other.imTalk.contract.ImTalkContract;
import com.ns.yc.lifehelper.ui.other.imTalk.model.FragmentFactory;
import com.ns.yc.lifehelper.ui.other.imTalk.presenter.ImTalkPresenter;
import com.ns.yc.lifehelper.utils.IMEMClientUtils;
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
        // 判断sdk是否登录成功过，并没有退出和被踢，否则跳转到登陆界面
        if (!IMEMClientUtils.getIMemClientInstance().isLoggedInBefore()) {
            Intent intent = new Intent(this, MeLoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        initTabLayout();
        initViewPager();
    }


    @Override
    public void initListener() {
        llTitleMenu.setOnClickListener(this);
    }

    @Override
    public void initData() {
        presenter.addConnectionListener();
        RegisterNewMessageBroadcastReceiver();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_title_menu:
                finish();
                break;
            default:
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
                    default:
                        break;
                }
            }

            @Override
            public void onTabReselect(int position) {}
        });
    }


    private void initViewPager() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(FragmentFactory.getInstance().getImConversationFragment());
        fragments.add(FragmentFactory.getInstance().getImContactFragment());
        fragments.add(FragmentFactory.getInstance().getImSettingFragment());
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


    /**
     * 注册接收新消息的监听广播
     */
    private void RegisterNewMessageBroadcastReceiver() {
        //只有注册了广播才能接收到新消息，目前离线消息，在线消息都是走接收消息的广播（离线消息目前无法监听，在登录以后，接收消息广播会执行一次拿到所有的离线消息）
        /*NewMessageBroadcastReceiver msgReceiver = new NewMessageBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.setPriority(3);
        registerReceiver(msgReceiver, intentFilter);*/
    }


    @Override
    public void onDisconnected(final int error) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(error == EMError.USER_REMOVED){
                    // 显示帐号已经被移除
                }else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                    // 显示帐号在其他设备登录
                }else if(error == EMError.USER_UPDATEINFO_FAILED) {
                    // 更新用户信息失败
                }else {
                    if (NetworkUtils.isConnected()){
                        //连接不到聊天服务器
                    } else{
                        //当前网络不可用，请检查网络设置
                    }
                }
            }
        });
    }


}

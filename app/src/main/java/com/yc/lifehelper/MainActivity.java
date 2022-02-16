package com.yc.lifehelper;

import android.Manifest;
import android.content.Intent;

import com.blankj.utilcode.util.SizeUtils;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.graphics.Color;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.pedaily.yc.ycdialoglib.toast.ToastUtils;
import com.yc.banner.view.BannerView;
import com.yc.cn.ycbannerlib.marquee.MarqueeView;
import com.yc.configlayer.arounter.ARouterUtils;
import com.yc.configlayer.arounter.RouterConfig;
import com.yc.configlayer.constant.Constant;
import com.yc.imageserver.utils.GlideImageUtils;
import com.yc.library.base.mvp.BaseActivity;
import com.yc.library.web.WebViewActivity;
import com.yc.monitorfilelib.FileExplorerActivity;
import com.yc.toollib.crash.CrashToolUtils;
import com.yc.toolutils.activity.ActivityManager;
import com.yc.toolutils.click.FastClickUtils;
import com.yc.toolutils.click.PerfectClickListener;
import com.yc.zxingserver.demo.EasyCaptureActivity;
import com.yc.zxingserver.scan.Intents;

import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter;
import org.yczbj.ycrefreshviewlib.inter.InterItemView;
import org.yczbj.ycrefreshviewlib.item.RecycleViewItemLine;
import org.yczbj.ycrefreshviewlib.view.YCRefreshView;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2016/03/22
 *     desc  : Main主页面
 *     revise:
 * </pre>
 */
public class MainActivity extends BaseActivity{

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavView;
    private TextView mSetting;
    private TextView mQuit;
    private Toolbar mToolbar;
    private FrameLayout mFlTitleMenu;
    private TextView mTvTitle;
    private YCRefreshView mRecyclerView;
    private long time;
    private MainAdapter mainAdapter;
    private BannerView banner;
    private View headerView;
    public static final int REQUEST_CODE_SCAN = 0X01;

    @Override
    public int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_setting_app, menu);
        menu.add(4, 4, 4, "开发作者介绍");
        menu.add(5, 5, 5,"分享此软件");
        menu.add(6, 6, 6,"开源项目介绍");
        menu.add(7, 7, 7,"我的掘金");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_setting:
                ToastUtils.showRoundRectToast("设置");
                break;
            case R.id.item_message:
                ToastUtils.showRoundRectToast("消息");
                break;
            case R.id.item_capture:
                String[] perms = {Manifest.permission.CAMERA};
                if (EasyPermissions.hasPermissions(this, perms)) {//有权限
                    Intent intent = new Intent(this, EasyCaptureActivity.class);
                    this.startActivityForResult(intent,REQUEST_CODE_SCAN);
                }
                break;
            case 4:
                WebViewActivity.lunch(this,Constant.GITHUB,"我的GitHub");
                break;
            case 5:
                WebViewActivity.lunch(this,Constant.LIFE_HELPER,"开源项目介绍");
                break;
            case 6:
                WebViewActivity.lunch(this,Constant.ZHI_HU,"我的知乎");
                break;
            case 7:
                WebViewActivity.lunch(this,Constant.JUE_JIN,"我的掘金");
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && data!=null){
            if (requestCode == REQUEST_CODE_SCAN) {
                String result = data.getStringExtra(Intents.Scan.RESULT);
                if (result.contains("http") || result.contains("https")) {
                    Intent intent = new Intent(this, WebViewActivity.class);
                    intent.putExtra("url", result);
                    startActivity(intent);
                } else {
                    ToastUtils.showRoundRectToast(result);
                }
            }
        }
    }

    @Override
    public void initView() {
        initFindViewID();
        initBar();
        initRecyclerView();
        initNav();
    }

    private void initFindViewID() {
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mNavView = findViewById(R.id.nav_view);
        mSetting =findViewById(R.id.setting);
        mQuit = findViewById(R.id.quit);
        mToolbar = findViewById(R.id.toolbar);
        mFlTitleMenu = findViewById(R.id.fl_title_menu);
        mTvTitle = findViewById(R.id.tv_title);
        mRecyclerView = findViewById(R.id.recyclerView);
    }


    @Override
    public void initListener() {
        mFlTitleMenu.setOnClickListener(listener);
        mNavView.setOnClickListener(listener);
        mSetting.setOnClickListener(listener);
        mQuit.setOnClickListener(listener);
    }


    @Override
    public void initData() {

    }

    /**
     * 初始化ActionBar按钮
     */
    private void initBar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //去除默认Title显示
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecycleViewItemLine line = new RecycleViewItemLine(this, LinearLayout.HORIZONTAL,
                SizeUtils.dp2px(1), Color.parseColor("#f5f5f7"));
        mRecyclerView.addItemDecoration(line);
        mainAdapter = new MainAdapter(this);
        initAddHeader();
        mRecyclerView.setAdapter(mainAdapter);
    }

    private void initAddHeader() {
        mainAdapter.addHeader(new InterItemView() {
            @Override
            public View onCreateView(ViewGroup parent) {
                headerView = View.inflate(parent.getContext(), R.layout.head_home_main, null);
                return headerView;
            }

            @Override
            public void onBindView(View header) {
                banner = header.findViewById(R.id.banner);
                TextView tvHomeFirst = header.findViewById(R.id.tv_home_first);
                TextView tvHomeSecond =header.findViewById(R.id.tv_home_second);
                TextView tvHomeThird = header.findViewById(R.id.tv_home_third);
                TextView tvHomeFour = header.findViewById(R.id.tv_home_four);
                View.OnClickListener listener = v -> {
                    if (FastClickUtils.isFastDoubleClick()){
                        return;
                    }
                    switch (v.getId()) {
                        //沙盒File
                        case R.id.tv_home_first:
                            FileExplorerActivity.startActivity(MainActivity.this);
                            break;
                        case R.id.tv_home_second:
                            break;
                        case R.id.tv_home_third:
                            break;
                        case R.id.tv_home_four:
                            break;
                        default:
                            break;
                    }
                };
                tvHomeFirst.setOnClickListener(listener);
                tvHomeSecond.setOnClickListener(listener);
                tvHomeThird.setOnClickListener(listener);
                tvHomeFour.setOnClickListener(listener);
                initBanner();
            }
        });
    }

    /**
     * 初始化轮播图
     */
    private void initBanner() {

    }

    /**
     * 初始化侧滑菜单
     */
    private void initNav() {
        View view = mNavView.inflateHeaderView(R.layout.nav_header_main);
        ImageView ivAvatar = view.findViewById(R.id.iv_avatar);
        TextView tvUsername = view.findViewById(R.id.tv_username);
        LinearLayout llNavHomepage = view.findViewById(R.id.ll_nav_homepage);
        LinearLayout llNavScanDownload = view.findViewById(R.id.ll_nav_scan_download);
        LinearLayout llNavFeedback = view.findViewById(R.id.ll_nav_feedback);
        LinearLayout llNavAbout = view.findViewById(R.id.ll_nav_about);
        LinearLayout llNavLogin = view.findViewById(R.id.ll_nav_login);
        LinearLayout llNavVideo = view.findViewById(R.id.ll_nav_video);
        GlideImageUtils.loadImageRound(this, R.drawable.ic_person_logo, ivAvatar);
        tvUsername.setText("杨充");
        ivAvatar.setOnClickListener(listener);
        llNavHomepage.setOnClickListener(listener);
        llNavScanDownload.setOnClickListener(listener);
        llNavFeedback.setOnClickListener(listener);
        llNavAbout.setOnClickListener(listener);
        llNavLogin.setOnClickListener(listener);
        llNavVideo.setOnClickListener(listener);
    }


    /**
     * 自定义菜单点击事件
     */
    private final PerfectClickListener listener = new PerfectClickListener() {
        @Override
        protected void onNoDoubleClick(final View v) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            mDrawerLayout.postDelayed(() -> {
                switch (v.getId()) {
                    case R.id.iv_avatar:

                        break;
                    case R.id.fl_title_menu:
                        mDrawerLayout.openDrawer(GravityCompat.START);
                        break;
                    // 主页
                    case R.id.ll_nav_homepage:
                        Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
                        intent.putExtra("url", "");
                        startActivity(intent);
                        break;
                    //扫码下载
                    case R.id.ll_nav_scan_download:
                        FileExplorerActivity.startActivity(MainActivity.this);
                        break;
                    // 问题反馈
                    case R.id.ll_nav_feedback:
                        ARouterUtils.navigation(RouterConfig.Demo.ACTIVITY_OTHER_FEEDBACK);
                        break;
                    // 关于
                    case R.id.ll_nav_about:
                        ARouterUtils.navigation(RouterConfig.Demo.ACTIVITY_OTHER_ABOUT_ME);
                        break;
                    // 个人
                    case R.id.ll_nav_login:
                        break;
                    case R.id.ll_nav_video:
                        ToastUtils.showRoundRectToast( "后期接入讯飞语音");
                        break;
                    case R.id.setting:
                        ARouterUtils.navigation(RouterConfig.App.ACTIVITY_APP_SETTING_ACTIVITY);
                        break;
                    case R.id.quit:
                        ActivityManager.getInstance().appExist();
                        break;
                    default:
                        break;
                }
            }, 0);
        }
    };

    /**
     * 是当某个按键被按下是触发。所以也有人在点击返回键的时候去执行该方法来做判断
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        LogUtils.e("触摸监听", "onKeyDown");
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mDrawerLayout!=null && mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
            } else {
                //双击返回桌面
                if ((System.currentTimeMillis() - time > 1000)) {
                    ToastUtils.showRoundRectToast("再按一次返回桌面");
                    time = System.currentTimeMillis();
                } else {
                    moveTaskToBack(true);
                }
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}

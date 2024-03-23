package com.yc.lifehelper;

import android.Manifest;
import android.content.Intent;

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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yc.apploglib.AppLogHelper;
import com.yc.activitymanager.activity.ActivityManager;
import com.yc.banner.view.BannerView;

import com.yc.ycthreadpoollib.easy.DelegateTaskExecutor;
import com.yc.imageserver.utils.GlideImageUtils;
import com.yc.library.api.ConstantStringApi;
import com.yc.library.base.config.Constant;
import com.yc.library.base.mvp.BaseActivity;
import com.yc.library.web.WebViewActivity;
import com.yc.lifehelper.adapter.MainAdapter;
import com.yc.lifehelper.component.AdListComponent;
import com.yc.lifehelper.component.BannerComponent;
import com.yc.lifehelper.component.HeaderComponent;
import com.yc.lifehelper.component.ListNewsComponent;
import com.yc.lifehelper.component.MarqueeComponent;
import com.yc.lifehelper.component.RecommendComponent;
import com.yc.lifehelper.component.SnapBannerComponent;
import com.yc.lifehelper.component.ToolComponent;
import com.yc.lifehelper.view.MyNotifyView;
import com.yc.logclient.LogUtils;
import com.yc.monitorfilelib.FileExplorerActivity;
import com.yc.notifymessage.CustomNotification;
import com.yc.notifymessage.OnDismissListener;
import com.yc.toastutils.ToastUtils;
import com.yc.toolutils.system.AppLogUtils;
import com.yc.toolutils.size.AppSizeUtils;
import com.yc.toolutils.click.PerfectClickListener;
import com.yc.zxingserver.demo.EasyCaptureActivity;
import com.yc.zxingserver.scan.Intents;

import org.yczbj.ycrefreshviewlib.item.RecycleViewItemLine;
import org.yczbj.ycrefreshviewlib.view.YCRefreshView;

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
    public static final int REQUEST_CODE_SCAN = 0X01;
    private BannerView banner;


    @Override
    public int getContentView() {
        AppLogHelper.d("getContentView");
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
                WebViewActivity.lunch(this, Constant.GITHUB,"我的GitHub");
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
    protected void onPause() {
        super.onPause();
        if (banner!=null){
            banner.startAutoPlay();
        }
        AppLogHelper.d("onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (banner!=null){
            banner.stopAutoPlay();
        }
        AppLogHelper.d("onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (banner!=null){
            banner.releaseBanner();
        }
        AppLogHelper.d("onDestroy");
    }

    @Override
    public void initView() {
        initFindViewID();
        initBar();
        initRecyclerView();
        initNav();
        initNotify();
        AppLogHelper.d("initView");
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
                AppSizeUtils.dp2px(this,1), Color.parseColor("#f5f5f7"));
        mRecyclerView.addItemDecoration(line);
        mainAdapter = new MainAdapter(this);
        initAddBanner();
        initAddHeader();
        initAddListNews();
        initMarquee();
        initRecommend();
        initSnapBanner();
        initAdListNews();
        initAboutTool();
        mRecyclerView.setAdapter(mainAdapter);
        mainAdapter.addAll(ConstantStringApi.getNewsList());
    }

    private void initAddBanner() {
        BannerComponent bannerComponent = new BannerComponent();
        mainAdapter.addHeader(bannerComponent);
        banner = bannerComponent.getBannerView();
    }

    private void initAddHeader() {
        HeaderComponent headerComponent = new HeaderComponent();
        mainAdapter.addHeader(headerComponent);
    }

    private void initAddListNews() {
        ListNewsComponent listNewsComponent = new ListNewsComponent();
        mainAdapter.addHeader(listNewsComponent);
    }

    private void initMarquee() {
        MarqueeComponent marqueeComponent = new MarqueeComponent();
        mainAdapter.addHeader(marqueeComponent);
    }

    private void initRecommend() {
        RecommendComponent recommendComponent = new RecommendComponent();
        mainAdapter.addHeader(recommendComponent);
    }

    private void initSnapBanner() {
        SnapBannerComponent snapBannerComponent = new SnapBannerComponent();
        mainAdapter.addHeader(snapBannerComponent);
    }

    private void initAdListNews() {
        AdListComponent adListComponent = new AdListComponent();
        mainAdapter.addHeader(adListComponent);
    }

    private void initAboutTool() {
        ToolComponent toolComponent = new ToolComponent();
        mainAdapter.addHeader(toolComponent);
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

    private void initNotify() {
        mNavView.postDelayed(new Runnable() {
            @Override
            public void run() {
                sendNotification();
            }
        },8000);
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
                        DelegateTaskExecutor.getInstance().executeOnDiskIO(new Runnable() {
                            @Override
                            public void run() {
                                for (int i= 0 ; i<100 ; i++){
                                    LogUtils.d("问题反馈 main activity log test " + i);
                                }
                                DelegateTaskExecutor.getInstance().postToMainThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ToastUtils.showRoundRectToast("日志输出完1");
                                    }
                                });
                            }
                        });
                        break;
                    // 关于
                    case R.id.ll_nav_about:
                        DelegateTaskExecutor.getInstance().executeOnDiskIO(new Runnable() {
                            @Override
                            public void run() {
                                for (int i= 0 ; i<1000 ; i++){
                                    LogUtils.d("关于 main activity log test " + i);
                                }
                                DelegateTaskExecutor.getInstance().postToMainThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ToastUtils.showRoundRectToast("日志输出完2");
                                    }
                                });
                            }
                        });
                        break;
                    // 个人
                    case R.id.ll_nav_login:
                        DelegateTaskExecutor.getInstance().executeOnDiskIO(new Runnable() {
                            @Override
                            public void run() {
                                for (int i= 0 ; i<600 ; i++){
                                    LogUtils.w("设置 main activity log test " + i);
                                }
                                DelegateTaskExecutor.getInstance().postToMainThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ToastUtils.showRoundRectToast("日志输出完4");
                                    }
                                });
                            }
                        });
                        break;
                    case R.id.ll_nav_video:
                        DelegateTaskExecutor.getInstance().executeOnDiskIO(new Runnable() {
                            @Override
                            public void run() {
                                for (int i= 0 ; i<500 ; i++){
                                    LogUtils.i("设置 main activity log test " + i);
                                }
                                DelegateTaskExecutor.getInstance().postToMainThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ToastUtils.showRoundRectToast("日志输出完4");
                                    }
                                });
                            }
                        });
                        //ToastUtils.showRoundRectToast( "后期接入讯飞语音");
                        break;
                    case R.id.setting:
                        DelegateTaskExecutor.getInstance().executeOnDiskIO(new Runnable() {
                            @Override
                            public void run() {
                                for (int i= 0 ; i<500 ; i++){
                                    LogUtils.e("设置 main activity log test " + i);
                                }
                                DelegateTaskExecutor.getInstance().postToMainThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ToastUtils.showRoundRectToast("日志输出完3");
                                    }
                                });
                            }
                        });
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
     * 当某个按键被按下是触发。所以也有人在点击返回键的时候去执行该方法来做判断
     * onBackPressed方法和onKeyDown方法同时存在的时候，按back键，系统调用的是onKeyDown这个方法，不会调用onBackPressed方法
     * onKeyDown 监听返回键逻辑，当某个按键被按下时触发
     * onKeyUp  监听返回键逻辑，当某个按键手指抬起时触发
     * onBackPressed  监听返回键逻辑，在onKeyDown源码内部可以看到
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        AppLogUtils.e("触摸监听", "onKeyDown");
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mDrawerLayout!=null && mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
            } else {
                //双击返回桌面
                if ((System.currentTimeMillis() - time > 1000)) {
                    ToastUtils.showRoundRectToast("再按一次返回桌面");
                    time = System.currentTimeMillis();
                } else {
                    //可将activity 退到后台，注意不是finish()退出。
                    //判断Activity是否是task根
                    //使用moveTaskToBack是为了让app退出时，不闪屏，退出柔和一些
                    if (this.isTaskRoot()){
                        //参数为false——代表只有当前activity是task根，指应用启动的第一个activity时，才有效;
                        moveTaskToBack(false);
                    } else {
                        //参数为true——则忽略这个限制，任何activity都可以有效。
                        //使用此方法，便不会执行Activity的onDestroy()方法
                        moveTaskToBack(true);
                    }
                }
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void sendNotification(){
        new CustomNotification<Void>()
                .setType(1)
                .setCollapsible(false)
                .setTimeOut(10000)
                .setPriority(100)
                .setDismissListener(new OnDismissListener() {
                    @Override
                    public void onDismiss() {

                    }
                })
                .setNotificationView(new MyNotifyView(this))
                .show();
    }

}

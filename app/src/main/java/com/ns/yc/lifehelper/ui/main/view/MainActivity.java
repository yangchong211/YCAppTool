package com.ns.yc.lifehelper.ui.main.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.comment.factory.FragmentFactory;
import com.ns.yc.lifehelper.base.adapter.BasePagerAdapter;
import com.ns.yc.lifehelper.base.mvp1.BaseActivity;
import com.ns.yc.lifehelper.inter.listener.PerfectClickListener;
import com.ns.yc.lifehelper.ui.main.contract.MainContract;
import com.ns.yc.lifehelper.ui.main.presenter.MainPresenter;
import com.ns.yc.lifehelper.ui.main.view.activity.AboutMeActivity;
import com.ns.yc.lifehelper.ui.me.view.activity.MeFeedBackActivity;
import com.ns.yc.lifehelper.ui.me.view.activity.MePersonActivity;
import com.ns.yc.lifehelper.ui.me.view.activity.MeSettingActivity;
import com.ns.yc.lifehelper.ui.webView.view.WebViewActivity;
import com.ns.yc.lifehelper.utils.image.ImageUtils;
import com.ns.yc.ycutilslib.activityManager.AppManager;
import com.ns.yc.ycutilslib.managerLeak.InputMethodManagerLeakUtils;
import com.ns.yc.ycutilslib.viewPager.NoSlidingViewPager;
import com.pedaily.yc.ycdialoglib.customToast.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cn.ycbjie.ycstatusbarlib.bar.YCAppBar;
import cn.ycbjie.ycstatusbarlib.dlBar.StatusBarUtils;
import pub.devrel.easypermissions.AppSettingsDialog;
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
public class MainActivity extends BaseActivity<MainPresenter> implements View.OnClickListener
        , EasyPermissions.PermissionCallbacks, MainContract.View {


    @Bind(R.id.fl_title_menu)
    FrameLayout flTitleMenu;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.vp_home)
    NoSlidingViewPager vpHome;
    @Bind(R.id.ctl_table)
    CommonTabLayout ctlTable;
    @Bind(R.id.ll_main)
    LinearLayout llMain;
    @Bind(R.id.setting)
    TextView setting;
    @Bind(R.id.quit)
    TextView quit;
    @Bind(R.id.nav_view)
    NavigationView navView;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    private ImageView ivAvatar;
    private LinearLayout llNavScanDownload;
    private LinearLayout llNavFeedback;
    private LinearLayout llNavAbout;
    private LinearLayout llNavLogin;
    private LinearLayout llNavVideo;
    private LinearLayout llNavHomepage;
    private View view;
    private long time;

    private MainContract.Presenter presenter = new MainPresenter(this);

    public static final int HOME = 0;
    public static final int FIND = 1;
    public static final int DATA = 2;
    public static final int USER = 3;
    @IntDef({HOME,FIND,DATA,USER})
    private  @interface PageIndex{}

    /**
     * 跳转首页
     * @param context               上下文
     * @param selectIndex           添加注解限制输入值
     */
    public static void startActivity(Context context, @PageIndex int selectIndex) {
        Intent intent = new Intent(context, MainActivity.class);
        //intent.addCategory(Intent.CATEGORY_DEFAULT);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("selectIndex", selectIndex);
        context.startActivity(intent);
    }

    /**
     * 处理onNewIntent()，以通知碎片管理器 状态未保存。
     * 如果您正在处理新的意图，并且可能是 对碎片状态进行更改时，要确保调用先到这里。
     * 否则，如果你的状态保存，但活动未停止，则可以获得 onNewIntent()调用，发生在onResume()之前，
     * 并试图 此时执行片段操作将引发IllegalStateException。 因为碎片管理器认为状态仍然保存。
     * @param intent intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(intent!=null){
            int selectIndex = intent.getIntExtra("selectIndex", HOME);
            vpHome.setCurrentItem(selectIndex);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        InputMethodManagerLeakUtils.fixInputMethodManagerLeak(this);
    }


    @Override
    public int getContentView() {
        return R.layout.activity_main;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_capture:
                ToastUtil.showToast(this,"二维码扫描");
                break;
            case R.id.action_about_us:
                ActivityUtils.startActivity(AboutMeActivity.class);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void initView() {
        initDrawerLayoutStatus();
        initBar();
        initTabLayout();
        initViewPager();
        initNav();
        initPermissions();
    }


    @Override
    public void initListener() {
        flTitleMenu.setOnClickListener(MainActivity.this);
        navView.setOnClickListener(MainActivity.this);

        //侧滑点击事件
        if (view != null) {
            ivAvatar.setOnClickListener(listener);
            llNavHomepage.setOnClickListener(listener);
            llNavScanDownload.setOnClickListener(listener);
            llNavFeedback.setOnClickListener(listener);
            llNavAbout.setOnClickListener(listener);
            llNavLogin.setOnClickListener(listener);
            llNavVideo.setOnClickListener(listener);
            setting.setOnClickListener(listener);
            quit.setOnClickListener(listener);
        }
    }


    @Override
    public void initData() {
        presenter.getUpdate();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fl_title_menu:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
                break;
        }
    }


    /**
     * 初始化侧滑菜单的状态栏
     */
    private void initDrawerLayoutStatus() {
        //为DrawerLayout 布局设置状态栏变色，也就是加上透明度
        //YCAppBar.setStatusBarLightMode(this, R.color.colorTheme);
        //YCAppBar.setStatusBarLightMode(this, R.color.colorTheme);
    }


    /**
     * 初始化ActionBar按钮
     */
    private void initBar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //去除默认Title显示
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }


    /**
     * 初始化底部导航栏数据
     */
    private void initTabLayout() {
        ArrayList<CustomTabEntity> mTabEntities = presenter.getTabEntity();
        ctlTable.setTabData(mTabEntities);
        tvTitle.setText("新闻首页");
        ctlTable.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                vpHome.setCurrentItem(position);
                switch (position) {
                    case 0:
                        tvTitle.setVisibility(View.VISIBLE);
                        tvTitle.setText("新闻首页");
                        break;
                    case 1:
                        tvTitle.setVisibility(View.VISIBLE);
                        tvTitle.setText("数据中心");
                        break;
                    case 2:
                        tvTitle.setVisibility(View.VISIBLE);
                        tvTitle.setText("生活应用");
                        ctlTable.showMsg(2, 0);
                        break;
                    case 3:
                        tvTitle.setVisibility(View.VISIBLE);
                        tvTitle.setText("更多内容");
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onTabReselect(int position) {
            }
        });
    }


    /**
     * 初始化ViewPager数据
     */
    private void initViewPager() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(FragmentFactory.getInstance().getHomeFragment());
        fragments.add(FragmentFactory.getInstance().getFindFragment());
        fragments.add(FragmentFactory.getInstance().getDataFragment());
        fragments.add(FragmentFactory.getInstance().getMeFragment());
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
        vpHome.setOffscreenPageLimit(4);
        vpHome.setCurrentItem(0);
    }

    /**
     * 初始化侧滑菜单
     */
    private void initNav() {
        view = navView.inflateHeaderView(R.layout.nav_header_main);
        ivAvatar = (ImageView) view.findViewById(R.id.iv_avatar);
        TextView tvUsername = (TextView) view.findViewById(R.id.tv_username);
        llNavHomepage = (LinearLayout) view.findViewById(R.id.ll_nav_homepage);
        llNavScanDownload = (LinearLayout) view.findViewById(R.id.ll_nav_scan_download);
        llNavFeedback = (LinearLayout) view.findViewById(R.id.ll_nav_feedback);
        llNavAbout = (LinearLayout) view.findViewById(R.id.ll_nav_about);
        llNavLogin = (LinearLayout) view.findViewById(R.id.ll_nav_login);
        llNavVideo = (LinearLayout) view.findViewById(R.id.ll_nav_video);
        ImageUtils.loadImgByPicassoWithCircle(this, R.drawable.ic_person_logo, ivAvatar);
        tvUsername.setText("杨充");
    }


    /**
     * 自定义菜单点击事件
     */
    private PerfectClickListener listener = new PerfectClickListener() {
        @Override
        protected void onNoDoubleClick(final View v) {
            drawerLayout.closeDrawer(GravityCompat.START);
            drawerLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    switch (v.getId()) {
                        case R.id.iv_avatar:

                            break;
                        // 主页
                        case R.id.ll_nav_homepage:
                            Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
                            intent.putExtra("url", "");
                            startActivity(intent);
                            break;
                        //扫码下载
                        case R.id.ll_nav_scan_download:

                            break;
                        // 问题反馈
                        case R.id.ll_nav_feedback:
                            ActivityUtils.startActivity(MeFeedBackActivity.class);
                            break;
                        // 关于
                        case R.id.ll_nav_about:
                            ActivityUtils.startActivity(AboutMeActivity.class);
                            break;
                        // 个人
                        case R.id.ll_nav_login:
                            ActivityUtils.startActivity(MePersonActivity.class);
                            break;
                        case R.id.ll_nav_video:
                            ToastUtil.showToast(MainActivity.this, "后期接入讯飞语音");
                            break;
                        case R.id.setting:
                            ActivityUtils.startActivity(MeSettingActivity.class);
                            break;
                        case R.id.quit:
                            AppManager.getAppManager().appExit(false);
                            break;
                        default:
                            break;
                    }
                }
            }, 0);
        }
    };


    /**
     * onBackPressed、onKeyDown和onKeyUp这三个事件的区别
     */
    @Override
    public void onBackPressed() {
        Log.e("触摸监听", "onBackPressed");
        if (drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    /**
     * 是当某个按键被按下是触发。所以也有人在点击返回键的时候去执行该方法来做判断
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.e("触摸监听", "onKeyDown");
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                //双击返回桌面
                if ((System.currentTimeMillis() - time > 1000)) {
                    ToastUtil.showToast(MainActivity.this, "再按一次返回桌面");
                    time = System.currentTimeMillis();
                } else {
                    moveTaskToBack(true);
                }
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     * 初始化权限
     */
    private void initPermissions() {
        //权限
        presenter.locationPermissionsTask();
    }

    /**
     * 将结果转发到EasyPermissions
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 将结果转发到EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode,
                permissions, grantResults, MainActivity.this);
    }

    /**
     * 某些权限已被授予
     */
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        //某些权限已被授予
        Log.d("权限", "onPermissionsGranted:" + requestCode + ":" + perms.size());
    }

    /**
     * 某些权限已被拒绝
     */
    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        //某些权限已被拒绝
        Log.d("权限", "onPermissionsDenied:" + requestCode + ":" + perms.size());
        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        if (EasyPermissions.somePermissionPermanentlyDenied(MainActivity.this, perms)) {
            AppSettingsDialog.Builder builder = new AppSettingsDialog.Builder(MainActivity.this);
            builder.setTitle("允许权限")
                    .setRationale("没有该权限，此应用程序部分功能可能无法正常工作。打开应用设置界面以修改应用权限")
                    .setPositiveButton("去设置")
                    .setNegativeButton("取消")
                    .setRequestCode(124)
                    .build()
                    .show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            // Do something after user returned from app settings screen, like showing a Toast.
            // 当用户从应用设置界面返回的时候，可以做一些事情，比如弹出一个土司。
            Log.d("权限", "onPermissionsDenied:" + requestCode + ":");
        }
    }

}

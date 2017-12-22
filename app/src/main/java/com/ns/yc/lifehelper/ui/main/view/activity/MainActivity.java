package com.ns.yc.lifehelper.ui.main.view.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.AppManager;
import com.ns.yc.lifehelper.base.BaseActivity;
import com.ns.yc.lifehelper.base.BasePagerAdapter;
import com.ns.yc.lifehelper.listener.PerfectClickListener;
import com.ns.yc.lifehelper.ui.data.DataFragment;
import com.ns.yc.lifehelper.ui.find.view.fragment.FindFragment;
import com.ns.yc.lifehelper.ui.home.view.fragment.HomeFragment;
import com.ns.yc.lifehelper.ui.main.contract.MainContract;
import com.ns.yc.lifehelper.ui.main.presenter.MainPresenter;
import com.ns.yc.lifehelper.ui.me.view.MeFragment;
import com.ns.yc.lifehelper.ui.me.view.activity.MeFeedBackActivity;
import com.ns.yc.lifehelper.ui.me.view.activity.MePersonActivity;
import com.ns.yc.lifehelper.ui.me.view.activity.MeSettingActivity;
import com.ns.yc.lifehelper.utils.ImageUtils;
import com.ns.yc.lifehelper.utils.statusbar.StatusBarUtils;
import com.ns.yc.ycutilslib.managerLeak.InputMethodManagerLeakUtils;
import com.ns.yc.ycutilslib.viewPager.NoSlidingViewPager;
import com.pedaily.yc.ycdialoglib.toast.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/3/18
 * 描    述：Main主页面
 * 修订历史：
 * ================================================
 */
public class MainActivity extends BaseActivity implements View.OnClickListener
        ,EasyPermissions.PermissionCallbacks , MainContract.View {


    @Bind(R.id.view_status)
    View viewStatus;
    @Bind(R.id.fl_title_menu)
    FrameLayout flTitleMenu;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.vp_title)
    ViewPager vpTitle;
    @Bind(R.id.vp_home)
    NoSlidingViewPager vpHome;
    @Bind(R.id.ctl_table)
    CommonTabLayout ctlTable;
    @Bind(R.id.ll_main)
    LinearLayout llMain;
    @Bind(R.id.nav_view)
    NavigationView navView;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @Bind(R.id.setting)
    TextView setting;
    @Bind(R.id.quit)
    TextView quit;


    private ImageView iv_avatar;
    private LinearLayout ll_nav_scan_download;
    private LinearLayout ll_nav_deedback;
    private LinearLayout ll_nav_about;
    private LinearLayout ll_nav_login;
    private LinearLayout ll_nav_video;
    private LinearLayout ll_nav_homepage;
    private View view;
    private long time;

    private MainContract.Presenter presenter = new MainPresenter(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        presenter.subscribe();
        presenter.bindView(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        InputMethodManagerLeakUtils.fixInputMethodManagerLeak(this);
        presenter.unSubscribe();
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
                Toast.makeText(this, "二维码扫描", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_about_us:
                startActivity(AboutMeActivity.class);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void initView() {
        initStatusView();
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
            iv_avatar.setOnClickListener(listener);
            ll_nav_homepage.setOnClickListener(listener);
            ll_nav_scan_download.setOnClickListener(listener);
            ll_nav_deedback.setOnClickListener(listener);
            ll_nav_about.setOnClickListener(listener);
            ll_nav_login.setOnClickListener(listener);
            ll_nav_video.setOnClickListener(listener);
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
        }
    }


    /**
     * 初始化View状态栏
     */
    private void initStatusView() {
        ViewGroup.LayoutParams layoutParams = viewStatus.getLayoutParams();
        layoutParams.height = StatusBarUtils.getStatusBarHeight(this);
        viewStatus.setLayoutParams(layoutParams);
    }


    /**
     * 初始化侧滑菜单的状态栏
     */
    private void initDrawerLayoutStatus() {
        StatusBarUtils.setColorNoTranslucentForDrawerLayout(this, drawerLayout,
                getResources().getColor(R.color.colorTheme));
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
        //ctlTable.showDot(3);                   //显示红点
        //ctlTable.showMsg(2,5);                 //显示未读信息
        //ctlTable.showMsg(1,3);                 //显示未读信息
        //ctlTable.setMsgMargin(1, 2, 2);        //显示红点信息位置
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
        HomeFragment homeFragment = new HomeFragment();
        FindFragment findFragment = new FindFragment();
        DataFragment otherFragment = new DataFragment();
        MeFragment meFragment = new MeFragment();
        fragments.add(homeFragment);
        fragments.add(findFragment);
        fragments.add(otherFragment);
        fragments.add(meFragment);

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
        iv_avatar = (ImageView) view.findViewById(R.id.iv_avatar);
        TextView tv_username = (TextView) view.findViewById(R.id.tv_username);
        ll_nav_homepage = (LinearLayout) view.findViewById(R.id.ll_nav_homepage);
        ll_nav_scan_download = (LinearLayout) view.findViewById(R.id.ll_nav_scan_download);
        ll_nav_deedback = (LinearLayout) view.findViewById(R.id.ll_nav_deedback);
        ll_nav_about = (LinearLayout) view.findViewById(R.id.ll_nav_about);
        ll_nav_login = (LinearLayout) view.findViewById(R.id.ll_nav_login);
        ll_nav_video = (LinearLayout) view.findViewById(R.id.ll_nav_video);

        ImageUtils.loadImgByPicassoWithCircle(MainActivity.this, R.drawable.ic_person_logo, iv_avatar);
        tv_username.setText("杨充");
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
                        case R.id.ll_nav_homepage:          // 主页
                            Intent intent = new Intent(MainActivity.this,WebViewActivity.class);
                            intent.putExtra("url","");
                            startActivity(intent);
                            break;
                        case R.id.ll_nav_scan_download:     //扫码下载
                            startActivity(DownLoadActivity.class);
                            break;
                        case R.id.ll_nav_deedback:          // 问题反馈
                            startActivity(MeFeedBackActivity.class);
                            break;
                        case R.id.ll_nav_about:             // 关于
                            startActivity(AboutMeActivity.class);
                            break;
                        case R.id.ll_nav_login:             // 个人
                            startActivity(MePersonActivity.class);
                            break;
                        case R.id.ll_nav_video:
                            ToastUtil.showToast(MainActivity.this, "后期接入讯飞语音");
                            break;
                        case R.id.setting:
                            startActivity(MeSettingActivity.class);
                            break;
                        case R.id.quit:
                            AppManager.getAppManager().AppExit();
                            //BaseAppManager.getInstance().AppExit();
                            //exit();
                            //moveTaskToBack(true);
                            break;
                    }
                }
            }, 0);
        }
    };


    /**
     * 历史栈销毁所有Activity
     */
    private void exit() {
        // 退出程序方法有多种
        // 这里使用clear + new task的方式清空整个任务栈,只保留新打开的Main页面
        // 然后Main页面接收到退出的标志位exit=true,finish自己,这样就关闭了全部页面
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("exit", true);
        startActivity(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            boolean isExit = intent.getBooleanExtra("exit", false);
            if (isExit) {
                this.finish();
            }
        }
    }


    /**
     * onBackPressed、onKeyDown和onKeyUp这三个事件的区别
     */
    @Override
    public void onBackPressed() {
        Log.e("触摸监听","onBackPressed");
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
        Log.e("触摸监听","onKeyDown");
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                // 不退出程序，进入后台
                //moveTaskToBack(true);

                //双击返回桌面
                if ((System.currentTimeMillis() - time > 1000)) {
                    ToastUtil.showToast(MainActivity.this,"再按一次返回桌面");
                    time = System.currentTimeMillis();
                } else {
                    moveTaskToBack(true);
                    /*Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    startActivity(intent);*/
                }
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     * 这个方法是当某个按键被按下后，抬起的时候执行，其实跟onKeyDown没什么差别
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        Log.e("触摸监听","onKeyUp");
        return super.onKeyUp(keyCode, event);
    }


    /**
     * 初始化权限
     */
    private void initPermissions() {
        locationPermissionsTask();
    }

    private static final int RC_LOCATION_CONTACTS_PERM = 124;
    private static final String[] LOCATION_AND_CONTACTS = {
            Manifest.permission.CALL_PHONE,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };
    @AfterPermissionGranted(RC_LOCATION_CONTACTS_PERM)
    public void locationPermissionsTask() {
        //检查是否获取该权限
        if (hasPermissions()) {
            //具备权限 直接进行操作
            //Toast.makeText(this, "Location and Contacts things", Toast.LENGTH_LONG).show();
        } else {
            //权限拒绝 申请权限
            //第二个参数是被拒绝后再次申请该权限的解释
            //第三个参数是请求码
            //第四个参数是要申请的权限
            EasyPermissions.requestPermissions(MainActivity.this,
                    getString(R.string.easy_permissions), RC_LOCATION_CONTACTS_PERM, LOCATION_AND_CONTACTS);
        }
    }

    /**
     * 判断是否添加了权限
     * @return    true
     */
    private boolean hasPermissions() {
        return EasyPermissions.hasPermissions(MainActivity.this, LOCATION_AND_CONTACTS);
    }

    /**
     * 将结果转发到EasyPermissions
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,@NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 将结果转发到EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, MainActivity.this);
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
            //new AppSettingsDialog.Builder(MainActivity.this).build().show();
            AppSettingsDialog.Builder builder = new AppSettingsDialog.Builder(MainActivity.this);
            builder.setTitle("允许权限")
                    .setRationale("没有该权限，此应用程序部分功能可能无法正常工作。打开应用设置界面以修改应用权限")
                    .setPositiveButton("去设置")
                    .setNegativeButton("取消")
                    .setRequestCode(RC_LOCATION_CONTACTS_PERM)
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
        }
    }

}

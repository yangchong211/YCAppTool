package com.yc.widgetbusiness.status;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.yc.statusbar.dlBar.DlStatusBar;
import com.yc.toolutils.system.AppRomUtils;

import cn.ycbjie.ycstatusbar.R;

public class BarActivity extends AppCompatActivity implements View.OnClickListener {

    private FrameLayout mFlTitleMenu;
    private TextView mTvTitle;
    private Toolbar mToolbar;
    private Button mBtnStatusbarColor;
    private Button mBtnStatusbarColorToolbar;
    private Button mBtnStatusbarTranslucent;
    private Button mBtnStatusbarColorCoordinator;
    private Button mBtnStatusbarTranslucentCoordinator;
    private Button mBtnStatusbarWhite;
    private Button mBtnStatusbarWhiteToolbar;
    private Button mBtnStatusbarWhiteCoordinator;
    private Button mBtnStatusbarWhiteCoordinator2;
    private Button mBtnStatusbarFragment;
    private Button mBtnStatusbarFragment2;
    private Button mBtnStatusbarFragment3;
    private LinearLayout mLlMain;
    private NavigationView mNavView;
    private DrawerLayout mDrawerLayout;


    /**
     * 开启页面
     *
     * @param context 上下文
     */
    public static void startActivity(Context context) {
        try {
            Intent target = new Intent();
            target.setClass(context, BarActivity.class);
            //target.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(target);
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_bar);
        initView();
        initDrawerLayoutStatus();
        initBar();
        initNav();

        boolean flyme = AppRomUtils.isFlyme();
        System.out.println("是否是魅族"+flyme);
        boolean emui = AppRomUtils.isEmui();
        System.out.println("是否是华为"+flyme);
        String name = AppRomUtils.getName();
        String version = AppRomUtils.getVersion();
        System.out.println("是否是"+name+"----"+version);
    }


    /**
     * 初始化侧滑菜单的状态栏
     */
    private void initDrawerLayoutStatus() {
        //为DrawerLayout 布局设置状态栏颜色,纯色
        /*StatusBarUtils.setColorNoTranslucentForDrawerLayout(this, drawerLayout,
                getResources().getColor(R.color.colorTheme));*/
        //为DrawerLayout 布局设置状态栏变色，也就是加上透明度
        DlStatusBar.setColorForDrawerLayout(this, mDrawerLayout,
                getResources().getColor(R.color.colorTheme), 0);
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

    /**
     * 初始化侧滑菜单
     */
    private void initNav() {
        mNavView.inflateHeaderView(R.layout.nav_header_main);
    }


    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        int id = view.getId();
        if (id == R.id.fl_title_menu) {
            mDrawerLayout.openDrawer(GravityCompat.START);
        } else if (id == R.id.btn_statusbar_color) {
            intent.setClass(this, StatusBarColorActivity.class);
            startActivity(intent);
        } else if (id == R.id.btn_statusbar_color_toolbar) {
            intent.setClass(this, StatusBarColorToolBarActivity.class);
            startActivity(intent);
        } else if (id == R.id.btn_statusbar_translucent) {
            intent.setClass(this, StatusBarTranslucent.class);
            startActivity(intent);
        } else if (id == R.id.btn_statusbar_color_coordinator) {
            intent.setClass(this, StatusBarColorCoordinatorActivity.class);
            startActivity(intent);
        } else if (id == R.id.btn_statusbar_translucent_coordinator) {
            intent.setClass(this, StatusBarTranslucentCoordinatorActivity.class);
            startActivity(intent);
        } else if (id == R.id.btn_statusbar_white) {
            intent.setClass(this, StatusBarWhiteActivity.class);
            startActivity(intent);
        } else if (id == R.id.btn_statusbar_white_toolbar) {
            intent.setClass(this, StatusBarWhiteToolBarActivity.class);
            startActivity(intent);
        } else if (id == R.id.btn_statusbar_white_coordinator) {
            intent.setClass(this, StatusBarWhiteCoordinatorActivity.class);
            startActivity(intent);
        } else if (id == R.id.btn_statusbar_white_coordinator2) {
            intent.setClass(this, StatusBarWhiteCoordinatorActivity2.class);
            startActivity(intent);
        } else if (id == R.id.btn_statusbar_fragment) {
            intent.setClass(this, StatusBarFragmentActivity.class);
            startActivity(intent);
        } else if (id == R.id.btn_statusbar_fragment2) {
            intent.setClass(this, StatusBarFragment2Activity.class);
            startActivity(intent);
        } else if (id == R.id.btn_statusbar_fragment3) {
            intent.setClass(this, StatusBarFragment3Activity.class);
            startActivity(intent);
        } else if (id == R.id.nav_view) {
        } else if (id == R.id.tv_title) {
        } else if (id == R.id.toolbar) {
        } else if (id == R.id.ll_main) {
        } else if (id == R.id.drawer_layout) {
        }
    }

    /**
     * onBackPressed、onKeyDown和onKeyUp这三个事件的区别
     */
    @Override
    public void onBackPressed() {
        Log.e("触摸监听", "onBackPressed");
        if (mDrawerLayout != null && mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    private void initView() {
        mFlTitleMenu = (FrameLayout) findViewById(R.id.fl_title_menu);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mBtnStatusbarColor = (Button) findViewById(R.id.btn_statusbar_color);
        mBtnStatusbarColor.setOnClickListener(this);
        mBtnStatusbarColorToolbar = (Button) findViewById(R.id.btn_statusbar_color_toolbar);
        mBtnStatusbarColorToolbar.setOnClickListener(this);
        mBtnStatusbarTranslucent = (Button) findViewById(R.id.btn_statusbar_translucent);
        mBtnStatusbarTranslucent.setOnClickListener(this);
        mBtnStatusbarColorCoordinator = (Button) findViewById(R.id.btn_statusbar_color_coordinator);
        mBtnStatusbarColorCoordinator.setOnClickListener(this);
        mBtnStatusbarTranslucentCoordinator = (Button) findViewById(R.id.btn_statusbar_translucent_coordinator);
        mBtnStatusbarTranslucentCoordinator.setOnClickListener(this);
        mBtnStatusbarWhite = (Button) findViewById(R.id.btn_statusbar_white);
        mBtnStatusbarWhite.setOnClickListener(this);
        mBtnStatusbarWhiteToolbar = (Button) findViewById(R.id.btn_statusbar_white_toolbar);
        mBtnStatusbarWhiteToolbar.setOnClickListener(this);
        mBtnStatusbarWhiteCoordinator = (Button) findViewById(R.id.btn_statusbar_white_coordinator);
        mBtnStatusbarWhiteCoordinator.setOnClickListener(this);
        mBtnStatusbarWhiteCoordinator2 = (Button) findViewById(R.id.btn_statusbar_white_coordinator2);
        mBtnStatusbarWhiteCoordinator2.setOnClickListener(this);
        mBtnStatusbarFragment = (Button) findViewById(R.id.btn_statusbar_fragment);
        mBtnStatusbarFragment.setOnClickListener(this);
        mBtnStatusbarFragment2 = (Button) findViewById(R.id.btn_statusbar_fragment2);
        mBtnStatusbarFragment2.setOnClickListener(this);
        mBtnStatusbarFragment3 = (Button) findViewById(R.id.btn_statusbar_fragment3);
        mBtnStatusbarFragment3.setOnClickListener(this);
        mLlMain = (LinearLayout) findViewById(R.id.ll_main);
        mNavView = (NavigationView) findViewById(R.id.nav_view);
        mNavView.setOnClickListener(this);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mFlTitleMenu.setOnClickListener(this);
        mTvTitle.setOnClickListener(this);
        mToolbar.setOnClickListener(this);
        mLlMain.setOnClickListener(this);
        mDrawerLayout.setOnClickListener(this);
    }
}

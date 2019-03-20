package com.ns.yc.lifehelper.ui.main.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IntDef;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.LogUtils;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.comment.FragmentFactory;
import com.ns.yc.lifehelper.service.LoopRequestService;
import com.ns.yc.lifehelper.ui.main.contract.MainContract;
import com.ns.yc.lifehelper.ui.main.presenter.MainPresenter;
import com.ns.yc.lifehelper.ui.me.view.activity.MePersonActivity;
import com.ns.yc.ycutilslib.activityManager.AppManager;
import com.ns.yc.ycutilslib.managerLeak.InputMethodManagerLeakUtils;
import com.ns.yc.ycutilslib.viewPager.NoSlidingViewPager;
import com.pedaily.yc.ycdialoglib.toast.ToastUtils;
import com.ycbjie.library.arounter.ARouterConstant;
import com.ycbjie.library.arounter.ARouterUtils;
import com.ycbjie.library.base.adapter.BasePagerAdapter;
import com.ycbjie.library.base.mvp.BaseActivity;
import com.ycbjie.library.constant.Constant;
import com.ycbjie.library.inter.listener.PerfectClickListener;
import com.ycbjie.library.utils.animation.AnimatorUtils;
import com.ycbjie.library.utils.image.ImageUtils;
import com.ycbjie.library.web.view.WebViewActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


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
        , MainContract.View {

    private DrawerLayout mDrawerLayout;
    private NoSlidingViewPager mVpHome;
    private CommonTabLayout mCtlTable;
    private NavigationView mNavView;
    private TextView mSetting;
    private TextView mQuit;
    private Toolbar mToolbar;
    private FrameLayout mFlTitleMenu;
    private TextView mTvTitle;
    private LinearLayout llMainButton;
    private TextView tvAnimView;
    private ImageView ivAnimView;

    private long time;
    public static final int HOME = 0;
    public static final int FIND = 1;
    public static final int DATA = 2;
    public static final int USER = 3;
    private MainContract.Presenter presenter = new MainPresenter(this);
    private int selectIndex;
    private int tvAnimViewWidth;
    protected ValueAnimator mAnimExpand;
    protected ValueAnimator mAnimReduce;

    @IntDef({HOME, FIND, DATA, USER})
    private @interface PageIndex {}

    /**
     * 定时任务工具类
     */
    public Timer timer;
    private boolean isTimerRunning = false;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HOME:
                    initRetractAnim();
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 跳转首页* @param context     上下文
     * @param selectIndex 添加注解限制输入值
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
     *
     * @param intent intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            selectIndex = intent.getIntExtra("selectIndex", HOME);
            mVpHome.setCurrentItem(selectIndex);
        }
    }

    @Override
    protected void onDestroy() {
        cancelAllAnim();
        cancelTimer();
        super.onDestroy();
        LoopRequestService.quitLoopService(this);
        InputMethodManagerLeakUtils.fixInputMethodManagerLeak(this);
        if (mHandler!=null){
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            selectIndex = savedInstanceState.getInt("selectIndex",0);
            mVpHome.setCurrentItem(selectIndex);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (outState!=null){
            outState.putInt("selectIndex",selectIndex);
        }
    }

    @Override
    public int getContentView() {
        return R.layout.activity_main;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_setting_app, menu);
        menu.add(0, 1, 0, "开发作者介绍");
        menu.add(0, 3, 1,"分享此软件");
        menu.add(0, 4, 2,"开源项目介绍");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_capture:
                ToastUtils.showRoundRectToast("二维码扫描"+1000);
                break;
            case R.id.action_about_us:
                ARouterUtils.navigation(ARouterConstant.ACTIVITY_OTHER_ABOUT_ME);
                break;
            case 1:
                WebViewActivity.lunch(this,Constant.GITHUB,"我的GitHub");
                break;
            case 2:
                WebViewActivity.lunch(this,Constant.LIFE_HELPER,"开源项目介绍");
                break;
            case 3:
                WebViewActivity.lunch(this,Constant.ZHI_HU,"我的知乎");
                break;
            case 4:
                WebViewActivity.lunch(this,Constant.JUE_JIN,"我的掘金");
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void initView() {
        initFindViewID();
        initDrawerLayoutStatus();
        initBar();
        initTabLayout();
        initViewPager();
        initNav();
        LoopRequestService.startLoopService(this);
        startTimer();
    }

    private void initFindViewID() {
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mVpHome = findViewById(R.id.vp_home);
        mCtlTable = findViewById(R.id.ctl_table);
        mNavView = findViewById(R.id.nav_view);
        mSetting =findViewById(R.id.setting);
        mQuit = findViewById(R.id.quit);
        mToolbar = findViewById(R.id.toolbar);
        mFlTitleMenu = findViewById(R.id.fl_title_menu);
        mTvTitle = findViewById(R.id.tv_title);
        llMainButton = findViewById(R.id.ll_main_button);
        tvAnimView = findViewById(R.id.tv_anim_view);
        ivAnimView = findViewById(R.id.iv_anim_view);

        tvAnimView.post(() -> {
            //获取宽度
            tvAnimViewWidth = tvAnimView.getWidth();
        });
        /*ViewTreeObserver observer = tvAnimView.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                tvAnimView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                tvAnimViewWidth = tvAnimView.getMeasuredWidth();
                //int height = tvAnimView.getMeasuredHeight();
            }
        });*/
        tvAnimView.setVisibility(View.VISIBLE);
    }


    @Override
    public void initListener() {
        mFlTitleMenu.setOnClickListener(MainActivity.this);
        mNavView.setOnClickListener(MainActivity.this);
        llMainButton.setOnClickListener(this);
        ivAnimView.setOnClickListener(this);
        tvAnimView.setOnClickListener(this);
        mSetting.setOnClickListener(listener);
        mQuit.setOnClickListener(listener);
    }


    @Override
    public void initData() {
        presenter.getUpdate();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fl_title_menu:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.ll_main_button:

                break;
            case R.id.iv_anim_view:
                if (tvAnimView.getVisibility() == View.INVISIBLE){
                    LogUtils.e("动画按钮","伸展");
                    initExpandAnim();
                } else {
                    LogUtils.e("动画按钮","收缩");
                    initRetractAnim();
                }
                break;
            case R.id.tv_anim_view:
                Bundle bundle1 = new Bundle();
                bundle1.putString(Constant.URL,Constant.GITHUB);
                bundle1.putString(Constant.TITLE,"关于更多博客内容");
                ARouterUtils.navigation(ARouterConstant.ACTIVITY_LIBRARY_WEB_VIEW,bundle1);
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
        setSupportActionBar(mToolbar);
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
        mCtlTable.setTabData(mTabEntities);
        mTvTitle.setText("新闻首页");
        mCtlTable.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                selectIndex = position;
                mVpHome.setCurrentItem(selectIndex);
                switch (position) {
                    case 0:
                        mTvTitle.setVisibility(View.VISIBLE);
                        mTvTitle.setText("新闻首页");
                        break;
                    case 1:
                        mTvTitle.setVisibility(View.VISIBLE);
                        mTvTitle.setText("数据中心");
                        break;
                    case 2:
                        mTvTitle.setVisibility(View.VISIBLE);
                        mTvTitle.setText("生活应用");
                        mCtlTable.showMsg(2, 0);
                        break;
                    case 3:
                        mTvTitle.setVisibility(View.VISIBLE);
                        mTvTitle.setText("更多内容");
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
        mVpHome.setAdapter(adapter);
        mVpHome.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position >= 0) {
                    mCtlTable.setCurrentTab(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        mVpHome.setOffscreenPageLimit(4);
        mVpHome.setCurrentItem(0);
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
        ImageUtils.loadImgByPicassoWithCircle(this, R.drawable.ic_person_logo, ivAvatar);
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
    private PerfectClickListener listener = new PerfectClickListener() {
        @Override
        protected void onNoDoubleClick(final View v) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            mDrawerLayout.postDelayed(() -> {
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
                        ARouterUtils.navigation(ARouterConstant.ACTIVITY_OTHER_FEEDBACK);
                        break;
                    // 关于
                    case R.id.ll_nav_about:
                        ARouterUtils.navigation(ARouterConstant.ACTIVITY_OTHER_ABOUT_ME);
                        break;
                    // 个人
                    case R.id.ll_nav_login:
                        ActivityUtils.startActivity(MePersonActivity.class);
                        break;
                    case R.id.ll_nav_video:
                        ToastUtils.showRoundRectToast( "后期接入讯飞语音");
                        break;
                    case R.id.setting:
                        ARouterUtils.navigation(ARouterConstant.ACTIVITY_APP_SETTING_ACTIVITY);
                        break;
                    case R.id.quit:
                        AppManager.getAppManager().appExit(false);
                        break;
                    default:
                        break;
                }
            }, 0);
        }
    };


    /**
     * 悬浮控件收缩动画
     */
    private void initRetractAnim() {
        LogUtils.e("动画按钮","控件宽度-----"+tvAnimViewWidth);
        cancelAllAnim();
        mAnimReduce = AnimatorUtils.setValueAnimator(llMainButton,
                0, 1, 300, 300, 1);
        mAnimReduce.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Object animatedValue = animation.getAnimatedValue();
                LogUtils.e("动画按钮","收缩结束onAnimationUpdate"+animatedValue);
                //int a = (int) animatedValue;
                //tv_anim.scrollTo(a, 0);
                //逐渐减小
                int value = (int) animatedValue;
                ViewGroup.LayoutParams layoutParams = tvAnimView.getLayoutParams();
                if (value!=0){
                    layoutParams.width = (int) (tvAnimViewWidth - value*tvAnimViewWidth);
                    tvAnimView.setLayoutParams(layoutParams);
                }
            }
        });
        mAnimReduce.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                LogUtils.e("动画按钮","收缩结束");
                tvAnimView.setVisibility(View.INVISIBLE);
                cancelTimer();
            }

            @Override
            public void onAnimationStart(Animator animation) {

            }
        });
        mAnimReduce.start();
    }

    /**
     * 悬浮控件伸展动画
     */
    private void initExpandAnim() {
        LogUtils.e("动画按钮","控件宽度-----"+tvAnimViewWidth);
        cancelAllAnim();
        mAnimExpand = AnimatorUtils.setValueAnimator(llMainButton,
                1, 0, 300, 300, 1);
        mAnimExpand.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Object animatedValue = animation.getAnimatedValue();
                LogUtils.e("动画按钮","伸展结束onAnimationUpdate"+animatedValue);
                //int a = (int) animatedValue;
                //tv_anim.scrollTo(a, 0);
                int value = (int) animatedValue;
                ViewGroup.LayoutParams layoutParams = tvAnimView.getLayoutParams();
                if (value!=0){
                    layoutParams.width = (int) (tvAnimViewWidth - value*tvAnimViewWidth);
                    tvAnimView.setLayoutParams(layoutParams);
                }else {
                    layoutParams.width = tvAnimViewWidth;
                    tvAnimView.setLayoutParams(layoutParams);
                }
            }
        });
        mAnimExpand.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                LogUtils.e("动画按钮","伸展结束");
                tvAnimView.setVisibility(View.VISIBLE);
                startTimer();
            }

            @Override
            public void onAnimationStart(Animator animation) {

            }
        });
        mAnimExpand.start();
    }

    /**
     * 暂停所有动画
     */
    private void cancelAllAnim() {
        if (mAnimExpand != null && mAnimExpand.isRunning()) {
            mAnimExpand.setTarget(null);
            mAnimExpand.cancel();
        }
        if (mAnimReduce != null && mAnimReduce.isRunning()) {
            mAnimReduce.setTarget(null);
            mAnimReduce.cancel();
        }
    }


    /**
     * 开启倒计时
     */
    private void startTimer() {
        if (isTimerRunning){
            return;
        }
        if (timer == null) {
            timer = new Timer();
        }
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                isTimerRunning = true;
                if (tvAnimView.getVisibility() == View.INVISIBLE){
                    LogUtils.e("动画按钮","伸展");
                } else {
                    LogUtils.e("动画按钮","收缩");
                    Message msg = new Message();
                    msg.what = HOME;
                    mHandler.sendMessage(msg);
                    //initRetractAnim();
                }
            }
        }, 3000, 3 * 1000);
    }


    /**
     * 取消倒计时
     */
    private void cancelTimer(){
        if (timer != null) {
            timer.cancel();
            timer = null;
            isTimerRunning = false;
        }
    }



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

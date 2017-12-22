package com.ns.yc.lifehelper.base;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.ns.yc.lifehelper.R;
import com.ns.yc.ycstatelib.StateLayoutManager;
import com.squareup.leakcanary.RefWatcher;

import butterknife.ButterKnife;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/5/18
 * 描    述：所有Activity的父类，使用开源库compile 'cn.yc:YCStateLib:1.0'进行状态管理
 * 修订历史：
 * 开源库地址：https://github.com/yangchong211/YCStateLayout
 *          v1.5版本，使用自己的开源库，可以自由切换5种不同状态
 *          且与Activity，Fragment让View状态的切换和Activity彻底分离开
 *          欢迎star和反馈
 * ================================================
 */
public abstract class BaseStateActivity extends AppCompatActivity{

    protected StateLayoutManager statusLayoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_state_view);
        initStatusLayout();
        initBaseView();
        ButterKnife.bind(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);              //避免切换横竖屏
        AppManager.getAppManager().addActivity(this);                                   //将当前Activity添加到容器
        initView();
        initListener();
        initData();
        if(!NetworkUtils.isConnected()){
            ToastUtils.showShort("请检查网络是否连接");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        initLeakCanary();             //测试内存泄漏，正式一定要隐藏
        AppManager.getAppManager().removeActivity(this);                                //将当前Activity移除到容器
        //AppManager.getAppManager().finishActivity(this);
    }


    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onStop();
    }


    /** 状态切换 **/
    protected abstract void initStatusLayout();

    /** 初始化View的代码写在这个方法中 */
    public abstract void initView();

    /** 初始化监听器的代码写在这个方法中 */
    public abstract void initListener();

    /** 初始数据的代码写在这个方法中，用于从服务器获取数据 */
    public abstract void initData();

    /**
     * 获取到子布局
     */
    private void initBaseView() {
        LinearLayout fl_main = (LinearLayout) findViewById(R.id.ll_main);
        fl_main.addView(statusLayoutManager.getRootLayout());
    }


    /**
     * 沉浸式状态栏
     */
    protected void initState() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            // 隐藏状态栏
            //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN); // 隐藏状态栏
        }
    }

    /**
     * 通过Class跳转界面
     **/
    public void startActivity(Class<?> cls) {
        startActivity(cls, null);
    }

    /**
     * 通过Class跳转界面
     **/
    public void startActivityForResult(Class<?> cls, int requestCode) {
        startActivityForResult(cls, null, requestCode);
    }

    /**
     * 含有Bundle通过Class跳转界面
     **/
    public void startActivityForResult(Class<?> cls, Bundle bundle, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    /**
     * 含有Bundle通过Class跳转界面
     **/
    public void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * 用来检测所有Activity的内存泄漏
     */
    private void initLeakCanary() {
        RefWatcher refWatcher = BaseApplication.getRefWatcher(this);
        refWatcher.watch(this);
    }


}

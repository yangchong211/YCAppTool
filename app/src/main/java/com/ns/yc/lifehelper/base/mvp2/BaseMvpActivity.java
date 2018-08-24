package com.ns.yc.lifehelper.base.mvp2;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.ns.yc.lifehelper.R;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.ycbjie.ycstatusbarlib.bar.YCAppBar;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/11/23
 * 描    述：改版封装MVP结构的Activity的父类
 * 修订历史：
 * ================================================
 */
public abstract class BaseMvpActivity<T extends BaseMvpPresenter> extends AppCompatActivity implements BaseMvpView {

    @Inject
    protected T mPresenter;
    protected Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        unbinder = ButterKnife.bind(this);

        //避免切换横竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        YCAppBar.setStatusBarColor(this, R.color.colorTheme);
        if(mPresenter!=null){
            mPresenter.subscribe(this);
        }
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
        //注意一定要先做销毁mPresenter.unSubscribe()，然后在调用这步ButterKnife.unbind(this)，销毁所有控件对象
        //查询unbind源码可知，执行这步后会将创建所有控件对象置null，如果后调用mPresenter.unSubscribe()，
        //那么如果有对象在做执行行为，将会导致控件对象报空指针。一定注意先后顺序。
        if(mPresenter!=null){
            mPresenter.unSubscribe();
        }
        unbinder.unbind();
    }



    /** 返回一个用于显示界面的布局id */
    public abstract int getContentView();

    /** 初始化View的代码写在这个方法中 */
    public abstract void initView();

    /** 初始化监听器的代码写在这个方法中 */
    public abstract void initListener();

    /** 初始数据的代码写在这个方法中，用于从服务器获取数据 */
    public abstract void initData();


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

}

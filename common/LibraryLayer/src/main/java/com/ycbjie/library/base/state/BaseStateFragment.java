package com.ycbjie.library.base.state;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ycbjie.library.R;
import com.ycbjie.library.base.mvp.BasePresenter;
import com.ns.yc.ycstatelib.StateLayoutManager;


/**
 * <pre>
 *     @author      杨充
 *     blog         https://www.jianshu.com/p/53017c3fc75d
 *     time         2015/08/22
 *     desc
 *     revise       使用自己的开源库，可以自由切换5种不同状态
 *                  且与Activity，Fragment让View状态的切换和Activity彻底分离开
 *                  欢迎star和反馈
 *     GitHub       https://github.com/yangchong211
 * </pre>
 */
public abstract class BaseStateFragment<T extends BasePresenter>  extends Fragment {

    protected StateLayoutManager statusLayoutManager;
    /**
     * 将代理类通用行为抽出来
     */
    protected T mPresenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.base_state_view, container , false);
        initStatusLayout();
        initBaseView(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mPresenter != null){
            mPresenter.subscribe();
        }
        initView(view);
        initListener();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null){
            mPresenter.unSubscribe();
        }
        initLeakCanary();             //测试内存泄漏，正式一定要隐藏
    }

    /**
     * 获取到子布局
     * @param view              view
     */
    private void initBaseView(View view) {
        LinearLayout flMain = view.findViewById(R.id.ll_main);
        flMain.addView(statusLayoutManager.getRootLayout());
    }

    /** 状态切换 **/
    protected abstract void initStatusLayout();

    /** 初始化View的代码写在这个方法中
     * @param view          view
     */
    public abstract void initView(View view);

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
        intent.setClass(getActivity(), cls);
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
        intent.setClass(getActivity(), cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**用来检测所有Fragment的内存泄漏*/
    private void initLeakCanary() {
        /*RefWatcher refWatcher = BaseApplication.getRefWatcher(getActivity());
        refWatcher.watch(this);*/
    }



}

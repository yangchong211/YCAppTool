package com.ns.yc.lifehelper.base.mvp2;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/8/18
 * 描    述：父类
 * 修订历史：
 * ================================================
 */
public interface BaseMvpPresenter<T extends BaseMvpView>{

    //绑定数据
    void subscribe(T view);
    //解除绑定
    void unSubscribe();

}

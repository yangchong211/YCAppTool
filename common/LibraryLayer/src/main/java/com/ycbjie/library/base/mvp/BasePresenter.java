package com.ycbjie.library.base.mvp;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/8/18
 * 描    述：父类
 * 修订历史：
 * ================================================
 */
public interface BasePresenter {
    //绑定数据
    void subscribe();
    //解除绑定
    void unSubscribe();
}
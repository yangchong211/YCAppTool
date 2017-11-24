package com.ns.yc.lifehelper.ui.data.contract;


import android.app.Activity;

import com.ns.yc.lifehelper.base.BasePresenter;
import com.ns.yc.lifehelper.base.BaseView;

import java.util.List;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/9/14
 * 描    述：启动页
 * 修订历史：
 * ================================================
 */
public interface OpenFileContract {

    //View(activity/fragment)继承，需要实现的方法
    interface View extends BaseView {
        Activity getActivity();
    }

    //Presenter控制器
    interface Presenter extends BasePresenter {
        //获取文件数据
        List<String> getFileData();
    }


}

package com.ns.yc.lifehelper.base;

import com.ns.yc.lifehelper.ui.data.DataFragment;
import com.ns.yc.lifehelper.ui.find.view.fragment.FindFragment;
import com.ns.yc.lifehelper.ui.home.view.fragment.HomeFragment;
import com.ns.yc.lifehelper.ui.me.view.MeFragment;



/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/12/22
 * 描    述：Fragment工厂
 * 修订历史：
 *      备注：看《Android源码设计》一书，学习设计模式并运用
 * ================================================
 */
public class BaseFragmentFactory {

    private static BaseFragmentFactory mInstance;
    private HomeFragment mHomeFragment;
    private FindFragment mFindFragment;
    private DataFragment mDataFragment;
    private MeFragment mMeFragment;

    private BaseFragmentFactory() {}

    public static BaseFragmentFactory getInstance() {
        if (mInstance == null) {
            synchronized (BaseFragmentFactory.class) {
                if (mInstance == null) {
                    mInstance = new BaseFragmentFactory();
                }
            }
        }
        return mInstance;
    }


    public HomeFragment getHomeFragment() {
        if (mHomeFragment == null) {
            synchronized (BaseFragmentFactory.class) {
                if (mHomeFragment == null) {
                    mHomeFragment = new HomeFragment();
                }
            }
        }
        return mHomeFragment;
    }


    public FindFragment getFindFragment() {
        if (mFindFragment == null) {
            synchronized (BaseFragmentFactory.class) {
                if (mFindFragment == null) {
                    mFindFragment = new FindFragment();
                }
            }
        }
        return mFindFragment;
    }


    public DataFragment getDataFragment() {
        if (mDataFragment == null) {
            synchronized (BaseFragmentFactory.class) {
                if (mDataFragment == null) {
                    mDataFragment = new DataFragment();
                }
            }
        }
        return mDataFragment;
    }


    public MeFragment getMeFragment() {
        if (mMeFragment == null) {
            synchronized (BaseFragmentFactory.class) {
                if (mMeFragment == null) {
                    mMeFragment = new MeFragment();
                }
            }
        }
        return mMeFragment;
    }




}

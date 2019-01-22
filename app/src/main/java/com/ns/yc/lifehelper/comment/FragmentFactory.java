package com.ns.yc.lifehelper.comment;

import com.ns.yc.lifehelper.ui.data.view.fragment.DataFragment;
import com.ns.yc.lifehelper.ui.find.view.fragment.FindFragment;
import com.ns.yc.lifehelper.ui.home.view.fragment.HomeFragment;
import com.ns.yc.lifehelper.ui.me.view.MeFragment;


/**
 * <pre>
 *     @author      杨充
 *     blog         https://www.jianshu.com/p/53017c3fc75d
 *     time         2017/12/22
 *     desc
 *     revise       看《Android源码设计》一书，学习设计模式并运用
 *     GitHub       https://github.com/yangchong211
 * </pre>
 */
public class FragmentFactory {

    private static FragmentFactory mInstance;
    private HomeFragment mHomeFragment;
    private FindFragment mFindFragment;
    private DataFragment mDataFragment;
    private MeFragment mMeFragment;

    private FragmentFactory() {}

    public static FragmentFactory getInstance() {
        if (mInstance == null) {
            synchronized (FragmentFactory.class) {
                if (mInstance == null) {
                    mInstance = new FragmentFactory();
                }
            }
        }
        return mInstance;
    }


    public HomeFragment getHomeFragment() {
        if (mHomeFragment == null) {
            synchronized (FragmentFactory.class) {
                if (mHomeFragment == null) {
                    mHomeFragment = new HomeFragment();
                }
            }
        }
        return mHomeFragment;
    }


    public FindFragment getFindFragment() {
        if (mFindFragment == null) {
            synchronized (FragmentFactory.class) {
                if (mFindFragment == null) {
                    mFindFragment = new FindFragment();
                }
            }
        }
        return mFindFragment;
    }


    public DataFragment getDataFragment() {
        if (mDataFragment == null) {
            synchronized (FragmentFactory.class) {
                if (mDataFragment == null) {
                    mDataFragment = new DataFragment();
                }
            }
        }
        return mDataFragment;
    }


    public MeFragment getMeFragment() {
        if (mMeFragment == null) {
            synchronized (FragmentFactory.class) {
                if (mMeFragment == null) {
                    mMeFragment = new MeFragment();
                }
            }
        }
        return mMeFragment;
    }




}

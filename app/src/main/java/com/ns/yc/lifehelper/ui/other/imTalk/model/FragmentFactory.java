package com.ns.yc.lifehelper.ui.other.imTalk.model;

import com.ns.yc.lifehelper.ui.other.imTalk.ui.fragment.ImContactFragment;
import com.ns.yc.lifehelper.ui.other.imTalk.ui.fragment.ImConversationFragment;
import com.ns.yc.lifehelper.ui.other.imTalk.ui.fragment.ImSettingFragment;


/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/12/22
 * 描    述：聊天页面3个Fragment工厂
 * 修订历史：
 *      备注：看《Android源码设计》一书，学习设计模式并运用
 * ================================================
 */
public class FragmentFactory {

    private static FragmentFactory mInstance;
    private ImConversationFragment mImConversationFragment;
    private ImContactFragment mImContactFragment;
    private ImSettingFragment mImSettingFragment;

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


    public ImConversationFragment getImConversationFragment() {
        if (mImConversationFragment == null) {
            synchronized (FragmentFactory.class) {
                if (mImConversationFragment == null) {
                    mImConversationFragment = new ImConversationFragment();
                }
            }
        }
        return mImConversationFragment;
    }


    public ImContactFragment getImContactFragment() {
        if (mImContactFragment == null) {
            synchronized (FragmentFactory.class) {
                if (mImContactFragment == null) {
                    mImContactFragment = new ImContactFragment();
                }
            }
        }
        return mImContactFragment;
    }


    public ImSettingFragment getImSettingFragment() {
        if (mImSettingFragment == null) {
            synchronized (FragmentFactory.class) {
                if (mImSettingFragment == null) {
                    mImSettingFragment = new ImSettingFragment();
                }
            }
        }
        return mImSettingFragment;
    }

}

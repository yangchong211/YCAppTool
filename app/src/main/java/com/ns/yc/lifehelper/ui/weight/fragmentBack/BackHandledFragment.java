package com.ns.yc.lifehelper.ui.weight.fragmentBack;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

public abstract class BackHandledFragment extends Fragment implements FragmentBackHandler {

    public BackHandledFragment() {
    }

    @Override
    public final boolean onBackPressed() {
        return interceptBackPressed() || (getBackHandleViewPager() == null
                ? BackHandlerHelper.handleBackPress(this)
                : BackHandlerHelper.handleBackPress(getBackHandleViewPager()));
    }

    /**
     * 子类可以继承，实现是否分发back事件
     * 默认返回false，表示子类不分发
     * @return
     */
    public boolean interceptBackPressed() {
        return false;
    }

    /**
     * 2.1 版本已经不在需要单独对ViewPager处理
     * @deprecated
     */
    @Deprecated
    public ViewPager getBackHandleViewPager() {
        return null;
    }
}

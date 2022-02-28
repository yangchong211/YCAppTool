package com.yc.fragmentlib;


import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/4/22
 * 描    述：处理Fragment返回键，需要继承该类
 * 修订历史：
 * ================================================
 */
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

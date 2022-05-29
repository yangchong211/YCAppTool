package com.yc.baseclasslib.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * <pre>
 *     @author 杨充
 *     GitHub : https://github.com/yangchong211/YCCommonLib
 *     time  : 2017/01/30
 *     desc  :
 *     revise: FragmentPageStateAdapter在每次切换页面的时候，是将Fragment进行回收，适合页面较多的Fragment使用，这样就不会消耗更多的内存
 * </pre>
 */
public class BasePagerStateAdapter extends FragmentStatePagerAdapter {

    private final List<?> mFragment;
    private final List<String> mTitleList;

    /**
     * 接收首页传递的标题
     */
    public BasePagerStateAdapter(FragmentManager fm, List<?> mFragment, List<String> mTitleList) {
        super(fm);
        this.mFragment = mFragment;
        this.mTitleList = mTitleList;
    }

    @Override
    public Fragment getItem(int position) {
        return (Fragment) mFragment.get(position);
    }

    @Override
    public int getCount() {
        return mFragment == null ? 0 : mFragment.size();
    }

    /**
     * 首页显示title，每日推荐等..
     * 若有问题，移到对应单独页面
     */
    @Override
    public CharSequence getPageTitle(int position) {
        if (mTitleList != null) {
            return mTitleList.get(position);
        } else {
            return "";
        }
    }
}

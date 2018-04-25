package com.ns.yc.lifehelper.ui.other.workDo.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ns.yc.lifehelper.ui.other.workDo.model.MainPageItem;

import java.util.List;

import javax.inject.Inject;


public class WorkPageAdapter extends FragmentPagerAdapter {

    private List<MainPageItem> mPageItems;

    @Inject
    public WorkPageAdapter(FragmentManager fm, List<MainPageItem> pageItems) {
        super(fm);
        mPageItems = pageItems;
    }

    @Override
    public Fragment getItem(int position) {
        return mPageItems.get(position).getFragment();
    }

    @Override
    public int getCount() {
        return mPageItems.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mPageItems.get(position).getTitle();
    }
}

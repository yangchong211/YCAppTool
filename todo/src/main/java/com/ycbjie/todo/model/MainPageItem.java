package com.ycbjie.todo.model;


import android.support.v4.app.Fragment;

public class MainPageItem {

    private int dayOfWeek;
    private String mTitle;
    private Fragment mFragment;

    public MainPageItem(int dayOfWeek, String title, Fragment fragment) {
        this.dayOfWeek = dayOfWeek;
        mTitle = title;
        mFragment = fragment;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Fragment getFragment() {
        return mFragment;
    }

    public void setFragment(Fragment fragment) {
        mFragment = fragment;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

}

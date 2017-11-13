package com.ns.yc.lifehelper.listener;


public interface OnDatePickListener {

    void onDatePick(int year, int monthOfYear, int dayOfMonth);

    void onDatePickCancel();
}

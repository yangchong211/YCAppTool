package com.ns.yc.lifehelper.listener;


public interface OnTimePickListener {

    void onTimePick(int hourOfDay, int minute);
    void onTimePickCancel();

}

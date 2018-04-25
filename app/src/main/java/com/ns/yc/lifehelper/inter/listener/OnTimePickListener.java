package com.ns.yc.lifehelper.inter.listener;


public interface OnTimePickListener {

    void onTimePick(int hourOfDay, int minute);
    void onTimePickCancel();

}

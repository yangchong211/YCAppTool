package com.ycbjie.library.inter.listener;


public interface OnTimePickListener {

    void onTimePick(int hourOfDay, int minute);
    void onTimePickCancel();

}

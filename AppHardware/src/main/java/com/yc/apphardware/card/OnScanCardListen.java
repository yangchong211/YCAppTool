package com.yc.apphardware.card;

/**
 * @author: Administrator
 * @date: 2023/12/7
 */

public interface OnScanCardListen {
    void onScanCard(String msg, String cardNum);
}

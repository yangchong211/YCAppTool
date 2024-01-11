package com.yc.keyeventlib.code;

import android.view.KeyEvent;

import com.yc.keyeventlib.code.IKeyCode;

/**
 * 按键的能力
 */
public interface IKeyEvent extends IKeyCode {

    /**
     * set keycode
     *
     * @param keyCode code
     */
    void setKeyCode(int keyCode);

    /**
     * set keyevent
     *
     * @param keyEvent event
     */
    void setKeyEvent(KeyEvent keyEvent);

    /**
     * get keycode
     *
     * @return code
     */
    int getKeyCode();

    /**
     * get keyevent
     *
     * @return event
     */
    KeyEvent getKeyEvent();

    /**
     * 获取按键和值的隐射值，比如KEYCODE_1返回1
     *
     * @return 映射值
     */
    String getKeyMapping();
}

package com.yc.keyeventlib.board;

import android.view.KeyEvent;

import com.yc.keyeventlib.code.IKeyEvent;
import com.yc.keyeventlib.code.KeyEventImpl;

/**
 * 按键的实现类
 */
public class AndroidKeyBoard extends DispatchEventImpl {

    @Override
    public IKeyEvent getKeyEvent() {
        return new MyKeyEventImpl();
    }

    /**
     * 如果按键code对不上具体功能。比如说有的设备点击enter键回调code是100，有的是101。这个时候可以自己实现
     */
    private static class MyKeyEventImpl extends KeyEventImpl{
        @Override
        public boolean isEnter() {
            return getKeyCode() == KeyEvent.KEYCODE_ENTER;
        }
    }
}

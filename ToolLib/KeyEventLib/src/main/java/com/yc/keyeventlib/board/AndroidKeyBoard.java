package com.yc.keyeventlib.board;

import com.yc.keyeventlib.code.IKeyEvent;
import com.yc.keyeventlib.code.KeyEventImpl;

/**
 * 按键的实现类
 */
public class AndroidKeyBoard extends DispatchEventImpl {

    @Override
    public IKeyEvent getKeyEvent() {
        return new KeyEventImpl();
    }
}

package com.yc.keyeventlib.board;

import android.content.Context;
public class KeyEventManager {

    private static volatile KeyEventManager singleton = null;
    private DispatchEventImpl keyboard;

    public static KeyEventManager getInstance(){
        if(singleton == null){
            synchronized (KeyEventManager.class){
                if(singleton == null){
                    singleton = new KeyEventManager();
                }
            }
        }
        return singleton;
    }

    private KeyEventManager(){
        keyboard = new AndroidKeyBoard();
    }

    public void init(Context context){
        keyboard.init(context);
    }

    public DispatchEventImpl getKeyboard() {
        return keyboard;
    }

    public void setKeyboard(DispatchEventImpl keyboard) {
        this.keyboard = keyboard;
    }

    /**
     * 传入按键值,读取按键信息
     * @param keyCode 按键值
     */
    public String getKeyMsg(int keyCode) {
        String msg = "无效按键";
        keyboard.getKeyEvent().setKeyCode(keyCode);
        String keyMapping = keyboard.getKeyEvent().getKeyMapping();
        if (keyMapping != null){
            msg = "有效按键：" + keyMapping;
        }
        return msg;
    }

    /**
     * 传入按键值,读取按键信息
     */
    public String getKeyMsg() {
        String msg = "无效按键";
        String keyMapping = keyboard.getKeyEvent().getKeyMapping();
        if (keyMapping != null){
            msg = "有效按键：" + keyMapping;
        }
        return msg;
    }
}

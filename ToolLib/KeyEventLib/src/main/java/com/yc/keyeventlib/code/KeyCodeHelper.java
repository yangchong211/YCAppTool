package com.yc.keyeventlib.code;


import java.util.HashMap;
import java.util.Map;

public class KeyCodeHelper {
    private static volatile KeyCodeHelper singleton = null;
    private IKeyEvent keyEvent;

    /**
     * 获取单例
     * @return 单例
     */
    public static KeyCodeHelper getInstance(){
        if(singleton == null){
            synchronized (KeyCodeHelper.class){
                if(singleton == null){
                    singleton = new KeyCodeHelper();
                }
            }
        }
        return singleton;
    }

    private KeyCodeHelper() {

    }

    /**
     * 设置功能按键
     * @param iKeycode 按键值
     */
    public void setKeycodeImpl(IKeyEvent iKeycode){
        this.keyEvent = iKeycode;
    }

    /**
     * 传入按键值,读取按键信息
     * @param keyCode 按键值
     */
    public String getKeyMsg(int keyCode) {
        String msg = "无效按键";
        keyEvent.setKeyCode(keyCode);
        String keyMapping = keyEvent.getKeyMapping();
        if (keyMapping != null){
            msg = "有效按键：" + keyMapping;
        }
        return msg;
    }
}

package com.ns.yc.lifehelper.ui.test.observable;

import java.util.Observable;
import java.util.Observer;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2016/3/31
 * 描    述：简单模拟订阅者设计模式
 * 修订历史：
 * 观察者
 * ================================================
 */

public class MeObserver implements Observer {

    private String yourName;
    public MeObserver(String yourName){
        this.yourName=yourName;
    }

    @Override
    public void update(Observable o, Object arg) {
        System.out.println("你订阅的"+arg.toString()+"更新了。");
    }

    @Override
    public String toString() {
        return "your name "+yourName;
    }

}

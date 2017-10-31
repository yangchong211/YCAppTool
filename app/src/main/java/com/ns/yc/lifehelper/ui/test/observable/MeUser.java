package com.ns.yc.lifehelper.ui.test.observable;

import java.util.Observable;


/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2016/3/31
 * 描    述：简单模拟订阅者设计模式
 * 修订历史：
 *
 * 被观察者：当他有更新时，所有的观察者都会接收到响应的通知
 * ================================================
 */
public class MeUser extends Observable {

    public void postNewContentToCoder(String content){
        setChanged();
        notifyObservers(content);
    }

}

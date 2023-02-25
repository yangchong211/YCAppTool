package com.yc.store.factory;

import android.content.Context;

import com.yc.store.ICacheable;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/11/9
 *     desc  : 此接口使用方法
 *     revise:
 * </pre>
 */
public interface CacheFactory<T extends ICacheable> {

    /**
     * 使用工厂模式
     * 抽象工厂，担任这个角色的是工厂方法模式的核心，任何在模式中创建对象的工厂类必须实现这个接口
     * 具体工厂，具体工厂角色含有与业务密切相关的逻辑，并且受到使用者的调用以创建具体产品对象
     *
     * 优点1：工厂方法用来创建所需要的产品，同时隐藏了哪种具体产品类将被实例化这一细节，
     *       用户只需要关心所需产品对应的工厂，无须关心创建细节，甚至无须知道具体产品类的类名。
     * 优点2：加入新的产品时，比如后期新加一个阿里播放器内核，这个时候就只需要添加一个具体工厂和具体产品就可以。
     *       系统的可扩展性也就变得非常好，完全符合“开闭原则”
     *
     * @return                              具体的实现者
     */
    T createCache();

}

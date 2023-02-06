package com.yc.kernel.factory;

import android.content.Context;

import com.yc.kernel.inter.AbstractVideoPlayer;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/11/9
 *     desc  : 此接口使用方法
 *     revise: 1.继承{@link AbstractVideoPlayer}扩展自己的播放器。
 *             2.继承此接口并实现{@link #createPlayer(Context)}，返回步骤1中的播放器。
 * </pre>
 */
public interface PlayerFactory<T extends AbstractVideoPlayer> {

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
     * 创建具体的内核播放器Player
     * @param context                       上下文
     * @return                              具体的player
     */
    T createPlayer(Context context);

}

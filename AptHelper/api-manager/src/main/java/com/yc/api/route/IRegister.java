package com.yc.api.route;

/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/12/23
 *     desc  : 接口
 *     revise: 注册接口
 * </pre>
 */
public interface IRegister {
    <I extends IRoute, E extends I> void register(Class<I> apiInterface, Class<E> apiImplement);
}

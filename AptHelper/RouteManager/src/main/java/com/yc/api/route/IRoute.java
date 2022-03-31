package com.yc.api.route;

/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/12/23
 *     desc  : 接口
 *     revise: 自定义接口需要继承该接口
 * </pre>
 */
public interface IRoute {

    default boolean isPresent() {
        return true;
    }
}

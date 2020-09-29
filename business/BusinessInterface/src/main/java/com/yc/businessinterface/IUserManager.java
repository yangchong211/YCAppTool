package com.yc.businessinterface;


/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time  : 2020/6/20
 *     desc  : 用户相关操作接口
 *     revise:
 * </pre>
 */
public interface IUserManager{

    /**
     * 获取用户token
     * @return                  返回token
     */
    String getToken();

    /**
     * 用户ID
     */
    String getUid();

}

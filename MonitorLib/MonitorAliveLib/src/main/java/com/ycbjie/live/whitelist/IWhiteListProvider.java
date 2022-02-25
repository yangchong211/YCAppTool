
package com.ycbjie.live.whitelist;

import android.app.Application;

import java.util.List;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/9/19
 *     desc  : 白名单跳转意图数据提供者
 *     revise:
 * </pre>
 */
public interface IWhiteListProvider {

    /**
     * 提供白名单跳转意图
     *
     * @param application
     * @return 白名单跳转意图
     */
    List<WhiteListIntentWrapper> getWhiteList(Application application);

}

package com.yc.appcommoninter.inter;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time   : 2017/5/11
 *     desc   : AB测试开关接口
 *     revise :
 *     GitHub :
 * </pre>
 */
public interface IMonitorToggle {
    /**
     * 是否开启降级
     *
     * @return true表示降级
     */
    boolean isOpen();
}

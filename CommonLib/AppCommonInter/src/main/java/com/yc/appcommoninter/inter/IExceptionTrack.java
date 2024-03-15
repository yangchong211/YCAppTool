package com.yc.appcommoninter.inter;

import java.util.HashMap;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time   : 2017/5/11
 *     desc   : 异常上报接口
 *     revise :
 *     GitHub :
 * </pre>
 */
public interface IExceptionTrack {

    /**
     * 异常上报
     *
     * @param name      名称
     * @param exception 异常
     */
    void onException(String name, Exception exception);

    /**
     * 异常上报
     *
     * @param exception 异常
     */
    void onException(Exception exception);

}

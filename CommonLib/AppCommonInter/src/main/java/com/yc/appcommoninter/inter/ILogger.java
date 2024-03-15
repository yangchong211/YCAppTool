package com.yc.appcommoninter.inter;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time   : 2017/5/11
 *     desc   : log自定义日志接口
 *     revise :
 *     GitHub :
 * </pre>
 */
public interface ILogger {

    /**
     * 普通log日志
     *
     * @param log log日志
     */
    void log(String log);

    /**
     * 异常日志
     *
     * @param error error信息
     */
    void error(String error);

}

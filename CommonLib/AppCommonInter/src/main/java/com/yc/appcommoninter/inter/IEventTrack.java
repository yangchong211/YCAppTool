package com.yc.appcommoninter.inter;

import java.util.HashMap;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time   : 2017/5/11
 *     desc   : event事件接口
 *     revise :
 *     GitHub :
 * </pre>
 */
public interface IEventTrack {

    /**
     * 事件统计
     *
     * @param name 名称
     * @param map  map集合
     */
    void onEvent(String name, HashMap<String, String> map);

    /**
     * 事件统计
     *
     * @param map map集合
     */
    void onEvent(HashMap<String, String> map);
}

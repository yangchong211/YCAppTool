package com.yc.longalive;

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
public interface ILongAliveEventTrack {

    void onEvent(HashMap<String, String> map);
}

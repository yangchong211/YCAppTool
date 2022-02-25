package com.yc.netlib.data;

import java.util.HashMap;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time  : 2019/07/22
 *     desc  : 接口
 *     revise:
 * </pre>
 */
public interface IDataPoolHandle {
    void initDataPool();
    void clearDataPool();
    void addNetworkFeedData(String key, NetworkFeedBean networkFeedModel);
    void removeNetworkFeedData(String key);
    HashMap<String, NetworkFeedBean> getNetworkFeedMap();
    NetworkFeedBean getNetworkFeedModel(String requestId);
    NetworkTraceBean getNetworkTraceModel(String id);
}

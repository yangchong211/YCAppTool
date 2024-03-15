package com.yc.networklib.net;

import android.net.Uri;

import java.util.ArrayList;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/11/21
 *     desc  : http工具类
 *     revise: v1.4 17年6月8日
 * </pre>
 */
public final class AppHttpUtils {


    /**
     * 判断当前url是否在白名单中
     *
     * @param arrayList 白名单集合
     * @param url       url
     * @return
     */
    public static boolean isWhiteList(ArrayList<String> arrayList, String url) {
        if (url == null) {
            return false;
        }
        if (arrayList == null || arrayList.size() == 0) {
            return false;
        }
        //重要提醒：建议只使用https协议通信，避免中间人攻击
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            return false;
        }
        //提取host
        String host = "";
        try {
            //提取host，如果需要校验Path可以通过url.getPath()获取
            host = Uri.parse(url).getHost();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < arrayList.size(); i++) {
            if (host != null && host.equals(arrayList.get(i))) {
                //是咱们自己的host
                return true;
            }
        }
        //不在白名单内
        return false;
    }


}

package com.yc.toolutils.appinfo;


import android.content.Context;

import com.yc.toolutils.context.AppToolUtils;

/**
 * <pre>
 *     @author yangchong
 *     GitHub : https://github.com/yangchong211/YCAppTool
 *     email  : yangchong211@163.com
 *     time  : 2016/09/23
 *     desc  : app资源相关工具类
 *     revise:
 * </pre>
 */
public final class AppResourcesUtils {



    /**
     * 获取资源字符串
     *
     * @param id      资源id
     * @return
     */
    public String getString(int id) {
        Context context = AppToolUtils.getApp();
        return context == null ? "" : context.getResources().getString(id);
    }

    /**
     * 获取资源字符串
     *
     * @param context 上下文
     * @param id      资源id
     * @return
     */
    private String getString(Context context, int id) {
        return context.getResources().getString(id);
    }

}

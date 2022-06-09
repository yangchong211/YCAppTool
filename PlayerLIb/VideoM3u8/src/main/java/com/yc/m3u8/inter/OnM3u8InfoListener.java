package com.yc.m3u8.inter;

import com.yc.m3u8.bean.M3u8;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/11/9
 *     desc  : 获取M3U8信息
 *     revise:
 * </pre>
 */
public interface OnM3u8InfoListener extends BaseListener {

    /**
     * 获取成功的时候回调
     */
    void onSuccess(M3u8 m3U8);
}

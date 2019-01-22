package com.ycbjie.music.utils.share;


import android.content.Context;

/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211/YCBlogs
 *     time  : 2017/01/30
 *     desc  : 分享弹窗，主要是为了练习策略者模式
 *     revise:
 * </pre>
 */
public class Share {

    private SharePolicy sharePolicy;

    public void setSharePolicy(SharePolicy sharePolicy) {
        this.sharePolicy = sharePolicy;
    }

    public void share(String type, ShareTypeBean shareTypeBean, Context context) {
        sharePolicy.share(type, shareTypeBean,context);
    }

}

package com.ycbjie.music.utils.share;


import java.io.Serializable;

/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211/YCBlogs
 *     time  : 2017/01/30
 *     desc  : 分享弹窗，主要是为了练习策略者模式
 *     revise:
 * </pre>
 */
public class ShareTypeBean implements Serializable {

    //  分享类型
    //  商品分享  SHARE_GOODS
    //  素材分享  SHARE_MATERIAL
    //  小队分享  SHARE_TEAM
    //  专题分享  SHARE_SPECIAL

    private String shareType;

    public String getShareType() {
        return shareType;
    }

    public void setShareType(String shareType) {
        this.shareType = shareType;
    }
}

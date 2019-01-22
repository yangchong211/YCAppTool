package com.ycbjie.music.utils.share;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211/YCBlogs
 *     time  : 2017/01/30
 *     desc  : 分享弹窗，主要是为了练习策略者模式
 *     revise:
 * </pre>
 */
public class ShareComment {

    /**
     * 分享view类型
     */
    @Retention(RetentionPolicy.SOURCE)
    public @interface ShareViewType {
        // 朋友圈
        String SHARE_FRIEND_CIRCLE = "share_friend_circle";
        // 微信好友
        String SHARE_FRIEND = "share_friend";
        // 海报图
        String SHARE_CREATE_POSTER = "create_poster";
        // 保存图片
        String SHARE_SAVE_PIC = "save_pic";
    }


    //  分享类型
    //  商品分享  SHARE_GOODS
    //  素材分享  SHARE_MATERIAL
    //  小队分享  SHARE_TEAM
    //  专题分享  SHARE_SPECIAL
    @Retention(RetentionPolicy.SOURCE)
    public @interface ShareType {
        // 商品分享
        String SHARE_GOODS = "share_goods";
        // 素材分享
        String SHARE_MATERIAL = "share_material";
        // 小队分享
        String SHARE_TEAM = "share_team";
        // 专题分享
        String SHARE_SPECIAL = "share_special";
    }

}

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
public class BaseSharePolicy implements SharePolicy{

    @Override
    public void share(String type, ShareTypeBean shareTypeBean, Context context) {
        switch (type){
            case ShareComment.ShareViewType.SHARE_FRIEND:           // 好友
                shareFriend(shareTypeBean , context);
                break;
            case ShareComment.ShareViewType.SHARE_FRIEND_CIRCLE:    // 朋友圈
                shareFriendCircle(shareTypeBean , context);
                break;
            case ShareComment.ShareViewType.SHARE_CREATE_POSTER:    // 生成海报图
                shareCreatePoster(shareTypeBean , context);
                break;
            case ShareComment.ShareViewType.SHARE_SAVE_PIC:         // 保存图片
                shareSaveImg(shareTypeBean , context);
                break;
            default:
                break;
        }
    }


    void shareFriend(ShareTypeBean shareTypeBean, Context context) {

    }

    void shareFriendCircle(ShareTypeBean shareTypeBean, Context context) {

    }

    void shareCreatePoster(ShareTypeBean shareTypeBean, Context context) {

    }

    void shareSaveImg(ShareTypeBean shareTypeBean, Context context) {

    }

}

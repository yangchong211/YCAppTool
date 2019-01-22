package com.ycbjie.music.utils.share;


import android.content.Context;
import android.graphics.Bitmap;

import com.pedaily.yc.ycdialoglib.toast.ToastUtils;
import com.ycbjie.library.utils.DoShareUtils;
import com.ycbjie.music.utils.FileSaveUtils;
import com.ycbjie.music.utils.share.bitmap.GoodShareBitmap;
import com.ycbjie.music.utils.share.bitmap.GoodShareCircleBitmap;


/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211/YCBlogs
 *     time  : 2017/01/30
 *     desc  : 分享弹窗，主要是为了练习策略者模式
 *     revise:
 * </pre>
 */
public class ShareGoodsPolicy extends BaseSharePolicy {

    @Override
    void shareFriend(ShareTypeBean shareTypeBean, final Context context) {
        super.shareFriend(shareTypeBean, context);
        GoodShareBitmap goodShareBitmap = new GoodShareBitmap(context);
        goodShareBitmap.setData(shareTypeBean,new GoodShareBitmap.onLoadFinishListener() {
            @Override
            public void listener(Bitmap bmp, boolean isSuccess) {
                if (bmp!=null && isSuccess){
//                    DoShareUtils.shareImage(context,bmp);
                }
            }
        });
    }

    @Override
    void shareFriendCircle(ShareTypeBean shareTypeBean, final Context context) {
        super.shareFriendCircle(shareTypeBean, context);
        GoodShareCircleBitmap goodShareCircleBitmap = new GoodShareCircleBitmap(context);
        goodShareCircleBitmap.setData(shareTypeBean,new GoodShareCircleBitmap.onLoadFinishListener() {
            @Override
            public void listener(Bitmap bmp, boolean isSuccess) {
                if (bmp!=null && isSuccess){
//                    DoShareUtils.shareImage(context,bmp);
                }
            }
        });
    }


    @Override
    void shareSaveImg(ShareTypeBean shareTypeBean, final Context context) {
        super.shareSaveImg(shareTypeBean, context);
        GoodShareBitmap goodShareBitmap = new GoodShareBitmap(context);
        goodShareBitmap.setData(shareTypeBean,new GoodShareBitmap.onLoadFinishListener() {
            @Override
            public void listener(Bitmap bmp, boolean isSuccess) {
                if (bmp!=null && isSuccess){
                    String s = FileSaveUtils.saveBitmap(context, bmp, null, true);
                    if (s!=null && s.length()>0){
                        ToastUtils.showRoundRectToast("保存成功");
                    }else {
                        ToastUtils.showRoundRectToast("保存失败");
                    }
                }
            }
        });
    }

    @Override
    void shareCreatePoster(ShareTypeBean shareTypeBean, final Context context) {
        super.shareCreatePoster(shareTypeBean, context);
        GoodShareBitmap goodShareBitmap = new GoodShareBitmap(context);
        goodShareBitmap.setData(shareTypeBean,new GoodShareBitmap.onLoadFinishListener() {
            @Override
            public void listener(Bitmap bmp, boolean isSuccess) {
                if (bmp!=null && isSuccess){
                    String s = FileSaveUtils.saveBitmap(context, bmp, null, true);
                    if (s!=null && s.length()>0){
                        ToastUtils.showRoundRectToast("保存成功");
                    }else {
                        ToastUtils.showRoundRectToast("保存失败");
                    }
                }
            }
        });
    }
}

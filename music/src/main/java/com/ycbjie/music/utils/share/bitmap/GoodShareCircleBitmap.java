package com.ycbjie.music.utils.share.bitmap;


import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ycbjie.library.base.config.AppConfig;
import com.ycbjie.library.utils.image.ImageUtils;
import com.ycbjie.music.R;
import com.ycbjie.music.utils.share.ShareDetailBean;
import com.ycbjie.music.utils.share.ShareTypeBean;

import java.util.concurrent.Callable;

import cn.ycbjie.ycthreadpoollib.PoolThread;
import cn.ycbjie.ycthreadpoollib.callback.AsyncCallback;

/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211/YCBlogs
 *     time  : 2017/01/30
 *     desc  : 分享弹窗，主要是为了练习策略者模式
 *     revise:
 * </pre>
 */
public class GoodShareCircleBitmap {

    private Context mContext;
    private ImageView mIvImage;
    private TextView mTvTitle;
    private ImageView mIvCode;
    private final View view;


    public GoodShareCircleBitmap(Context context){
        mContext = context;
        view = View.inflate(context, R.layout.view_share_circle_goods, null);
        bindView(view);
    }

    private void bindView(View view) {
        mIvImage = view.findViewById(R.id.iv_image);
        mTvTitle = view.findViewById(R.id.tv_title);
        mIvCode = view.findViewById(R.id.iv_code);
    }

    public void setData(ShareTypeBean shareTypeBean , final onLoadFinishListener listener){
        ShareDetailBean detailBean = (ShareDetailBean) shareTypeBean;
        this.mListener = listener;
        mTvTitle.setText(detailBean.getTitle());
        ImageUtils.loadImgByPicasso(mContext,detailBean.getImage(),R.drawable.image_default,mIvImage);
        mIvCode.setBackgroundResource(R.drawable.icon_mine_wx);
        PoolThread executor = AppConfig.INSTANCE.getExecutor();
        executor.setName("getBitmapFromView");
        executor.async(new Callable<Bitmap>() {
            @Override
            public Bitmap call() throws Exception {
                return ImageUtils.loadBitmapFromView(view);
            }
        }, new AsyncCallback<Bitmap>() {
            @Override
            public void onSuccess(Bitmap bitmap) {
                if (bitmap==null){
                    if (mListener!=null){
                        mListener.listener(null,false);
                    }
                }else {
                    if (mListener!=null){
                        mListener.listener(bitmap,true);
                    }
                }
            }

            @Override
            public void onFailed(Throwable t) {
                if (mListener!=null){
                    mListener.listener(null,false);
                }
            }

            @Override
            public void onStart(String threadName) {

            }
        });

    }

    private onLoadFinishListener mListener;
    public interface onLoadFinishListener {
        void listener(Bitmap bmp, boolean isSuccess);
    }

}

package com.yc.imageserver.transformations;

import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.request.target.ImageViewTarget;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/9/9
 *     desc  : Glide加载图片时，根据图片宽度等比缩放
 *     revise:
 * </pre>
 */
public class TransformationScale extends ImageViewTarget<Bitmap> {

    private ImageView target;

    public TransformationScale(ImageView target) {
        super(target);
        this.target = target;
    }

    @Override
    protected void setResource(Bitmap resource) {
        view.setImageBitmap(resource);

        if (resource != null) {
            //获取原图的宽高
            int width = resource.getWidth();
            int height = resource.getHeight();

            //获取imageView的宽
            int imageViewWidth = target.getWidth();

            //计算缩放比例
            float sy = (float) (imageViewWidth * 0.1) / (float) (width * 0.1);


            //计算图片等比例放大后的高
            int imageHeight = (int) (height * sy);
            //ViewGroup.LayoutParams params = target.getLayoutParams();
            //params.height = imageHeight;
            //固定图片高度，记得设置裁剪剧中
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT, imageHeight);
            params.bottomMargin = 10;
            target.setLayoutParams(params);
        }
    }
}

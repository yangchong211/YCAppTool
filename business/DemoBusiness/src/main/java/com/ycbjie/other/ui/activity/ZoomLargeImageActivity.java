package com.ycbjie.other.ui.activity;

import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.yc.cn.ycgallerylib.zoom.view.ZoomLayoutView;
import com.yc.configlayer.arounter.RouterConfig;
import com.yc.imageserver.utils.GlideImageUtils;
import com.ycbjie.library.base.mvp.BaseActivity;
import com.ycbjie.other.R;

import cn.ycbjie.ycstatusbarlib.bar.StateAppBar;

/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/3/11
 *     desc  : 大图加载页面
 *     revise:
 * </pre>
 */
@Route(path = RouterConfig.Demo.ACTIVITY_LARGE_IMAGE_ACTIVITY)
public class ZoomLargeImageActivity extends BaseActivity {

    private ZoomLayoutView zoomView;

    @Override
    public int getContentView() {
        return R.layout.activity_zoom_large_image;
    }

    @Override
    public void initView() {
        StateAppBar.setStatusBarColor(this,
                ContextCompat.getColor(this, R.color.black));
        zoomView = findViewById(R.id.zoomView);
        zoomView.setLoadingVisibility(true);
        zoomView.getImageView().setMaxScale(5);
        GlideImageUtils.loadImageLocal(this, R.drawable.girl13,
                zoomView.getImageView(), new GlideImageUtils.CallBack() {
            @Override
            public void onLoadFailed() {
                //设置加载loading隐藏
                zoomView.setZoomViewVisibility(true);
            }

            @Override
            public void onResourceReady(Bitmap resource) {

            }
        });
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (zoomView!=null){
            //重置所有状态，清空mask，停止所有手势，停止所有动画
            zoomView.getImageView().reset();
        }
    }


}

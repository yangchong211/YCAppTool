package com.ycbjie.other.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.yc.cn.ycgallerylib.zoom.view.ZoomLayoutView;
import com.ycbjie.library.arounter.ARouterConstant;
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
@Route(path = ARouterConstant.ACTIVITY_LARGE_IMAGE_ACTIVITY)
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
        Picasso.with(this)
                .load(R.drawable.girl3)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .into(zoomView.getImageView(), new Callback() {
                    @Override
                    public void onSuccess() {
                        //设置加载loading隐藏
                        zoomView.setZoomViewVisibility(true);
                    }

                    @Override
                    public void onError() {

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

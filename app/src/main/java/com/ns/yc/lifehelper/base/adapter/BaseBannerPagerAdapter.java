package com.ns.yc.lifehelper.base.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ns.yc.lifehelper.R;
import com.yc.cn.ycbannerlib.banner.adapter.AbsStaticPagerAdapter;
import com.yc.imageserver.utils.GlideImageUtils;

import java.util.List;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2016/11/12
 * 描    述：会议轮播图适配器
 * 修订历史：
 * ================================================
 */
public class BaseBannerPagerAdapter extends AbsStaticPagerAdapter {

    private Context ctx;
    private List<Object> list;

    public BaseBannerPagerAdapter(Context ctx, List<Object> list) {
        this.ctx = ctx;
        this.list = list;
    }


    @Override
    public View getView(ViewGroup container, final int position) {
        ImageView imageView = new ImageView(ctx);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        //加载图片
        if(list!=null){
            if(list.get(position) instanceof String){
                GlideImageUtils.loadImageNet(ctx,(String) list.get(position),imageView);
            }else if(list.get(position) instanceof Integer){
                GlideImageUtils.loadImageLocal(ctx,(Integer) list.get(position),imageView);
            }else if(list.get(position) instanceof Bitmap){
                GlideImageUtils.loadImageLocal(ctx,(Bitmap) list.get(position),imageView);
            }
        }else {
            GlideImageUtils.loadImageLocal(ctx, R.drawable.image_default,imageView);
        }
        return imageView;
    }

    @Override
    public int getCount() {
        return list==null ? 0 : list.size();
    }

}
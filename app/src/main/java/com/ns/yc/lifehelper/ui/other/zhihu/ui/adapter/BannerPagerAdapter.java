package com.ns.yc.lifehelper.ui.other.zhihu.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.ui.other.zhihu.model.bean.ZhiHuDailyListBean;
import com.ns.yc.lifehelper.utils.image.ImageUtils;
import com.yc.cn.ycbannerlib.banner.adapter.AbsStaticPagerAdapter;

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
public class BannerPagerAdapter extends AbsStaticPagerAdapter {

    private Context ctx;
    private List<ZhiHuDailyListBean.TopStoriesBean> list;

    BannerPagerAdapter(Context ctx, List<ZhiHuDailyListBean.TopStoriesBean> list) {
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
            ImageUtils.loadImgByPicasso(ctx,list.get(position).getImage(), R.drawable.image_default,imageView);
        }else {
            ImageUtils.loadImgByPicasso(ctx, R.drawable.image_default,imageView);
        }
        return imageView;
    }

    @Override
    public int getCount() {
        return list==null ? 0 : list.size();
    }

}
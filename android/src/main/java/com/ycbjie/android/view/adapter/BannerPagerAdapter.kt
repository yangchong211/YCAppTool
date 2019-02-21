package com.ycbjie.android.view.adapter

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView


import com.ycbjie.android.model.bean.BannerBean
import com.yc.cn.ycbannerlib.banner.adapter.AbsStaticPagerAdapter
import com.ycbjie.android.R
import com.ycbjie.library.utils.image.ImageUtils


class BannerPagerAdapter constructor(private val ctx: Activity?, private val list: MutableList<BannerBean>) : AbsStaticPagerAdapter() {

    override fun getView(container: ViewGroup, position: Int): View {
        val imageView = ImageView(ctx)
        imageView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        //加载图片
        ImageUtils.loadImgByPicasso(ctx, list[position].imagePath, R.drawable.bg_small_autumn_tree_min, imageView)
        return imageView
    }

    override fun getCount(): Int {
        return list.size
    }

}
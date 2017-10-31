package com.ns.yc.lifehelper.ui.home.view.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.ui.home.bean.HomeBlogEntity;
import com.ns.yc.lifehelper.utils.ImageUtils;

import cn.bingoogolapple.androidcommon.adapter.BGARecyclerViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/8/21
 * 描    述：Home主页面适配器
 * 修订历史：
 * ================================================
 */
public class HomeBlogAdapter extends BGARecyclerViewAdapter<HomeBlogEntity> {

    private Activity activity;

    public HomeBlogAdapter(Activity activity, RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_home_list);
        this.activity = activity;
    }

    @Override
    protected void fillData(BGAViewHolderHelper helper, int position, HomeBlogEntity model) {
        if(position==0){
            helper.getView(R.id.ll_new_content).setVisibility(View.GONE);
            helper.getView(R.id.ll_news_head).setVisibility(View.VISIBLE);
        }else {
            helper.getView(R.id.ll_new_content).setVisibility(View.VISIBLE);
            helper.getView(R.id.ll_news_head).setVisibility(View.GONE);
            helper.setText(R.id.tv_title,model.getTitle());
            ImageUtils.loadImgByPicassoError(activity,model.getImageUrl(),R.drawable.image_default,helper.getImageView(R.id.iv_img));
            helper.setText(R.id.tv_author,model.getAuthor());
            helper.setText(R.id.tv_time,model.getTime());
        }
    }


}
